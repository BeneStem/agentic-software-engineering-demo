package de.blume2000.finden.domain.model.produkte.produkt.preis

import org.jmolecules.ddd.annotation.Entity
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

@Entity
data class Preis(
  val bruttoBetrag: BigDecimal,
  val währung: Currency = Currency.getInstance(Locale.GERMANY)
) {
  init {
    if (bruttoBetrag < BigDecimal(0.00)) {
      throw NegativerPreisException(bruttoBetrag.toString())
    }
    if (währung != Currency.getInstance(Locale.GERMANY)) {
      throw UnerwarteteWährungException(währung.displayName)
    }
  }

  private val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)

  fun asString(): String {
    formatter.apply {
      minimumFractionDigits = 2
      maximumFractionDigits = 2
    }
    return formatter.format(bruttoBetrag)
  }

  fun istGrößerAlsOderGleich(preis: Preis?): Boolean = preis == null || bruttoBetrag >= preis.bruttoBetrag

  fun istGrößerAls(preis: Preis?): Boolean = preis == null || bruttoBetrag > preis.bruttoBetrag

  fun istKleinerAls(preis: Preis?): Boolean = preis == null || bruttoBetrag < preis.bruttoBetrag
}
