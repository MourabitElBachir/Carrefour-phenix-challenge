package features

import models.{ItemSale, SalesPerShop, Turnover, TurnoversPerShop}

trait Calculation {

  def computeGlobalSales(shopsSales: Stream[SalesPerShop]): Stream[ItemSale] = {

    val globalSales: Stream[ItemSale] = shopsSales.flatMap(shopSales => shopSales.itemsSales)
      .groupBy(itemSale => itemSale.id)
      .mapValues(transaction => transaction.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity))
      .toStream
      .map(result => ItemSale(result._1, result._2))

    globalSales
  }

  def computeGlobalTurnovers(turnoversPerShop: Stream[TurnoversPerShop]): Stream[Turnover] = {

    val dayTurnovers = turnoversPerShop.flatMap(shopTurnovers => shopTurnovers.turnovers)

    val globalTurnovers = dayTurnovers
      .groupBy(itemTurnover => itemTurnover.itemID)
      .mapValues(turnovers => {
        turnovers.foldLeft(0.0)((acc, turnover2) => acc + turnover2.turnover)
      })
      .toStream
      .map(globalResultMap => {
        Turnover(globalResultMap._1, globalResultMap._2)
      })

    globalTurnovers
  }

}
