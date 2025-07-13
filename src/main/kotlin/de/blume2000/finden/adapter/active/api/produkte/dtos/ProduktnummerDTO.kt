package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class ProduktnummerDTO(
  @field:NotBlank
  @field:Pattern(regexp = "^(\\d)+$")
  val nummer: String
) {
  fun zuProduktnummer() = Produktnummer(nummer)
}
