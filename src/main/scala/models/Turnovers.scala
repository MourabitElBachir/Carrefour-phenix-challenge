package models

import java.util.UUID

// Result Turnover
case class Turnover(itemID: Int, turnover: Double) extends Ordered[Turnover] {
  def compare(that: Turnover): Int = this.turnover compare that.turnover
  override def toString: String = this.itemID + "|" + Math.round(this.turnover * 100).toDouble / 100 + "\n"
}

// Turnover container
case class TurnoversPerShop(shopUUID: UUID, turnovers: Stream[Turnover])