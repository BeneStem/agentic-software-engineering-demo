package de.blume2000.finden.adapter.passive.database.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

class MongoKlassifikation @BsonCreator constructor(
  @param:BsonProperty("id") val id: String,
  @param:BsonProperty("name") val name: String
) {
  fun nachKlassifikation(): Klassifikation = Klassifikation(
    KlassifikationId(this.id),
    KlassifikationName(this.name)
  )

  companion object {
    fun vonKlassifikation(klassifikation: Klassifikation): MongoKlassifikation =
      MongoKlassifikation(klassifikation.id.value, klassifikation.name.value)
  }
}
