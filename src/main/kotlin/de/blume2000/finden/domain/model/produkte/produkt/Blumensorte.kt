package de.blume2000.finden.domain.model.produkte.produkt

import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class Blumensorte(private val value: String) {
  fun asString() = value
}
