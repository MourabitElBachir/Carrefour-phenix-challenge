package models

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import org.scalatest.FunSuite

class TransactionTest extends FunSuite {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  test("Simple item creation") {

    val expected = Some(Transaction(1,
      LocalDate.parse("20170514T223544+0100", formatter),
      UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
      531,
      5)
    )

    val result = Transaction.parse("1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5")

    assert(expected === result)
  }

  test("Transaction with no supported cells formats") {

    val itemResult = Transaction
      .parse("txID|Date|ShopUUID|ItemID|Quantity")

    assert(itemResult === None)
  }

  test("Transactions with number of cells least than required") {

    val itemResult1 = Item
      .parse("5|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71")

    val itemResult2 = Item
      .parse("")


    assert(itemResult1 === None)
    assert(itemResult2 === None)
  }
}
