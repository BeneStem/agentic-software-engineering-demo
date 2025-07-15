package de.blume2000.finden.adapter.active.messaging.produkte

enum class KafkaProduktEventType(val readableName: String) {
  PRODUKT_UPDATE_KAFKA_EVENT("ProduktUpdate"),
  PRODUKT_DELETION_KAFKA_EVENT("ProduktDeletion"),
  TOMBSTONE_EVENT("Tombstone")
}
