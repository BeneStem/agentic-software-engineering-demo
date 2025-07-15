package de.blume2000.finden.adapter.passive.operations

import de.blume2000.finden.domain.model.produkte.ProduktRepository
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import io.quarkus.arc.profile.UnlessBuildProfile
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped

@Startup
@ApplicationScoped
@UnlessBuildProfile("test")
class MetricsProvider(produktRepository: ProduktRepository, meterRegistry: MeterRegistry) {
  init {
    Gauge.builder("produkt/anzahl/gauge") { produktRepository.zähleProdukte().toDouble() }
      .register(meterRegistry)
  }
}
