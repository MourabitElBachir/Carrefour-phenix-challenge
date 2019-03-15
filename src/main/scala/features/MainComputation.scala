package features

import java.time.LocalDate
import java.util.logging.Logger

import models._

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

      val turnoversPerShops: Stream[TurnoversPerShop] = TurnoverPerDay.computePerShop(arguments, transactionsObjects)
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
          arguments,
          transactionsByDates,
          salesPerShops
        )
        SalesPerWeek.saveSalesPerShop(Arguments.nbDescLines, Arguments.daySalesPerShopPath(arguments), salesPerShopsByDates)

        val globalSalesByDates: Stream[ItemSale] = SalesPerWeek.computeGlobalSales(salesPerShopsByDates)
        SalesPerWeek.saveGlobalSales(Arguments.nbDescLines, Arguments.weekGlobalSalesPath(arguments), globalSalesByDates)

        val turnoversPerShopsByDates: Stream[TurnoversPerShop] = TurnoverPerWeek.computePerShop(
          arguments,
          transactionsByDates,
          turnoversPerShops
        )
        TurnoverPerWeek.saveTurnoversPerShop(Arguments.nbDescLines, Arguments.dayTurnoversPerShopPath(arguments), turnoversPerShopsByDates)

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
}