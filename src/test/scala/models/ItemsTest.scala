package models

import org.scalatest.FunSuite

class ItemsTest extends FunSuite {

  test("Simple item creation") {

    val expected = Some(Item(10, 30.86))

    val result = Item.parse("10|30.86")

    assert(expected === result)
  }

  test("Items with cells no supported format") {

    val itemResult = Transaction
      .parse("id|Date")

    assert(itemResult === None)
  }

  test("Items with number of cells least than required") {

    val itemResult1 = Item
      .parse("5|")

    val itemResult2 = Item
      .parse("")


    assert(itemResult1 === None)
    assert(itemResult2 === None)
  }

  test("Items with number of cells greater than required") {

    val expected = Some(Item(10, 30.86))

    val itemResult = Transaction
      .parse("10|30.86|a|b|c|d|e")

    assert(itemResult === expected)
  }

}
