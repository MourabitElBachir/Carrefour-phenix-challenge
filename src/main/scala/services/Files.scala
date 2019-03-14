package services

import java.io.{File, PrintWriter}
import java.nio.file.Paths


object Files {

  def filterFiles(matchFirst: List[String], matchLast: List[String]): File =>
    Boolean = file => matchFirst.exists(file.getName.startsWith(_)) && matchLast.exists(file.getName.endsWith(_))

  def getListOfFiles(dir: File, matchFirst: List[String], matchLast: List[String]): Stream[File] = {
    if (dir.exists && dir.isDirectory) {
      dir.listFiles.filter(_.isFile).toStream.filter(filterFiles(matchFirst, matchLast))
    } else {
      Stream.empty
    }
  }

  def deleteFile(fileTemp: File): Boolean = {
    if (fileTemp.exists) {
      return fileTemp.delete()
    }
    false // Throw an exception
  }

  def makeFile[A](file: File, stream: Stream[A]): Unit = {

    val writer = new PrintWriter(file)
    try {
      stream.foreach(element => writer.write(element.toString))
    }
    finally writer.close()

  }

  def checkFolderExistence(path: String): Boolean = {
    Paths.get(path).toAbsolutePath
      .toFile
      .isDirectory
  }

  //  def getShopsList(dir: File, matchStrings: List[String]): Stream[String] = {
  //    dir.listFiles.filter(_.isFile).toStream.filter { file =>
  //      matchStrings.exists(file.getName.startsWith(_))
  //    }.map(file => RegExp.matchMagasinFromReference(file.getName))
  //  }

}
