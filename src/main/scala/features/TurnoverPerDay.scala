package features

import java.util.UUID

import models._


object TurnoverPerDay extends TurnoversComputation {

  def computePerShop(transactions: Stream[Transaction],
                     referencesStreams: Stream[(UUID, Stream[Item])]): Stream[TurnoversPerShop] = {

    val turnoversPerShop: Stream[TurnoversPerShop] = transactions
      .groupBy(transaction => (transaction.shopUUID, transaction.itemID))
      .mapValues(transactions => transactions.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity))
      .toStream
      .map(transactionMap => {
        val itemID = transactionMap._1._2
        val shopUUID = transactionMap._1._1
        val quantity = transactionMap._2
        val turnover = quantity * getItemPrice(itemID, shopUUID, referencesStreams)
        (shopUUID, itemID, quantity, turnover)

      }).groupBy(calculationResult => calculationResult._1)
      .map(turnovers => TurnoversPerShop(turnovers._1, turnovers._2.map(turnover => Turnover(turnover._2, turnover._4))))
      .toStream

    turnoversPerShop
  }

  def getItemPrice(itemID: Int, shopUUID: UUID, referencesStreams: Stream[(UUID, Stream[Item])]): Double = {
    referencesStreams
      .find(x => x._1 == shopUUID)
    match {
      case Some(tuple) =>
        tuple._2.find(item => item.id == itemID)
        match {
          case Some(item) => item.price
          case None => 0.0
        }
      case None => 0.0
    }
  }
}
