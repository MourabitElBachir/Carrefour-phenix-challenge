package models

import java.io.File
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.UUID

import services.Files


case class Arguments(inputFolder: File, outputFolder: File, dateChars: String, date: LocalDate)

object Arguments {

  def nextOption(map : Map[String, ArgumentOption], list: List[String]) : Map[String, ArgumentOption] = {
    list match {
      case "-i" :: value :: tail =>
        nextOption(map ++ Map("input" -> verifyFile(value, "Input")), tail)
      case "-o" :: value :: tail =>
        nextOption(map ++ Map("output" -> verifyFile(value, "Output")), tail)
      case "-d" :: value :: tail =>
        nextOption(map ++ Map("date" -> verifyDate(value)), tail)
      case _ :: Nil =>  nextOption(map, list.tail)
      case _ :: tail => nextOption(map, tail)
      case Nil => map

    }
  }

  def verifyFile(dirPath: String, whichFile: String): ArgumentOption =  {
    val dir = new File(dirPath)
    if (dir.isDirectory) ArgumentOption("", Some(dir), None, None)
    else ArgumentOption(s"$whichFile file not found", None, None, None)
  }

  def verifyDate(dateString: String): ArgumentOption =  {
    try {
      val localDate: LocalDate = LocalDate.parse(dateString, formatter)
      ArgumentOption("", None, Some(dateString), Some(localDate))
    } catch {
      case _: DateTimeParseException => ArgumentOption("Date string could not be parsed", None, None, None)
    }
  }

  // Use Some here instead of Exceptions
  def parse(args: Array[String]): ArgumentsDescription = {

    val mapOptions = nextOption(Map(), args.toList)

    val inputFolderArgOption = mapOptions.getOrElse("input", ArgumentOption("", None, None, None))
    val outputFolderArgOption = mapOptions.getOrElse("output", ArgumentOption("", None, None, None))
    val localDateArgOption = mapOptions.getOrElse("date", ArgumentOption("", None, None, None))
    val dateStringArgOption = mapOptions.getOrElse("date", ArgumentOption("", None, None, None))


    (inputFolderArgOption, outputFolderArgOption, dateStringArgOption, localDateArgOption) match {

      // A : When arguments are correct
      case (
        ArgumentOption(_, Some(inputFolder), None, None),
        ArgumentOption(_, Some(outputFolder), None, None),
        ArgumentOption(_, None, Some(dateString), _),
        ArgumentOption(_, None, _, Some(localDate))
        ) => ArgumentsDescription(
        Seq("Correct arguments"),
        Some(Arguments(
          inputFolder,
          outputFolder,
          dateString,
          localDate))
      )

      // B: Where arguments are not correct
      case (
        ArgumentOption(desc1, _, _, _),
        ArgumentOption(desc2, _, _, _),
        ArgumentOption(desc3, _, _, _),
        ArgumentOption(desc4, _, _, _)
        ) => ArgumentsDescription(
        Seq(
          desc1,
          desc2,
          desc3,
          desc4,
          s"$argumentsNb arguments required to run program | Example : -i inputFolder -o outputFolder -d 20170514"),
        None)
    }
  }

  def referencesFilesByDate(dateKey: LocalDate): Arguments => Stream[File] = args => Files.getListOfFiles(
    args.inputFolder,
    List(Arguments.referencesFilePrefix),
    List(dateKey.format(Arguments.formatter) + Arguments.extension) //References of a specific date
  )

  def transactionPath(dateKey: LocalDate): Arguments => File = args => new File(args.inputFolder,
    "%s%s%s".format(
      Arguments.transactionFilePrefix,
      dateKey.format(Arguments.formatter),
      Arguments.extension)
  )

  def dayGlobalSalesPath: Arguments => File = args => new File(args.outputFolder,
    "%s%s%s".format(
      Arguments.ventesGlobalTop100Prefix,
      args.dateChars,
      Arguments.extension)
  )

  def daySalesPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    "%s%s%s%s%s".format(
      Arguments.ventesTop100Prefix,
      shopUUID.toString,
      Arguments.filenameSeparator,
      args.dateChars,
      Arguments.extension)
  )

  def dayGlobalTurnoversPath: Arguments => File = args => new File(args.outputFolder,
    "%s%s%s".format(
      Arguments.turnoverGlobalTop100Prefix,
      args.dateChars,
      Arguments.extension)
  )

  def dayTurnoversPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    "%s%s%s%s%s".format(
      Arguments.turnoverTop100Prefix,
      shopUUID.toString,
      Arguments.filenameSeparator,
      args.dateChars,
      Arguments.extension)
  )

  def weekGlobalSalesPath: Arguments => File = args => new File(args.outputFolder,
    "%s%s%s%s".format(
      Arguments.ventesGlobalTop100Prefix,
      args.dateChars,
      Arguments.periodIndex,
      Arguments.extension)
  )

  def weekSalesPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    "%s%s%s%s%s%s".format(
      Arguments.ventesTop100Prefix,
      shopUUID.toString,
      Arguments.filenameSeparator,
      args.dateChars,
      Arguments.periodIndex,
      Arguments.extension)
  )

  def weekGlobalTurnoversPath: Arguments => File = args => new File(args.outputFolder,
    "%s%s%s%s".format(
      Arguments.turnoverGlobalTop100Prefix,
      args.dateChars,
      Arguments.periodIndex,
      Arguments.extension)
  )

  def weekTurnoversPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    "%s%s%s%s%s%s".format(
      Arguments.turnoverTop100Prefix,
      shopUUID.toString,
      Arguments.filenameSeparator,
      args.dateChars,
      Arguments.periodIndex,
      Arguments.extension)
  )


  val ventesGlobalTop100Prefix: String = "top_100_ventes_GLOBAL_"
  val ventesTop100Prefix: String = "top_100_ventes_"
  val turnoverGlobalTop100Prefix: String = "top_100_ca_GLOBAL_"
  val turnoverTop100Prefix: String = "top_100_ca_"
  val transactionFilePrefix: String = "transactions_"
  val referencesFilePrefix: String = "reference_prod"
  val extension: String = ".data"
  val filenameSeparator: String = "_"
  val periodIndex: String = "-J7"
  val previousDays: Range = 1 to 6
  val argumentsNb: Int = 3
  val nbDescLines: Int = 100

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
}
