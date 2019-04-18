package twitter.config.model

case class Config(data: Map[String, Map[String, SettingValue[_]]]) {

  def get(group: String): Settings =
    Settings(data.getOrElse(group, Map.empty[String, SettingValue[_]]))
}
