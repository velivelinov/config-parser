package twitter.config.model

import org.scalatest.{FlatSpec, Matchers, OptionValues}

object SettingsSpec {
  val settings = Settings(Map(
    "str" -> StringValue("this is a string!"),
    "long" -> LongValue(100),
    "bool" -> BooleanValue(true),
    "list" -> StringListValue(List("this", "is", "a", "list"))
  ))
}

class SettingsSpec extends FlatSpec with Matchers with OptionValues {

  import SettingsSpec._

  "getString" should "return a string if that is the correct type and none if not" in {
    settings.getString("str").value shouldBe "this is a string!"
    settings.getString("long") shouldBe None
    settings.getString("non-existent") shouldBe None
  }

  "getLong" should "return a long if that is the correct type and none if not" in {
    settings.getLong("long").value shouldBe 100
    settings.getLong("str") shouldBe None
    settings.getLong("non-existent") shouldBe None
  }

  "getBoolean" should "return a boolean if that is the correct type and none if not" in {
    settings.getBoolean("bool").value shouldBe true
    settings.getBoolean("str") shouldBe None
    settings.getBoolean("non-existent") shouldBe None
  }

  "getList" should "return a list if that is the correct type and none if not" in {
    settings.getList("list").value shouldBe List("this", "is", "a", "list")
    settings.getList("str") shouldBe None
    settings.getList("non-existent") shouldBe None
  }
}