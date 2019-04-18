package twitter.config.parser

import scala.util.Try
import scala.util.matching.Regex

object AnotherParser {
  implicit class Parser(str: String) {
    def parsePath(r: Regex): Try[String] = Try {
      val r(extracted) = str
      extracted
    }
  }

  // a path cannot contain new lines or any of the following characters: space, comma, <, >, =, ;
  val pathRegex = "(/[^\n ,<>=;]*/?)".r

  println("/var/tmp".parsePath(pathRegex))
}

