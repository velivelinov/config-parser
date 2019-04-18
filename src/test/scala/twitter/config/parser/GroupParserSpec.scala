package twitter.config.parser

import org.scalatest.{FlatSpec, OptionValues}

class GroupParserSpec extends FlatSpec with ParserTestingUtils with GroupParser with OptionValues {

  import ParserTestingUtils._

  "groupTitle" should "parse a group name correctly" in {
    implicit val parser = groupTitle

    parsing("[title]") should equal("title")
    assertFail("titleWithoutBrackets")
  }

  "comment" should "parse comments correctly" in {
    implicit val parser = comment

    parsing("; some comment") should equal("; some comment")
  }

  "group" should "parse a single group correctly" in {
    implicit val parser = group

    val parsed = parsing(oneGroup)
    parsed.name should equal("ftp")
    parsed.settings should contain theSameElementsAs oneGroupSettings
  }

  it should "fail if an empty group is provided" in {
    implicit val parser = group

    assertFail("[title with no setting]")
  }
}
