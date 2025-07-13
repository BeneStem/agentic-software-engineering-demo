package de.blume2000.finden.adapter.passive.database.produkte.verfuegbareFilterwerte

import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

data class MongoAnzahlProdukteFürLiefertagQueryResult @BsonCreator constructor(
  @param:BsonProperty("anzahl") val anzahl: Int,
)
