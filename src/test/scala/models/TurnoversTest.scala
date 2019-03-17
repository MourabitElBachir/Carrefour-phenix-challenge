package models

import org.scalatest.FunSuite

class TurnoversTest extends FunSuite {

  test("Turnover compare method test") {

    val expected = -1

    val turnover1 = Turnover(5, 66)
    val turnover2 = Turnover(6, 67)

    val result = turnover1.compare(turnover2)

    assert(expected === result)
  }

  test("Turnover toString method test") {

    val expected = "5|66.5\n"

    val turnover = Turnover(5, 66.5)


    val result = turnover.toString

    assert(expected === result)
  }

}
