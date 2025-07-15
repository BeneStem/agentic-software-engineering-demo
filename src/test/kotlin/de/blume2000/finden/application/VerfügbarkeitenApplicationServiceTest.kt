package de.blume2000.finden.application

import com.mongodb.client.MongoClient
import de.blume2000.finden.adapter.passive.database.produkte.MongoProdukt
import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Lieferregion
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.NeueVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.testutils.erstelleProdukt
import de.blume2000.util.DateTimeUtil
import de.blume2000.util.TestContainers
import io.mockk.spyk
import io.mockk.verify
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import java.time.LocalDate
import jakarta.inject.Inject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
internal class VerfügbarkeitenApplicationServiceTest {

  @Inject
  private lateinit var produktRepository: ProduktRepository

  @Inject
  private lateinit var mongoClient: MongoClient

  @Inject
  lateinit var testee: VerfügbarkeitenApplicationService

  @BeforeEach
  fun cleanup() {
    mongoClient.getDatabase("finden-test").getCollection(MongoProdukt.MONGO_COLLECTION).drop()
  }

  @Test
  internal fun `Importierte Verfügbarkeit nicht, wenn das Produkt nicht existiert`() {
    // Given
    val produktRepositorySpy = spyk<ProduktRepository>()
    val bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow())
    val liefertag = Liefertag(LocalDate.now())
    val neueVerfügbarkeit = NeueVerfügbarkeit(
      produktnummer = Produktnummer("20003804"),
      lieferregionen = listOf(Lieferregion("SH")),
      liefertag = liefertag,
      bestellschluss = bestellschluss
    )
    // When
    VerfügbarkeitenApplicationService(produktRepositorySpy).importiereNeueVerfügbarkeit(neueVerfügbarkeit)

    // Then
    verify(exactly = 0) { produktRepositorySpy.speicherProdukt(any()) }
    verify(exactly = 1) { produktRepositorySpy.ladeProdukt(any()) }
  }

  @Test
  internal fun `Importierte Verfügbarkeit ist im richtigen Produkt in der DB gespeichert`() {
    // Given
    produktRepository.speicherProdukt(erstelleProdukt(nummer = "12341234"))
    produktRepository.speicherProdukt(erstelleProdukt(nummer = "20003804"))
    val bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
    val liefertag = Liefertag(LocalDate.now().plusDays(2))
    val neueVerfügbarkeit = NeueVerfügbarkeit(
      produktnummer = Produktnummer("20003804"),
      lieferregionen = listOf(Lieferregion("SH")),
      liefertag = liefertag,
      bestellschluss = bestellschluss
    )
    // When
    testee.importiereNeueVerfügbarkeit(neueVerfügbarkeit)

    // Then
    val gespeichertesProdukt = produktRepository.ladeProdukt(Produktnummer("20003804"))
    expectThat(gespeichertesProdukt).isNotNull()
    expectThat(gespeichertesProdukt?.verfügbarkeiten?.get(0)?.liefertag).isEqualTo(liefertag)
    expectThat(gespeichertesProdukt?.verfügbarkeiten?.get(0)?.bestellschluss).isEqualTo(bestellschluss)
  }

  @Test
  internal fun `Füge neue Verfügbarkeit zu vorhandenen Verfügbarkeiten im Produkt hinzu`() {
    // Given
    val produktnummer = "20003804"
    val produktVerfügbarkeit = ProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(1)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(2))
    )
    produktRepository.speicherProdukt(erstelleProdukt(nummer = produktnummer, verfügbarkeiten = listOf(produktVerfügbarkeit)))
    val liefertag = Liefertag(LocalDate.now().plusDays(2))
    val bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(2))
    val neueVerfügbarkeit = NeueVerfügbarkeit(
      produktnummer = Produktnummer(produktnummer),
      lieferregionen = listOf(Lieferregion("SH")),
      liefertag = liefertag,
      bestellschluss = bestellschluss
    )
    // When
    testee.importiereNeueVerfügbarkeit(neueVerfügbarkeit)

    // Then
    val gespeichertesProdukt = produktRepository.ladeProdukt(Produktnummer(produktnummer))
    expectThat(gespeichertesProdukt).isNotNull()
    expectThat(gespeichertesProdukt?.verfügbarkeiten?.map { it.liefertag.value }).isEqualTo(listOf(produktVerfügbarkeit.liefertag.value, neueVerfügbarkeit.liefertag.value))
    expectThat(gespeichertesProdukt?.verfügbarkeiten?.map { it.bestellschluss.value }).isEqualTo(
      listOf(produktVerfügbarkeit.bestellschluss.value, neueVerfügbarkeit.bestellschluss!!.value))
  }

  @Test
  internal fun `Löscht bestehende ProduktVerfügbarkeit, wenn Lieferregionen eine leere Liste ist`() {
    // Given
    val produktnummer = "20003804"
    val liefertag = Liefertag(LocalDate.now())
    val produktVerfügbarkeit = ProduktVerfügbarkeit(
      liefertag = liefertag,
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow())
    )
    produktRepository.speicherProdukt(erstelleProdukt(nummer = produktnummer, verfügbarkeiten = listOf(produktVerfügbarkeit)))

    val bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow())
    val neueVerfügbarkeit = NeueVerfügbarkeit(
      produktnummer = Produktnummer(produktnummer),
      lieferregionen = emptyList(),
      liefertag = liefertag,
      bestellschluss = bestellschluss
    )
    // When
    testee.importiereNeueVerfügbarkeit(neueVerfügbarkeit)

    // Then
    val gespeichertesProdukt = produktRepository.ladeProdukt(Produktnummer(produktnummer))
    expectThat(gespeichertesProdukt).isNotNull()
    expectThat(gespeichertesProdukt?.verfügbarkeiten).isEqualTo(emptyList())
  }

  @Test
  internal fun `Löscht ProduktVerfügbarkeiten mit Lieferdatum in der Vergangenheit`() {
    // Given
    val produktnummer = "20003804"
    val dateNow = LocalDate.now()
    val offsetDateTimeNow = DateTimeUtil.erstelleUTCNow()
    val produktVerfügbarkeit = ProduktVerfügbarkeit(
      liefertag = Liefertag(dateNow.minusDays(1)),
      bestellschluss = Bestellschluss(offsetDateTimeNow.minusDays(1).plusHours(1))
    )
    produktRepository.speicherProdukt(erstelleProdukt(nummer = produktnummer, verfügbarkeiten = listOf(produktVerfügbarkeit)))

    val neueVerfügbarkeit = NeueVerfügbarkeit(
      produktnummer = Produktnummer(produktnummer),
      lieferregionen = listOf(Lieferregion("HH")),
      liefertag = Liefertag(dateNow.plusDays(1)),
      bestellschluss = Bestellschluss(offsetDateTimeNow.plusHours(1))
    )
    // When
    testee.importiereNeueVerfügbarkeit(neueVerfügbarkeit)

    // Then
    val gespeichertesProdukt = produktRepository.ladeProdukt(Produktnummer(produktnummer))
    expectThat(gespeichertesProdukt).isNotNull()
    expectThat(gespeichertesProdukt?.verfügbarkeiten?.map { it.liefertag.value }).isEqualTo(
      listOf(
        neueVerfügbarkeit.liefertag.value
      )
    )
    expectThat(gespeichertesProdukt?.verfügbarkeiten?.map { it.bestellschluss.value }).isEqualTo(
      listOf(
        neueVerfügbarkeit.bestellschluss!!.value
      )
    )
  }
}
