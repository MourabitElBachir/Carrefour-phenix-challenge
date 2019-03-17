package models

// References files line container
case class Item(id: Int,
                price: Double)

// Item: is
object Item {

  val delimiter: String = """\|"""
  val cellsNb: Int = 2

  def parse(line: String): Option[Item] = {
    val array: Array[String] = line.split(delimiter).map(_.trim).filter(cell => cell.nonEmpty)
    if (array.length >= cellsNb) {
      try {
        Some(Item(array(0).toInt, array(1).toDouble))
      } catch {
        case _: Exception => None
      }
    } else {
      None
    }
  }

}