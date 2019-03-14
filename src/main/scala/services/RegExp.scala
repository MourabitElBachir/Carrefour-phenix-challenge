package services

import java.io.FileNotFoundException

import scala.util.matching.Regex



object RegExp {

  val patternReferences: Regex = """reference_prod-([A-Za-z0-9\-]+)_[0-9]+\.data""".r
  val patternTransactions: Regex = """transactions_([0-9]+)\.data""".r

  def matchExp(pattern:Regex, line: String): String =
    line match {
      case pattern(group) => group
      case _ => throw new FileNotFoundException
    }
}
//
//
//  def matchMagasinFromReference(line: String): String = {
//
//    line match {
//      case pattern(group) => group
//      case _ => "Error"
//    }
//  }
//
//
//  def matchDateFromReference(line: String): String = {
//    val pattern = """reference_prod-[A-Za-z0-9\-]+_([0-9]+)\.data""".r
//
//    line match {
//      case pattern(group) => group
//      case _ => "Error"
//    }
//  }
//
//
//  def matchDateFromTransaction(line: String): String = {
//
//    val pattern = """transactions_([0-9]+)\.data""".r
//    line match {
//      case pattern(group) => group
//      case _ => "Error"
//    }
//  }
