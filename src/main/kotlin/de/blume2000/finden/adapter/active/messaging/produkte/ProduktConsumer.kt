package de.blume2000.finden.adapter.active.messaging.produkte

import de.blume2000.avro.produkte.AvroProdukt
import de.blume2000.finden.adapter.active.messaging.KeineMetadatenException
import de.blume2000.finden.adapter.active.messaging.UngültigerEventTypeException
import de.blume2000.finden.application.ProduktÄnderungApplicationService
import de.blume2000.finden.domain.model.produkte.ProdukteException
import io.smallrye.reactive.messaging.kafka.IncomingKafkaCloudEventMetadata
import mu.KLogging
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import java.util.concurrent.CompletionStage
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProduktConsumer(
  private val produktÄnderungApplicationService: ProduktÄnderungApplicationService,
  private val produktImportMeterProvider: ProduktImportMeterProvider,
) {

  companion object : KLogging()

  @Suppress("ReturnCount", "TooGenericExceptionCaught")
  @Incoming("produkte")
  fun consume(message: Message<AvroProdukt>): CompletionStage<Void> {
    val metadata = message.getMetadata(IncomingKafkaCloudEventMetadata::class.java)
    val payload = message.payload

    meldeStart(payload)

    if (metadata.isEmpty) {
      return meldeFehler(KeineMetadatenException("Anfrage von ProduktConsumer erhielt eine leere Message"), message)
    }

    val eventType = metadata.get().type

    if (eventType != KafkaProduktEventType.TOMBSTONE_EVENT.name && payload == null) {
      return meldeFehler(UngültigerEventTypeException("Kafka Event Type $eventType mit leerem Payload"), message)
    }

    try {
      when (eventType) {
        KafkaProduktEventType.PRODUKT_UPDATE_KAFKA_EVENT.name -> aktualisiereProdukt(payload)
        KafkaProduktEventType.PRODUKT_DELETION_KAFKA_EVENT.name -> löscheProdukt(payload)
      }
    } catch (produkteException: ProdukteException) {
      return meldeFehler(produkteException, message)
    } catch (exception: Exception) {
      return meldeTechnischenFehler(exception, message)
    }

    return meldeErfolg(message)
  }

  private fun meldeStart(payload: AvroProdukt?) {
    produktImportMeterProvider.attemptCounter.increment()
    logger.info { "Produkt wird eingelesen: $payload" }
  }

  private fun meldeErfolg(message: Message<AvroProdukt>): CompletionStage<Void> {
    logger.info { "Message erfolgreich konsumiert." }
    produktImportMeterProvider.successCounter.increment()
    return message.ack.get()
  }

  private fun meldeFehler(exception: Exception, message: Message<AvroProdukt>): CompletionStage<Void> {
    logger.warn { exception.message }
    produktImportMeterProvider.businessErrorCounter.increment()
    return message.nack(exception)
  }

  private fun meldeTechnischenFehler(exception: Exception, message: Message<AvroProdukt>): CompletionStage<Void> {
    logger.error { exception.message }
    produktImportMeterProvider.technicalErrorCounter.increment()
    return message.ack()
  }

  private fun löscheProdukt(produkt: AvroProdukt) {
    produktÄnderungApplicationService.entferneProdukt(produkt)
  }

  private fun aktualisiereProdukt(produkt: AvroProdukt) {
    if (produkt.getAktiv() && !produkt.getAusverkauft()) {
      produktÄnderungApplicationService.importiereNeueProduktDaten(produkt)
    } else {
      produktÄnderungApplicationService.entferneProdukt(produkt)
    }
  }
}
