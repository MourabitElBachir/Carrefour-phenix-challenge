package features

import java.time.LocalDate
import java.util.UUID

import models._

object TurnoverPerWeek extends TurnoversComputation {

  // Turnovers by shop generation
  def computePerShop(transactionsByDates: Stream[(LocalDate, Stream[Transaction])],
                     dayShopsTurnovers: Stream[TurnoversPerShop],
                     referencesStreams: Stream[(LocalDate, Stream[(UUID, Stream[Item])])]): Stream[TurnoversPerShop] = {

    // Price calculation :
    val shopsTurnovers: Stream[TurnoversPerShop] = transactionsByDates
      .flatMap(dateTransactions => TurnoverPerDay.computePerShop(
        dateTransactions._2,
        referencesStreams
          .filter(referencesForDate => referencesForDate._1 == dateTransactions._1)
          .flatMap(references => references._2))
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
      .groupBy(result => result._1)
      .map(turnovers => TurnoversPerShop(turnovers._1, turnovers._2.map(turnover => Turnover(turnover._2, turnover._3))))
      .toStream

    allShopsTurnovers
  }
}
