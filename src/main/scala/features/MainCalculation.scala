package features

import java.time.LocalDate

import models.{Arguments, _}

import scala.io.{BufferedSource, Source}

object MainCalculation {


  def lunchCalculation(arguments: Arguments): Unit = {

    val transactionsFile: BufferedSource = Source.fromFile(
      Arguments.join(
        arguments.inputFolder,
        Arguments.transactionFilePrefix +
          arguments.dateChars +
          Arguments.extension
      ))

    val transactionsIterator: Iterator[String] = transactionsFile.getLines()

    val transactionsObjects: Stream[Transaction] = transactionsIterator.toStream
      .flatMap(line => Transaction.parse(line))

    val salesPerShops: Stream[SalesPerShop] = SalesPerDay.computePerShop(transactionsObjects)
    SalesPerDay.saveSalesPerShop(arguments, salesPerShops)

    val globalSales: Stream[ItemSale] = SalesPerDay.computeGlobalSales(salesPerShops)
    SalesPerDay.saveGlobalSales(arguments, globalSales)

    val turnoversPerShops: Stream[TurnoversPerShop] = TurnoverPerDay.computePerShop(arguments, transactionsObjects)
    TurnoverPerDay.saveTurnoversPerShop(arguments, turnoversPerShops)

    val globalTurnovers: Stream[Turnover] = TurnoverPerDay.computeGlobalTurnovers(turnoversPerShops)
    TurnoverPerDay.saveGlobalTurnovers(arguments, globalTurnovers)

    val transactionsByDates: Stream[(LocalDate, Stream[Transaction])] =
      Arguments.previousDays  // Test if files exists ; To be fixed Arguments.First -
        .toStream
        .map(arguments.date.minusDays(_))
        .flatMap(checkAndCreateTransactions(arguments, _))

    val salesPerShopsByDates: Stream[SalesPerShop] = SalesPerWeek.computePerShop(
      arguments,
      transactionsByDates,
      salesPerShops
    )
    SalesPerWeek.saveSalesPerShop(arguments, salesPerShopsByDates)

    val globalSalesByDates: Stream[ItemSale]  = SalesPerWeek.computeGlobalSales(salesPerShopsByDates)
    SalesPerWeek.saveGlobalSales(arguments, globalSalesByDates)

    val turnoversPerShopsByDates: Stream[TurnoversPerShop] = TurnoverPerWeek.computePerShop(
      arguments,
      transactionsByDates,
      turnoversPerShops
    )
    TurnoverPerWeek.saveTurnoversPerShop(arguments, turnoversPerShopsByDates)

    val globalTurnoversByDates: Stream[Turnover] = TurnoverPerWeek.computeGlobalTurnovers(turnoversPerShopsByDates)
    TurnoverPerWeek.saveGlobalTurnovers(arguments, globalTurnoversByDates)

  }

  def checkAndCreateTransactions(arguments: Arguments, dateKey: LocalDate): Option[(LocalDate, Stream[Transaction])] =
  {
    val newFile = Arguments.join(
      arguments.inputFolder,
      Arguments.transactionFilePrefix +
        dateKey.format(Arguments.formatter) +
        Arguments.extension
    )
    if(newFile.isFile)
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


//    val salesPerShopsByDates: Stream[(LocalDate, Stream[SalesPerShop])] = transactionsByDates
//      .map(transactionsByDates => (transactionsByDates._1, SalesPerWeek.computePerShop(arguments, transactionsByDates._2)))
//
//    val globalSalesByDates: Stream[(LocalDate, Stream[ItemSale])]  = salesPerShopsByDates
//      .map(salesPerShopsByDate => (salesPerShopsByDate._1, SalesPerWeek.computeGlobal(arguments, salesPerShopsByDate._2)))
//
//    val turnoversPerShopsByDates: Stream[(LocalDate, Stream[TurnoversPerShop])] = transactionsByDates
//      .map(turnoversPerShopByDate => (turnoversPerShopByDate._1, TurnoverPerWeek.computePerShop(arguments, turnoversPerShopByDate._2)))
//
//    val globalTurnoversByDates: Stream[(LocalDate, Stream[Turnover])] = turnoversPerShopsByDates
//      .map(turnoversPerShopsByDate => (turnoversPerShopsByDate._1, TurnoverPerWeek.computeGlobal(arguments, turnoversPerShopsByDate._2)))