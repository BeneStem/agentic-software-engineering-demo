package de.blume2000.finden.adapter.passive.database.produkte

import com.mongodb.client.MongoClient
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.UserProdukteFilter
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.Liefertage
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.PreisBereich
import de.blume2000.finden.testutils.erstelleBlumensorten
import de.blume2000.finden.testutils.erstelleEuroPreis
import de.blume2000.finden.testutils.erstelleKlassifikation
import de.blume2000.finden.testutils.erstelleLiefertag
import de.blume2000.finden.testutils.erstelleProdukt
import de.blume2000.finden.testutils.erstelleProduktfarben
import de.blume2000.finden.testutils.erstelleVerfügbarkeiten
import de.blume2000.util.TestContainers
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEqualTo

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
class VerfügbareFilterwerteMongoRepositoryTest {

  @Inject
  private lateinit var produktRepository: ProduktMongoRepository

  @Inject
  private lateinit var repository: VerfügbareFilterwerteMongoRepository

  @Inject
  private lateinit var mongoClient: MongoClient

  @BeforeEach
  fun cleanup() {
    mongoClient.getDatabase("finden-test").getCollection(MongoProdukt.MONGO_COLLECTION).drop()
  }

  @Test
  internal fun `Es gibt keine verfügbaren Filterwerte wenn es keine Produkte gibt`() {
    val verfügbareFilterwerte =
      repository.ladeVerfügbareFilterWerte(CmsProdukteFilter(), UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterwerte.farben).isEqualTo(emptyList())
    expectThat(verfügbareFilterwerte.klassifikationen).isEqualTo(emptyList())
    expectThat(verfügbareFilterwerte.klassifikationen).isEqualTo(emptyList())
    expectThat(verfügbareFilterwerte.liefertage).isEqualTo(Liefertage(verfügbareLiefertage = emptyList()))
    expectThat(verfügbareFilterwerte.preisBereich).isEqualTo(PreisBereich(minPreis = null, maxPreis = null))
  }

  @Test
  internal fun `Kein initialer Filter liefert keine Einschränkung`() {
    val produktfarben = erstelleProduktfarben("grün", "rot", "bunt")
    val blumensorten = erstelleBlumensorten("Baum", "Schöne Blume", "Dingsbums")
    val preis = erstelleEuroPreis(10.0)
    val klassifikation = erstelleKlassifikation("11", "Strauss")
    val verfügbarkeiten = erstelleVerfügbarkeiten()

    persistiereProdukt(
      farben = produktfarben,
      blumensorten = blumensorten,
      preis = preis,
      klassifikation = klassifikation,
      verfügbarkeiten = verfügbarkeiten
    )

    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(CmsProdukteFilter(), UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterWerte.sorten).containsExactlyInAnyOrder(blumensorten)
    expectThat(verfügbareFilterWerte.farben).containsExactlyInAnyOrder(produktfarben)
    expectThat(verfügbareFilterWerte.preisBereich).isEqualTo(PreisBereich(preis, preis))
    expectThat(verfügbareFilterWerte.klassifikationen).containsExactlyInAnyOrder(listOf(klassifikation))
    expectThat(verfügbareFilterWerte.liefertage).isEqualTo(
      Liefertage(
        verfügbareLiefertage = listOf(
          Liefertag(LocalDate.now().plusDays(1)),
          Liefertag(LocalDate.now().plusDays(2))
        )
      )
    )
  }

  @Test
  internal fun `Kein initialer Filter liefert korrekte Liefertage wenn keine Produkte morgen lieferbar sind`() {
    val produktfarben = erstelleProduktfarben("grün", "rot", "bunt")
    val blumensorten = erstelleBlumensorten("Baum", "Schöne Blume", "Dingsbums")
    val preis = erstelleEuroPreis(10.0)
    val klassifikation = erstelleKlassifikation("11", "Strauss")
    val verfügbarkeiten = erstelleVerfügbarkeiten(morgen = false)

    persistiereProdukt(
      farben = produktfarben,
      blumensorten = blumensorten,
      preis = preis,
      klassifikation = klassifikation,
      verfügbarkeiten = verfügbarkeiten
    )

    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(CmsProdukteFilter(), UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterWerte.sorten).containsExactlyInAnyOrder(blumensorten)
    expectThat(verfügbareFilterWerte.farben).containsExactlyInAnyOrder(produktfarben)
    expectThat(verfügbareFilterWerte.preisBereich).isEqualTo(PreisBereich(preis, preis))
    expectThat(verfügbareFilterWerte.klassifikationen).containsExactlyInAnyOrder(listOf(klassifikation))
    expectThat(verfügbareFilterWerte.liefertage).isEqualTo(
      Liefertage(
        verfügbareLiefertage = listOf(
          Liefertag(
            LocalDate.now().plusDays(2)
          )
        )
      )
    )
  }

