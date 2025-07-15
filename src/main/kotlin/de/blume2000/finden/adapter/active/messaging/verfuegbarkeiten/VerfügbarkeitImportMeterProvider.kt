package de.blume2000.finden.adapter.active.messaging.verfuegbarkeiten

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class VerfügbarkeitImportMeterProvider(meterRegistry: MeterRegistry) {
  val attemptCounter: Counter = meterRegistry.counter("verfuegbarkeitimport/counter", "result", "attempt")
  val successCounter: Counter = meterRegistry.counter("verfuegbarkeitimport/counter", "result", "success")
  val businessErrorCounter: Counter = meterRegistry.counter("verfuegbarkeitimport/counter", "result", "business-error")
  val technicalErrorCounter: Counter = meterRegistry.counter("verfuegbarkeitimport/counter", "result", "technical-error")
}
