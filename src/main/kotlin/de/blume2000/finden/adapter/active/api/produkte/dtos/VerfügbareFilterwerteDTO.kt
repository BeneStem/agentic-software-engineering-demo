package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.VerfügbareFilterwerte

data class VerfügbareFilterwerteDTO(
  val sorten: List<SorteDTO>,
  val farben: List<FarbeDTO>,
  val preisbereich: PreisbereichDTO,
  val klassifikationen: List<KlassifikationDTO>,
  val liefertage: LiefertageDTO
) {
  fun entferneFilterMitNurEinemFilterwert() = this.copy(
    sorten = leereFilterwertListMitMaximalEinemWert(sorten),
    farben = leereFilterwertListMitMaximalEinemWert(farben),
    klassifikationen = leereFilterwertListMitMaximalEinemWert(klassifikationen)
  )

  private fun <T> leereFilterwertListMitMaximalEinemWert(liste: List<T>) = if (liste.size > 1) liste else emptyList()

  companion object {
    val LEERE_VERFUEGBARE_FILTERWERTE = VerfügbareFilterwerteDTO(
      sorten = emptyList(),
        farben = emptyList(),
        preisbereich = PreisbereichDTO(minPreis = null, maxPreis = null),
        klassifikationen = emptyList(),
        liefertage = LiefertageDTO(verfuegbareLiefertage = emptyList()),
    )

    fun vonVerfügbareFilterwerte(verfügbareFilterwerte: VerfügbareFilterwerte): VerfügbareFilterwerteDTO {
      return VerfügbareFilterwerteDTO(
        sorten = verfügbareFilterwerte.sorten.map { SorteDTO(it.asString()) },
        farben = verfügbareFilterwerte.farben.map { FarbeDTO(it.asString()) },
        preisbereich = PreisbereichDTO.vonPreisbereich(verfügbareFilterwerte.preisBereich),
        klassifikationen = verfügbareFilterwerte.klassifikationen.map {
          KlassifikationDTO(
            name = it.name.value,
            id = it.id.value
          )
        },
        liefertage = LiefertageDTO.vonLiefertage(verfügbareFilterwerte.liefertage)
      )
    }
  }
}
