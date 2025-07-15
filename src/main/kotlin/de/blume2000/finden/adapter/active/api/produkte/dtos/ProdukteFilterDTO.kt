package de.blume2000.finden.adapter.active.api.produkte.dtos

import jakarta.validation.Valid

data class ProdukteFilterDTO(
  @field:Valid
  val cmsProdukteFilter: CmsProdukteFilterDTO,

  @field:Valid
  val userProdukteFilter: UserProdukteFilterDTO
)
