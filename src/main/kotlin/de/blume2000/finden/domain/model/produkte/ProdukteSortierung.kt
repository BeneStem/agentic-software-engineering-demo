package de.blume2000.finden.domain.model.produkte

data class ProdukteSortierung(
  val reihenfolge: List<Sortierung> = emptyList()
)

data class Sortierung(val feld: ProdukteSortierFeld, val richtung: Richtung)

enum class ProdukteSortierFeld {
  PRODUKTNAME,
  PREIS,
}

enum class Richtung {
  ASC, DESC
}
