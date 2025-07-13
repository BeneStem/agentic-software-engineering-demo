package de.blume2000.finden.adapter.passive.database.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

data class MongoBlumensorte @BsonCreator constructor(
  @param:BsonProperty("name") val name: String
) {
  fun nachBlumensorte(): Blumensorte = Blumensorte(name)

  companion object {
    fun vonBlumensorte(blumensorte: Blumensorte): MongoBlumensorte = MongoBlumensorte(blumensorte.asString())
  }
}
