package de.blume2000.finden.adapter.passive.operations

import io.micrometer.core.instrument.MeterRegistry
import io.quarkus.arc.Priority
import io.quarkus.arc.Unremovable
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.Dependent
import javax.enterprise.event.Observes
import javax.enterprise.inject.Alternative
import javax.interceptor.Interceptor.Priority.PLATFORM_AFTER
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
