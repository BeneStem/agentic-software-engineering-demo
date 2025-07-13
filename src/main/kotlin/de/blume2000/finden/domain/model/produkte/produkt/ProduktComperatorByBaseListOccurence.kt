package de.blume2000.finden.domain.model.produkte.produkt

/**
 * Vergleicht Produkte danach in welcher Reihenfolge sie in einer Basissortierung auftauchen.
 * Angewandt auf einer Liste von Produkten wird die Liste so sortiert wie die Produktnummern in der sortingBase sortiert sind.
 * Produkte die nicht in der sortierbasis auftauchen werden ans listenende sortiert
 */
class ProduktComperatorByBaseListOccurence(private val sortingBase: List<Produktnummer>) : Comparator<Produkt> {
  override fun compare(o1: Produkt, o2: Produkt): Int {
    val indexOfO1 = sortingBase.indexOf(o1.nummer)
    val indexOfO2 = sortingBase.indexOf(o2.nummer)

    return if (indexOfO1 < 0 && indexOfO2 >= 0) {
      // o1 is not in sortingbase, but o2 is -> o2 before o1
      1
    } else if (indexOfO2 < 0 && indexOfO1 >= 0) {
      // o2 is not in sortingbase, but o1 is -> o1 before o2
      -1
    } else {
      // else compare indices
      indexOfO1.compareTo(indexOfO2)
    }
  }
}
