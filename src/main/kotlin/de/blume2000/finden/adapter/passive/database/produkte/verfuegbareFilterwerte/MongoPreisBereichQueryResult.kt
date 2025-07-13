package de.blume2000.finden.adapter.passive.database.produkte.verfuegbareFilterwerte

import java.math.BigDecimal
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty

data class MongoPreisBereichQueryResult @BsonCreator constructor(
  @param:BsonProperty("id") val id: String,
  @param:BsonProperty("minPreis") val minPreis: BigDecimal,
  @param:BsonProperty("maxPreis") val maxPreis: BigDecimal
)
