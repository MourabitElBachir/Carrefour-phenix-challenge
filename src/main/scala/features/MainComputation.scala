package features

import java.io.File
import java.time.LocalDate
import java.util.UUID
import java.util.logging.Logger

import models._
import services.RegExp

import scala.io.Source


object MainComputation {

  private val LOGGER = Logger.getLogger(getClass.getName)

  def launchCalculation(arguments: Arguments): Unit = {

    val transactionsFile = checkAndCreateTransactions(arguments, arguments.date)

    val transactionsObjects = transactionsFile
    match {
      case Some((_, transactions)) => transactions
      case None => Stream.Empty
    }

    // Test if day transaction exists or not
    if(transactionsObjects.nonEmpty) {

      val salesPerShops: Stream[SalesPerShop] = SalesPerDay.computePerShop(transactionsObjects)
      SalesPerDay.saveSalesPerShop(Arguments.nbDescLines, Arguments.daySalesPerShopPath(arguments), salesPerShops)

      val globalSales: Stream[ItemSale] = SalesPerDay.computeGlobalSales(salesPerShops)
      SalesPerDay.saveGlobalSales(Arguments.nbDescLines, Arguments.dayGlobalSalesPath(arguments), globalSales)

      val turnoversPerShops: Stream[TurnoversPerShop] = TurnoverPerDay.computePerShop(
        transactionsObjects,
        checkAndCreateReferences(arguments, arguments.date)._2)

      TurnoverPerDay.saveTurnoversPerShop(Arguments.nbDescLines, Arguments.dayTurnoversPerShopPath(arguments), turnoversPerShops)

      val globalTurnovers: Stream[Turnover] = TurnoverPerDay.computeGlobalTurnovers(turnoversPerShops)
      TurnoverPerDay.saveGlobalTurnovers(Arguments.nbDescLines, Arguments.dayGlobalTurnoversPath(arguments), globalTurnovers)

      val transactionsByDates: Stream[(LocalDate, Stream[Transaction])] =
        Arguments.previousDays
          .toStream
          .map(arguments.date.minusDays(_))
          .flatMap(checkAndCreateTransactions(arguments, _))

      // Test if transactions for 7 days exist or not
      if(transactionsByDates.nonEmpty) {

        val salesPerShopsByDates: Stream[SalesPerShop] = SalesPerWeek.computePerShop(
          transactionsByDates,
          salesPerShops
        )
        SalesPerWeek.saveSalesPerShop(
          Arguments.nbDescLines,
          Arguments.daySalesPerShopPath(arguments),
          salesPerShopsByDates)

        val globalSalesByDates: Stream[ItemSale] = SalesPerWeek.computeGlobalSales(salesPerShopsByDates)
        SalesPerWeek.saveGlobalSales(Arguments.nbDescLines, Arguments.weekGlobalSalesPath(arguments), globalSalesByDates)

        val turnoversPerShopsByDates: Stream[TurnoversPerShop] = TurnoverPerWeek.computePerShop(
          transactionsByDates,
          turnoversPerShops,
          transactionsByDates.map(transactionByDate => checkAndCreateReferences(arguments, transactionByDate._1))
        )
        TurnoverPerWeek.saveTurnoversPerShop(
          Arguments.nbDescLines,
          Arguments.weekTurnoversPerShopPath(arguments),
          turnoversPerShopsByDates)

        val globalTurnoversByDates: Stream[Turnover] = TurnoverPerWeek.computeGlobalTurnovers(turnoversPerShopsByDates)
        TurnoverPerWeek.saveGlobalTurnovers(Arguments.nbDescLines, Arguments.weekGlobalTurnoversPath(arguments), globalTurnoversByDates)

      } else {

        LOGGER.info("Program terminated with no 6 previous days computation")

      }

      LOGGER.info("Check your output folder for results")

    } else {

      LOGGER.info("Program terminated with no actions")

    }

  }


  def checkAndCreateTransactions(arguments: Arguments, dateKey: LocalDate): Option[(LocalDate, Stream[Transaction])] = {
    val newFile = Arguments.transactionPath(dateKey)(arguments)
    if (newFile.isFile)
      Some(dateKey,
        Source.fromFile(newFile)
          .getLines()
          .toStream
          .flatMap(line => Transaction.parse(line))
      )
    else {
      LOGGER.info(s"Transactions for : $dateKey not found")
      None
    }

  }

  def checkAndCreateReferences(arguments: Arguments, dateKey: LocalDate): (LocalDate, Stream[(UUID, Stream[Item])]) = {

    // Get and verify the existence of references files
    val referencesFiles: Stream[File] = Arguments.referencesFilesByDate(dateKey)(arguments) //References of a specific date
    
    // Get items by shopUUID using references files
    val referencesStreams: Stream[(UUID, Stream[Item])] = referencesFiles
      .map(fileSrc => (fileSrc, RegExp.matchExp(RegExp.patternReferences, fileSrc.getName)))
      .filter(_._2.nonEmpty)
      .map(
        fileTuple => (UUID.fromString(fileTuple._2),
          io.Source.fromFile(fileTuple._1).getLines
            .flatMap(line => Item.parse(line))
            .toStream)
      )

    (dateKey, referencesStreams)
  }

}


