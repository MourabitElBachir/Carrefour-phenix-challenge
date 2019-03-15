package services

import scala.util.matching.Regex


object RegExp {

  val patternReferences: Regex = """reference_prod-([A-Za-z0-9\-]+)_[0-9]+\.data""".r
  val patternTransactions: Regex = """transactions_([0-9]+)\.data""".r

  def matchExp(pattern: Regex, line: String): String =
    line match {
      case pattern(group) => group
      case _ => new String("")
    }
}