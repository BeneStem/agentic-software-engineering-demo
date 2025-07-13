package de.blume2000.finden.domain.model.produkte.produkt

import org.jmolecules.ddd.annotation.Entity

@Entity
data class ProduktVerfügbarkeit(
  val bestellschluss: Bestellschluss,
  val liefertag: Liefertag
)
