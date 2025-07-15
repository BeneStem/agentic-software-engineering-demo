package de.blume2000.util

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.eclipse.microprofile.config.ConfigProvider
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

class TestContainers : QuarkusTestResourceLifecycleManager {
  private val useTestContainers =
    ConfigProvider.getConfig().getOptionalValue("b2k.testing.integration.use-testcontainers", Boolean::class.java)
      .orElse(true)
  private val mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:6.0"))
  //  TODO fix deprecation
  private val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))

  override fun start(): Map<String, String> {

    if (useTestContainers) {
      mongoDBContainer.start()
      kafka.start()
      return mapOf(
        Pair("quarkus.mongodb.connection-string", mongoDBContainer.replicaSetUrl),
        Pair("kafka.bootstrap.servers", kafka.bootstrapServers)
      )
    }

    return emptyMap()
  }

  override fun stop() {
    mongoDBContainer.close()
    kafka.close()
  }
}
