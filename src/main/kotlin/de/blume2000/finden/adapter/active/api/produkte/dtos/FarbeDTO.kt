package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class FarbeDTO(
  @field:NotBlank
  @field:Pattern(regexp = "^[a-zäöüß]+$")
  val name: String
) {
  fun zuProduktfarbe() = Produktfarbe(name)
}
