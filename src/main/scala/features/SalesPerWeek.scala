package features

import java.time.LocalDate

import models._
import services.{Files, RegExp}

object SalesPerWeek extends Calculation { // Heritage required

  def computePerShop(arguments: Arguments,
                     transactionsByDates: Stream[(LocalDate, Stream[Transaction])],
                     dayTransaction: Stream[SalesPerShop]): Stream[SalesPerShop] = {

    val shopsSales: Stream[SalesPerShop] = transactionsByDates
      .flatMap(transactionsByDate => SalesPerDay
        .computePerShop(transactionsByDate._2))
      .append(dayTransaction)

    val allShopsSales = shopsSales.flatMap(shopSales => shopSales.itemsSales
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


  def saveGlobalSales(arguments: Arguments, globalSales: Stream[ItemSale]): Unit = {

    Files.makeFile(
      Arguments.join(
        arguments.outputFolder,
        Arguments.ventesGlobalTop100Prefix +
          arguments.dateChars +
          Arguments.periodIndex +
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
            Arguments.periodIndex +
            Arguments.extension
        ),
        shopSales.itemsSales.sorted.reverse.take(100)
      )

    })
  }

}

