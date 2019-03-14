package models

import java.util.UUID


case class ItemSale(id : Int, quantity : Int) extends Ordered[ItemSale] {
  def compare(that: ItemSale): Int = this.quantity compare that.quantity

  override def toString: String= this.id + "|" + this.quantity + "\n"
}

object ItemSale {

  def parse(array: Array[String]): ItemSale = {

    ItemSale(array(0).toInt, array(1).toInt)
  }

}

case class ItemSalePerShop(shopUUID: UUID, id : Int, quantity : Int) extends Ordered[ItemSalePerShop] {
  def compare(that: ItemSalePerShop): Int = this.quantity - that.quantity
  override def toString: String= this.id + "|" + this.quantity + "\n"
}

object ItemSalePerShop {

  def parse(array: Array[String]): ItemSalePerShop = ItemSalePerShop(UUID.fromString(array(0)), array(1).toInt, array(2).toInt)

}

case class SalesPerShop(shopUUID: UUID, itemsSales: Stream[ItemSalePerShop])


//case class ShopSales(ventesProduitsMagasin: Stream[ItemSalePerShop]) {

//  def filter(shopUUID: UUID): ShopSales = this.copy(ventesProduitsMagasin = this.ventesProduitsMagasin.filter(vpm => vpm.shopUUID == shopUUID))
//
//
//  def sort(): ShopSales = this.copy(ventesProduitsMagasin = this.ventesProduitsMagasin.sorted.reverse)


  // def truncateTop100(): ShopSales = this.copy(ventesProduitsMagasin = ventesProduitsMagasin.take(100))

//}

