package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import org.scalatest.FunSuite

class TransactionTest extends FunSuite {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  test("Simple Transaction creation") {

    val expected = Some(Transaction(1,
      LocalDate.parse("20170514", formatter),
      UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
      531,
      5)
    )

    val result = Transaction.parse("1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5")

    assert(expected === result)
  }

  test("Transaction with no supported cells formats") {

    val transactionResult = Transaction
      .parse("txID|Date|ShopUUID|ItemID|Quantity")

    assert(transactionResult === None)
  }

  test("Transactions with number of cells least than required") {

    val expected = None

    val transactionResult1 = Transaction
      .parse("5|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71")

    val transactionResult2 = Transaction
      .parse("")


    assert(transactionResult1 === expected)
    assert(transactionResult2 === expected)
  }

  test("Transactions with number of cells greater than required") {

    val expected = Some(Transaction(1,
      LocalDate.parse("20170514", formatter),
      UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
      531,
      5)
    )

    val transactionResult = Transaction
      .parse("1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5|a|b|c|d|e")

    assert(transactionResult === expected)
  }
}
