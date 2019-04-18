package twitter.config

import org.scalatest.{FlatSpec, OptionValues}
import twitter.config.model.{BooleanValue, Config, StringValue}
import twitter.config.parser.{ConfigLoader, ParserTestingUtils}

object ConfigLoaderSpec {
  val configLoader = new ConfigLoader
}

class ConfigLoaderSpec extends FlatSpec with ParserTestingUtils with OptionValues {

  import ConfigLoaderSpec._

  "loadConfig" should "parse a file without overrides and return a config object" in {
    val loadedConfig = configLoader.loadConfig("some.conf", overrides = Nil).value

    val expectedConfig = Config(
      Map("ftp" ->
        Map(
          "name" -> StringValue("hello there, ftp uploading"),
          "path" -> StringValue("/tmp/"),
          "enabled" -> BooleanValue(false)
        )
      )
    )

    loadedConfig shouldBe expectedConfig
  }

  it should "parse a file with one enabled override and read it correctly" in {
    val loadedConfig = configLoader.loadConfig("some.conf", overrides = List("staging")).value

    val expectedConfig = Config(
      Map("ftp" ->
        Map(
          "name" -> StringValue("hello there, ftp uploading"),
          "path" -> StringValue("/srv/uploads/"),
          "enabled" -> BooleanValue(false)
        )
      )
    )

    loadedConfig shouldBe expectedConfig
  }

  it should "parse a file with multiple enabled override and read it correctly" in {
    val loadedConfig = configLoader.loadConfig("some.conf", overrides = List("staging", "ubuntu")).value

    val expectedConfig = Config(
      Map("ftp" ->
        Map(
          "name" -> StringValue("hello there, ftp uploading"),
          "path" -> StringValue("/etc/var/uploads"),
          "enabled" -> BooleanValue(false)
        )
      )
    )

    loadedConfig shouldBe expectedConfig
  }

  it should "return no config if wrong path is provided" in {
    configLoader.loadConfig("non-existent.conf") shouldBe None
  }

  it should "return no config if a wrongly formatted file is provided" in {
    configLoader.loadConfig("badformat.conf") shouldBe None
  }
}