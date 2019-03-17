package features

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import models._
import org.scalatest.FunSuite

class SalesComputationTest extends FunSuite {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")


  test ("Compute global sales Test")  {

    val expected: List[ItemSale] = List(ItemSale(600, 17)
      , ItemSale(17, 9)
      , ItemSale(531, 10)
      , ItemSale(88, 8)
    )

    val shopsSales: Stream[SalesPerShop] = Stream(

      SalesPerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), Stream(
        ItemSalePerShop(
          UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
          531,
          6),
        ItemSalePerShop(
          UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
          531,
          4),
        ItemSalePerShop(
          UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
          17,
          9)
      )),
      SalesPerShop(UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"), Stream(
        ItemSalePerShop(
          UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
          600,
          5),
        ItemSalePerShop(
          UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
          600,
          12),
        ItemSalePerShop(
          UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
          88,
          8)))
    )

    val result: List[ItemSale] = SalesPerDay
      .computeGlobalSales(shopsSales)
      .toList

    println(expected)
    println(result)
    assert(expected === result)
  }
  
}
