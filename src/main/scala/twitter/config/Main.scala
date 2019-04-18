package twitter.config

import com.typesafe.scalalogging.LazyLogging
import twitter.config.parser.ConfigLoader

import scala.util.Try

object Main extends App with LazyLogging {

  override def main(args: Array[String]): Unit = {
    println(args(0))
    //split the overrides and pass them to the loadConfig method
    val overrides = Try(args(1)).getOrElse("").split(",").toList
    println(overrides)

    val config = new ConfigLoader().loadConfig(args(0), overrides) match {
      case Some(c) =>
        logger.info("Successfully loaded config!")
        c
      case None =>
        logger.error("Error loading config.")
        throw new Exception("Error loading config.")
    }
  }

}