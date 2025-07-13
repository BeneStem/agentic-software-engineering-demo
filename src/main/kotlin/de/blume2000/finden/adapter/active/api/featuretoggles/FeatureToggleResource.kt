package de.blume2000.finden.adapter.active.api.featuretoggles

import de.blume2000.finden.domain.model.featureToggles.FeatureToggle
import de.blume2000.finden.domain.service.featureToggles.FeatureToggleDomainService
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces

data class FeatureToggleDTO(val feature: String, val active: Boolean) {
  companion object {
    fun vonFeatureToggle(featureToggle: FeatureToggle): FeatureToggleDTO {
      return FeatureToggleDTO(
        feature = featureToggle.feature,
        active = featureToggle.active
      )
    }
  }
}

@Path("/api/finden/feature-toggles")
class FeatureToggleResource(val featureToggleDomainService: FeatureToggleDomainService) {

  @GET
  @Produces("application/json")
  fun holeFeatureToggles(): List<FeatureToggleDTO> = featureToggleDomainService.holeFeatureToggles().map { FeatureToggleDTO.vonFeatureToggle(it) }
}
