package services

import org.scalatest.FunSuite

class RegExpTest extends FunSuite {

  test("RegExp.matchExp : match a string and extract it") {

    val expected = "20170514"


    val result: String = RegExp.matchExp(
        "[A-Za-z]+_([0-9]+)".r,
        "transactions_20170514"
      )

    assert(expected === result)

  }

  test("RegExp.matchExp : string with no match") {

    val expected = ""


    val result: String = RegExp.matchExp(
      "[A-Za-z]+_([0-9]+)".r,
      "references_prod"
    )

    assert(expected === result)

  }

}
