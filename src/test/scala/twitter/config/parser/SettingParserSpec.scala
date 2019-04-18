package twitter.config.parser

import org.scalatest.FlatSpec
import twitter.config.model.{Setting, StringValue}

class SettingParserSpec extends FlatSpec with ParserTestingUtils with SettingParser {

  "overrides" should "parse overrides" in {
    implicit val parser = overrides

    parsing("<overrideValue>") should equal("overrideValue")
    assertFail("<overrideValue>fsags")
    assertFail("overrideValue>")
    assertFail("overrideValue>")
    assertFail("overrideValue>")
  }

  "setting key" should "correctly parse only the setting keys" in {
    implicit val parser = settingKey

    parsing("path") should equal("path")
    assertFail("path<staging>")
    assertFail("path = 2")
  }

  "setting" should "correctly parse the full setting" in {
    implicit val parser = setting

    parsing("path = /var/") should equal(Setting("path", StringValue("/var/"), None))
    parsing("path<staging> = /var/st/") should equal(Setting("path", StringValue("/var/st/"), Some("staging")))
    assertFail("<staging>=/var/st/")
  }
}
