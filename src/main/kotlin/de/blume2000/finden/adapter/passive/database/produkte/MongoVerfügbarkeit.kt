package de.blume2000.finden.adapter.passive.database.produkte

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.LocalDate
import java.time.OffsetDateTime

data class MongoVerfügbarkeit @BsonCreator constructor(
  @param:BsonProperty("liefertag") val liefertag: LocalDate,
  @param:BsonProperty("bestellschlussUTC") val bestellschlussUTC: OffsetDateTime?
)
