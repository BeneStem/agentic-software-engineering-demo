package de.blume2000.util

import java.time.OffsetDateTime
import java.time.ZoneOffset

object DateTimeUtil {
  fun erstelleUTCNow(): OffsetDateTime = wandleZeitpunktNachUTCUm(OffsetDateTime.now())

  fun wandleZeitpunktNachUTCUm(offsetDateTime: OffsetDateTime): OffsetDateTime =
    offsetDateTime.withOffsetSameInstant(ZoneOffset.UTC).withNano(0)
}
