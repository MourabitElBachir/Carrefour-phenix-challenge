package features

import java.time.LocalDate

import models._

object TurnoverPerWeek extends Computation {

  def computePerShop(arguments: Arguments,
                     transactionsByDates: Stream[(LocalDate, Stream[Transaction])],
                     dayShopsTurnovers: Stream[TurnoversPerShop]): Stream[TurnoversPerShop] = {

    // Price calculation :
    val shopsTurnovers: Stream[TurnoversPerShop] = transactionsByDates
      .flatMap(dateTransactions => TurnoverPerDay.computePerShop(
        arguments.copy(dateChars = dateTransactions._1.format(Arguments.formatter), date = dateTransactions._1),
        dateTransactions._2)
      ).append(dayShopsTurnovers)

    // Result Aggregation :
    val allShopsTurnovers: Stream[TurnoversPerShop] = shopsTurnovers
      .flatMap(shopTurnover => shopTurnover.turnovers
        .map(turnover => (shopTurnover.shopUUID, turnover.itemID, turnover.turnover)))
      .groupBy(turnover => (turnover._1, turnover._2))
      .mapValues(turnovers => {
        turnovers.foldLeft(0.0)((acc, turnover2) => acc + turnover2._3)
      })
      .toStream
      .map(turnoverMap => {
        val shopUUID = turnoverMap._1._1
        val itemID = turnoverMap._1._2
        val turnover = turnoverMap._2

        (shopUUID, itemID, turnover)

      })
      .groupBy(calculationResult => calculationResult._1)
      .map(turnovers => TurnoversPerShop(turnovers._1, turnovers._2.map(turnover => Turnover(turnover._2, turnover._3))))
      .toStream

    allShopsTurnovers
  }
}
