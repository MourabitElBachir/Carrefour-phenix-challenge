package services

import java.io.File

import org.scalatest.FunSuite


class FilesTest extends FunSuite {

  test("Files.filterFiles") {

    assert(Files.filterMatching(
      List("reference_prod"),
      List("20170514")
    )(new File("reference_prod-e3d54d00-18be-45e1-b648-41147638bafe_20170514.data")) === true)

    assert(Files.filterMatching(
      List("reference_prod"),
      List("20170514")
    )(new File("reference_prod-e3d54d00-18be-45e1-b648-41147638bafe_20170513.data")) === false)

  }

  test("Files.getListOfFiles") {

    assert(Files.getListOfFiles(
      new File("data", "data_test"),
      List("transactions"),
      List("20170514")
    ) === true)

    assert(Files.getListOfFiles(
      new File("data", "data_test"),
      List("transactions"),
      List("20170513")
    ) === false)

  }

  test("Files.checkFolderExistence") {

    assert(Files.getListOfFiles(
      new File("data", "data_test"),
      List("transactions"),
      List("20170514")
    ) === true)

    assert(Files.getListOfFiles(
      new File("data", "data_test"),
      List("transactions"),
      List("20170513")
    ) === false)

  }

}
