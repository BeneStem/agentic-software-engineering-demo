package de.blume2000.finden.adapter.active.messaging.produkte

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProduktImportMeterProvider(meterRegistry: MeterRegistry) {
  val attemptCounter: Counter = meterRegistry.counter("produktimport/counter", "result", "attempt")
  val successCounter: Counter = meterRegistry.counter("produktimport/counter", "result", "success")
  val businessErrorCounter: Counter = meterRegistry.counter("produktimport/counter", "result", "business-error")
  val technicalErrorCounter: Counter = meterRegistry.counter("produktimport/counter", "result", "technical-error")
}
