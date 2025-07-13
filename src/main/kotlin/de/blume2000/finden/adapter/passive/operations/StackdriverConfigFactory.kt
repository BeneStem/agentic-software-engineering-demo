package de.blume2000.finden.adapter.passive.operations

import io.micrometer.stackdriver.StackdriverConfig
import io.smallrye.config.ConfigMapping
import java.util.UUID
import javax.enterprise.context.Dependent
import javax.enterprise.inject.Produces
import javax.inject.Singleton
import mu.KLogging
import org.eclipse.microprofile.config.Config
import org.eclipse.microprofile.config.inject.ConfigProperty

@Dependent
class StackdriverConfigFactory(
  private val stackdriverConfig: StackdriverConfiguration,
  @ConfigProperty(name = "b2k.zone") private val zone: String,
  @ConfigProperty(name = "quarkus.application.name") private val namespace: String
) {

  companion object : KLogging() {
    private const val CONFIGURATION_PREFIX = "stackdriver."
  }

  @Produces
  @Singleton
  @Throws(Throwable::class)
  fun configure(config: Config): StackdriverConfig {
    logger.info { "configuring stackdriver...." }

    return ResourceLabelAwareStackdriverConfig(
      mapOf(
        CONFIGURATION_PREFIX + StackdriverConfiguration::enabled.name to stackdriverConfig.enabled(),
        CONFIGURATION_PREFIX + StackdriverConfiguration::projectId.name to stackdriverConfig.projectId(),
        CONFIGURATION_PREFIX + StackdriverConfiguration::resourceType.name to stackdriverConfig.resourceType()
      ),
      mapOf(
        "location" to zone,
        "namespace" to namespace,
        "node_id" to UUID.randomUUID().toString()
      )
    )
  }
}

class ResourceLabelAwareStackdriverConfig(
  private val properties: Map<String, String>,
  private val resourceLabels: Map<String, String>
) : StackdriverConfig {

  override fun get(key: String): String? {
    return properties[key]
  }

  override fun resourceLabels(): Map<String, String> {
    return resourceLabels
  }
}

@ConfigMapping(prefix = "quarkus.micrometer.export.stackdriver", namingStrategy = ConfigMapping.NamingStrategy.VERBATIM)
interface StackdriverConfiguration {
  fun enabled(): String
  fun projectId(): String
  fun resourceType(): String
}
