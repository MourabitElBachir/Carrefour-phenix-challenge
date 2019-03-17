package features

import java.io.File
import java.util.UUID

import models.{Turnover, TurnoversPerShop}
import services.Files

trait TurnoversComputation {

  def computeGlobalTurnovers(turnoversPerShop: Stream[TurnoversPerShop]): Stream[Turnover] = {

    val globalTurnovers = turnoversPerShop
      .flatMap(shopTurnovers => shopTurnovers.turnovers)
      .groupBy(itemTurnover => itemTurnover.itemID)
      .mapValues(turnovers => turnovers.foldLeft(0.0)((acc, turnover2) => acc + turnover2.turnover))
      .toStream
      .map(result => Turnover(result._1, result._2))

    globalTurnovers
  }

  def saveGlobalTurnovers(nbLines: Int, outputPath: File, globalTurnovers: Stream[Turnover]): Unit = {
    Files.makeFile(outputPath, globalTurnovers.sorted.reverse.take(nbLines))
  }

  def saveTurnoversPerShop(nbLines: Int, outputPath: UUID => File, shopsTurnovers: Stream[TurnoversPerShop]): Unit = {
    shopsTurnovers.foreach(shopTurnovers => {
      Files.makeFile(outputPath(shopTurnovers.shopUUID),
        shopTurnovers.turnovers.sorted.reverse.take(nbLines)
      )
    })
  }

}
