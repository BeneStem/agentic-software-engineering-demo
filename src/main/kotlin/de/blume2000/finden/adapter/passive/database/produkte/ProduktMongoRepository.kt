package de.blume2000.finden.adapter.passive.database.produkte

import com.mongodb.client.model.Filters
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.Produkte
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import io.quarkus.arc.DefaultBean
import io.quarkus.mongodb.panache.kotlin.PanacheMongoRepositoryBase
import mu.KLogging
import org.bson.conversions.Bson
import org.jmolecules.ddd.annotation.Repository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@DefaultBean
@Repository
class ProduktMongoRepository : PanacheMongoRepositoryBase<MongoProdukt, String>, ProduktRepository {

  companion object : KLogging()

  override fun ladeVerfügbareProdukte(
    cmsProdukteFilter: CmsProdukteFilter,
    userProdukteFilter: UserProdukteFilter,
    produkteSortierung: ProdukteSortierung
  ): Produkte {
    logger.debug { "Lade verfügbare Produkte aus MongoDB" }
    logger.debug { "Farben: ${cmsProdukteFilter.farben}" }
    logger.debug { "Sorten: ${cmsProdukteFilter.blumensorten}" }

    val filter: Bson = Filters.and(
      listOf(
        MongoProdukteFilter.vonCmsProdukteFilter(cmsProdukteFilter),
        MongoProdukteFilter.vonUserProdukteFilter(userProdukteFilter),
        MongoProdukteFilter.erstelleFilterVerfügbareProdukte()
      )
    )

    val result = mongoCollection().find(filter).sort(MongoProdukteSortierung.vonProdukteSortierung(produkteSortierung))
      .let {
        val limit = cmsProdukteFilter.getLimit()
        if (limit != null) {
          it.limit(limit)
        } else {
          it
        }
      }

    val produktListe = result.toList().map { it.nachProdukt() }
    logger.debug { "Anzahl geladener Produkte '${produktListe.size}'" }
    return Produkte(produktListe)
  }

  override fun speicherProdukt(produkt: Produkt) {
    logger.debug { "Speicher Produkt '${produkt.nummer.asString()}'" }
    persistOrUpdate(MongoProdukt.vonProdukt(produkt))
    val persistiertesProdukt = find(MongoProdukt::nummer.name, produkt.nummer.asString()).firstResult()
    if (persistiertesProdukt != null) {
      logger.debug { "${persistiertesProdukt.id} erfolgreich persistiert." }
    } else {
      logger.error { "Produkt ${produkt.nummer} konnte nicht persistiert werden!" }
    }
  }

  override fun entferneProdukt(nummer: Produktnummer) {
    logger.debug { "Entferne Produkt '${nummer.asString()}'" }
    delete(MongoProdukt::nummer.name, nummer.asString())
  }

  override fun ladeProdukt(nummer: Produktnummer): Produkt? {
    logger.debug { "Lade Produkt '${nummer.asString()}'" }
    return find(MongoProdukt::nummer.name, nummer.asString()).firstResult()?.nachProdukt()
  }

  // TODO BS LATER this panache method should not be used as it is highly inefficient
  override fun zähleProdukte(): Long = count()
}
