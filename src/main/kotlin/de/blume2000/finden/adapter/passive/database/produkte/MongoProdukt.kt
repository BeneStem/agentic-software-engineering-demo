package de.blume2000.finden.adapter.passive.database.produkte

import de.blume2000.finden.adapter.passive.database.produkte.MongoProdukt.Companion.MONGO_COLLECTION
import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.ProduktbildUrl
import de.blume2000.finden.domain.model.produkte.produkt.ProduktdetailseiteUrlSeoName
import de.blume2000.finden.domain.model.produkte.produkt.Produktname
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import io.quarkus.mongodb.panache.common.MongoEntity
import io.quarkus.mongodb.panache.kotlin.PanacheMongoEntityBase
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

@MongoEntity(collection = MONGO_COLLECTION)
data class MongoProdukt @BsonCreator constructor(
  @param:BsonProperty("id") val id: String,
  @param:BsonProperty("nummer") val nummer: String,
  @param:BsonProperty("name") val name: String,
  @param:BsonProperty("preis") val preis: MongoPreis,
  @param:BsonProperty("streichpreis") val streichpreis: MongoPreis,
  @param:BsonProperty("klassifikation") val klassifikation: MongoKlassifikation?,
  @param:BsonProperty("klassifikationId") val klassifikationId: String,
  @param:BsonProperty("bildUrl") val bildUrl: String,
  @param:BsonProperty("pdsUrlSeoName") val pdsUrlSeoName: String,
  @param:BsonProperty("farben") val farben: List<MongoProduktfarbe>?,
  @param:BsonProperty("blumensorten") val blumensorten: List<MongoBlumensorte>?,
  @param:BsonProperty("verfuegbarkeiten") val verfuegbarkeiten: List<MongoVerfügbarkeit>?,
) : PanacheMongoEntityBase() {
  fun nachProdukt(): Produkt {
    return Produkt(
      nummer = Produktnummer(this.nummer),
      name = Produktname(this.name),
      preis = this.preis.nachPreis(),
      streichpreis = this.streichpreis.nachPreis(),
      klassifikation = if (this.klassifikation != null) {
        this.klassifikation.nachKlassifikation()
      } else {
        Klassifikation(KlassifikationId(klassifikationId), KlassifikationName("Temporär nicht verfügbar"))
      },
      bildUrl = ProduktbildUrl(this.bildUrl),
      pdsUrlSeoName =
      ProduktdetailseiteUrlSeoName(this.pdsUrlSeoName),
      farben = if (this.farben != null) {
        this.farben.map { it.nachProduktfarbe() }
      } else {
        listOf()
      },
      blumensorten = if (this.blumensorten != null) {
        this.blumensorten.map { it.nachBlumensorte() }
      } else {
        listOf()
      },
      verfügbarkeiten = if (this.verfuegbarkeiten != null) {
        this.verfuegbarkeiten.map {
          if (it.bestellschlussUTC != null) {
            ProduktVerfügbarkeit(
              liefertag = Liefertag(it.liefertag),
              bestellschluss = Bestellschluss(it.bestellschlussUTC)
            )
          } else {
            null
          }
        }.filterNotNull()
      } else {
        emptyList()
      }
    )
  }

  companion object {

    const val MONGO_COLLECTION = "produkte"

    fun vonProdukt(produkt: Produkt): MongoProdukt {
      return MongoProdukt(
        id = produkt.nummer.asString(),
        nummer = produkt.nummer.asString(),
        name = produkt.name.asString(),
        preis = MongoPreis.vonPreis(produkt.preis),
        streichpreis = MongoPreis.vonPreis(produkt.streichpreis),
        klassifikation = MongoKlassifikation(produkt.klassifikation.id.value, produkt.klassifikation.name.value),
        klassifikationId = produkt.klassifikation.id.value,
        bildUrl = produkt.bildUrl.asString(),
        pdsUrlSeoName = produkt.pdsUrlSeoName.asString(),
        farben = produkt.farben.map { MongoProduktfarbe.vonProduktfarbe(it) },
        blumensorten = produkt.blumensorten.map { MongoBlumensorte.vonBlumensorte(it) },
        verfuegbarkeiten = produkt.verfügbarkeiten.map {
          MongoVerfügbarkeit(
            liefertag = it.liefertag.value,
            bestellschlussUTC = it.bestellschluss.value
          )
        }
      )
    }
  }
}
