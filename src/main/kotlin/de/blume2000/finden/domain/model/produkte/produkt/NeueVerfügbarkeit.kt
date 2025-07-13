package de.blume2000.finden.domain.model.produkte.produkt

import org.jmolecules.ddd.annotation.Entity

@Entity
data class NeueVerfügbarkeit(
  val produktnummer: Produktnummer,
  val lieferregionen: List<Lieferregion>,
  val liefertag: Liefertag,
  val bestellschluss: Bestellschluss?
) {
  fun zuProduktVerfügbarkeit(): ProduktVerfügbarkeit = ProduktVerfügbarkeit(
    liefertag = liefertag,
    bestellschluss = bestellschluss
      ?: throw VerfügbarkeitException("Produktverfügbarkeit muss mit bestellschluss erzeugt werden")
  )

  fun istVerfügbar() = lieferregionen.isNotEmpty() && bestellschluss != null
}
