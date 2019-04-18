package twitter.config.model

sealed trait SettingValue[T] {
  def value: T
}

case class StringValue(value: String) extends SettingValue[String]
case class LongValue(value: Long) extends SettingValue[Long]
case class StringListValue(value: List[String]) extends SettingValue[List[String]]
case class BooleanValue(value: Boolean) extends SettingValue[Boolean]
