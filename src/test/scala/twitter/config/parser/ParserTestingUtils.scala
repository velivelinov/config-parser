package twitter.config.parser

import org.scalatest.Matchers
import twitter.config.model._

import scala.util.parsing.input.CharSequenceReader

object ParserTestingUtils {

  val oneGroup =
    """[ftp]
      |bool = no
      |path = /var/tmp
      |message = "hello world"
      |list = a,b,c
      |size = 26214400""".stripMargin

  val oneGroupSettings = List(
    Setting("size", LongValue(26214400), None),
    Setting("bool", BooleanValue(false), None),
    Setting("path", StringValue("/var/tmp"), None),
    Setting("message", StringValue("hello world"), None),
    Setting("list", StringListValue(List("a", "b", "c")), None)
  )

  val parsedOneGroup = Group("ftp", oneGroupSettings)

  val anotherGroup =
    s"""
       |
       |
       |[http]
       |name = "http uploading";some comment
       |request_type = get,post,options""".stripMargin

  val anotherGroupSettings = List(
    Setting("name", StringValue("http uploading"), None),
    Setting("request_type", StringListValue(List("get", "post", "options")), None)
  )
}

// Used ideas from https://henkelmann.eu/2011/01/an-introduction-to-scala-parser-combinators---part-3-writing-unit-tests-for-parsers/
// for testing parsers.
trait ParserTestingUtils extends CommonParsers with Matchers {

  def parsing[T](s: String)(implicit p: Parser[T]): T = {
    val phraseParser = phrase(p)
    val input = new CharSequenceReader(s)

    phraseParser(input) match {
      case Success(t, _)     => t
      case NoSuccess(msg, _) => throw new IllegalArgumentException(
        "Could not parse '" + s + "': " + msg)
    }
  }

  def assertFail[T](input:String)(implicit p:Parser[T]) {
    an [IllegalArgumentException] shouldBe thrownBy {
      parsing(input)(p)
    }
  }
}
