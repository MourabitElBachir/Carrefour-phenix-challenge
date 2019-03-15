package models

import java.io.File
import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.UUID

import scala.collection.mutable.ArrayBuffer

case class Arguments(inputFolder: File, outputFolder: File, dateChars: String, date: LocalDate)

object Arguments {


  def nextOption(map : Map[String, ArgumentOption], list: List[String]) : Map[String, ArgumentOption] = {
    list match {
      case Nil => map
      case "-i" :: value :: tail =>
        nextOption(map ++ Map("input" -> verifyFile(value, "Input")), tail)
      case "-o" :: value :: tail =>
        nextOption(map ++ Map("output" -> verifyFile(value, "Output")), tail)
      case "-d" :: value :: tail =>
        nextOption(map ++ Map("date" -> verifyDate(value)), tail)
      case _ :: Nil =>  nextOption(map, list.tail)
      case _ :: tail => nextOption(map, tail)

    }
  }

  def verifyFile(dirPath: String, whichFile: String): ArgumentOption =  {
    val dir = new File(dirPath)
    if (dir.isDirectory) ArgumentOption("Correct", Some(dir), None, None)
    else ArgumentOption(s"$whichFile file not found", None, None, None)
  }

  def verifyDate(dateString: String): ArgumentOption =  {
    try {
      val localDate: LocalDate = LocalDate.parse(dateString, formatter)
      ArgumentOption("Correct", None, Some(dateString), Some(localDate))
    } catch {
      case _: DateTimeParseException => ArgumentOption("Date string could not be parsed", None, None, None)
    }
  }


  // Use Some here instead of Exceptions
  def parse(args: Array[String]): ArgumentsDescription = {

    val mapOptions = nextOption(Map(), args.toList)

    if(mapOptions
      .mapValues(arg => arg.na())
      .values
      .foldLeft(false)((a,b) => a || b)) {

      val descSeq: ArrayBuffer[String] = ArrayBuffer[String]()

      mapOptions
        .values
        .filter(x => x.na())
        .foreach(x => descSeq.append(x.desc))

      descSeq.append(s"You must set $argumentsNb arguments")

      ArgumentsDescription(descSeq, None)

    } else {

      val inputFolder = mapOptions("input").file.get
      val outputFolder = mapOptions("output").file.get
      val localDate: LocalDate = mapOptions("date").dateOption.get
      val dateString: String = mapOptions("date").dateStringOption.get

      ArgumentsDescription(Seq("Correct arguments"), Some(Arguments(inputFolder, outputFolder, dateString, localDate)))
    }
  }

  def transactionPath(dateKey: LocalDate): Arguments => File = args => new File(args.inputFolder,
    s"${Arguments.transactionFilePrefix}${dateKey.format(Arguments.formatter)}${Arguments.extension}")

  def dayGlobalSalesPath: Arguments => File = args => new File(args.outputFolder,
    s"${Arguments.ventesGlobalTop100Prefix}${args.dateChars}${Arguments.extension}")

  def daySalesPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    s"${Arguments.ventesTop100Prefix}${shopUUID.toString}${Arguments.filenameSeparator}${args.dateChars}${Arguments.extension}")

  def dayGlobalTurnoversPath: Arguments => File = args => new File(args.outputFolder,
    s"${Arguments.turnoverGlobalTop100Prefix}${args.dateChars}${Arguments.extension}")

  def dayTurnoversPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    s"${Arguments.turnoverTop100Prefix}${shopUUID.toString}${Arguments.filenameSeparator}${args.dateChars}${Arguments.extension}")

  def weekGlobalSalesPath: Arguments => File = args => new File(args.outputFolder,
    s"${Arguments.ventesGlobalTop100Prefix}${args.dateChars}${Arguments.periodIndex }${Arguments.extension}")

  def weekSalesPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    s"${Arguments.ventesTop100Prefix}${shopUUID.toString}${Arguments.filenameSeparator}${args.dateChars}${Arguments.periodIndex }${Arguments.extension}")

  def weekGlobalTurnoversPath: Arguments => File = args => new File(args.outputFolder,
    s"${Arguments.turnoverGlobalTop100Prefix}${args.dateChars}${Arguments.periodIndex }${Arguments.extension}")

  def weekTurnoversPerShopPath(args: Arguments): UUID => File = shopUUID => new File(args.outputFolder,
    s"${Arguments.turnoverTop100Prefix}${shopUUID.toString}${Arguments.filenameSeparator}${args.dateChars}${Arguments.periodIndex }${Arguments.extension}")


  def join(dir: File, parts: String*): File = { // filesNames : WrappedArray
    new File(dir, parts.mkString(File.separator))
  }

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
  val argumentsNb = 3

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
}
