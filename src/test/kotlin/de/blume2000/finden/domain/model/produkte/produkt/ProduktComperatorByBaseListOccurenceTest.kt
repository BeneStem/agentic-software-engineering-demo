package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.finden.testutils.erstelleProdukt
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("unit")
class ProduktComperatorByBaseListOccurenceTest {

  @Test
  internal fun `empty sortingBase list leads to all products being equal regarding sort`() {
    val emptyListComperator = ProduktComperatorByBaseListOccurence(emptyList())

    expectThat(emptyListComperator.compare(erstelleProdukt(nummer = "1"), erstelleProdukt(nummer = "1"))).isEqualTo(0)
    expectThat(emptyListComperator.compare(erstelleProdukt(nummer = "1"), erstelleProdukt(nummer = "2"))).isEqualTo(0)
  }

  @Test
  internal fun `sorting ragarding produktnummer works`() {
    val listComperator = ProduktComperatorByBaseListOccurence(listOf(Produktnummer("1"), Produktnummer("2"),
      Produktnummer("3")))

    val nr1 = erstelleProdukt(nummer = "1")
    val nr2 = erstelleProdukt(nummer = "2")
    val nr3 = erstelleProdukt(nummer = "3")

    val sortedList = listOf(nr2, nr3, nr1).sortedWith(listComperator)
    expectThat(sortedList[0]).isEqualTo(nr1)
    expectThat(sortedList[1]).isEqualTo(nr2)
    expectThat(sortedList[2]).isEqualTo(nr3)

    expectThat(listComperator.compare(nr1, nr2)).isEqualTo(-1)
    expectThat(listComperator.compare(nr2, nr1)).isEqualTo(1)
    expectThat(listComperator.compare(nr1, nr1)).isEqualTo(0)
  }

  @Test
  internal fun `produkts not in the base list get appended at the end`() {
    val listComperatorWithout1 = ProduktComperatorByBaseListOccurence(listOf(Produktnummer("2"),
      Produktnummer("3")))

    val nr1 = erstelleProdukt(nummer = "1")
    val nr2 = erstelleProdukt(nummer = "2")
    val nr3 = erstelleProdukt(nummer = "3")

    expectThat(listComperatorWithout1.compare(nr1, nr2)).isEqualTo(1)
    expectThat(listComperatorWithout1.compare(nr2, nr1)).isEqualTo(-1)
    expectThat(listComperatorWithout1.compare(nr2, nr3)).isEqualTo(-1)
    expectThat(listComperatorWithout1.compare(nr1, nr1)).isEqualTo(0)

    val sortedList = listOf(nr3, nr2, nr1).sortedWith(listComperatorWithout1)

    expectThat(sortedList[0]).isEqualTo(nr2)
    expectThat(sortedList[1]).isEqualTo(nr3)
    expectThat(sortedList[2]).isEqualTo(nr1)
  }
}
