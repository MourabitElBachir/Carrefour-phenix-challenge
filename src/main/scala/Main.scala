import java.util.logging.{Level, Logger}

import features.MainComputation
import models.{ArgumentsDescription, Arguments}


object Main extends App {

  private val LOGGER = Logger.getLogger(getClass.getName)

  LOGGER.info("Starting Program ...")

  Arguments.parse(args) match {
    case ArgumentsDescription(_, Some(arguments)) =>
      MainComputation.launchCalculation(arguments)
      LOGGER.info("Check your output folder for results")

    case ArgumentsDescription(descriptions, None) =>
      descriptions.foreach(
        description => LOGGER.log(Level.SEVERE, description)
    )
  }
}

// Reste :

// 1- Tests Unitaires
// 2- Tester si le dossier data est vide
// 3- ItemSale => Item
