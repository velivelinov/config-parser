package twitter.config.parser

import org.scalatest.{FlatSpec, OptionValues}

class ConfigParserSpec extends FlatSpec with ParserTestingUtils with ConfigParser with OptionValues {

  import ParserTestingUtils._

  "groups" should "parse multiple groups correctly" in {
    implicit val parser = groups

    val multipleGroups = s"$oneGroup$anotherGroup"

    val parsed = parsing(multipleGroups)
    parsed.map(_.name) should contain theSameElementsAs List("ftp", "http")
    parsed.find(_.name == "ftp").value.settings should contain theSameElementsAs oneGroupSettings
    parsed.find(_.name == "http").value.settings should contain theSameElementsAs anotherGroupSettings
  }

  it should "fail if a setting has no group" in {
    implicit val parser = groups

    val config =
      """"setting = value"
        |
        |[ftp]
        |open = no
      """.stripMargin

    assertFail(config)
  }
}
