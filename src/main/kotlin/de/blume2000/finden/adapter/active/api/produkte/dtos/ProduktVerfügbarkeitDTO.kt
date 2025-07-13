package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit

data class ProduktVerfügbarkeitDTO(
  val bestellschluss: String,
  val liefertag: String,
) {
  companion object {
    fun vonProduktVerfügbarkeit(produktVerfügbarkeit: ProduktVerfügbarkeit): ProduktVerfügbarkeitDTO {
      return ProduktVerfügbarkeitDTO(
        bestellschluss = produktVerfügbarkeit.bestellschluss.asString(),
        liefertag = produktVerfügbarkeit.liefertag.asString()
      )
    }
  }
}