  @Test
  internal fun `Kein initialer Filter gibt richtigen Preisbereich zurück`() {
    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(50.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(100.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(CmsProdukteFilter(), UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterWerte.preisBereich).isEqualTo(
      PreisBereich(
        erstelleEuroPreis(10.0),
        erstelleEuroPreis(100.0)
      )
    )
  }

  @Test
  internal fun `Gibt Farben aller Produkte zurück, die dem gegebenem Farb-Filter entsprechen`() {
    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("blau", "rot"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb", "blau"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    val cmsProdukteFilter = CmsProdukteFilter(farben = erstelleProduktfarben("gelb"))
    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(cmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterWerte.farben).containsExactlyInAnyOrder(erstelleProduktfarben("gelb", "blau"))
  }

  @Test
  internal fun `Gibt Sorten aller Produkte zurück, die dem gegebenem Sorten-Filter entsprechen`() {
    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Schöne Blume"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum", "Dingsbums"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    val cmsProdukteFilter = CmsProdukteFilter(blumensorten = erstelleBlumensorten("Baum"))
    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(cmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterWerte.sorten).containsExactlyInAnyOrder(erstelleBlumensorten("Baum", "Dingsbums"))
  }

  @Test
  internal fun `Gibt Preisbereich aller Produkte zurück, die dem gegebenem Preis-Filter entsprechen`() {
    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(50.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(100.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    val cmsProdukteFilter = CmsProdukteFilter(minPreis = erstelleEuroPreis(20.0), maxPreis = erstelleEuroPreis(100.0))
    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(cmsProdukteFilter, UserProdukteFilter.NEUTRALER_FILTER)

    expectThat(verfügbareFilterWerte.preisBereich).isEqualTo(
      PreisBereich(
        erstelleEuroPreis(50.0),
        erstelleEuroPreis(100.0)
      )
    )
  }

  @Test
  internal fun `Userfilter auf Farben wird durch CMS filter auf Farben eingeschränkt`() {
    val cmsFarbFilterFarben = erstelleProduktfarben("gelb")
    val userFarbfilterFarben = erstelleProduktfarben("gelb", "blau")

    persistiereProdukt(
      farben = cmsFarbFilterFarben,
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = userFarbfilterFarben.filter { !cmsFarbFilterFarben.contains(it) },
      blumensorten = erstelleBlumensorten("Schöne Blume"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    val cmsProdukteFilter = CmsProdukteFilter(farben = cmsFarbFilterFarben)
    val userProdukteFilter = createUserProdukteFilter(farben = userFarbfilterFarben)

    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(cmsProdukteFilter, userProdukteFilter)

    expectThat(verfügbareFilterWerte.farben).containsExactlyInAnyOrder(cmsFarbFilterFarben)
  }

  @Test
  internal fun `Filterwerte von abgelaufenen Produkten werden nicht zurückgegeben`() {
    persistiereProdukt(
      farben = erstelleProduktfarben("gelb"),
      blumensorten = erstelleBlumensorten("Baum"),
      preis = erstelleEuroPreis(10.0),
      klassifikation = erstelleKlassifikation("11", "Strauss"),
      verfügbarkeiten = erstelleVerfügbarkeiten()
    )

    persistiereProdukt(
      farben = erstelleProduktfarben("blau"),
      blumensorten = erstelleBlumensorten("Palme"),
      preis = erstelleEuroPreis(8.0),
      klassifikation = erstelleKlassifikation("10", "Pflanzen"),
      verfügbarkeiten = erstelleVerfügbarkeiten(morgen = true, bestellbarMorgen = false, übermorgen = false, bestellbarÜbermorgen = false)
    )

    val verfügbareFilterWerte =
      repository.ladeVerfügbareFilterWerte(CmsProdukteFilter(), createUserProdukteFilter())

    expectThat(verfügbareFilterWerte.farben).containsExactlyInAnyOrder(erstelleProduktfarben("gelb"))
    expectThat(verfügbareFilterWerte.sorten).containsExactlyInAnyOrder(erstelleBlumensorten("Baum"))
    expectThat(verfügbareFilterWerte.preisBereich.minPreis).isEqualTo(erstelleEuroPreis(10.0))
    expectThat(verfügbareFilterWerte.klassifikationen).isEqualTo(listOf(erstelleKlassifikation("11", "Strauss")))
    expectThat(verfügbareFilterWerte.liefertage.verfügbareLiefertage).isEqualTo(listOf(erstelleLiefertag(1), erstelleLiefertag(2)))
  }

  private fun createUserProdukteFilter(
    klassifikationen: List<Klassifikation> = emptyList(),
    preisBereich: PreisBereich? = null,
    farben: List<Produktfarbe> = emptyList(),
    blumensorten: List<Blumensorte> = emptyList(),
    liefertage: Liefertage = Liefertage(verfügbareLiefertage = emptyList()),
  ) = UserProdukteFilter(
    farben = farben,
    blumensorten = blumensorten,
    liefertage = liefertage,
    klassifikationen = klassifikationen,
    preisBereich = preisBereich
  )

  private fun persistiereProdukt(
    farben: List<Produktfarbe>,
    blumensorten: List<Blumensorte>,
    preis: Preis,
    klassifikation: Klassifikation,
    verfügbarkeiten: List<ProduktVerfügbarkeit>
  ): Produkt {
    val produkt = erstelleProdukt(
      nummer = nextInt(10000000, 99999999).toString(),
      farben = farben,
      blumensorten = blumensorten,
      preis = preis,
      streichpreis = preis,
      klassifikation = klassifikation,
      verfügbarkeiten = verfügbarkeiten
    )
    produktRepository.speicherProdukt(produkt)
    return produkt
  }
}
