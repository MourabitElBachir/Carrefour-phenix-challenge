package models

import java.io.File
import java.time.LocalDate

import org.scalatest.FunSuite

class ArgumentConfigTest extends FunSuite {

  test("Test Argument Option : Is empty") {

    val expected = true

    val result = ArgumentOption("",
      None,
      None,
      None).empty()

    assert(expected === result)
  }

  test("Test Argument Option : Is not empty") {

    val expected = false

    val result = ArgumentOption("",
      Some(new File("data_test")),
      None,
      None).empty()

    assert(expected === result)
  }

}
