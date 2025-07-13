package de.blume2000.finden.domain.model.produkte.produkt

import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class Produktfarbe(private val value: String) {
  fun asString() = value

  init {
    if (value.any { it.isUpperCase() }) {
      throw ProduktfarbeEnthältUpperCaseZeichenException("Produktfarbe enthält Uppercase Zeichen: '$value'")
    }
  }
}
