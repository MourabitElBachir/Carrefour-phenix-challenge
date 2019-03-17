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

  def launchComputation(arguments: Arguments): Unit = {

    // First transaction file : specific to the date in args
    val transactionsFile = checkAndCreateTransactions(arguments, arguments.date)

    // The day specific transactions
    val dayTransactions = transactionsFile
    match {
      case Some((_, transactions)) => transactions
      case None => Stream.Empty
    }

    // Test if day transaction exists or not
    if(dayTransactions.nonEmpty) {

      // Compute sales per shop for a day & save result in a descending order with a defined number of lines in file
      val salesPerShops: Stream[SalesPerShop] = SalesPerDay.computePerShop(dayTransactions)
      SalesPerDay.saveSalesPerShop(
        Arguments.nbDescLines,
        Arguments.daySalesPerShopPath(arguments),
        salesPerShops)

      // Compute global sales for a day & save result in a descending order with a defined number of lines in file
      val globalSales: Stream[ItemSale] = SalesPerDay.computeGlobalSales(salesPerShops)
      SalesPerDay.saveGlobalSales(
        Arguments.nbDescLines,
        Arguments.dayGlobalSalesPath(arguments),
        globalSales)

      // Compute turnovers per shop for a day & save result in a descending order with a defined number of lines in file
      val turnoversPerShops: Stream[TurnoversPerShop] = TurnoverPerDay.computePerShop(
        dayTransactions,
        checkAndCreateReferences(arguments, arguments.date)._2)
      TurnoverPerDay.saveTurnoversPerShop(
        Arguments.nbDescLines,
        Arguments.dayTurnoversPerShopPath(arguments),
        turnoversPerShops)

      // Compute global turnovers for a day & save result in a descending order with a defined number of lines in file
      val globalTurnovers: Stream[Turnover] = TurnoverPerDay.computeGlobalTurnovers(turnoversPerShops)
      TurnoverPerDay.saveGlobalTurnovers(
        Arguments.nbDescLines,
        Arguments.dayGlobalTurnoversPath(arguments),
        globalTurnovers)


      // Get transactions for a specified number of the day's previous days
      val transactionsByDates: Stream[(LocalDate, Stream[Transaction])] =
        Arguments.previousDays
          .toStream
          .map(arguments.date.minusDays(_))
          .flatMap(checkAndCreateTransactions(arguments, _))

      // Test if transactions for previous days exist or not
      if(transactionsByDates.nonEmpty) {

        // Compute sales per shop for all previous days
        // & save result in a descending order with a defined number of lines in file
        val salesPerShopsByDates: Stream[SalesPerShop] = SalesPerWeek.computePerShop(
          transactionsByDates,
          salesPerShops
        )
        SalesPerWeek.saveSalesPerShop(
          Arguments.nbDescLines,
          Arguments.daySalesPerShopPath(arguments),
          salesPerShopsByDates)

        // Compute global sales for all previous days
        // & save result in a descending order with a defined number of lines in file
        val globalSalesByDates: Stream[ItemSale] = SalesPerWeek.computeGlobalSales(salesPerShopsByDates)
        SalesPerWeek.saveGlobalSales(
          Arguments.nbDescLines,
          Arguments.weekGlobalSalesPath(arguments),
          globalSalesByDates)

        // Compute turnovers per shop for all previous days
        // & save result in a descending order with a defined number of lines in file
        val turnoversPerShopsByDates: Stream[TurnoversPerShop] = TurnoverPerWeek.computePerShop(
          transactionsByDates,
          turnoversPerShops,
          transactionsByDates.map(transactionByDate => checkAndCreateReferences(arguments, transactionByDate._1))
        )
        TurnoverPerWeek.saveTurnoversPerShop(
          Arguments.nbDescLines,
          Arguments.weekTurnoversPerShopPath(arguments),
          turnoversPerShopsByDates)

        // Compute global turnovers for all previous days
        // & save result in a descending order with a defined number of lines in file
        val globalTurnoversByDates: Stream[Turnover] = TurnoverPerWeek.computeGlobalTurnovers(turnoversPerShopsByDates)
        TurnoverPerWeek.saveGlobalTurnovers(
          Arguments.nbDescLines,
          Arguments.weekGlobalTurnoversPath(arguments),
          globalTurnoversByDates)

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


