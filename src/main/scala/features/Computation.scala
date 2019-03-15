package features

import java.io.File
import java.util.UUID

import models._
import services.Files

trait Computation {

  def computeGlobalSales(shopsSales: Stream[SalesPerShop]): Stream[ItemSale] = {

    val globalSales: Stream[ItemSale] = shopsSales
      .flatMap(shopSales => shopSales.itemsSales)
      .groupBy(itemSale => itemSale.id)
      .mapValues(transaction => transaction.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity))
      .toStream
      .map(result => ItemSale(result._1, result._2))

    globalSales
  }

  def computeGlobalTurnovers(turnoversPerShop: Stream[TurnoversPerShop]): Stream[Turnover] = {

    val globalTurnovers = turnoversPerShop
      .flatMap(shopTurnovers => shopTurnovers.turnovers)
      .groupBy(itemTurnover => itemTurnover.itemID)
      .mapValues(turnovers => turnovers.foldLeft(0.0)((acc, turnover2) => acc + turnover2.turnover))
      .toStream
      .map(result => Turnover(result._1, result._2))

    globalTurnovers
  }

  def saveGlobalSales(outputPath: File, globalSales: Stream[ItemSale]): Unit = {
    Files.makeFile(outputPath, globalSales.sorted.reverse.take(100))
  }

  def saveGlobalTurnovers(outputPath: File, globalTurnovers: Stream[Turnover]): Unit = {
    Files.makeFile(outputPath, globalTurnovers.sorted.reverse.take(100))
  }

  def saveSalesPerShop(outputPath: UUID => File, shopsSales: Stream[SalesPerShop]): Unit = {
    shopsSales.foreach(shopSales => {
      Files.makeFile(outputPath(shopSales.shopUUID),
        shopSales.itemsSales.sorted.reverse.take(100)
      )
    })
  }

  def saveTurnoversPerShop(outputPath: UUID => File, shopsTurnovers: Stream[TurnoversPerShop]): Unit = {
    shopsTurnovers.foreach(shopTurnovers => {
      Files.makeFile(outputPath(shopTurnovers.shopUUID),
        shopTurnovers.turnovers.sorted.reverse.take(100)
      )
    })
  }

}
