package twitter.config.parser

import twitter.config.model.{Config, Group, Setting, SettingValue}

import scala.io.Source
import scala.util.Try

class ConfigLoader extends ConfigParser {

  // Picks the value that a setting should take, based on the overrides.
  // For instance, if both path and path<production> are present,
  // the former would be chosen if there are no overrides and the latter,
  // if the overrides contain the value "production"
  def chooseRelevantSettingValue(settings: List[Setting], overrideValues: List[String]): SettingValue[_] = {
    settings.filter( setting =>
      setting.overrides match {
        case Some(s) if !overrideValues.contains(s) => false
        case _ => true
      }
    ).last.value
  }

  // Processes groups available in the config.
  // For each one, picks correct values, based on the overrides provided.
  def configWithProcessedGroups(groups: List[Group], overrideValues: List[String]): Config = {
    val relevantSettings = groups
      .map { group =>
        group.name -> group.settings.groupBy(_.key).mapValues(chooseRelevantSettingValue(_, overrideValues))
      }.toMap

    Config(relevantSettings)
  }

  def parseConfig(config: String, overrides: List[String]): Option[Config] =
    parseAll(groups, config) match {
      case Success(groups, _) => Some(configWithProcessedGroups(groups, overrides))
      case Failure(msg, _) =>
        logger.info(s"Failed to parse config with error message: $msg")
        None
      case Error(msg, _) =>
        logger.info(s"Error parsing config with error message: $msg")
        None
    }

  def loadConfig(fileName: String, overrides: List[String] = Nil): Option[Config] = {
    Try {
      val resource = Source.fromResource(s"$fileName")
      //TODO avoid .getLines - reads whole file into memory. Use streaming instead.
      val content = resource.getLines.mkString("\n")

      val config = parseConfig(content, overrides)
      //TODO handle opening and closing more safely
      resource.close()

      config
    }.toOption.flatten
  }
}
