package twitter.config.model

case class Settings(settings: Map[String, SettingValue[_]]) {

  def get(settingName: String): Option[SettingValue[_]] =
    settings.get(settingName)

  def getString(settingName: String): Option[String] =
    settings.get(settingName).flatMap {
      case StringValue(str) => Some(str)
      case _ => None
    }

  def getBoolean(settingName: String): Option[Boolean] =
    settings.get(settingName).flatMap {
      case BooleanValue(bool) => Some(bool)
      case _ => None
    }

  def getLong(settingName: String): Option[Long] =
    settings.get(settingName).flatMap {
      case LongValue(long) => Some(long)
      case _ => None
    }

  def getList(settingName: String): Option[List[String]] =
    settings.get(settingName).flatMap {
      case StringListValue(strList) => Some(strList)
      case _ => None
    }
}
