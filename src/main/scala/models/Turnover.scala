package models

import java.util.UUID


case class Turnover(itemID : Int, turnover: Double) extends Ordered[Turnover] {
  def compare(that: Turnover): Int = this.turnover compare that.turnover

  override def toString: String = this.itemID + "|" + Math.round(this.turnover * 100).toDouble/100 + "\n"
}

object Turnover {

  def parse(array: Array[String]) : Turnover = Turnover(array(0).toInt, array(2).toDouble)

}

case class TurnoversPerShop(shopUUID : UUID, turnovers: Stream[Turnover])

//object TurnoverPerShop {
//
//  def parse(array: Array[String]) : TurnoverPerShop = Turnover(array(0).toInt, array(1).toInt)
//
//}
