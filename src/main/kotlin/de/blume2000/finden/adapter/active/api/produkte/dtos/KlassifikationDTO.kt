package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class KlassifikationDTO(
  @field:NotBlank
  val name: String,

  @field:NotBlank
  @field:Pattern(regexp = "^(\\d)+$")
  val id: String
) {
  fun zuKlassifikationId() = KlassifikationId(id)

  fun zuKlassifikation() = Klassifikation(KlassifikationId(id), KlassifikationName(name))
}
