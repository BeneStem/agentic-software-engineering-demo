package de.blume2000.finden.adapter.active.messaging.verfuegbarkeiten

import de.blume2000.finden.adapter.active.messaging.KeineMetadatenException
import de.blume2000.finden.application.VerfügbarkeitenApplicationService
import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Lieferregion
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.NeueVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.VerfügbarkeitException
import io.smallrye.common.annotation.Blocking
import io.smallrye.reactive.messaging.kafka.IncomingKafkaCloudEventMetadata
import java.time.LocalDate
import java.util.concurrent.CompletionStage
import javax.enterprise.context.ApplicationScoped
import mu.KLogging
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import de.blume2000.avro.verfuegbarkeiten.Verfuegbarkeit as AvroVerfuegbarkeit

@ApplicationScoped
class VerfügbarkeitConsumer(
  val verfügbarkeitenApplicationService: VerfügbarkeitenApplicationService,
  val verfügbarkeitImportMeterProvider: VerfügbarkeitImportMeterProvider,
) {
  companion object : KLogging()

  @Incoming("verfuegbarkeiten")
  @Blocking
  @Suppress("ReturnCount", "TooGenericExceptionCaught")
  fun consume(message: Message<AvroVerfuegbarkeit>): CompletionStage<Void> {
    val metadata = message.getMetadata(IncomingKafkaCloudEventMetadata::class.java)
    val payload = message.payload

    meldeStart(payload)

    if (metadata.isEmpty) {
      return meldeTechnischenFehler(
        KeineMetadatenException("Anfrage von VerfügbarkeitConsumer erhielt eine leere Message"),
        message
      )
    }

    if (metadata.get().type != KafkaVerfügbarkeitEventType.VERFUEGBARKEITEN_UPDATE_KAFKA_EVENT.name) {
      return ignoriereNachricht(message, metadata.get().type)
    }

    try {
      verfügbarkeitenApplicationService.importiereNeueVerfügbarkeit(payload.zuNeueVerfügbarkeit())
    } catch (verfügbarkeitException: VerfügbarkeitException) {
      return meldeFachlichenFehler(verfügbarkeitException, message)
    } catch (exception: Exception) {
      return meldeTechnischenFehler(exception, message)
    }

    return meldeErfolg(message)
  }

  private fun meldeStart(payload: AvroVerfuegbarkeit?) {
    verfügbarkeitImportMeterProvider.attemptCounter.increment()
    logger.info { "Verfügbarkeit wird eingelesen: $payload" }
  }

  private fun meldeErfolg(message: Message<AvroVerfuegbarkeit>): CompletionStage<Void> {
    logger.info { "Message erfolgreich konsumiert." }
    verfügbarkeitImportMeterProvider.successCounter.increment()
    return message.ack()
  }

  private fun ignoriereNachricht(message: Message<AvroVerfuegbarkeit>, eventType: String): CompletionStage<Void> {
    logger.warn { "Ignoriere Verfügbarkeit, da EventType $eventType nicht unterstützt wird." }
    return message.ack()
  }

  private fun meldeTechnischenFehler(
    exception: Exception,
    message: Message<AvroVerfuegbarkeit>
  ): CompletionStage<Void> {
    logger.error { exception.message }
    verfügbarkeitImportMeterProvider.technicalErrorCounter.increment()
    return message.nack(exception)
  }

  private fun meldeFachlichenFehler(exception: Exception, message: Message<AvroVerfuegbarkeit>): CompletionStage<Void> {
    logger.warn { exception.message }
    verfügbarkeitImportMeterProvider.businessErrorCounter.increment()
    return message.ack()
  }
}

fun AvroVerfuegbarkeit.zuNeueVerfügbarkeit(): NeueVerfügbarkeit {
  return NeueVerfügbarkeit(
    produktnummer = Produktnummer(this.getProduktNummer()),
    lieferregionen = this.getLieferRegion().map { Lieferregion(it) },
    liefertag = Liefertag(LocalDate.parse(this.getLieferTag())),
    bestellschluss = this.getAblaufDaten().map {
      Bestellschluss.vonString(it.getDatumBestellSchluss())
    }.maxByOrNull { it.value }
  )
}
