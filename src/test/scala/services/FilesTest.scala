package services

import java.io.File

import org.scalatest.FunSuite


class FilesTest extends FunSuite {

  test("Files.filterFiles") {

    val expectedTrue = true

    val resultTrue = Files.filterMatching(
      List("reference_prod"),
      List("20170514.data")
    )(new File("reference_prod-e3d54d00-18be-45e1-b648-41147638bafe_20170514.data"))

    assert(expectedTrue === resultTrue)

    val expectedFalse = false

    val resultFalse = Files.filterMatching(
      List("reference_prod"),
      List("20170514.data")
    )(new File("reference_prod-e3d54d00-18be-45e1-b648-41147638bafe_20170513.data"))

    assert(expectedFalse === resultFalse)

  }

  test("Files.getListOfFiles test") {

    val expectedFileExist =
      new File(
        Seq("data_test",
          "transactions_20170514.data"
        ).mkString(File.separator)).isFile

    val resultFileExist = Files.getListOfFiles(
      new File("data_test"),
      List("transactions"),
      List("20170514.data")
    ).nonEmpty

    assert(expectedFileExist === resultFileExist)


    val expectedDoNotExist = true

    val resultFileDoNotExist = Files.getListOfFiles(
      new File("data_test"),
      List("transactions"),
      List("20170513.data")
    ).isEmpty

    assert(expectedDoNotExist === resultFileDoNotExist)

  }

}
