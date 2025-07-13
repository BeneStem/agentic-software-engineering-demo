package de.blume2000.finden.adapter.passive.database.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

data class MongoProduktfarbe @BsonCreator constructor(
  @param:BsonProperty("name") val name: String
) {
  fun nachProduktfarbe(): Produktfarbe = Produktfarbe(name.lowercase())

  companion object {
    fun vonProduktfarbe(produktFarbe: Produktfarbe): MongoProduktfarbe = MongoProduktfarbe(produktFarbe.asString())
  }
}
