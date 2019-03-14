package services

import java.io.{File, PrintWriter}
import java.nio.file.Paths


object Files {

  def filterMatching(matchFirst: List[String], matchLast: List[String]): File =>
    Boolean = file => matchFirst.exists(file.getName.startsWith(_)) && matchLast.exists(file.getName.endsWith(_))

  def getListOfFiles(dir: File, matchFirst: List[String], matchLast: List[String]): Stream[File] = {
    if (dir.exists && dir.isDirectory) {
      dir.listFiles.filter(_.isFile).toStream.filter(filterMatching(matchFirst, matchLast))
    } else {
      Stream.empty
    }
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

  def r : String => Int = s => s.length

}
