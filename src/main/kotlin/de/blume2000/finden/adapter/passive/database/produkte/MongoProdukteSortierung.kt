package de.blume2000.finden.adapter.passive.database.produkte

import com.mongodb.client.model.Sorts
import de.blume2000.finden.domain.model.produkte.ProdukteSortierFeld
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import de.blume2000.finden.domain.model.produkte.Richtung
import org.bson.conversions.Bson

@Suppress("UtilityClassWithPublicConstructor")
class MongoProdukteSortierung {

  companion object {

    fun vonProdukteSortierung(produkteSortierung: ProdukteSortierung): Bson {
      val sorts: MutableList<Bson> = mutableListOf()

      produkteSortierung.reihenfolge.forEach {
        if (it.richtung === Richtung.ASC) {
          sorts.add(Sorts.ascending(MongoProdukteSortierFeld.vonProdukteSortierFeld(it.feld)))
        } else {
          sorts.add(Sorts.descending(MongoProdukteSortierFeld.vonProdukteSortierFeld(it.feld)))
        }
      }

      return Sorts.orderBy(sorts)
    }
  }
}

@Suppress("UtilityClassWithPublicConstructor")
class MongoProdukteSortierFeld {

  companion object {

    fun vonProdukteSortierFeld(produkteSortierFeld: ProdukteSortierFeld): String = when (produkteSortierFeld) {
      ProdukteSortierFeld.PRODUKTNAME -> MongoProdukt::name.name
      ProdukteSortierFeld.PREIS -> "${MongoProdukt::preis.name}.${MongoPreis::bruttoBetragDecimal.name}"
    }
  }
}
