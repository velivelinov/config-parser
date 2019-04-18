package twitter.config.model

import org.scalatest.{FlatSpec, Matchers}

object ConfigSpec {
  val ftpSettingsMap = Map("bool_size_limit" -> LongValue(20000))
  val config = Config(
    Map(
      "ftp" -> ftpSettingsMap
    )
  )
}

class ConfigSpec extends FlatSpec with Matchers {

  import ConfigSpec._

  "get" should "return a map of all settings for a group that is present" in {
    config.get("ftp") shouldBe Settings(ftpSettingsMap)
  }

  it should "return an empty map for a group that is not present" in {
    config.get("non-existent") shouldBe Settings(Map.empty[String, SettingValue[_]])
  }
}