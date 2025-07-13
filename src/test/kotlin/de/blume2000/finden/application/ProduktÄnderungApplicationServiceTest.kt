package de.blume2000.finden.application

import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.testutils.erstelleAvroProdukt
import de.blume2000.finden.testutils.erstelleProdukt
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit")
internal class ProduktÄnderungApplicationServiceTest {

  @Test
  internal fun `Füge Produkt hinzu sofern es noch nicht vorhanden ist`() {
    // Given
    val repository: ProduktRepository = mockk()
    val service = ProduktÄnderungApplicationService(repository)
    val avroProdukt = erstelleAvroProdukt()
    every { repository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer())) } returns null
    every { repository.speicherProdukt(any()) } returns Unit

    // When
    service.importiereNeueProduktDaten(avroProdukt)

    // Then
    verify(exactly = 1) { repository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer())) }
    verify(exactly = 1) { repository.speicherProdukt(any()) }
    confirmVerified(repository)
  }

  @Test
  internal fun `Lösche ein vorhandenes Produkt`() {
    // Given
    val repository: ProduktRepository = mockk()
    val service = ProduktÄnderungApplicationService(repository)
    val avroProdukt = erstelleAvroProdukt(nummer = "20001234")
    every { repository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer())) } returns erstelleProdukt(nummer = "20001234")
    every { repository.entferneProdukt(Produktnummer(avroProdukt.getProduktNummer())) } returns Unit

    // When
    service.entferneProdukt(avroProdukt)

    // Then
    verify(exactly = 1) { repository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer())) }
    verify(exactly = 1) { repository.entferneProdukt(Produktnummer(avroProdukt.getProduktNummer())) }
    confirmVerified(repository)
  }

  @Test
  internal fun `Lösche nicht ein nicht vorhandenes Produkt`() {
    // Given
    val repository: ProduktRepository = mockk()
    val service = ProduktÄnderungApplicationService(repository)
    val avroProdukt = erstelleAvroProdukt()
    every { repository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer())) } returns null

    // When
    service.entferneProdukt(avroProdukt)

    // Then
    verify(exactly = 1) { repository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer())) }
    verify(exactly = 0) { repository.entferneProdukt(any()) }
    confirmVerified(repository)
  }
}
