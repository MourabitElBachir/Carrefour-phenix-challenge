package models

import java.util.UUID


case class ItemSale(id: Int, quantity: Int) extends Ordered[ItemSale] {

  def compare(that: ItemSale): Int = this.quantity compare that.quantity

  override def toString: String = this.id + "|" + this.quantity + "\n"
}

case class ItemSalePerShop(shopUUID: UUID, id: Int, quantity: Int) extends Ordered[ItemSalePerShop] {

  def compare(that: ItemSalePerShop): Int = this.quantity - that.quantity

  override def toString: String = this.id + "|" + this.quantity + "\n"

}

case class SalesPerShop(shopUUID: UUID, itemsSales: Stream[ItemSalePerShop])

