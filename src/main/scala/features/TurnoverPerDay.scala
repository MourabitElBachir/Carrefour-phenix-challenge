package features

import java.util.UUID

import models._
import services.{Files, RegExp}


object TurnoverPerDay extends Calculation {

  def computePerShop(arguments: Arguments, transactions: Stream[Transaction]): Stream[TurnoversPerShop] = {

    // Get and verify the existence of references files
    val refenrencesFiles = Files.getListOfFiles(
      arguments.inputFolder,
      List("reference_prod"),
      List(arguments.dateChars + Arguments.extension) //References of a specific date
    )

    val referencesStreams: Stream[(UUID, Stream[Item])] = refenrencesFiles.map( // Test fileSrc
      fileSrc => (UUID.fromString(RegExp.matchExp(RegExp.patternReferences, fileSrc.getName)),
        io.Source.fromFile(fileSrc).getLines
          .flatMap(line => Item.parse(line))
          .toStream)
    )

    val turnoversPerShop = transactions
      .groupBy(transaction => (transaction.shopUUID, transaction.itemID))
      .mapValues(transactions => {
        transactions.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity)
      })
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

  def saveGlobalTurnovers(arguments: Arguments, globalTurnovers: Stream[Turnover]): Unit = {

    Files.makeFile(
      Arguments.join(
        arguments.outputFolder,
        Arguments.turnoverGlobalTop100Prefix +
          arguments.dateChars +
          Arguments.extension
      ),
      globalTurnovers.sorted.reverse.take(100)
    )
  }

  def saveTurnoversPerShop(arguments: Arguments, turnoversPerShop: Stream[TurnoversPerShop]): Unit = {

    turnoversPerShop.foreach(shopTurnovers => {

      Files.makeFile(
        Arguments.join(
          arguments.outputFolder,
          Arguments.turnoverTop100Prefix +
            shopTurnovers.shopUUID.toString +
            Arguments.filenameSeparator +
            arguments.dateChars +
            Arguments.extension
        ),
        shopTurnovers.turnovers.sorted.reverse.take(100)
      )

    })
  }

}
