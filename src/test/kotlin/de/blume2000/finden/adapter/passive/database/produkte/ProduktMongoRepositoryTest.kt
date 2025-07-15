package de.blume2000.finden.adapter.passive.database.produkte

import com.mongodb.client.MongoClient
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProdukteSortierFeld
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import de.blume2000.finden.domain.model.produkte.Richtung
import de.blume2000.finden.domain.model.produkte.Sortierung
import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.testutils.erstelleProdukt
import de.blume2000.finden.testutils.erstelleVerfügbarkeiten
import de.blume2000.util.TestContainers
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsSequence
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale
import jakarta.inject.Inject

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
class ProduktMongoRepositoryTest {

  @Inject
  private lateinit var repository: ProduktMongoRepository

  @Inject
  private lateinit var mongoClient: MongoClient

  @BeforeEach
  @AfterEach
  fun cleanup() {
    mongoClient.getDatabase("finden-test").getCollection(MongoProdukt.MONGO_COLLECTION).drop()
  }

  @Test
  internal fun `Ein persistiertes Produkt kann anschließend wieder geladen werden`() {
    val produkt = erstelleProdukt(verfügbarkeiten = erstelleVerfügbarkeiten())
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    repository.speicherProdukt(produkt)

    expectThat(repository.ladeVerfügbareProdukte(cmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER,
      leererSortierung).gebeMirAlleProdukte().size).isEqualTo(1)
  }

  @Test
  internal fun `Ein bereits gespeichertes Produkt wird überschrieben`() {
    // Given
    val produkt = erstelleProdukt(verfügbarkeiten = erstelleVerfügbarkeiten())
    repository.speicherProdukt(produkt)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    // When
    repository.speicherProdukt(produkt)

    // Then
    expectThat(repository.ladeVerfügbareProdukte(cmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER,
      leererSortierung).gebeMirAlleProdukte().size).isEqualTo(1)
  }

  @Test
  internal fun `Liefert die korrekte Produktanzahl`() {
    // Given
    repository.speicherProdukt(erstelleProdukt())

    // When
    val anzahlProdukte = repository.zähleProdukte()

    // Then
    expectThat(anzahlProdukte).isEqualTo(1L)
  }

  @Test
  internal fun `Ein bereits gespeichertes Produkt wird entfernt`() {
    // Given
    val produkt = erstelleProdukt()
    repository.speicherProdukt(produkt)
    val cmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val leererSortierung = ProdukteSortierung()

    // When
    repository.entferneProdukt(produkt.nummer)

    // Then
    expectThat(repository.ladeVerfügbareProdukte(cmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER,
      leererSortierung).gebeMirAlleProdukte().size).isEqualTo(0)
  }

