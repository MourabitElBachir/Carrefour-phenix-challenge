package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

// Input Transaction
case class Transaction(txID: Int,
                       date: LocalDate,
                       shopUUID: UUID,
                       itemID: Int,
                       quantity: Int)

// Transaction Parsing - All use cases are covered
object Transaction {

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssZ")
  val delimiter: String = """\|"""
  val cellsNb: Int = 5

  def parse(line: String): Option[Transaction] = {
    val array: Array[String] = line.split(delimiter).map(_.trim).filter(cell => cell.nonEmpty)
    if (array.length >= cellsNb) {
      try {
        val localDate = LocalDate.parse(array(1), formatter)
        Some(Transaction(array(0).toInt, localDate, UUID.fromString(array(2)), array(3).toInt, array(4).toInt))
      } catch {
        case _: Exception => None
      }
    } else {
      None
    }
  }

}