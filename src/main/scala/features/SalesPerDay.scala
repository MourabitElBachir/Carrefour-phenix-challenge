package features

import models._


object SalesPerDay extends SalesComputation {

  // Sales by shop generation
  def computePerShop(transactions: Stream[Transaction]): Stream[SalesPerShop] = {

    val shopsSales: Stream[SalesPerShop] = transactions
      .groupBy(transaction => (transaction.shopUUID, transaction.itemID))
      .mapValues(transaction => transaction.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity))
      .toStream
      .map(result => ItemSalePerShop(result._1._1, result._1._2, result._2))
      .groupBy(result => result.shopUUID)
      .map(salesPerShop => SalesPerShop(salesPerShop._1, salesPerShop._2))
      .toStream

    shopsSales
  }

}


