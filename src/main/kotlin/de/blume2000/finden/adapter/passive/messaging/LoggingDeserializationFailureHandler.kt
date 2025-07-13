package de.blume2000.finden.adapter.passive.messaging

import io.quarkus.arc.Unremovable
import io.smallrye.reactive.messaging.kafka.DeserializationFailureHandler
import javax.enterprise.context.ApplicationScoped
import mu.KLogging
import org.apache.kafka.common.header.Headers
import javax.inject.Named

@ApplicationScoped
@Unremovable
@Named(value = "LoggingDeserializationFailureHandler")
class LoggingDeserializationFailureHandler<T> : DeserializationFailureHandler<T> {

  companion object : KLogging()

  override fun handleDeserializationFailure(
    topic: String, isKey: Boolean, deserializer: String, data: ByteArray,
    exception: Exception, headers: Headers
  ): T? {
    logger.error(exception) { "error during deserialization of record from topic $topic" }
    return null
  }
}
