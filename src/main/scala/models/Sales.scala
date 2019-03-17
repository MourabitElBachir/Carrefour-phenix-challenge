package models

import java.util.UUID


// ItemSale: is a selection of id and quatity cells from a Transaction
case class ItemSale(id: Int, quantity: Int) extends Ordered[ItemSale] {

  def compare(that: ItemSale): Int = this.quantity compare that.quantity

  override def toString: String = this.id + "|" + this.quantity + "\n"
}

// ItemSale by shopUUID
case class ItemSalePerShop(shopUUID: UUID, id: Int, quantity: Int) extends Ordered[ItemSalePerShop] {

  def compare(that: ItemSalePerShop): Int = this.quantity - that.quantity

  override def toString: String = this.id + "|" + this.quantity + "\n"

}

// ItemSale by shopUUID container
case class SalesPerShop(shopUUID: UUID, itemsSales: Stream[ItemSalePerShop])

