package de.blume2000.util

import de.blume2000.avro.produkte.AvroArtikelTypNonFlower
import de.blume2000.avro.produkte.AvroKlassifikation3
import de.blume2000.avro.produkte.AvroPreis
import de.blume2000.avro.produkte.AvroProdukt
import de.blume2000.avro.produkte.AvroProduktBild
import java.time.LocalDate

@Suppress("UtilityClassWithPublicConstructor")
class AvroProduktTestUtil {
  companion object {
    @Suppress("LongParameterList")
    fun createMessagePayload(
      nummer: String = "PN-123",
      name: String = "Rosen",
      aktiv: Boolean = true,
      startdatum: LocalDate = LocalDate.of(2021, 2, 1),
      enddatum: LocalDate = LocalDate.of(2021, 2, 15),
      klassifikation: AvroKlassifikation3 = AvroKlassifikation3.newBuilder().setId("11").setName("K1").build(),
      preis: AvroPreis = AvroPreis.newBuilder().setBruttoBetrag("10.00").setNettoBetrag("9.99").setMwst("5")
        .setWaehrung("EUR").build(),
      uvp: AvroPreis? = AvroPreis.newBuilder().setBruttoBetrag("11.00").setNettoBetrag("10.99").setMwst("5")
        .setWaehrung("EUR").build(),
      ausverkauft: Boolean = false,
      produktbild: AvroProduktBild = AvroProduktBild.newBuilder()
        .setBaseLink("https://res.cloudinary.com/blume2000/image/upload/c_fill,f_auto,h_%h%,q_auto,w_%w%").build()
    ): AvroProdukt {
      return AvroProdukt.newBuilder().setTyp("Typ").setErpNummer("ERP").setProduktNummer(nummer).setName(name)
        .setProduktNameShops(name).setAktiv(aktiv)
        .setLieferZeitraumStartdatum(startdatum.toString()).setLieferZeitraumEnddatum(enddatum.toString())
        .setKlassifikation3(klassifikation)
        .setPreis(preis).setUvp(uvp).setAusverkauft(ausverkauft).setProduktBild(produktbild)
        .setProduktLangBeschreibung("Bechreibung Lang").setAenderungsDatum(startdatum.toString())
        .setArtikelTypNonFlower(
          AvroArtikelTypNonFlower.newBuilder().setId("Id").setName("Name").build()
        ).setAusgeschlosseneLiefertage(listOf()).build()
    }
  }
}
