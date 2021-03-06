package models

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import models.Arguments.nextOption
import org.scalatest.FunSuite


class ArgumentsTest extends FunSuite {

  private val separator: String = " "
  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")


  test("Simple Arguments nextOption function test") {

    val inputDir = new File("data_test/")

      val expectedMap = Map("input" -> ArgumentOption("", Some(inputDir), None, None))

      val args = "-i data_test".split(separator)

      val resultMap = nextOption(Map(), args.toList)

      assert(resultMap === expectedMap)

  }


  test("Simple Arguments parse method test - must set 3 arguments") {

    val args = "aaaa bbbb cccc".split(separator)

    val expectedArguments = ArgumentsDescription(
      List(s"${Arguments.argumentsNb} arguments required to run program" +
            " | Example : -i inputFolder -o outputFolder -d 20170514"),
      None
    )

    val resultArguments = Arguments.parse(
      args
    )

    assert(expectedArguments === resultArguments)

  }


  test("Simple Arguments parse method test - incorrect input argument") {

    val args = "-i data_test/not_found".split(separator)

    val expectedArguments = ArgumentsDescription(
      List(
        "Input directory not found",
        s"${Arguments.argumentsNb} arguments required to run program" +
          " | Example : -i inputFolder -o outputFolder -d 20170514"),
      None
    )

    val resultArguments = Arguments.parse(
      args
    )

    assert(expectedArguments === resultArguments)

  }


  test("Simple Arguments parse method test - Correct arguments") {

    val args = "-i data_test -o data_test -d 20170514".split(" ")

    val expectedArguments = ArgumentsDescription(
      List("Correct arguments"),
      Some(
        Arguments(
          new File("data_test"),
          new File("data_test"),
          "20170514",
          LocalDate.parse("20170514",formatter)
        )
      )
    )

    val resultArguments = Arguments.parse(
      args
    )

    assert(expectedArguments === resultArguments)

  }


  test("VerifyFile function test - Directory found") {

    val expected = ArgumentOption(
      "",
      Some(new File("data_test")),
      None,
      None
    ).file.getOrElse(new File("")).isDirectory

    val result = Arguments.verifyFile(
      "data_test",
      "Input"
    ).file.getOrElse(new File("")).isDirectory

    assert(expected === result)
  }


  test("VerifyFile function test - Directory not found") {

    val expectedArgumentOption = ArgumentOption(
      "Input directory not found",
      None,
      None,
      None
    )

    val resultArgumentsOption = Arguments.verifyFile(
      "data_test/not_found",
      "Input"
    )

    assert(expectedArgumentOption === resultArgumentsOption)
  }

  test("Verify date test - Correct date format") {

    val expectedArgumentOption = ArgumentOption(
      "",
      None,
      Some("20170514"),
      Some(
        LocalDate.parse(
          "20170514",
          formatter)
      )
    )

    val resultArgumentsOption = Arguments.verifyDate(
      "20170514"
    )

    assert(expectedArgumentOption === resultArgumentsOption)
  }

  test("Verify date test - Incorrect date format") {

    val expectedArgumentOption = ArgumentOption("Date string could not be parsed",None,None,None)

    val resultArgumentsOption = Arguments.verifyDate(
      "date"
    )

    assert(expectedArgumentOption === resultArgumentsOption)

  }

}
