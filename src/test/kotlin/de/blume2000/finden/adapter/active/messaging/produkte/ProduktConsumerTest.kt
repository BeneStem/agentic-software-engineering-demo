package de.blume2000.finden.adapter.active.messaging.produkte

import de.blume2000.avro.produkte.AvroProdukt
import de.blume2000.finden.adapter.active.messaging.KeineMetadatenException
import de.blume2000.finden.adapter.active.messaging.UngültigerEventTypeException
import de.blume2000.finden.application.ProduktÄnderungApplicationService
import de.blume2000.util.AvroProduktTestUtil
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.smallrye.reactive.messaging.ce.impl.DefaultIncomingCloudEventMetadata
import io.smallrye.reactive.messaging.kafka.IncomingKafkaCloudEventMetadata
import io.smallrye.reactive.messaging.kafka.impl.ce.DefaultIncomingKafkaCloudEventMetadata
import java.net.URI
import java.util.Optional
import java.util.UUID
import org.eclipse.microprofile.reactive.messaging.Message
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class ProduktConsumerTest {

  private val meterProvider: ProduktImportMeterProvider = mockk()

  @BeforeEach
  fun prepareMeterProvider() {
    every { meterProvider.attemptCounter.increment() } returns Unit
    every { meterProvider.successCounter.increment() } returns Unit
    every { meterProvider.technicalErrorCounter.increment() } returns Unit
    every { meterProvider.businessErrorCounter.increment() } returns Unit
  }

  @Test
  internal fun `Sollte ein Produkt importieren, wenn es aktiv ist`() {
    // Given
    val service: ProduktÄnderungApplicationService = mockk()
    val expectedAvroProdukt = slot<AvroProdukt>()

    every { service.importiereNeueProduktDaten(capture(expectedAvroProdukt)) } returns Unit
    val payload = AvroProduktTestUtil.createMessagePayload()
    val consumer = ProduktConsumer(service, meterProvider)

    // When
    consumer.consume(buildKafkaMessage(payload, buildMetadata()))

    // Then
    verify(exactly = 1) { service.importiereNeueProduktDaten(any()) }
    confirmVerified(service)
  }

  @Test
  internal fun `Sollte ein Produkt entfernen, wenn ein 'Deletion'-Event eintrifft`() {
    // Given
    val service: ProduktÄnderungApplicationService = mockk()
    every { service.entferneProdukt(any()) } returns Unit
    val payload = AvroProduktTestUtil.createMessagePayload()
    val consumer = ProduktConsumer(service, meterProvider)

    // When
    consumer.consume(
      buildKafkaMessage(
        payload,
        buildMetadata(KafkaProduktEventType.PRODUKT_DELETION_KAFKA_EVENT.name)
      )
    )

    // Then
    verify(exactly = 1) { service.entferneProdukt(any()) }
    confirmVerified(service)
  }

  @Test
  internal fun `Sollte nichts tun, wenn ein 'Tombstone'-Event eintrifft`() {
    // Given
    val service: ProduktÄnderungApplicationService = mockk()
    val payload = AvroProduktTestUtil.createMessagePayload()
    val consumer = ProduktConsumer(service, meterProvider)

    // When
    consumer.consume(buildKafkaMessage(payload, buildMetadata(KafkaProduktEventType.TOMBSTONE_EVENT.name)))

    // Then
    verify(exactly = 0) { service.entferneProdukt(any()) }
    verify(exactly = 0) { service.importiereNeueProduktDaten(any()) }
    confirmVerified(service)
  }

  @Test
  internal fun `Sollte ein Produkt entfernen, wenn es nicht aktiv ist`() {
    // Given
    val service: ProduktÄnderungApplicationService = mockk()
    every { service.entferneProdukt(any()) } returns Unit
    val payload = AvroProduktTestUtil.createMessagePayload(aktiv = false)
    val consumer = ProduktConsumer(service, meterProvider)

    // When
    consumer.consume(buildKafkaMessage(payload, buildMetadata()))

    // Then
    verify(exactly = 1) { service.entferneProdukt(any()) }
    confirmVerified(service)
  }

  @Test
  internal fun `Wenn keine Metadaten vorhanden sind, wird die Message genackt`() {
    // Given
    val service = mockk<ProduktÄnderungApplicationService>()
    val message = mockk<Message<AvroProdukt>>()
    every {
      hint(AvroProdukt::class)
      message.payload
    } answers { AvroProduktTestUtil.createMessagePayload() }
    every { message.getMetadata(IncomingKafkaCloudEventMetadata::class.java) } returns Optional.empty()
    every { message.nack(any()) } answers { message.nack.apply(KeineMetadatenException("Anfrage von ProduktConsumer erhielt eine leere Message")) }
    every { message.nack } answers { callOriginal() }

    // When
    ProduktConsumer(service, meterProvider).consume(message)

    // Then
    verify(exactly = 1) { message.getMetadata(IncomingKafkaCloudEventMetadata::class.java) }
    verify(exactly = 1) { message.getPayload() }
    verify(exactly = 1) { message.nack(KeineMetadatenException("Anfrage von ProduktConsumer erhielt eine leere Message")) }
    verify(exactly = 1) { message.getNack() }
    confirmVerified(message)
    confirmVerified(service)
  }

  @Test
  internal fun `Wenn der Payload leer und es kein Tombstone-Event ist, wird die Message genackt`() {
    // Given
    val eventType = KafkaProduktEventType.PRODUKT_UPDATE_KAFKA_EVENT.name
    val service = mockk<ProduktÄnderungApplicationService>()
    val message = mockk<Message<AvroProdukt>>()
    every {
      hint(AvroProdukt::class)
      message.payload
    } answers { null }
    every { message.getMetadata(IncomingKafkaCloudEventMetadata::class.java) } returns Optional.of(
      buildMetadata(
        eventType
      )
    )
    every { message.nack(any()) } answers { message.nack.apply(UngültigerEventTypeException("Kafka Event Type $eventType mit leerem Payload")) }
    every { message.nack } answers { callOriginal() }

    // When
    ProduktConsumer(service, meterProvider).consume(message)

    // Then
    verify(exactly = 1) { message.getMetadata(IncomingKafkaCloudEventMetadata::class.java) }
    verify(exactly = 1) { message.getPayload() }
    verify(exactly = 1) { message.nack(UngültigerEventTypeException("Kafka Event Type $eventType mit leerem Payload")) }
    verify(exactly = 1) { message.getNack() }
    confirmVerified(message)
    confirmVerified(service)
  }

  @Test
  internal fun `Wenn der EventType unbekannt ist, wird nichts gemacht und wir bekommen ein ack`() {
    // Given
    val eventType = "unbekannter_Eventtype"
    val service = mockk<ProduktÄnderungApplicationService>()
    val message = mockk<Message<AvroProdukt>>()
    every {
      hint(AvroProdukt::class)
      message.payload
    } answers { AvroProduktTestUtil.createMessagePayload() }
    every { message.getMetadata(IncomingKafkaCloudEventMetadata::class.java) } returns Optional.of(
      buildMetadata(
        eventType
      )
    )
    every { message.ack } answers { callOriginal() }

    // When
    ProduktConsumer(service, meterProvider).consume(message)

    // Then
    verify(exactly = 1) { message.getMetadata(IncomingKafkaCloudEventMetadata::class.java) }
    verify(exactly = 1) { message.getPayload() }
    verify(exactly = 1) { message.getAck() }
    confirmVerified(message)
    confirmVerified(service)
  }

  private fun <T> buildKafkaMessage(
    payload: T,
    metadata: DefaultIncomingKafkaCloudEventMetadata<String, Unit>
  ): Message<T> {
    return Message.of(payload).addMetadata(metadata)
  }

  private fun buildMetadata(
    kafkaEventType: String = KafkaProduktEventType.PRODUKT_UPDATE_KAFKA_EVENT.name,
    eventVersion: String = "1.1.0"
  ): DefaultIncomingKafkaCloudEventMetadata<String, Unit> {
    val incomingCloudEventMetadata = DefaultIncomingCloudEventMetadata(
      "1",
      UUID.randomUUID().toString(),
      URI.create("https://entscheiden-produkte.blume2000.de"),
      kafkaEventType,
      null,
      null,
      null,
      null,
      mapOf(Pair("eventversion", eventVersion)),
      Unit
    )
    return DefaultIncomingKafkaCloudEventMetadata<String, Unit>(incomingCloudEventMetadata)
  }
}
