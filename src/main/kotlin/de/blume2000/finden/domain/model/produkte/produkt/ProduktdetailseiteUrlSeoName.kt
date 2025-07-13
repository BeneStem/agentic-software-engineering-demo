package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.util.sanitize

data class ProduktdetailseiteUrlSeoName(
  private var value: String
) {
  init {
    value = value.sanitize()
  }

  fun asString(): String = value
}
