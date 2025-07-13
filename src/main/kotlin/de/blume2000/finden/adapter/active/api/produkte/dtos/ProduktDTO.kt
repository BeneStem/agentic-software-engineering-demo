package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.Produkt

data class ProduktDTO(
  val nummer: String,
  val name: String,
  val preis: String,
  val streichpreis: String,
  val klassifikationName: String,
  val zeigeStreichpreis: Boolean,
  val bildUrl: String,
  val produktUrl: String,
  val naechstmoeglicheVerfuegbarkeit: ProduktVerfügbarkeitDTO,
  val letztmoeglicheVerfuegbarkeit: ProduktVerfügbarkeitDTO
) {
  companion object {
    fun vonProdukt(produkt: Produkt): ProduktDTO? {
      val nächstmöglicheVerfügbarkeit = produkt.holeNächstmöglicheVerfügbarkeit()
      val letztmoeglicheVerfügbarkeit = produkt.holeLetztmöglicheVerfügbarkeit()

      if (nächstmöglicheVerfügbarkeit == null || letztmoeglicheVerfügbarkeit == null) return null

      return ProduktDTO(
        nummer = produkt.nummer.asString(),
        name = produkt.name.asString(),
        preis = produkt.preis.asString(),
        streichpreis = produkt.streichpreis.asString(),
        klassifikationName = produkt.klassifikation.name.value,
        zeigeStreichpreis = produkt.streichpreis.istGrößerAls(produkt.preis),
        bildUrl = produkt.bildUrl.asString(),
        produktUrl = produkt.getProduktDetailseiteUrlPfad(),
        naechstmoeglicheVerfuegbarkeit = ProduktVerfügbarkeitDTO.vonProduktVerfügbarkeit(nächstmöglicheVerfügbarkeit),
        letztmoeglicheVerfuegbarkeit = ProduktVerfügbarkeitDTO.vonProduktVerfügbarkeit(letztmoeglicheVerfügbarkeit),
      )
    }
  }
}
