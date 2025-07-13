package de.blume2000.finden.adapter.passive.database.produkte

import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.util.NoArg
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.math.BigDecimal
import java.util.Currency

@NoArg
data class MongoPreis @BsonCreator constructor(
  @param:BsonProperty("bruttoBetragDecimal") val bruttoBetragDecimal: BigDecimal,
  @param:BsonProperty("waehrung") val waehrung: String
) {
  fun nachPreis(): Preis = Preis(
    this.bruttoBetragDecimal,
    Currency.getInstance(this.waehrung)
  )

  companion object {
    fun vonPreis(preis: Preis): MongoPreis =
      MongoPreis(preis.bruttoBetrag, preis.währung.currencyCode)
  }
}
