package features

import java.io.File
import java.util.UUID

import models.{ItemSale, SalesPerShop}
import services.Files

trait SalesComputation {

  def computeGlobalSales(shopsSales: Stream[SalesPerShop]): Stream[ItemSale] = {

    val globalSales: Stream[ItemSale] = shopsSales
      .flatMap(shopSales => shopSales.itemsSales)
      .groupBy(itemSale => itemSale.id)
      .mapValues(transaction => transaction.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity))
      .toStream
      .map(result => ItemSale(result._1, result._2))

    globalSales
  }

  def saveGlobalSales(nbLines: Int, outputPath: File, globalSales: Stream[ItemSale]): Unit = {
    Files.makeFile(outputPath, globalSales.sorted.reverse.take(nbLines))
  }

  def saveSalesPerShop(nbLines: Int, outputPath: UUID => File, shopsSales: Stream[SalesPerShop]): Unit = {
    shopsSales.foreach(shopSales => {
      Files.makeFile(outputPath(shopSales.shopUUID),
        shopSales.itemsSales.sorted.reverse.take(nbLines)
      )
    })
  }

}
