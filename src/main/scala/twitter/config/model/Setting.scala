package twitter.config.model

case class Setting(key: String, value: SettingValue[_], overrides: Option[String] = None)