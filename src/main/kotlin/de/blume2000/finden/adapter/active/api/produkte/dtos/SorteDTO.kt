package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SorteDTO(
  @field:NotBlank
  @field:Pattern(regexp = "^[a-zA-Z0-9äöüÄÖÜß ]+$")
  val name: String
) {
  fun zuBlumensorte() = Blumensorte(name)
}
