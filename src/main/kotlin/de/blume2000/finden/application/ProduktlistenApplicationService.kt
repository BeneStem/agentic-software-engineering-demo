package de.blume2000.finden.application

import de.blume2000.finden.adapter.active.api.produkte.dtos.ProduktDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.ProdukteFilterDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.ProdukteMitVerfügbarenFilterwertenDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.VerfügbareFilterwerteDTO
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import de.blume2000.finden.domain.model.produkte.VerfügbareFilterwerteRepository
import de.blume2000.finden.domain.model.produkte.produkt.ProduktComperatorByBaseListOccurence
import de.blume2000.finden.domain.service.featureToggles.FeatureToggleDomainService
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProduktlistenApplicationService(
  private val produkteRepository: ProduktRepository,
  private val verfügbareFilterwerteRepository: VerfügbareFilterwerteRepository,
  private val featureToggleDomainService: FeatureToggleDomainService
) {

  fun gebeMirAlleVerfuegbarenProdukte(cmsProdukteFilter: CmsProdukteFilter, produkteSortierung: ProdukteSortierung) =
    gebeMirAlleVerfuegbarenProdukte(
      cmsProdukteFilter = cmsProdukteFilter,
      userProdukteFilter = UserProdukteFilter.NEUTRALER_FILTER,
      produkteSortierung = produkteSortierung
    )

  fun gebeMirAlleVerfuegbarenProdukte(
    cmsProdukteFilter: CmsProdukteFilter,
    userProdukteFilter: UserProdukteFilter,
    produkteSortierung: ProdukteSortierung
  ): List<ProduktDTO> {
    val featureSortierenEnabled = featureToggleDomainService.istFeatureToggleAktiv("produkte_sortieren")
    val inMemorySortierungNötig =
      featureSortierenEnabled && cmsProdukteFilter.produktNummern.isNotEmpty() && cmsProdukteFilter.produktnummernVerwendung == ProduktnummernVerwendung.SELEKTIONSBASIS
    val originalLimit = cmsProdukteFilter.getLimit()

    if (inMemorySortierungNötig) {
      cmsProdukteFilter.disableLimit()
    }

    var produkte = produkteRepository.ladeVerfügbareProdukte(
      cmsProdukteFilter = cmsProdukteFilter,
      userProdukteFilter = userProdukteFilter,
      produkteSortierung = produkteSortierung
    )

    if (inMemorySortierungNötig) {
      produkte = produkte.sortiere(
        ProduktComperatorByBaseListOccurence(cmsProdukteFilter.produktNummern)).begrenzeAufNEinträge(originalLimit)
    }

    return produkte.zuValideProduktDTOs()
  }

  fun gebeMirAlleVerfuegbarenProdukteUndFilterwerte(
    produkteFilterDTO: ProdukteFilterDTO): ProdukteMitVerfügbarenFilterwertenDTO {
    val cmsFilter = produkteFilterDTO.cmsProdukteFilter.zuProdukteFilter()
    val userFilter = produkteFilterDTO.userProdukteFilter.zuUserProdukteFilter()

    val produkte = gebeMirAlleVerfuegbarenProdukte(
      cmsProdukteFilter = cmsFilter,
      userProdukteFilter = userFilter,
      produkteSortierung = ProdukteSortierung()
    )
    val verfuegbareFilterwerte = gebeMirAlleVerfügbarenFilterwerte(
      cmsProdukteFilter = cmsFilter,
      userProdukteFilter = userFilter
    )

    return if (produkte.isEmpty()) {
      ProdukteMitVerfügbarenFilterwertenDTO(
        produkte = produkte,
        verfuegbareFilterwerte = VerfügbareFilterwerteDTO.LEERE_VERFUEGBARE_FILTERWERTE
      )
    } else {
      ProdukteMitVerfügbarenFilterwertenDTO(produkte = produkte, verfuegbareFilterwerte = verfuegbareFilterwerte)
    }
  }

  private fun gebeMirAlleVerfügbarenFilterwerte(
    cmsProdukteFilter: CmsProdukteFilter,
    userProdukteFilter: UserProdukteFilter
  ) =
    VerfügbareFilterwerteDTO.vonVerfügbareFilterwerte(
      verfügbareFilterwerteRepository.ladeVerfügbareFilterWerte(
        cmsProdukteFilter = cmsProdukteFilter,
        userProdukteFilter = userProdukteFilter
      ).schränkeAufCmsFilterwerteEin(cmsProdukteFilter = cmsProdukteFilter)
    ).entferneFilterMitNurEinemFilterwert()
}
