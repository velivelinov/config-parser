package twitter.config.parser

import org.scalatest.FlatSpec
import twitter.config.model.{BooleanValue, LongValue, StringListValue, StringValue}

class ValueParserSpec extends FlatSpec with ParserTestingUtils with ValueParser {

  "booleanVal parser" should "parse booleans correctly" in {
    implicit val parser = booleanVal

    parsing("true") should equal(true)
    parsing("yes") should equal(true)
    parsing("1") should equal(true)
    parsing("false") should equal(false)
    parsing("no") should equal(false)
    parsing("0") should equal(false)
  }

  "stringVal parser" should "parse strings correctly" in {
    implicit val parser = stringVal

    parsing("\"some string\"") should equal("some string")
  }

  "pathVal parser" should "parse strings correctly" in {
    implicit val parser = pathVal

    parsing("/some/path/") should equal("/some/path/")
    parsing("/some/path") should equal("/some/path")
  }

  "numberVal parser" should "parse numbers correctly" in {
    implicit val parser = numberVal

    parsing("2000") should equal(2000)
    parsing("-595035") should equal(-595035)
    parsing("9223372036854775807") should equal(9223372036854775807L)
    parsing("-9223372036854775808") should equal(-9223372036854775808L)
  }

  "stringListVal parser" should "parse lists of strings correctly" in {
    implicit val parser = stringListVal

    parsing("a,b,c") should equal(List("a", "b", "c"))
    parsing("a,b,c   ") should equal(List("a", "b", "c"))
    parsing("a,b,c,") should equal(List("a", "b", "c"))
  }

  "valueParser" should "parse to the correct type based on the value given" in {
    implicit val parser = valueParser

    parsing("0") should equal(BooleanValue(false))
    parsing("10894") should equal(LongValue(10894))
    parsing("-20894") should equal(LongValue(-20894))
    parsing("a,b,c") should equal(StringListValue(List("a", "b", "c")))
    parsing("\"hello world\"") should equal(StringValue("hello world"))
    parsing("\"some, string\"") should equal(StringValue("some, string"))
  }

}
