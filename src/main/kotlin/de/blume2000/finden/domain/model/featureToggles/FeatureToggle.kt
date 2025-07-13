package de.blume2000.finden.domain.model.featureToggles

import org.jmolecules.ddd.annotation.Entity

@Entity
data class FeatureToggle(val feature: String, val active: Boolean)
