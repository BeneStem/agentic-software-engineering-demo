package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.PreisBereich
import jakarta.validation.Valid

data class PreisbereichDTO(
  @field:Valid
  val minPreis: PreisDTO? = null,

  @field:Valid
  val maxPreis: PreisDTO? = null
) {
  fun zuPreisbereich() = PreisBereich(
    minPreis?.zuEuroPreis(), maxPreis?.zuEuroPreis()
  )

  companion object {
    fun vonPreisbereich(preisBereich: PreisBereich) =
      PreisbereichDTO(preisBereich.minPreis?.let { PreisDTO.vonPreis(preisBereich.minPreis) },
        preisBereich.maxPreis?.let { PreisDTO.vonPreis(preisBereich.maxPreis) })
  }
}
