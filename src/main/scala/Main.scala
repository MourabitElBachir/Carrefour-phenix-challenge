import features.MainCalculation
import models.{ArgumentParser, Arguments}
import java.util.logging.{Level, Logger}


object Main {

  val LOGGER = Logger.getLogger(classOf[Nothing].getName)

  def main(args: Array[String]): Unit = {

    Arguments.parse(args)
    match {
      case ArgumentParser(_, Some(arguments)) => MainCalculation.lunchCalculation(arguments)
      case ArgumentParser(desc, None) => LOGGER.log(Level.SEVERE, desc)
    }

  }
}
