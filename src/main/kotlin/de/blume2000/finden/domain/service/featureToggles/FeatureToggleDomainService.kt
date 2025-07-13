package de.blume2000.finden.domain.service.featureToggles

import de.blume2000.finden.configuration.UnleashConfiguration
import de.blume2000.finden.domain.model.featureToggles.FeatureToggle
import mu.KLogging
import org.jmolecules.ddd.annotation.Service
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@Service
class FeatureToggleDomainService(unleash: UnleashConfiguration) {

  companion object : KLogging()

  final val config = unleash.unleashConfig()
  val defaultUnleash = unleash.defaultUnleash(config)

  fun holeFeatureToggles(): List<FeatureToggle> = defaultUnleash.DefaultMore().featureToggleNames.map {
    FeatureToggle(it, defaultUnleash.isEnabled(it))
  }

  fun holeAktiveFeatureToggles(): List<FeatureToggle> = holeFeatureToggles().filter { it.active }

  fun istFeatureToggleAktiv(featureToggleName: String) =
    holeAktiveFeatureToggles().any { it.feature == featureToggleName }
}
