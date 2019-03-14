package models

import java.io.File
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}

import services.Files

case class Arguments(inputFolder: File, outputFolder: File, dateChars: String, date:LocalDate)

object Arguments {

  val ventesGlobalTop100Prefix: String = "top_100_ventes_GLOBAL_"
  val ventesTop100Prefix: String  = "top_100_ventes_"
  val turnoverGlobalTop100Prefix: String  = "top_100_ca_GLOBAL_"
  val turnoverTop100Prefix: String  = "top_100_ca_"
  val transactionFilePrefix: String = "transactions_"
  val extension: String = ".data"
  val filenameSeparator: String = "_"
  val periodIndex: String = "-J7"
  val previousDays: Range = 1 to 6

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  // Use Some here instead of Exceptions
  def parse(args: Array[String]): ArgumentParser = {

    val inputFolder = new File(args(0))
    val outputFolder = new File(args(1))

    if(args.length < 3)
      ArgumentParser("You must set 3 arguments", None)

    else if(!inputFolder.isDirectory)
      ArgumentParser("Input file does not exist", None)

    else if(!outputFolder.isDirectory)
      ArgumentParser("Output file does not exist", None)

    else {

      try {

        val localDate: LocalDate = LocalDate.parse(args(2), formatter)

        val transactionsFile = join(
          new File(args(0)),
          transactionFilePrefix +
            args(2) +
            extension
        )

        if(!transactionsFile.exists())
          ArgumentParser("Transactions file of " + localDate +  " does not exist", None)

        else
        // Here we have at least our transaction file for the date specified by user
          ArgumentParser("Correct Arguments", Some(Arguments(new File(args(0)), new File(args(1)), args(2), localDate)))

      } catch {
        case e: DateTimeParseException => ArgumentParser("Date string could not be parsed", None)
      }
    }
  }


  def join(dir: File, parts: String*): File = { // filesNames : WrappedArray
    new File(dir, parts.mkString(File.separator))
  }

}
