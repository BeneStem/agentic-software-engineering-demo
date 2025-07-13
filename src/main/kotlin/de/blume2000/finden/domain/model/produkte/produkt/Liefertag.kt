package de.blume2000.finden.domain.model.produkte.produkt

import org.jmolecules.ddd.annotation.ValueObject
import java.time.LocalDate

@ValueObject
data class Liefertag(
  val value: LocalDate
) {
  fun asString(): String = value.toString()

  fun toISOString(): String = value.toString()
}
