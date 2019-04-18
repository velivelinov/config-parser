package twitter.config.parser

import com.typesafe.scalalogging.LazyLogging
import twitter.config.model._

import scala.util.parsing.combinator._

trait CommonParsers extends RegexParsers with LazyLogging {
  override val whiteSpace = """[ \t]+""".r
  def eol: Parser[Any] = "\n" | "\r\n" | "\r"
}

trait ValueParser extends CommonParsers {
  def field: Parser[String] = """[^\"\n<>=;\[\]]+""".r
  def fieldWithoutComma: Parser[String] = """[^ \"\n<>=,;\[\]]+""".r

  def stringVal: Parser[String] =
    "\"" ~> field <~ "\""

  def pathVal: Parser[String] =
    "/[^ \n<>=,;]*/?".r

  def trueParser: Parser[Boolean] =
    ("true" | "yes" | "1$".r) ^^ {_ => true}

  def falseParser: Parser[Boolean] =
    ("false" | "no" | "0$".r) ^^ {_ => false}

  def booleanVal: Parser[Boolean] =
    trueParser | falseParser

  //TODO - check for overflowing when using toLong
  def numberVal: Parser[Long] = """-?\d+""".r ^^ { _.toLong }

  def stringListVal: Parser[List[String]] =
    fieldWithoutComma ~ "," ~ rep1(fieldWithoutComma <~ ",".?) ^^ {
      case field ~ "," ~ fields => field +: fields
    }

  def valueParser: Parser[SettingValue[_]] =
    (booleanVal | numberVal | pathVal | stringListVal | stringVal) <~ eol.? ^^ {
      case s: String => StringValue(s)
      case l: Long => LongValue(l)
      case b: Boolean => BooleanValue(b)
      case sl: List[_] => StringListValue(sl.map(_.toString))
    }
}

trait SettingParser extends ValueParser {

  def settingKey: Parser[String] =
    """[^ <>=,;\n\[\]]+""".r

  def overrides: Parser[String] =
    "<" ~> settingKey <~ ">"

  def setting: Parser[Setting] =
    settingKey ~ overrides.? ~ "=" ~ valueParser ^^ {
      case sk ~ ov ~ _ ~ v => Setting(sk, v, ov)
    }
}

trait GroupParser extends SettingParser {

  def groupTitle: Parser[String] =
    "[" ~> """[^\[\]]+""".r <~ "]" <~ eol.*

  //comments can't span on multiple lines
  def comment: Parser[String] =
    """;[^\n]*""".r <~ eol.?

  def group: Parser[Group] =
    groupTitle ~ rep1(setting <~ comment.? ~ eol.*) ^^ {
      case title ~ settings =>
        logger.info(s"Parsed a valid group with name: $title")
        settings.foreach(setting => logger.info(s"Parsed a new setting: $setting"))
        Group(title, settings)
    }
}

trait ConfigParser extends GroupParser {
  def groups: Parser[List[Group]] =
    rep1(group)
}

