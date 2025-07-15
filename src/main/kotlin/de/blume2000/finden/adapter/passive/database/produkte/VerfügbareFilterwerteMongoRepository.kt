package de.blume2000.finden.adapter.passive.database.produkte

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import de.blume2000.finden.adapter.passive.database.produkte.verfuegbareFilterwerte.MongoAnzahlProdukteFürLiefertagQueryResult
import de.blume2000.finden.adapter.passive.database.produkte.verfuegbareFilterwerte.MongoPreisBereichQueryResult
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import de.blume2000.finden.domain.model.produkte.VerfügbareFilterwerteRepository
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.Liefertage
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.PreisBereich
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.VerfügbareFilterwerte
import de.blume2000.util.DateTimeUtil
import io.micrometer.core.annotation.Counted
import io.micrometer.core.annotation.Timed
import io.quarkus.arc.DefaultBean
import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepositoryBase
import java.time.LocalDate
import java.util.Currency
import java.util.Date
import java.util.Locale
import jakarta.enterprise.context.ApplicationScoped
import mu.KLogging
import org.bson.BsonBoolean
import org.bson.BsonDocument
import org.bson.conversions.Bson
import org.jmolecules.ddd.annotation.Repository

@ApplicationScoped
@DefaultBean
@Repository
class VerfügbareFilterwerteMongoRepository : PanacheMongoRepositoryBase<MongoProdukt, String>,
  VerfügbareFilterwerteRepository {
  companion object : KLogging()

  @Counted(value = "db/ladeVerfuegbareFilterWerte/requests/count")
  @Timed(value = "db/ladeVerfuegbareFilterWerte/requests/timer", percentiles = [0.50, 0.95, 0.99])
  override fun ladeVerfügbareFilterWerte(
    cmsProdukteFilter: CmsProdukteFilter,
    userProdukteFilter: UserProdukteFilter
  ): VerfügbareFilterwerte {
    val filter: Bson = Filters.and(
      listOf(
        MongoProdukteFilter.vonCmsProdukteFilter(cmsProdukteFilter),
        MongoProdukteFilter.vonUserProdukteFilter(userProdukteFilter),
        MongoProdukteFilter.erstelleFilterVerfügbareProdukte()
      )
    )

    return VerfügbareFilterwerte(
      sorten = holeVerfügbareBlumensorten(filter),
      farben = holeVerfügbareProduktfarben(filter),
      preisBereich = holeVerfügbarenPreisbereich(filter),
      klassifikationen = holeVerfügbareKlassifikationen(filter),
      liefertage = holeVerfügbareLiefertage(filter)
    )
  }

  @Counted(value = "db/holeVerfuegbareBlumensorten/requests/count")
  @Timed(value = "db/holeVerfuegbareBlumensorten/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeVerfügbareBlumensorten(filter: Bson) =
    mongoCollection().distinct(MongoProdukt::blumensorten.name, filter, MongoBlumensorte::class.java)
      .map { it.nachBlumensorte() }.toList()

  @Counted(value = "db/holeVerfuegbareProduktfarben/requests/count")
  @Timed(value = "db/holeVerfuegbareProduktfarben/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeVerfügbareProduktfarben(filter: Bson) =
    mongoCollection().distinct(MongoProdukt::farben.name, filter, MongoProduktfarbe::class.java)
      .map { it.nachProduktfarbe() }.toList()

  @Counted(value = "db/holeVerfuegbarenPreisbereich/requests/count")
  @Timed(value = "db/holeVerfuegbarenPreisbereich/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeVerfügbarenPreisbereich(filter: Bson): PreisBereich {
    val mongoPreisBereichQueryResult = mongoCollection().aggregate(
      listOf(
        Aggregates.match(filter),
        Aggregates.group(
          "preis",
          Accumulators.min(
            MongoPreisBereichQueryResult::minPreis.name,
            "\$${MongoProdukt::preis.name}.${MongoPreis::bruttoBetragDecimal.name}"
          ),
          Accumulators.max(
            MongoPreisBereichQueryResult::maxPreis.name,
            "\$${MongoProdukt::preis.name}.${MongoPreis::bruttoBetragDecimal.name}"
          )
        ),
      ), MongoPreisBereichQueryResult::class.java
    ).first()

    return if (mongoPreisBereichQueryResult == null) {
      PreisBereich(minPreis = null, maxPreis = null)
    } else {
      PreisBereich(
        minPreis = Preis(
          bruttoBetrag = mongoPreisBereichQueryResult.minPreis,
          währung = Currency.getInstance(Locale.GERMANY)
        ),
        maxPreis = Preis(
          bruttoBetrag = mongoPreisBereichQueryResult.maxPreis,
          währung = Currency.getInstance(Locale.GERMANY)
        )
      )
    }
  }

  @Counted(value = "db/holeVerfuegbareKlassifikationen/requests/count")
  @Timed(value = "db/holeVerfuegbareKlassifikationen/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeVerfügbareKlassifikationen(filter: Bson) =
    mongoCollection().distinct(MongoProdukt::klassifikation.name, filter, MongoKlassifikation::class.java)
      .map { it.nachKlassifikation() }.toList()

  @Counted(value = "db/holeVerfuegbareLiefertage/requests/count")
  @Timed(value = "db/holeVerfuegbareLiefertage/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeVerfügbareLiefertage(filter: Bson): Liefertage {
    val morgen = LocalDate.now().plusDays(1)
    val uebermorgen = LocalDate.now().plusDays(2)

    val verfügbareLiefertage: MutableList<Liefertag> = mutableListOf()
    if (existierenProduktefürLiefertag(filter, morgen)) verfügbareLiefertage.add(Liefertag(morgen))
    if (existierenProduktefürLiefertag(filter, uebermorgen)) verfügbareLiefertage.add(Liefertag(uebermorgen))

    return Liefertage(verfügbareLiefertage = verfügbareLiefertage)
  }

  private fun existierenProduktefürLiefertag(produkteFilter: Bson, lieferdatum: LocalDate): Boolean {
    val mongoVerfügbarkeitenMorgen = mongoCollection().aggregate(
      listOf(
        Aggregates.match(produkteFilter),
        Aggregates.project(BsonDocument(MongoProdukt::verfuegbarkeiten.name, BsonBoolean.TRUE)),
        Aggregates.unwind("\$${MongoProdukt::verfuegbarkeiten.name}"),
        Aggregates.match(
          Filters.eq(
            "${MongoProdukt::verfuegbarkeiten.name}.${MongoVerfügbarkeit::liefertag.name}",
            lieferdatum
          )
        ),
        Aggregates.match(
          Filters.gt(
            "${MongoProdukt::verfuegbarkeiten.name}.${MongoVerfügbarkeit::bestellschlussUTC.name}",
            Date.from(DateTimeUtil.erstelleUTCNow().toInstant())
          )
        ),
        Aggregates.count(MongoAnzahlProdukteFürLiefertagQueryResult::anzahl.name)
      ), MongoAnzahlProdukteFürLiefertagQueryResult::class.java
    )

    return try {
      val anzahl = mongoVerfügbarkeitenMorgen.first()?.anzahl ?: 0
      anzahl > 0
    } catch (exception: ClassCastException) {
      logger.error("Fehler beim Prüfen von Produktverfügbarkeiten für den Liefertag $lieferdatum.", exception)
      false
    }
  }
}
