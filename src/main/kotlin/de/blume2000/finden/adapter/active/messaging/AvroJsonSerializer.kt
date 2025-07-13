package de.blume2000.finden.adapter.active.messaging

import mu.KLogging
import org.apache.kafka.common.serialization.Serializer
import kotlin.text.Charsets.UTF_8

class AvroJsonSerializer<T> : Serializer<T> {

  companion object : KLogging()

  @Suppress("TooGenericExceptionCaught")
  override fun serialize(topic: String, data: T?): ByteArray? {
    return if (data != null) {
      try {
        data.toString().toByteArray(UTF_8)
      } catch (exception: Exception) {
        logger.error(exception) { "exception during serialization of $data" }
        throw exception
      }
    } else null
  }

  @Suppress("EmptyFunctionBlock")
  override fun close() {
  }

  @Suppress("EmptyFunctionBlock")
  override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {
  }
}
