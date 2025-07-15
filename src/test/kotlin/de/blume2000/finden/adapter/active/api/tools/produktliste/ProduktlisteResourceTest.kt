package de.blume2000.finden.adapter.active.api.tools.produktliste

import com.mongodb.client.MongoClient
import de.blume2000.finden.adapter.passive.database.produkte.MongoProdukt
import de.blume2000.finden.adapter.passive.database.produkte.ProduktMongoRepository
import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.ProduktbildUrl
import de.blume2000.finden.domain.model.produkte.produkt.ProduktdetailseiteUrlSeoName
import de.blume2000.finden.domain.model.produkte.produkt.Produktname
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.testutils.erstelleProduktVerfügbarkeit
import de.blume2000.util.DateTimeUtil
import de.blume2000.util.TestContainers
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.core.IsEqual.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency
import java.util.Locale
import jakarta.inject.Inject

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProduktlisteResourceTest {

  @Inject
  private lateinit var produktRepository: ProduktMongoRepository

  @Inject
  private lateinit var mongoClient: MongoClient

  @BeforeEach
  @AfterEach
  fun cleanup() {
    mongoClient.getDatabase("finden-test").getCollection(MongoProdukt.MONGO_COLLECTION).drop()
  }

  @Test
  internal fun `Liefert eine leere Liste, wenn kein RequestBody übergeben werden`() {
    given()
      .`when`()
      .body(
        """
            {}
          """.trimIndent()
      )
      .contentType(ContentType.JSON)
      .post("/api/finden/tool/produktliste")
      .then()
      .statusCode(400)
  }

  @Test
  internal fun `Liefert eine leere Liste, wenn eine leere Liste übergeben werden`() {
    given()
      .`when`()
      .body("[]")
      .contentType(ContentType.JSON)
      .post("/api/finden/tool/produktliste")
      .then()
      .statusCode(200)
      .body("", equalTo(listOf<String>()))
  }

  @Test
  internal fun `Liefert eine Liste der gefilterten Produkte`() {
    prepareDatabase(produktRepository)

    given()
      .`when`()
      .body("[\"123\",\"456\"]")
      .contentType(ContentType.JSON)
      .post("/api/finden/tool/produktliste")
      .then()
      .statusCode(200)
      .body("", equalTo(listOf("123")))
  }

  @Test
  internal fun `Liefert eine Liste der gefilterten Produkte, auch bei einem trailing Komma`() {
    prepareDatabase(produktRepository)

    given()
      .`when`()
      .body("[\"123\",\"456\",]")
      .contentType(ContentType.JSON)
      .post("/api/finden/tool/produktliste")
      .then()
      .statusCode(400)
  }

  private fun prepareDatabase(repository: ProduktMongoRepository) {
    val produktnummer = "123"
    val nächstmöglicheVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(2)),
      bestellschluss = Bestellschluss(
        DateTimeUtil.erstelleUTCNow().plusDays(1)
      )
    )
    val letztmöglicheVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(2)),
      bestellschluss = Bestellschluss(
        DateTimeUtil.erstelleUTCNow().plusDays(1)
      )
    )

    val verfügbaresProdukt = Produkt(
      Produktnummer(produktnummer),
      Produktname("Produkt123"),
      Preis(
        BigDecimal.TEN, Currency.getInstance(
          Locale.GERMANY
        )
      ),
      Preis(
        BigDecimal.TEN, Currency.getInstance(
          Locale.GERMANY
        )
      ),
      Klassifikation(KlassifikationId("11"), KlassifikationName("Strauss")), ProduktbildUrl("https://blumenbild.de"),
      ProduktdetailseiteUrlSeoName("ding"), listOf(), listOf(),
      verfügbarkeiten = listOf(nächstmöglicheVerfügbarkeit, letztmöglicheVerfügbarkeit)
    )

    repository.speicherProdukt(verfügbaresProdukt)
  }
}
