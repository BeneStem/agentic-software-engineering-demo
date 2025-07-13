package de.blume2000.finden.testutils

import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.ProduktbildUrl
import de.blume2000.finden.domain.model.produkte.produkt.ProduktdetailseiteUrlSeoName
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.Produktname
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

fun erstelleProduktliste() = listOf(
  erstelleProdukt()
)

@Suppress("LongParameterList")
fun erstelleProdukt(
  nummer: String = "ABC123",
  name: String = "Name",
  preis: Preis = Preis(BigDecimal.ONE, Currency.getInstance(Locale.GERMANY)),
  streichpreis: Preis = Preis(BigDecimal.ONE, Currency.getInstance(Locale.GERMANY)),
  klassifikation: Klassifikation = Klassifikation(KlassifikationId("11"), KlassifikationName("Strauss")),
  produktbildUrl: ProduktbildUrl = ProduktbildUrl(
    "https://res.cloudinary.com/blume2000/image/upload/c_fill,f_auto,h_%h%,q_auto,w_%w%"
  ),
  pdsUrl: ProduktdetailseiteUrlSeoName = ProduktdetailseiteUrlSeoName("zurpds"),
  farben: List<Produktfarbe> = emptyList(),
  blumensorten: List<Blumensorte> = emptyList(),
  verfügbarkeiten: List<ProduktVerfügbarkeit> = emptyList(),
) = Produkt(
  nummer = Produktnummer(nummer),
  name = Produktname(name),
  preis = preis,
  streichpreis = streichpreis,
  klassifikation = klassifikation,
  bildUrl = produktbildUrl,
  pdsUrlSeoName = pdsUrl,
  farben = farben,
  blumensorten = blumensorten,
  verfügbarkeiten = verfügbarkeiten,
)
