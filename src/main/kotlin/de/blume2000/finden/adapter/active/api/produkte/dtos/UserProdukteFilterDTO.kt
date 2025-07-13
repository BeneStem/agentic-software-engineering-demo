package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import javax.validation.Valid

data class UserProdukteFilterDTO(
  @field:Valid
  val sorten: List<SorteDTO>,

  @field:Valid
  val farben: List<FarbeDTO>,

  @field:Valid
  val preisbereich: PreisbereichDTO? = null,

  @field:Valid
  val klassifikationen: List<KlassifikationDTO>,

  @field:Valid
  val liefertage: LiefertageDTO
) {

  fun zuUserProdukteFilter(): UserProdukteFilter {
    return UserProdukteFilter(
      blumensorten = sorten.map { it.zuBlumensorte() },
      farben = farben.map { it.zuProduktfarbe() },
      preisBereich = preisbereich?.zuPreisbereich(),
      klassifikationen = klassifikationen.map { it.zuKlassifikation() },
      liefertage = liefertage.zuLiefertage()
    )
  }
}
