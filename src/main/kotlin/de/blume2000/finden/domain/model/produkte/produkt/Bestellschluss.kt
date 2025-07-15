package de.blume2000.finden.domain.model.produkte.produkt

import java.time.OffsetDateTime
import org.jmolecules.ddd.annotation.ValueObject

@ValueObject
data class Bestellschluss(private val init: OffsetDateTime) {
  // to ensure consistent datetime objects we work with seconds precision only
  val value: OffsetDateTime = init.withNano(0)

  fun asString(): String = value.toString()

  fun istAbgelaufen(): Boolean {
    return value.isBefore(OffsetDateTime.now(value.offset).withNano(0))
  }

  override fun equals(other: Any?) = (other is Bestellschluss) && other.value.isEqual(value)
  override fun hashCode() = value.hashCode()
  override fun toString() = value.toString()

  companion object {
    fun vonString(offsetDateTimeString: String) = Bestellschluss(OffsetDateTime.parse(offsetDateTimeString))
  }
}
