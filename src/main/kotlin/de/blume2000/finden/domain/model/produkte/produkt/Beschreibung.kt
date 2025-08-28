package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.util.sanitize
import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class Beschreibung(
  private var value: String
) {
  init {
    if (value.isBlank()) {
      throw BeschreibungIstLeerException("Beschreibung darf nicht leer sein: '$value'")
    }
    if (value.length > 500) {
      throw BeschreibungZuLangException("Beschreibung darf maximal 500 Zeichen lang sein, aber hat ${value.length} Zeichen: '$value'")
    }
    value = value.sanitize()
  }

  fun asString(): String = value
}
