package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.util.sanitize
import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class Produktnummer(
  private var value: String
) {
  init {
    if (value.isBlank()) {
      throw ProduktnummerIstLeerException(value)
    }
    value = value.sanitize()
  }

  fun istIdentisch(produktnummer: Produktnummer): Boolean = produktnummer.value == this.value

  fun asString(): String = value
}
