package de.blume2000.finden.adapter.passive.operations

import io.micrometer.core.instrument.MeterRegistry
import jakarta.annotation.Priority
import io.quarkus.arc.Unremovable
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.Dependent
import jakarta.enterprise.event.Observes
import jakarta.enterprise.inject.Alternative
import jakarta.interceptor.Interceptor.Priority.PLATFORM_AFTER
import org.eclipse.microprofile.config.inject.ConfigProperty

// TODO do we still need this?! we have StackdriverConfigFactory now.
@Dependent
@Unremovable
@Alternative
@Priority(PLATFORM_AFTER)
class MetricsAugmentor {

  fun startup(
    @Observes startupEvent: StartupEvent, globalRegistry: MeterRegistry,
    @ConfigProperty(name = "quarkus.application.name") applicationName: String
  ) {
    globalRegistry.config().commonTags("application", applicationName)
  }
}
