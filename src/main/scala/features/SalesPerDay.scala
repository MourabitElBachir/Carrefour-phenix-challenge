package features

import models._
import services.Files


object SalesPerDay extends Calculation {

  def computePerShop(transactions: Stream[Transaction]): Stream[SalesPerShop] = {

    val shopsSales: Stream[SalesPerShop] = transactions
      .groupBy(transaction => (transaction.shopUUID, transaction.itemID))
      .mapValues(transaction => transaction.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity))
      .toStream
      .map(result => ItemSalePerShop(result._1._1, result._1._2, result._2))
      .groupBy(calculationResult => calculationResult.shopUUID)
      .map(salesPerShop => SalesPerShop(salesPerShop._1, salesPerShop._2))
      .toStream

//    val shopsList = salesCalculatedPerShop
//      .groupBy(itemSale => itemSale.shopUUID)
//      .keys
//      .toStream

    // Convert tuple to class
//    val shopsSales = shopsList
//      .map(shopUUID =>
//        SalesPerShop(shopUUID, salesCalculatedPerShop.filter(itemSale => itemSale.shopUUID == shopUUID))
//      )

    shopsSales
  }

  def saveGlobalSales(arguments: Arguments, globalSales: Stream[ItemSale]): Unit = {

    Files.makeFile(
      Arguments.join(
        arguments.outputFolder,
        Arguments.ventesGlobalTop100Prefix +
          arguments.dateChars +
          Arguments.extension
      ),
      globalSales.sorted.reverse.take(100)
    )

  }

  def saveSalesPerShop(arguments: Arguments, shopsSales: Stream[SalesPerShop]): Unit = {

    shopsSales.foreach(shopSales => {

      Files.makeFile(
        Arguments.join(
          arguments.outputFolder,
          Arguments.ventesTop100Prefix +
            shopSales.shopUUID.toString +
            Arguments.filenameSeparator +
            arguments.dateChars +
            Arguments.extension
        ),
        shopSales.itemsSales.sorted.reverse.take(100)
      )

    })
  }

}
