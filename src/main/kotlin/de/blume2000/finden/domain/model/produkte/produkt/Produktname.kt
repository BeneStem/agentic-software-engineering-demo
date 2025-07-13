package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.util.sanitize
import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class Produktname(
  private var value: String
) {
  init {
    if (value.isBlank()) {
      throw ProduktnameIstLeerException(value)
    }
    value = value.sanitize()
  }
  fun asString(): String = value
}
