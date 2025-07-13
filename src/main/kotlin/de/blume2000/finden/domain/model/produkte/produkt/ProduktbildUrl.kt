package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.util.sanitize
import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class ProduktbildUrl(
  private var value: String
) {
  init {
    if (value.isBlank()) {
      throw ProduktbildUrlIstLeerException()
    }

    if (!value.startsWith("https")) {
      throw UnsicheresProtokollException(value)
    }

    value = value.sanitize()
  }

  fun asString(): String = value
}
