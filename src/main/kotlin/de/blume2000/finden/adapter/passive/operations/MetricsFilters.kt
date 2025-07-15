package de.blume2000.finden.adapter.passive.operations

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.config.MeterFilter
import io.micrometer.core.instrument.config.MeterFilterReply
import mu.KLogging
import jakarta.enterprise.inject.Produces
import jakarta.inject.Singleton

@Singleton
class MetricsFilters {
  companion object : KLogging() {
    private val allowedKafkaMetrics = listOf(
      "kafka.producer.node.request.latency.max",
      "kafka.producer.record.error.total",
    )
  }

  @Produces
  @Singleton
  fun denyNotNeededKafkaMetrics() = object : MeterFilter {
    override fun accept(id: Meter.Id) = if (!id.name.startsWith("kafka") || allowedKafkaMetrics.contains(id.name)) {
      MeterFilterReply.ACCEPT
    } else {
      MeterFilterReply.DENY
    }
  }
}
