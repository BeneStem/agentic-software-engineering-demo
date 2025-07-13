package de.blume2000.finden.application

import com.mongodb.client.MongoClient
import de.blume2000.finden.adapter.passive.database.produkte.MongoProdukt
import de.blume2000.finden.adapter.passive.database.produkte.ProduktMongoRepository
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.Produkte
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import de.blume2000.finden.domain.model.produkte.VerfügbareFilterwerteRepository
import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.service.featureToggles.FeatureToggleDomainService
import de.blume2000.finden.testutils.erstelleProdukt
import de.blume2000.finden.testutils.erstelleProduktVerfügbarkeit
import de.blume2000.finden.testutils.erstelleVerfügbarkeiten
import de.blume2000.util.DateTimeUtil
import de.blume2000.util.TestContainers
import io.mockk.every
import io.mockk.mockk
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import java.time.LocalDate
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
internal class ProduktlistenApplicationServiceTest {

  @Inject
  private lateinit var produktRepository: ProduktMongoRepository

  @Inject
  private lateinit var verfügbareFilterwerteRepository: VerfügbareFilterwerteRepository

  private val mockedFeatureService: FeatureToggleDomainService = mockk()

  @Inject
  private lateinit var mongoClient: MongoClient

  init {
    every { mockedFeatureService.istFeatureToggleAktiv("produkte_sortieren") } returns true
  }

  @BeforeEach
  fun cleanup() {
    produktRepository.deleteAll()
    mongoClient.getDatabase("produkte-test").getCollection(MongoProdukt.MONGO_COLLECTION).dropIndexes()
  }

  @Test
  internal fun `Liefert keine Produkte, wenn das ProdukteRepository leer ist`() {
    // Given
    val applicationService =
      ProduktlistenApplicationService(produktRepository, verfügbareFilterwerteRepository, mockedFeatureService)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()
    // When
    val produkte = applicationService.gebeMirAlleVerfuegbarenProdukte(cmsProdukteFilter, leererSortierung)

    // Then
    expectThat(produkte).isEmpty()
    expectThat(produkte.size).isEqualTo(0)
  }

