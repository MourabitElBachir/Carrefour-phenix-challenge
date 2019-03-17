package features

import java.util.UUID

import models.{Turnover, TurnoversPerShop}
import org.scalatest.FunSuite

class TurnoversComputationTest extends FunSuite {

  test ("Compute global turnovers Test")  {

    val expected: List[Turnover] = List(
      Turnover(600, 17.0)
      , Turnover(17, 9.0)
      , Turnover(531, 10.0)
      , Turnover(88, 8.0)
    )

    val turnoversPerShop: Stream[TurnoversPerShop] = Stream(

      TurnoversPerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), Stream(
        Turnover(
          531,
          6),
        Turnover(
          531,
          4),
        Turnover(
          17,
          9)
      )),
      TurnoversPerShop(UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"), Stream(
        Turnover(
          600,
          5),
        Turnover(
          600,
          12),
        Turnover(
          88,
          8)))
    )

    val result: List[Turnover] = TurnoverPerDay
      .computeGlobalTurnovers(turnoversPerShop)
      .toList

    assert(expected === result)
  }

}
