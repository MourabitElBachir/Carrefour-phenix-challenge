package features

import java.io.File
import java.time.LocalDate

import models._

import scala.io.{BufferedSource, Source}

object MainComputation {


  def launchCalculation(arguments: Arguments): Unit = {

    val transactionsFile: BufferedSource = Source.fromFile(
      new File(
        arguments.inputFolder,
        s"${Arguments.transactionFilePrefix}${arguments.dateChars}${Arguments.extension}"
      )
    )

    val transactionsIterator: Iterator[String] = transactionsFile.getLines()

    val transactionsObjects: Stream[Transaction] = transactionsIterator.toStream
      .flatMap(line => Transaction.parse(line))

    val salesPerShops: Stream[SalesPerShop] = SalesPerDay.computePerShop(transactionsObjects)
    SalesPerDay.saveSalesPerShop(Arguments.daySalesPerShopPath(arguments), salesPerShops)

    val globalSales: Stream[ItemSale] = SalesPerDay.computeGlobalSales(salesPerShops)
    SalesPerDay.saveGlobalSales(Arguments.dayGlobalSalesPath(arguments), globalSales)

    val turnoversPerShops: Stream[TurnoversPerShop] = TurnoverPerDay.computePerShop(arguments, transactionsObjects)
    TurnoverPerDay.saveTurnoversPerShop(Arguments.dayTurnoversPerShopPath(arguments), turnoversPerShops)

    val globalTurnovers: Stream[Turnover] = TurnoverPerDay.computeGlobalTurnovers(turnoversPerShops)
    TurnoverPerDay.saveGlobalTurnovers(Arguments.dayGlobalTurnoversPath(arguments), globalTurnovers)

    val transactionsByDates: Stream[(LocalDate, Stream[Transaction])] =
      Arguments.previousDays // Test if files exists ; To be fixed Arguments.First -
        .toStream
        .map(arguments.date.minusDays(_))
        .flatMap(checkAndCreateTransactions(arguments, _))

    val salesPerShopsByDates: Stream[SalesPerShop] = SalesPerWeek.computePerShop(
      arguments,
      transactionsByDates,
      salesPerShops
    )
    SalesPerWeek.saveSalesPerShop(Arguments.daySalesPerShopPath(arguments), salesPerShopsByDates)

    val globalSalesByDates: Stream[ItemSale] = SalesPerWeek.computeGlobalSales(salesPerShopsByDates)
    SalesPerWeek.saveGlobalSales(Arguments.weekGlobalSalesPath(arguments), globalSalesByDates)

    val turnoversPerShopsByDates: Stream[TurnoversPerShop] = TurnoverPerWeek.computePerShop(
      arguments,
      transactionsByDates,
      turnoversPerShops
    )
    TurnoverPerWeek.saveTurnoversPerShop(Arguments.dayTurnoversPerShopPath(arguments), turnoversPerShopsByDates)

    val globalTurnoversByDates: Stream[Turnover] = TurnoverPerWeek.computeGlobalTurnovers(turnoversPerShopsByDates)
    TurnoverPerWeek.saveGlobalTurnovers(Arguments.weekGlobalTurnoversPath(arguments), globalTurnoversByDates)

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
    else
      None
  }
}