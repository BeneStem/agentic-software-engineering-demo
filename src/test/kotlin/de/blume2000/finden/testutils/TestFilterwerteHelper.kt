package de.blume2000.finden.testutils

import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.Liefertage
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.PreisBereich
import de.blume2000.util.DateTimeUtil
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency
import java.util.Locale

internal fun erstelleProduktfarben(vararg produktfarben: String) = produktfarben.map { Produktfarbe(it) }

internal fun erstelleBlumensorten(vararg blumensorten: String) = blumensorten.map { Blumensorte(it) }

internal fun erstelleEuroPreisbereich(minPreisBruttobetrag: Double?, maxPreisBruttobetrag: Double?) = PreisBereich(
  minPreis = minPreisBruttobetrag?.let { erstelleEuroPreis(minPreisBruttobetrag) },
  maxPreis = maxPreisBruttobetrag?.let { erstelleEuroPreis(maxPreisBruttobetrag) }
)

internal fun erstelleEuroPreis(bruttoBetrag: Double) =
  Preis(BigDecimal(bruttoBetrag), Currency.getInstance(Locale.GERMANY))

internal fun erstelleKlassifikation(id: String, name: String) =
  Klassifikation(KlassifikationId(id), KlassifikationName(name))

internal fun erstelleLiefertage(morgen: Boolean, übermorgen: Boolean): Liefertage {
  val verfügbareLiefertage: MutableList<Liefertag> = mutableListOf()
  if (morgen) verfügbareLiefertage.add(Liefertag(LocalDate.now().plusDays(1)))
  if (übermorgen) verfügbareLiefertage.add(Liefertag(LocalDate.now().plusDays(2)))

  return Liefertage(
    verfügbareLiefertage = verfügbareLiefertage
  )
}

internal fun erstelleVerfügbarkeiten(
  morgen: Boolean = true,
  bestellbarMorgen: Boolean = true,
  übermorgen: Boolean = true,
  bestellbarÜbermorgen: Boolean = true
): List<ProduktVerfügbarkeit> {
  val produktVerfügbarkeitMorgen = erstelleProduktVerfügbarkeit(
    liefertag = erstelleLiefertag(1),
    bestellschluss = erstelleBestellschluss(bestellbarMorgen)
  )
  val produktVerfügbarkeitÜbermorgen = erstelleProduktVerfügbarkeit(
    liefertag = erstelleLiefertag(2),
    bestellschluss = erstelleBestellschluss(bestellbarÜbermorgen)
  )
  val verfügbarkeiten: MutableList<ProduktVerfügbarkeit> = mutableListOf()
  if (morgen) verfügbarkeiten.add(produktVerfügbarkeitMorgen)
  if (übermorgen) verfügbarkeiten.add(produktVerfügbarkeitÜbermorgen)

  return verfügbarkeiten
}

internal fun erstelleLiefertag(daysToAdd: Long) = Liefertag(
  LocalDate.now().plusDays(daysToAdd),
)

internal fun erstelleBestellschluss(bestellbar: Boolean) = if (bestellbar) {
  Bestellschluss(DateTimeUtil.erstelleUTCNow().plusHours(1))
} else {
  Bestellschluss(DateTimeUtil.erstelleUTCNow().minusHours(1))
}
