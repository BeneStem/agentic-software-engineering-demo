package de.blume2000.finden.testutils

import de.blume2000.avro.produkte.AvroArtikelTypNonFlower
import de.blume2000.avro.produkte.AvroKlassifikation3
import de.blume2000.avro.produkte.AvroPreis
import de.blume2000.avro.produkte.AvroProdukt
import de.blume2000.avro.produkte.AvroProduktBild
import de.blume2000.avro.produkte.AvroStandort
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

@Suppress("LongParameterList")
fun erstelleAvroProdukt(
  nummer: String = "ABC123",
  name: String = "Name",
) =
  AvroProdukt.newBuilder().setProduktNummer(nummer).setName(name)
    .setErpNummer("ErpNummer")
    .setTyp("Typ")
    .setProduktNameShops("ProduktNameShops")
    .setProduktLangBeschreibung("ProduktLangBeschreibung")
    .setAktiv(true)
    .setAusverkauft(false)
    .setLieferZeitraumStartdatum("LieferZeitraumStartdatum")
    .setLieferZeitraumEnddatum("LieferZeitraumEnddatum")
    .setAenderungsDatum("AenderungsDatum")
    .setStandort(AvroStandort())
    .setArtikelTypNonFlower(AvroArtikelTypNonFlower())
    .setKlassifikation3(AvroKlassifikation3("11", "Strauss"))
    .setPreis(
      AvroPreis(
        BigDecimal.ONE.toString(),
        BigDecimal.ONE.toString(),
        "0.19",
        Currency.getInstance(Locale.GERMANY).toString()
      )
    )
    .setUvp(
      AvroPreis(
        BigDecimal.ONE.toString(),
        BigDecimal.ONE.toString(),
        "0.19",
        Currency.getInstance(Locale.GERMANY).toString()
      )
    )
    .setAusgeschlosseneLiefertage(emptyList())
    .setProduktBild(AvroProduktBild("https://res.cloudinary.com/blume2000/image/upload/c_fill,f_auto,h_%h%,q_auto,w_%w%"))
    .setAusgeschlosseneVersandarten(emptyList())
    .setAusgeschlosseneWochentage(emptyList())
    .setDetailBilder(emptyList())
    .setBlumenSorten(emptyList())
    .setArtikelFarben(emptyList())
    .setPflegetippsAnzeigen(false)
    .setAbo(false)
    .setLieferAussage("LieferAussage")
    .setUrlName("/zurpds")
    .setMuttertagsVersand(false)
    .setErlaubteZusatzArtikelGroessen(emptyList())
    .setZusatzArtikel(false)
    .build()
