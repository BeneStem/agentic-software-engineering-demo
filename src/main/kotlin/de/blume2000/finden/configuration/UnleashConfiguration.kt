package de.blume2000.finden.configuration

import no.finn.unleash.DefaultUnleash
import no.finn.unleash.util.UnleashConfig
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.inject.Singleton
import javax.ws.rs.Produces

@Singleton
class UnleashConfiguration(
  @ConfigProperty(name = "b2k.gcp-project.environment")
  private val environment: String,
  @ConfigProperty(name = "b2k.unleash.instance-id")
  private val instanceId: String,
  @ConfigProperty(name = "b2k.unleash.project-id")
  private val projectId: String
) {

  @Produces
  fun unleashConfig(): UnleashConfig = UnleashConfig.builder()
    .appName(environment)
    .instanceId(instanceId)
    .unleashAPI("https://gitlab.com/api/v4/feature_flags/unleash/$projectId")
    .build()

  @Produces
  fun defaultUnleash(unleashConfig: UnleashConfig) = DefaultUnleash(unleashConfig)
}