  @Test
  internal fun `Preisfilter funktioniert`() {
    val untergrenze = Preis(15.toBigDecimal())
    val obergrenze = Preis(30.toBigDecimal())

    val zuGuenstigesProdukt = erstelleProdukt(
      nummer = "1",
      preis = Preis(BigDecimal.valueOf(10), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY)),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(zuGuenstigesProdukt)
    val perfektesProdukt = erstelleProdukt(
      nummer = "2",
      preis = Preis(BigDecimal.valueOf(25), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY)),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(perfektesProdukt)
    val zuTeuresProdukt = erstelleProdukt(
      nummer = "3",
      preis = Preis(BigDecimal.valueOf(9000), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY)),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(zuTeuresProdukt)

    val oberCmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, obergrenze, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val unterCmsProdukteFilter =
      CmsProdukteFilter(emptyList(), untergrenze, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val zwischenCmsProdukteFilter =
      CmsProdukteFilter(emptyList(), untergrenze, obergrenze, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS,
        null)
    val leererSortierung = ProdukteSortierung()

    assertThat(
      repository.ladeVerfügbareProdukte(oberCmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte(),
      containsInAnyOrder(zuGuenstigesProdukt, perfektesProdukt)
    )
    assertThat(
      repository.ladeVerfügbareProdukte(unterCmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte(),
      containsInAnyOrder(perfektesProdukt, zuTeuresProdukt)
    )
    assertThat(repository.ladeVerfügbareProdukte(zwischenCmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER,
      leererSortierung).gebeMirAlleProdukte(), containsInAnyOrder(perfektesProdukt))
    // assertNotNull(repository.ladeProdukte(quatschProdukteFilter))
  }

  @Test
  @Suppress("LongMethod")
  internal fun `Produktnummer filter funktioniert`() {
    val apfelProdukt = erstelleProdukt(nummer = "apfel", verfügbarkeiten = erstelleVerfügbarkeiten())
    repository.speicherProdukt(apfelProdukt)
    val birneProdukt = erstelleProdukt(nummer = "birne", verfügbarkeiten = erstelleVerfügbarkeiten())
    repository.speicherProdukt(birneProdukt)
    val kirscheProdukt = erstelleProdukt(nummer = "kirsche", verfügbarkeiten = erstelleVerfügbarkeiten())
    repository.speicherProdukt(kirscheProdukt)
    val leererSortierung = ProdukteSortierung()

    val keinIDCmsProdukteFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val nurKirsche = CmsProdukteFilter(
      emptyList(),
      null,
      null,
      listOf(kirscheProdukt.nummer),
      ProduktnummernVerwendung.SELEKTIONSBASIS,
      null
    )
    val keineBirne = CmsProdukteFilter(
      emptyList(),
      null,
      null,
      listOf(birneProdukt.nummer),
      ProduktnummernVerwendung.AUSSCHLUSSLISTE,
      null
    )
    val nurQuatsch = CmsProdukteFilter(
      emptyList(),
      null,
      null,
      listOf(Produktnummer("Quitsch"), Produktnummer("quatsch"), Produktnummer("qutsch")),
      ProduktnummernVerwendung.SELEKTIONSBASIS,
      null
    )
    val keinQuatsch = CmsProdukteFilter(
      emptyList(),
      null,
      null,
      listOf(Produktnummer("Quitsch"), Produktnummer("quatsch"), Produktnummer("qutsch")),
      ProduktnummernVerwendung.AUSSCHLUSSLISTE,
      null
    )

    assertThat(
      repository.ladeVerfügbareProdukte(keinIDCmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER,
        leererSortierung).gebeMirAlleProdukte(),
      containsInAnyOrder(apfelProdukt, birneProdukt, kirscheProdukt)
    )
    assertThat(repository.ladeVerfügbareProdukte(nurKirsche, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
      .gebeMirAlleProdukte(),
      containsInAnyOrder(kirscheProdukt))
    assertThat(repository.ladeVerfügbareProdukte(keineBirne, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
      .gebeMirAlleProdukte(),
      containsInAnyOrder(kirscheProdukt, apfelProdukt))
    assertTrue(
      repository.ladeVerfügbareProdukte(nurQuatsch, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte().isEmpty())
    assertTrue(
      repository.ladeVerfügbareProdukte(keinQuatsch, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte().size == 3)
  }

  @Test
  internal fun `Produkte werden limitiert wenn ein Limit angegeben ist`() {
    // Given
    val erstesProdukt = erstelleProdukt(nummer = "Produkt eins", verfügbarkeiten = erstelleVerfügbarkeiten())
    repository.speicherProdukt(erstesProdukt)
    val zweitesProdukt = erstelleProdukt(
      nummer = "Produkt Zwei",
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(zweitesProdukt)
    val limitFilter =
      CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, 1)
    val leererSortierung = ProdukteSortierung()

    // When
    // Then
    expectThat(
      repository.ladeVerfügbareProdukte(limitFilter, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte()).hasSize(1)
  }

  @Test
  internal fun `Produkte werden sortiert`() {
    // Given
    val erstesProdukt = erstelleProdukt(
      nummer = "Produkt kleiner",
      preis = Preis(BigDecimal(18), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal(19), Currency.getInstance(Locale.GERMANY)),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(erstesProdukt)
    val zweitesProdukt = erstelleProdukt(
      nummer = "Produkt groesser",
      preis = Preis(BigDecimal(28), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal(29), Currency.getInstance(Locale.GERMANY)),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(zweitesProdukt)
    val filter = CmsProdukteFilter(emptyList(), null, null, emptyList(), ProduktnummernVerwendung.SELEKTIONSBASIS, null)
    val sortierungNachPreisAufsteigend = ProdukteSortierung(listOf(Sortierung(ProdukteSortierFeld.PREIS, Richtung.ASC)))
    val sortierungNachPreisAbsteigend = ProdukteSortierung(listOf(Sortierung(ProdukteSortierFeld.PREIS, Richtung.DESC)))

    // Then
    expectThat(repository.ladeVerfügbareProdukte(filter, UserProdukteFilter.NEUTRALER_FILTER,
      sortierungNachPreisAufsteigend).gebeMirAlleProdukte()).containsSequence(
      listOf(
        erstesProdukt,
        zweitesProdukt
      )
    )
    expectThat(repository.ladeVerfügbareProdukte(filter, UserProdukteFilter.NEUTRALER_FILTER,
      sortierungNachPreisAbsteigend).gebeMirAlleProdukte()).containsSequence(
      listOf(
        zweitesProdukt,
        erstesProdukt
      )
    )
  }

  @Test
  internal fun `Produkte werden nach Farben gefiltert`() {
    // Given
    val leererSortierung = ProdukteSortierung()
    val blauesProdukt1 = erstelleProdukt(
      nummer = "blauesProdukt1",
      farben = listOf(Produktfarbe("blau"), Produktfarbe("grün")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(blauesProdukt1)

    val blauesProdukt2 = erstelleProdukt(
      nummer = "blauesProdukt2",
      farben = listOf(Produktfarbe("blau")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(blauesProdukt2)

    val rotesProdukt = erstelleProdukt(
      nummer = "rotesProdukt",
      farben = listOf(Produktfarbe("rot"), Produktfarbe("grün")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(rotesProdukt)

    // When
    val filterBlau = CmsProdukteFilter(
      emptyList(),
      null,
      null,
      emptyList(),
      ProduktnummernVerwendung.SELEKTIONSBASIS,
      null,
      farben = listOf(Produktfarbe("blau"))
    )

    // Then
    assertThat(
      repository.ladeVerfügbareProdukte(filterBlau, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte(),
      containsInAnyOrder(blauesProdukt1, blauesProdukt2)
    )
  }

  @Test
  internal fun `Produkte werden nach Blumenart gefiltert`() {
    // Given
    val leererSortierung = ProdukteSortierung()
    val alpenstrauß = erstelleProdukt(
      nummer = "alpenstrauß",
      blumensorten = listOf(Blumensorte("Alpenveilchen"), Blumensorte("Tränendes Herz"), Blumensorte("Kriechgünsel")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(alpenstrauß)

    val nurKriechgünsel = erstelleProdukt(
      nummer = "nur Kriechgünsel",
      blumensorten = listOf(Blumensorte("Kriechgünsel")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(nurKriechgünsel)

    val dieJungfer = erstelleProdukt(
      nummer = "Die Jungfer",
      blumensorten = listOf(Blumensorte("Jungfer im Grünen"), Blumensorte("Loewenmaeulchen")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(dieJungfer)

    // When
    val kriechgünselfilter = CmsProdukteFilter(
      emptyList(),
      null,
      null,
      emptyList(),
      ProduktnummernVerwendung.SELEKTIONSBASIS,
      null,
      blumensorten = listOf(Blumensorte("Kriechgünsel"))
    )

    // Then
    assertThat(
      repository.ladeVerfügbareProdukte(kriechgünselfilter, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung)
        .gebeMirAlleProdukte(),
      containsInAnyOrder(alpenstrauß, nurKriechgünsel)
    )
  }

  @Test
  internal fun `Produkte werden nach Klassifikation gefiltert`() {
    // Given
    val leererSortierung = ProdukteSortierung()
    val straussProdukt1 = erstelleProdukt(
      nummer = "StraussProdukt1",
      klassifikation = Klassifikation(KlassifikationId("11"), KlassifikationName("Strauss")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(straussProdukt1)

    val straussProdukt2 = erstelleProdukt(
      nummer = "StraussProdukt2",
      klassifikation = Klassifikation(KlassifikationId("11"), KlassifikationName("Strauss")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(straussProdukt2)

    val marktwareProdukt = erstelleProdukt(
      nummer = "MarktwareProdukt",
      klassifikation = Klassifikation(KlassifikationId("12"), KlassifikationName("Marktware")),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )
    repository.speicherProdukt(marktwareProdukt)

    // When
    val filterStrauss = CmsProdukteFilter(
      klassifikationen = listOf(KlassifikationId("11")),
      null,
      null,
      emptyList(),
      ProduktnummernVerwendung.KEINE,
      null,
    )

    // Then
    assertThat(
      repository.ladeVerfügbareProdukte(filterStrauss, UserProdukteFilter.NEUTRALER_FILTER, leererSortierung).gebeMirAlleProdukte(),
      containsInAnyOrder(straussProdukt1, straussProdukt2)
    )
  }
}
