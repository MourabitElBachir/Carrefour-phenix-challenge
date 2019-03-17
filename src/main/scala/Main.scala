import java.util.logging.{Level, Logger}

import features.MainComputation
import models.{ArgumentsDescription, Arguments}


object Main extends App {

  private val LOGGER = Logger.getLogger(getClass.getName)

  LOGGER.info("Starting Program ...")

  Arguments.parse(args) match {
    case ArgumentsDescription(_, Some(arguments)) =>
      MainComputation.launchCalculation(arguments)

    case ArgumentsDescription(descriptions, None) =>
      descriptions.foreach(
        description => if (description.nonEmpty) LOGGER.log(Level.SEVERE, description)
      )
  }
}
