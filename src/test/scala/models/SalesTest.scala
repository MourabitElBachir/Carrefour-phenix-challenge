package models

import java.util.UUID

import org.scalatest.FunSuite

class SalesTest extends FunSuite{

  test("ItemSales compare method test") {

    val expected = -1

    val itemSale1 = ItemSale(5, 66)
    val itemSale2 = ItemSale(6, 67)

    val result = itemSale1.compare(itemSale2)

    assert(expected === result)
  }

  test("ItemSales toString method test") {

    val expected = "5|66\n"

    val itemSale = ItemSale(5, 66)


    val result = itemSale.toString

    assert(expected === result)
  }

  test("ItemSalePerShop compare method test") {

    val expected = -1

    val itemSale1 = ItemSalePerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 5, 55)
    val itemSale2 = ItemSalePerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 6, 56)

    val result = itemSale1.compare(itemSale2)

    assert(expected === result)
  }

  test("ItemSalePerShop toString method test") {

    val expected = "5|66\n"

    val itemSale = ItemSalePerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 5, 66)

    val result = itemSale.toString

    assert(expected === result)
  }

}
