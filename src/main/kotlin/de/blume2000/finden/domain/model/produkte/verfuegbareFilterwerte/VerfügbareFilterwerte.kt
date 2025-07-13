package de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte

import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import org.jmolecules.ddd.annotation.AggregateRoot

@AggregateRoot
data class VerfügbareFilterwerte(
  val sorten: List<Blumensorte>,
  val farben: List<Produktfarbe>,
  val preisBereich: PreisBereich,
  val klassifikationen: List<Klassifikation>,
  val liefertage: Liefertage
) {
  fun schränkeAufCmsFilterwerteEin(cmsProdukteFilter: CmsProdukteFilter) = this.copy(
    sorten = schränkeAufCmsSortenEin(cmsProdukteFilter.blumensorten),
    farben = schränkeAufCmsFarbenEin(cmsProdukteFilter.farben),
    klassifikationen = schränkeAufCmsKlassifikationenEin(cmsProdukteFilter.klassifikationen),
    preisBereich = schränkePreisbereichEin(
      cmsMinPreis = cmsProdukteFilter.minPreis,
      cmsMaxPreis = cmsProdukteFilter.maxPreis
    )
  )

  private fun schränkeAufCmsSortenEin(cmsSorten: List<Blumensorte>) =
    if (cmsSorten.isEmpty()) sorten else sorten.intersect(cmsSorten.toSet()).toList()

  private fun schränkeAufCmsFarbenEin(cmsFarben: List<Produktfarbe>) =
    if (cmsFarben.isEmpty()) farben else farben.intersect(cmsFarben.toSet()).toList()

  private fun schränkeAufCmsKlassifikationenEin(cmsKlassifikationIds: List<KlassifikationId>): List<Klassifikation> {
    if (cmsKlassifikationIds.isEmpty()) return klassifikationen

    val eingeschränkteKlassifikationIds =
      klassifikationen.map { it.id }.intersect(cmsKlassifikationIds.toSet()).toList()

    return klassifikationen.filter { eingeschränkteKlassifikationIds.contains(it.id) }
  }

  private fun schränkePreisbereichEin(cmsMinPreis: Preis?, cmsMaxPreis: Preis?): PreisBereich {
    val cmsMinPreisSchränktEin = cmsMinPreis != null && cmsMinPreis.istGrößerAls(preisBereich.minPreis)
    val cmsMaxPreisSchränktEin = cmsMaxPreis != null && cmsMaxPreis.istKleinerAls(preisBereich.maxPreis)

    return PreisBereich(
      minPreis = if (cmsMinPreisSchränktEin) cmsMinPreis else preisBereich.minPreis,
      maxPreis = if (cmsMaxPreisSchränktEin) cmsMaxPreis else preisBereich.maxPreis
    )
  }
}
