package services

import org.scalatest.FunSuite

class RegExpTest extends FunSuite {

  test("RegExp.matchExp") {

    assert(RegExp.matchExp(
      "[A-Za-z]+_([0-9]+)".r,
      "transactions_20170514"
    ) === "20170514")

    assert(RegExp.matchExp(
      "[A-Za-z]+_([0-9]+)".r,
      "references_prod"
    ) === "")

  }

}
