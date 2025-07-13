package de.blume2000.finden.adapter.passive.database.produkte

import com.mongodb.client.model.Filters
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.util.DateTimeUtil
import org.bson.conversions.Bson
import java.util.Date

enum class FilterComparisonOperator {
  GTE, LTE
}

object MongoProdukteFilter {

  fun vonCmsProdukteFilter(cmsProdukteFilter: CmsProdukteFilter): Bson {
    val filters: MutableList<Bson> = mutableListOf()

    filterIn(filters, MongoProdukt::klassifikationId.name, cmsProdukteFilter.klassifikationen.map { it.value })

    filterIn(filters,
      "${MongoProdukt::farben.name}.${MongoProduktfarbe::name.name}",
      cmsProdukteFilter.farben.map { it.asString() })

    filterIn(filters,
      "${MongoProdukt::blumensorten.name}.${MongoBlumensorte::name.name}",
      cmsProdukteFilter.blumensorten.map { it.asString() })

    filterPreis(filters, cmsProdukteFilter.minPreis, FilterComparisonOperator.GTE)
    filterPreis(filters, cmsProdukteFilter.maxPreis, FilterComparisonOperator.LTE)

    filterProduktNummern(filters, cmsProdukteFilter)

    return if (filters.isEmpty()) {
      Filters.empty()
    } else {
      Filters.and(filters)
    }
  }

  fun vonUserProdukteFilter(userProdukteFilter: UserProdukteFilter): Bson {
    val filters: MutableList<Bson> = mutableListOf()

    filterIn(filters, MongoProdukt::klassifikationId.name, userProdukteFilter.klassifikationen.map { it.id.value })

    filterIn(filters,
      "${MongoProdukt::farben.name}.${MongoProduktfarbe::name.name}",
      userProdukteFilter.farben.map { it.asString() })

    filterIn(filters,
      "${MongoProdukt::blumensorten.name}.${MongoBlumensorte::name.name}",
      userProdukteFilter.blumensorten.map { it.asString() })

    userProdukteFilter.preisBereich?.let {
      filterPreis(filters, userProdukteFilter.preisBereich.minPreis, FilterComparisonOperator.GTE)
      filterPreis(filters, userProdukteFilter.preisBereich.maxPreis, FilterComparisonOperator.LTE)
    }

    filterLiefertage(filters, userProdukteFilter.liefertage.verfügbareLiefertage)

    return if (filters.isEmpty()) {
      Filters.empty()
    } else {
      Filters.and(filters)
    }
  }

  fun erstelleFilterVerfügbareProdukte(): Bson {
    val verfügbarkeitenFilter: MutableList<Bson> = mutableListOf()
    verfügbarkeitenFilter.add(Filters.exists(MongoProdukt::verfuegbarkeiten.name))
    verfügbarkeitenFilter.add(Filters.not(Filters.size(MongoProdukt::verfuegbarkeiten.name, 0)))
    verfügbarkeitenFilter.add(
      Filters.elemMatch(
        MongoProdukt::verfuegbarkeiten.name, Filters.gt(
          MongoVerfügbarkeit::bestellschlussUTC.name, Date.from(DateTimeUtil.erstelleUTCNow().toInstant())
        )
      )
    )

    return Filters.and(verfügbarkeitenFilter)
  }

  private fun <E> filterIn(filters: MutableList<Bson>, name: String, liste: List<E>) {
    if (liste.isNotEmpty()) {
      filters.add(
        Filters.`in`(name, liste)
      )
    }
  }

  private fun filterPreis(
    filters: MutableList<Bson>, preis: Preis?, filterComparisonOperator: FilterComparisonOperator
  ) {
    if (preis == null) return

    val fieldName = "${MongoProdukt::preis.name}.${MongoPreis::bruttoBetragDecimal.name}"
    filters.add(
      when (filterComparisonOperator) {
        FilterComparisonOperator.GTE -> Filters.gte(
          fieldName, preis.bruttoBetrag
        )
        FilterComparisonOperator.LTE -> Filters.lte(
          fieldName, preis.bruttoBetrag
        )
      }
    )
  }

  private fun filterLiefertage(
    filters: MutableList<Bson>, liefertage: List<Liefertag>
  ) {
    val liefertageFilterListe = liefertage.map {
      Filters.elemMatch(
        MongoProdukt::verfuegbarkeiten.name, Filters.and(
          listOf(
            Filters.eq(MongoVerfügbarkeit::liefertag.name, it.value), Filters.gt(
              MongoVerfügbarkeit::bestellschlussUTC.name, Date.from(DateTimeUtil.erstelleUTCNow().toInstant())
            )
          )
        )
      )
    }
    if (liefertageFilterListe.isNotEmpty()) {
      filters.add(Filters.or(liefertageFilterListe))
    }
  }

  private fun filterProduktNummern(filters: MutableList<Bson>, cmsProdukteFilter: CmsProdukteFilter) {
    if (cmsProdukteFilter.produktNummern.isNotEmpty()) {
      val produktNummernFilter =
        Filters.`in`(MongoProdukt::nummer.name, cmsProdukteFilter.produktNummern.map { it.asString() })

      when (cmsProdukteFilter.produktnummernVerwendung) {
        ProduktnummernVerwendung.AUSSCHLUSSLISTE -> {
          filters.add(
            Filters.not(
              produktNummernFilter
            )
          )
        }
        ProduktnummernVerwendung.SELEKTIONSBASIS -> {
          filters.add(
            produktNummernFilter
          )
        }
        ProduktnummernVerwendung.KEINE -> {
        }
      }
    }
  }
}
