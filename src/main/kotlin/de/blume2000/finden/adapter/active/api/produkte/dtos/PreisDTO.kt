package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

data class PreisDTO(
  @field:NotNull
  @field:Positive
  val bruttoBetrag: BigDecimal,

  @field:NotBlank
  @field:Pattern(regexp = "^[A-Z]{3}$")
  val waehrung: String
) {
  fun zuEuroPreis() = Preis(bruttoBetrag = bruttoBetrag, währung = Currency.getInstance(Locale.GERMANY))

  companion object {
    fun vonPreis(preis: Preis) = PreisDTO(bruttoBetrag = preis.bruttoBetrag, waehrung = preis.währung.currencyCode)
  }
}
