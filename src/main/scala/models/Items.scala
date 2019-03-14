package models

case class Item(id : Int,
                price : Double)

object Item {

  val delimiter: String = """\|"""
  val cellsNb: Int = 2

  def parse(line: String): Option[Item] = {
    val array: Array[String] = line.split(delimiter).map(_.trim).filter(cell => cell.nonEmpty)
    if(array.length >= cellsNb) {
      Some(Item(array(0).toInt, array(1).toDouble))
    } else {
      None
    }
  }

}

//case class ItemPerShop(shopUUID: UUID, id : Int, price : Double)
//
//object ItemPerShop {
//  def parse(shopUUID: UUID, array: Array[String]): ItemPerShop = {
//    ItemPerShop(shopUUID, array(0).toInt, array(1).toDouble)
//  }
//
//}

//case class Items(shopUUID: UUID, date: LocalDate, items: Stream[Item])
//
//object Items {
//  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//
//  def parse(shopUUID: UUID, date: String, items: Stream[Item]): Items = {
//    val localDate: LocalDate = LocalDate.parse(date, formatter)
//    Items(shopUUID, localDate, items)
//  }
//}