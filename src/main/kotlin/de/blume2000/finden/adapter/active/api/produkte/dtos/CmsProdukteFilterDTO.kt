package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

data class CmsProdukteFilterDTO(
  @field:Valid
  val klassifikationen: List<KlassifikationDTO> = emptyList(),

  @field:Valid
  val preisbereich: PreisbereichDTO? = null,

  @field:Valid
  val produktNummern: List<ProduktnummerDTO> = emptyList(),

  @field:Pattern(regexp = "^(ausblenden|selektionsbasis)$")
  val produktnummernVerwendung: String? = null,

  @field:Min(value = 0L)
  val limit: Int? = null,

  @field:Valid
  val farben: List<FarbeDTO> = emptyList(),

  @field:Valid
  val blumensorten: List<SorteDTO> = emptyList()
) {
  fun zuProdukteFilter(): CmsProdukteFilter {
    return CmsProdukteFilter(
      klassifikationen = klassifikationen.map { it.zuKlassifikationId() },
      minPreis = preisbereich?.minPreis?.zuEuroPreis(),
      maxPreis = preisbereich?.maxPreis?.zuEuroPreis(),
      produktNummern = produktNummern.map { it.zuProduktnummer() },
      produktnummernVerwendung = produktnummernVerwendung?.let { ProduktnummernVerwendung.vonString(it) }
        ?: ProduktnummernVerwendung.KEINE,
      limit = limit,
      farben = farben.map { it.zuProduktfarbe() },
      blumensorten = blumensorten.map { it.zuBlumensorte() }
    )
  }
}
