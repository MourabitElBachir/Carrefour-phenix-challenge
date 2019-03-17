package features

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import models.{Arguments, Item, Transaction, Turnover}
import org.scalatest.FunSuite
import services.Files

class TurnoversPerWeekTest extends FunSuite {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  test("Week-Turnover: ComputePerShop Test") {

    val expected: List[(UUID, List[Turnover])] = List(
      (UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
        List(Turnover(600, 432.48),
          Turnover(88, 274.32)
        )
      ),
      (UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
        List(Turnover(17, 200.07),
          Turnover(531, 111.5)
        )
      )
    )

    val transactions: Stream[(LocalDate, Stream[Transaction])] = Stream(
      (LocalDate.parse("20170514", formatter),
        Stream(
          Transaction(1,
            LocalDate.parse("20170514", formatter),
            UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
            531,
            6),
          Transaction(2,
            LocalDate.parse("20170514", formatter),
            UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
            600,
            5),
          Transaction(3,
            LocalDate.parse("20170514", formatter),
            UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
            531,
            4),
          Transaction(4,
            LocalDate.parse("20170514", formatter),
            UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
            600,
            12),
          Transaction(5,
            LocalDate.parse("20170514", formatter),
            UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
            17,
            9),
          Transaction(6,
            LocalDate.parse("20170514", formatter),
            UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
            88,
            8)
        ))
    )

    val referencesStreams: Stream[(LocalDate, Stream[(UUID, Stream[Item])])] = Stream(

      (LocalDate.parse("20170514", formatter), Stream(
        (UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
          Stream(
            Item(531, 11.15),
            Item(17, 22.23)
          )
        )
        ,
        (UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
          Stream(Item(600, 25.44),
            Item(88, 34.29))
        ))
      )
    )

    val result: List[(UUID, List[Turnover])] = TurnoverPerWeek.computePerShop(
      transactions,
      Stream.empty,
      referencesStreams
    )
      .map(element => (element.shopUUID, element.turnovers.toList))
      .toList

    assert(expected === result)
  }
}