  @Test
  internal fun `Liefert kein Produkt wenn Produktverfügbarkeit abgelaufen ist`() {
    // Given
    val applicationService =
      ProduktlistenApplicationService(produktRepository, verfügbareFilterwerteRepository, mockedFeatureService)
    val abgelaufeneVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().minusDays(1)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().minusDays(2))
    )
    val produkt = erstelleProdukt(verfügbarkeiten = listOf(abgelaufeneVerfügbarkeit))
    produktRepository.speicherProdukt(produkt)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    // When
    val produkte = applicationService.gebeMirAlleVerfuegbarenProdukte(cmsProdukteFilter, leererSortierung)

    // Then
    expectThat(produkte).isEmpty()
  }

  @Test
  internal fun `Liefert kein Produkt wenn keine Produktverfügbarkeit vorhanden ist`() {
    // Given
    val applicationService =
      ProduktlistenApplicationService(produktRepository, verfügbareFilterwerteRepository, mockedFeatureService)
    val produkt = erstelleProdukt()
    produktRepository.speicherProdukt(produkt)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    // When
    val produkte = applicationService.gebeMirAlleVerfuegbarenProdukte(cmsProdukteFilter, leererSortierung)

    // Then
    expectThat(produkte).isEmpty()
  }

  @Test
  internal fun `Liefert Produkte die nur eine Verfügbarkeit haben (wobei dann letztmögliche und nächstmögliche verfügbarkeit die selbe sein sollen)`() {
    // Given

    val applicationService =
      ProduktlistenApplicationService(produktRepository, verfügbareFilterwerteRepository, mockedFeatureService)
    val produktVerfügbarkeiten = listOf(erstelleProduktVerfügbarkeit())
    val produkt = erstelleProdukt(verfügbarkeiten = produktVerfügbarkeiten)
    val nichtVerfügbaresProdukt = erstelleProdukt(nummer = "NICHT_VERFUEGBAR")
    produktRepository.speicherProdukt(produkt)
    produktRepository.speicherProdukt(nichtVerfügbaresProdukt)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    // When
    val produkte = applicationService.gebeMirAlleVerfuegbarenProdukte(cmsProdukteFilter, leererSortierung)

    // Then
    expectThat(produkte.size).isEqualTo(1)
    expectThat(produkte.first().nummer).isEqualTo(produkt.nummer.asString())
    expectThat(produkte.first().naechstmoeglicheVerfuegbarkeit.liefertag).isEqualTo(
      produktVerfügbarkeiten.first().liefertag.asString())
    expectThat(produkte.first().naechstmoeglicheVerfuegbarkeit.bestellschluss).isEqualTo(
      produktVerfügbarkeiten.first().bestellschluss.asString())
    expectThat(produkte.first().letztmoeglicheVerfuegbarkeit.liefertag).isEqualTo(
      produktVerfügbarkeiten.first().liefertag.asString())
    expectThat(produkte.first().letztmoeglicheVerfuegbarkeit.bestellschluss).isEqualTo(
      produktVerfügbarkeiten.first().bestellschluss.asString())
  }

  @Test
  internal fun `Liefert alle Produkte die verfügbar sind mit unterschiedlicher erster und letzter Verfügbarkeit`() {
    // Given
    val applicationService =
      ProduktlistenApplicationService(produktRepository, verfügbareFilterwerteRepository, mockedFeatureService)
    val nächstmöglicheVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(2)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
    )
    val letztmöglicheVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(2)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
    )
    val produktVerfügbarkeiten = listOf(nächstmöglicheVerfügbarkeit, letztmöglicheVerfügbarkeit)

    val produkt = erstelleProdukt(verfügbarkeiten = produktVerfügbarkeiten)
    val nichtVerfügbaresProdukt = erstelleProdukt(nummer = "NICHT_VERFUEGBAR")
    produktRepository.speicherProdukt(produkt)
    produktRepository.speicherProdukt(nichtVerfügbaresProdukt)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    // When
    val produkte = applicationService.gebeMirAlleVerfuegbarenProdukte(cmsProdukteFilter, leererSortierung)

    // Then
    expectThat(produkte.size).isEqualTo(1)
    expectThat(produkte.first().nummer).isEqualTo(produkt.nummer.asString())
    expectThat(produkte.first().naechstmoeglicheVerfuegbarkeit.liefertag).isEqualTo(
      nächstmöglicheVerfügbarkeit.liefertag.asString())
    expectThat(produkte.first().naechstmoeglicheVerfuegbarkeit.bestellschluss).isEqualTo(
      nächstmöglicheVerfügbarkeit.bestellschluss.asString())
    expectThat(produkte.first().letztmoeglicheVerfuegbarkeit.liefertag).isEqualTo(
      letztmöglicheVerfügbarkeit.liefertag.asString())
    expectThat(produkte.first().letztmoeglicheVerfuegbarkeit.bestellschluss).isEqualTo(
      letztmöglicheVerfügbarkeit.bestellschluss.asString())
  }

  @Test
  internal fun `liefert sortierte produkte wenn cms filter produktnummernvervendung selektionsbasis ist und produktnummern ausgewählt sind`() {
    // Given
    val mockkRepository = mockk<ProduktRepository>()
    val applicationService = ProduktlistenApplicationService(mockkRepository, mockk(), mockedFeatureService)
    val produktNummernFilterUndSortierung = listOf("2", "31", "32", "3")

    every { mockkRepository.ladeVerfügbareProdukte(any(), any(), any()) } returns Produkte(
      listOf(erstelleProdukt(nummer = "3", verfügbarkeiten = erstelleVerfügbarkeiten()),
        erstelleProdukt(nummer = "2", verfügbarkeiten = erstelleVerfügbarkeiten()),
        erstelleProdukt(nummer = "1", verfügbarkeiten = erstelleVerfügbarkeiten()),
        erstelleProdukt(nummer = "31", verfügbarkeiten = erstelleVerfügbarkeiten())))

    // That
    val produktDTOS =
      applicationService.gebeMirAlleVerfuegbarenProdukte(
        CmsProdukteFilter(produktNummern = produktNummernFilterUndSortierung.map { Produktnummer(it) },
          produktnummernVerwendung = ProduktnummernVerwendung.SELEKTIONSBASIS), ProdukteSortierung())

    expectThat(produktDTOS[0].nummer).isEqualTo("2")
    expectThat(produktDTOS[1].nummer).isEqualTo("31")
    expectThat(produktDTOS[2].nummer).isEqualTo("3")
  }

  @Test
  internal fun `Sortiertung nach Produktnummern und Limit Filter funktionieren in kombination`() {
    // Given
    val applicationService =
      ProduktlistenApplicationService(produktRepository, verfügbareFilterwerteRepository, mockedFeatureService)
    produktRepository.speicherProdukt(erstelleProdukt(nummer = "1", verfügbarkeiten = erstelleVerfügbarkeiten()))
    produktRepository.speicherProdukt(erstelleProdukt(nummer = "2", verfügbarkeiten = erstelleVerfügbarkeiten()))
    produktRepository.speicherProdukt(erstelleProdukt(nummer = "3", verfügbarkeiten = erstelleVerfügbarkeiten()))
    produktRepository.speicherProdukt(erstelleProdukt(nummer = "4", verfügbarkeiten = erstelleVerfügbarkeiten()))

    // Then
    val fetchedProdukte = applicationService.gebeMirAlleVerfuegbarenProdukte(
      CmsProdukteFilter(
        produktNummern = listOf(Produktnummer("3"), Produktnummer("4"), Produktnummer("1")),
        produktnummernVerwendung = ProduktnummernVerwendung.SELEKTIONSBASIS, limit = 2
      ),
      ProdukteSortierung())

    expectThat(fetchedProdukte[0].nummer).isEqualTo("3")
    expectThat(fetchedProdukte[1].nummer).isEqualTo("4")
  }
}
