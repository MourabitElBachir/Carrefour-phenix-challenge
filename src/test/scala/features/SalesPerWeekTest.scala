package features

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

import models.{ItemSalePerShop, Transaction}
import org.scalatest.FunSuite

class SalesPerWeekTest extends FunSuite {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

  test("Week-Sales: ComputePerShop Test") {

    val expected: List[(UUID, List[ItemSalePerShop])] = List(
      (UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"),
        List(ItemSalePerShop(UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"), 600, 17),
          ItemSalePerShop(UUID.fromString("dd43720c-be43-41b6-bc4a-ac4beabd0d9b"), 88, 8)
        )
      ),
      (UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"),
        List(ItemSalePerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 17, 9),
          ItemSalePerShop(UUID.fromString("2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71"), 531, 10)
        )
      )
    )

    val sales: Stream[(LocalDate, Stream[Transaction])] = Stream(

      (LocalDate.parse("20170514", formatter),

        Stream(Transaction(1,
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
        )
      )
    )

    val result: List[(UUID, List[ItemSalePerShop])] = SalesPerWeek.computePerShop(sales, Stream.Empty)
      .map(elem => (elem.shopUUID, elem.itemsSales.toList))
      .toList

    assert(expected === result)

  }
}
