package features

import java.time.LocalDate

import models._

object SalesPerWeek extends Computation {

  def computePerShop(arguments: Arguments,
                     transactionsByDates: Stream[(LocalDate, Stream[Transaction])],
                     dayTransaction: Stream[SalesPerShop]): Stream[SalesPerShop] = {

    val shopsSales: Stream[SalesPerShop] = transactionsByDates
      .flatMap(transactionsByDate => SalesPerDay
        .computePerShop(transactionsByDate._2))
      .append(dayTransaction)

    val allShopsSales: Stream[SalesPerShop] = shopsSales.flatMap(shopSales => shopSales.itemsSales
      .groupBy(itemSale => (itemSale.shopUUID, itemSale.id))
      .mapValues(sales => {
        sales.foldLeft(0)((acc, itemSale2) => acc + itemSale2.quantity)
      }))
      .map(turnoverMap => {
        val shopUUID = turnoverMap._1._1
        val itemID = turnoverMap._1._2
        val quantity = turnoverMap._2

        (shopUUID, itemID, quantity)
      })
      .groupBy(calculationResult => calculationResult._1)
      .map(salesPerShop => SalesPerShop(salesPerShop._1, salesPerShop._2.map(itemSale => ItemSalePerShop(itemSale._1, itemSale._2, itemSale._3))))
      .toStream

    allShopsSales
  }

}

