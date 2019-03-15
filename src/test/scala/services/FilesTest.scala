package services

import java.io.File

import org.scalatest.FunSuite


class FilesTest extends FunSuite {

  test("Files.filterFiles") {

    assert(Files.filterMatching(
      List("reference_prod"),
      List("20170514.data")
    )(new File("reference_prod-e3d54d00-18be-45e1-b648-41147638bafe_20170514.data")) === true)

    assert(Files.filterMatching(
      List("reference_prod"),
      List("20170514.data")
    )(new File("reference_prod-e3d54d00-18be-45e1-b648-41147638bafe_20170513.data")) === false)

  }

  test("Files.getListOfFiles") {

    assert(Files.getListOfFiles(
      new File("data_test"),
      List("transactions"),
      List("20170514.data")
    ) == Stream(
      new File(
        Seq("data",
          "data_test",
          "transactions_20170514.data"
        ).mkString(File.separator))))

    assert(Files.getListOfFiles(
      new File("data_test"),
      List("transactions"),
      List("20170513.data")
    ).isEmpty)

  }

}
