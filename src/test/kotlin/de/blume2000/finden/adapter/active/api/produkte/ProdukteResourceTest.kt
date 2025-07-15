package de.blume2000.finden.adapter.active.api.produkte

import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.client.MongoClient
import de.blume2000.finden.adapter.active.api.produkte.dtos.CmsProdukteFilterDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.FarbeDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.KlassifikationDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.LiefertageDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.PreisDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.PreisbereichDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.ProduktDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.ProdukteFilterDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.ProduktnummerDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.SorteDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.UserProdukteFilterDTO
import de.blume2000.finden.adapter.passive.database.produkte.MongoProdukt
import de.blume2000.finden.adapter.passive.database.produkte.ProduktMongoRepository
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.testutils.erstelleLeereFarben
import de.blume2000.finden.testutils.erstelleLeereKlassifikationen
import de.blume2000.finden.testutils.erstelleLeereLiefertage
import de.blume2000.finden.testutils.erstelleLeereSorten
import de.blume2000.finden.testutils.erstelleProdukt
import de.blume2000.finden.testutils.erstelleProduktVerfügbarkeit
import de.blume2000.finden.testutils.erstelleProdukteResourceParameter
import de.blume2000.finden.testutils.erstelleValideProdukteResourceParameter
import de.blume2000.finden.testutils.erstelleValidenMaxPreis
import de.blume2000.util.DateTimeUtil
import de.blume2000.util.TestContainers
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency
import java.util.Locale
import java.util.stream.Stream
import jakarta.inject.Inject
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.Matchers.comparesEqualTo
import org.hamcrest.Matchers.empty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Suppress("LargeClass")
internal class ProdukteResourceTest {

  @Inject
  private lateinit var repository: ProduktMongoRepository

  @Inject
  private lateinit var mongoClient: MongoClient

  @BeforeEach
  fun cleanup() {
    mongoClient.getDatabase("finden-test").getCollection(MongoProdukt.MONGO_COLLECTION).drop()
  }

  @Test
  internal fun `POST Endpoint ohne Userfilter sollte ein persistiertes ProduktDto zurückgeben`() {
    // Given
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
    repository.speicherProdukt(produkt)

    val requestBody = ObjectMapper().writeValueAsString(
      CmsProdukteFilterDTO()
    )

    // Then
    given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte")
      .then()
      .statusCode(200)
      .body("nummer", hasItem(produkt.nummer.asString()))
      .body("name", hasItem(produkt.name.asString()))
      .body("preis", hasItem(produkt.preis.asString()))
      .body("streichpreis", hasItem(produkt.streichpreis.asString()))
      .body("bildUrl", hasItem(produkt.bildUrl.asString()))
      .body("produktUrl", hasItem("/p/zurpds"))
      .body(
        "naechstmoeglicheVerfuegbarkeit.bestellschluss",
        hasItem(nächstmöglicheVerfügbarkeit.bestellschluss.asString())
      )
      .body(
        "naechstmoeglicheVerfuegbarkeit.liefertag",
        hasItem(nächstmöglicheVerfügbarkeit.liefertag.value.toString())
      )
      .body(
        "letztmoeglicheVerfuegbarkeit.bestellschluss",
        hasItem(letztmöglicheVerfügbarkeit.bestellschluss.asString())
      ).body("letztmoeglicheVerfuegbarkeit.liefertag", hasItem(letztmöglicheVerfügbarkeit.liefertag.value.toString()))
  }

  @Test
  internal fun `POST Endpoint ohne Userfilter  liefert eine leere Liste, wenn keine Produkte vorhanden sind`() {
    // Given
    val requestBody = ObjectMapper().writeValueAsString(
      CmsProdukteFilterDTO()
    )
    // Then
    given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte").then().statusCode(200).body("isEmpty()", `is`(true))
  }

  @Test
  internal fun `POST Endpoint ohne Userfilter liefert eine leere Liste, wenn die Produkte per nummer ausgeblendet sind`() {
    // GIVEN
    val produkt = erstelleProdukt(nummer = "12345")
    repository.speicherProdukt(produkt)

    val requestBody = ObjectMapper().writeValueAsString(
      CmsProdukteFilterDTO(
        produktNummern = emptyList(),
        produktnummernVerwendung = ProduktnummernVerwendung.SELEKTIONSBASIS.name.lowercase()
      )
    )

    // Then
    given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte")
      .then()
      .statusCode(200).body("isEmpty()", `is`(true))
  }

  @Test
  internal fun `POST Endpoint ohne Userfilter Endpoint liefert nur Produkte der selektionsbasis`() {
    // Given
    val produktSelektion = erstelleVerfügbaresProdukt(produktnummer = "12345")
    val produktNichtSelektion = erstelleVerfügbaresProdukt(produktnummer = "12346")

    repository.speicherProdukt(produktSelektion)
    repository.speicherProdukt(produktNichtSelektion)

    val requestBody = ObjectMapper().writeValueAsString(
      CmsProdukteFilterDTO(
        produktNummern = listOf(ProduktnummerDTO(produktSelektion.nummer.asString())),
        produktnummernVerwendung = "selektionsbasis"
      )
    )

    // Then
    given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte").then()
      .statusCode(200).body("nummer", hasItem(produktSelektion.nummer.asString()))
      .body("nummer", not(hasItem(produktNichtSelektion.nummer.asString())))
  }

  @Test
  internal fun `POST Endpoint ohne Userfilter sollte ein Liste von Produkten in der definierten Preisspanne zurückgeben`() {
    // Given

    val zuGuenstigesProdukt = erstelleVerfügbaresProdukt(
      produktnummer = "1",
      preis = Preis(BigDecimal.valueOf(10), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY))
    )
    repository.speicherProdukt(zuGuenstigesProdukt)
    val perfektesProdukt = erstelleVerfügbaresProdukt(
      produktnummer = "2",
      preis = Preis(BigDecimal.valueOf(25), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY))
    )
    repository.speicherProdukt(perfektesProdukt)
    val zuTeuresProdukt = erstelleVerfügbaresProdukt(
      produktnummer = "3",
      preis = Preis(BigDecimal.valueOf(9000), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY))
    )
    repository.speicherProdukt(zuTeuresProdukt)

    val requestBody = ObjectMapper().writeValueAsString(
      CmsProdukteFilterDTO(
        preisbereich = PreisbereichDTO(
          minPreis = PreisDTO(BigDecimal(15.0), "EUR"),
          maxPreis = PreisDTO(BigDecimal(30.0), "EUR")
        )
      )
    )

    // Then
    given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte").then().statusCode(200)
      .body("nummer", hasItem(perfektesProdukt.nummer.asString()))
      .body("nummer", not(hasItem(zuGuenstigesProdukt.nummer.asString())))
      .body("nummer", not(hasItem(zuTeuresProdukt.nummer.asString())))
  }

  @Test
  internal fun `POST Endpoint ohne Userfilter sollte ein Liste ohne Produkte zurückgeben, wenn der minPreis höher als der maxPreis ist`() {
    // Given
    val perfektesProdukt = erstelleProdukt(
      nummer = "2",
      preis = Preis(BigDecimal.valueOf(25), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY))
    )
    repository.speicherProdukt(perfektesProdukt)

    val requestBody = ObjectMapper().writeValueAsString(
      CmsProdukteFilterDTO(
        preisbereich = PreisbereichDTO(
          minPreis = PreisDTO(BigDecimal(9000), "EUR"),
          maxPreis = PreisDTO(BigDecimal(30.0), "EUR")
        )
      )
    )

    // Then
    given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte").then().statusCode(200)
      .body("nummer", not(hasItem(perfektesProdukt.nummer.asString())))
  }

  @Test
  internal fun `Bekomme alle verfügbare Filterwerte ohne Einschränkung`() {
    // Given
    val produkt = erstelleProdukt(
      nummer = "2",
      preis = Preis(BigDecimal.valueOf(24.99), Currency.getInstance(Locale.GERMANY)),
      streichpreis = Preis(BigDecimal.valueOf(10000), Currency.getInstance(Locale.GERMANY)),
      farben = listOf(Produktfarbe("blau"), Produktfarbe("gruen")),
      blumensorten = listOf(Blumensorte("Nelke"), Blumensorte("Rose")),
      verfügbarkeiten = erstelleVerfügbarkeitenFürProdukt()
    )
    repository.speicherProdukt(produkt)

    val requestBody =
      ObjectMapper().writeValueAsString(
        ProdukteFilterDTO(
          cmsProdukteFilter = CmsProdukteFilterDTO(),
          userProdukteFilter = UserProdukteFilterDTO(
            sorten = emptyList(),
            farben = emptyList(),
            preisbereich = null,
            klassifikationen = emptyList(),
            liefertage = LiefertageDTO(
              verfuegbareLiefertage = emptyList()
            )
          )
        )
      )

    // Then
    val response = given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte/mituserfilterung")

    response.then().statusCode(200)
      .body(
        "verfuegbareFilterwerte.sorten", hasItems(
          mapOf("name" to produkt.blumensorten[0].asString()),
          mapOf("name" to produkt.blumensorten[1].asString())
        )
      )
      .body(
        "verfuegbareFilterwerte.farben",
        hasItems(mapOf("name" to produkt.farben[0].asString()), mapOf("name" to produkt.farben[1].asString()))
      )
      .body(
        "verfuegbareFilterwerte.preisbereich.minPreis.bruttoBetrag",
        comparesEqualTo(produkt.preis.bruttoBetrag.toFloat())
      )
      .body("verfuegbareFilterwerte.preisbereich.minPreis.waehrung", comparesEqualTo("EUR"))
      .body(
        "verfuegbareFilterwerte.preisbereich.maxPreis.bruttoBetrag",
        comparesEqualTo(produkt.preis.bruttoBetrag.toFloat())
      )
      .body("verfuegbareFilterwerte.preisbereich.maxPreis.waehrung", comparesEqualTo("EUR"))
      .body("verfuegbareFilterwerte.klassifikationen", `is`(emptyList<KlassifikationDTO>()))
      .body("verfuegbareFilterwerte.liefertage.verfuegbareLiefertage", hasItem(LocalDate.now().plusDays(2).toString()))
  }

  @Test
  internal fun `Bestellschluss für Morgen ist abgelaufen, erst nach morgen Lieferbar, führt zu Produkt wird nicht für Morgen lieferbar ausgegeben`() {
    // Given
    val abgelaufeneMorgenLieferbar = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(1)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().minusHours(2))
    )
    val inZukunftLieferbar = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(5)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
    )

    val produkt = erstelleProdukt(
      verfügbarkeiten = listOf(abgelaufeneMorgenLieferbar, inZukunftLieferbar)
    )
    repository.speicherProdukt(produkt)

    val requestBody =
      ObjectMapper().writeValueAsString(
        ProdukteFilterDTO(
          cmsProdukteFilter = CmsProdukteFilterDTO(),
          userProdukteFilter = UserProdukteFilterDTO(
            sorten = emptyList(),
            farben = emptyList(),
            preisbereich = null,
            klassifikationen = emptyList(),
            liefertage = LiefertageDTO(
              verfuegbareLiefertage = listOf(
                LocalDate.now().plusDays(1).toString()
              )
            )
          )
        )
      )

    // Then
    val response = given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte/mituserfilterung")

    response.then().statusCode(200)
      .body("produkte", `is`(empty<ProduktDTO>()))
  }

  @Test
  internal fun `Produkte mit Liefertag in Zukunft, Bestellschluss abgelaufen, darf keine verfügbaren Filterwerte zurückgeben`() {
    // Given
    val inZukunftLieferbarBestellschlussAbgelaufen = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(5)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().minusHours(1))
    )

    val preis1 = Preis(BigDecimal.valueOf(4.99), Currency.getInstance(Locale.GERMANY))
    val produkt1 = erstelleProdukt(
      nummer = "00000001",
      preis = preis1,
      streichpreis = preis1,
      farben = listOf(Produktfarbe("blau"), Produktfarbe("gruen")),
      blumensorten = listOf(Blumensorte("Nelke"), Blumensorte("Rose")),
      klassifikation = Klassifikation(KlassifikationId("11"), KlassifikationName("Strauss")),
      verfügbarkeiten = listOf(inZukunftLieferbarBestellschlussAbgelaufen)
    )
    repository.speicherProdukt(produkt1)
    val preis2 = Preis(BigDecimal.valueOf(24.99), Currency.getInstance(Locale.GERMANY))
    val produkt2 = erstelleProdukt(
      nummer = "00000002",
      preis = preis2,
      streichpreis = preis2,
      farben = listOf(Produktfarbe("gelb")),
      blumensorten = listOf(Blumensorte("Lilien")),
      klassifikation = Klassifikation(KlassifikationId("13"), KlassifikationName("Topfpflanze")),
      verfügbarkeiten = listOf(inZukunftLieferbarBestellschlussAbgelaufen)
    )
    repository.speicherProdukt(produkt2)

    val requestBody =
      ObjectMapper().writeValueAsString(
        ProdukteFilterDTO(
          cmsProdukteFilter = CmsProdukteFilterDTO(),
          userProdukteFilter = UserProdukteFilterDTO(
            sorten = emptyList(),
            farben = emptyList(),
            preisbereich = null,
            klassifikationen = emptyList(),
            liefertage = LiefertageDTO(
              verfuegbareLiefertage = emptyList()
            )
          )
        )
      )

    // Then
    val response = given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte/mituserfilterung")

    response.then().statusCode(200)
      .body("produkte", `is`(empty<ProduktDTO>()))
      .body("verfuegbareFilterwerte.sorten", `is`(empty<SorteDTO>()))
      .body("verfuegbareFilterwerte.farben", `is`(empty<FarbeDTO>()))
      .body("verfuegbareFilterwerte.preisbereich.minPreis", nullValue())
      .body("verfuegbareFilterwerte.preisbereich.maxPreis", nullValue())
      .body("verfuegbareFilterwerte.klassifikationen", `is`(empty<KlassifikationDTO>()))
      .body("verfuegbareFilterwerte.liefertage.verfuegbareLiefertage", `is`(empty<String>()))
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit validen Parametern gibt den Status 200 zurück`() {
    produkteResourceMitUserfilterungErwartetResponseCode(erstelleValideProdukteResourceParameter(), 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit minimal Anforderungen gibt den Status 200 zurück`() {
    // Given
    val requestBody = """
      {
        "cmsProdukteFilter": {},
        "userProdukteFilter": {
          ${erstelleLeereSorten()},
          ${erstelleLeereFarben()},
          ${erstelleLeereKlassifikationen()},
          ${erstelleLeereLiefertage()}
        }
      }
    """.trimIndent()

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit leerem Body gibt den Status 400 zurück`() {
    // Given
    val requestBody = "{}"

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit validen Klassifikationen gibt den Status 200 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsKlassifikationen = """
      "klassifikationen": [
        {"name": "Zubehör NonFlower", "id": "1"},
        {"name": "Deko/Home", "id": "233"},
        {"name": "Strauß", "id": "2"},
        {"name": "Wert1", "id": "5"}
      ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit invaliden Klassifikationen gibt den Status 400 zurück`() {
    // Given
    val requestBody1 = erstelleProdukteResourceParameter(
      cmsKlassifikationen = """
      "klassifikationen": [
        {"name": "", "id": "1"}
      ]
    """.trimIndent()
    )

    val requestBody2 = erstelleProdukteResourceParameter(
      cmsKlassifikationen = """
      "klassifikationen": [
        {"name": "Test", "id": ""}
      ]
    """.trimIndent()
    )

    val requestBody3 = erstelleProdukteResourceParameter(
      cmsKlassifikationen = """
      "klassifikationen": [
        {"name": "Test", "id": "A"}
      ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody1, 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody2, 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody3, 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einem Minus-Preis gibt den Status 400 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsPreisbereich = """
      "preisbereich": {
         "minPreis": {
          "bruttoBetrag": -1,
          "waehrung": "EUR"
        },
        ${erstelleValidenMaxPreis()}
      }
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einem invaliden Preis gibt den Status 400 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsPreisbereich = """
      "preisbereich": {
         "minPreis": {
          "bruttoBetrag": "",
          "waehrung": "EUR"
        },
        ${erstelleValidenMaxPreis()}
      }
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einer invaliden Währung gibt den Status 400 zurück`() {
    // Given
    val währung1 = ""
    val währung2 = "eur"
    val währung3 = "EURO"
    val währung4 = "€"
    val requestBody = erstelleProdukteResourceParameter(
      cmsPreisbereich = """
      "preisbereich": {
         "minPreis": {
          "bruttoBetrag": "1",
          "waehrung": "<waehrung>"
        },
        ${erstelleValidenMaxPreis()}
      }
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<waehrung>", währung1), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<waehrung>", währung2), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<waehrung>", währung3), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<waehrung>", währung4), 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einer validen Produktnummer gibt den Status 200 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsProduktnummern = """
       "produktNummern": [
          {"nummer": "1"},
          {"nummer": "1000"},
          {"nummer": "20003075"}
        ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einer invaliden Produktnummer gibt den Status 400 zurück`() {
    // Given
    val nummer1 = ""
    val nummer2 = "2000a"
    val nummer3 = "200-100"
    val requestBody = erstelleProdukteResourceParameter(
      cmsProduktnummern = """
       "produktNummern": [
         {"nummer": "<nummer>"}
       ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<nummer>", nummer1), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<nummer>", nummer2), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<nummer>", nummer3), 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einem validen Limit gibt den Status 200 zurück`() {
    // Given
    val limit1 = ""
    val limit2 = "10"
    val requestBody = erstelleProdukteResourceParameter(
      cmsLimit = """
       "limit": "<limit>"
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<limit>", limit1), 200)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<limit>", limit2), 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einem invaliden Limit gibt den Status 400 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsLimit = """
       "limit": "a"
    """.trimIndent()
    )
    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einer validen Farbe gibt den Status 200 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsFarben = """
       "farben": [
          {"name": "weiß"},
          {"name": "blau"},
          {"name": "grün"}
        ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einer invaliden Farbe gibt den Status 400 zurück`() {
    // Given
    val farbe1 = "\"\""
    val farbe2 = "weiss-blau"
    val farbe3 = "<blau>"
    val farbe4 = "Gelb"
    val requestBody = erstelleProdukteResourceParameter(
      cmsFarben = """
       "farben": [
          {"name": "<farbe>"}
        ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<farbe>", farbe1), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<farbe>", farbe2), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<farbe>", farbe3), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<farbe>", farbe4), 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit validen Blumensorten gibt den Status 200 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsBlumensorten = """
       "blumensorten": [
          {"name": "Dill"},
          {"name": "Frühblüher"},
          {"name": "verzweigte Rosen"},
          {"name": "5 weiße Blumen"}
       ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit einer invaliden Blumensorte gibt den Status 400 zurück`() {
    // Given
    val sorte1 = "Bla/Bla"
    val sorte2 = "Früh-blüher"
    val requestBody = erstelleProdukteResourceParameter(
      cmsBlumensorten = """
       "blumensorten": [
         {"name": "<sorte>"}
       ]
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<sorte>", sorte1), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<sorte>", sorte2), 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit invaliden Liefertagen gibt den Status 400 zurück`() {
    // Given
    val liefertagRandomString = "not a date string"
    val liefertagWrongFormat = "01.01.2022"
    val requestBody = erstelleProdukteResourceParameter(
      userLiefertage = """
       "liefertage": {
        "verfuegbareLiefertage": [
          "<liefertag>",
          "2022-01-12"
        ]
      }
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<liefertag>", liefertagRandomString), 400)
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody.replace("<liefertag>", liefertagWrongFormat), 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit validen Liefertagen gibt den Status 200 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      userLiefertage = """
       "liefertage": {
        "verfuegbareLiefertage": [
          "2022-01-01",
          "2022-01-12"
        ]
      }
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 200)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit invalider ProduktnummernVerwendung gibt den Status 400 zurück`() {
    // Given
    val requestBody = erstelleProdukteResourceParameter(
      cmsProduktnummernVerwendung = """
       "produktnummernVerwendung": "invalidValue"
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(requestBody, 400)
  }

  @Test
  internal fun `Der Aufruf von ProdukteResource mit Userfilterung mit valider ProduktnummernVerwendung gibt den Status 200 zurück`() {
    // Given
    val produktnummernVerwendungAusblenden = "ausblenden"
    val produktnummernVerwendungSelektionsbasis = "selektionsbasis"
    val requestBody = erstelleProdukteResourceParameter(
      cmsProduktnummernVerwendung = """
       "produktnummernVerwendung": "<produktnummernVerwendung>"
    """.trimIndent()
    )

    // Then
    produkteResourceMitUserfilterungErwartetResponseCode(
      requestBody.replace(
        "<produktnummernVerwendung>",
        produktnummernVerwendungAusblenden
      ), 200
    )
    produkteResourceMitUserfilterungErwartetResponseCode(
      requestBody.replace(
        "<produktnummernVerwendung>",
        produktnummernVerwendungSelektionsbasis
      ), 200
    )
  }

  @ParameterizedTest(name = "Der Aufruf von ProdukteResource mit CMS Filterung blumensorten: {0}, farben: {1}, klassifikationen: {2}, preisbereich: {3}, limit: {4}, produktNummern: {5}, produktnummernVerwendung: {6}, gibt den Status {7} zurück")
  @MethodSource("provideCmsProdukteFilterDTO")
  @Suppress("LongParameterList")
  internal fun `Der Aufruf von ProdukteResource mit CMS Filterung gibt den korrekten Status zurück`(
    blumensorten: List<SorteDTO>,
    farben: List<FarbeDTO>,
    klassifikationen: List<KlassifikationDTO>,
    preisbereich: PreisbereichDTO?,
    limit: Int?,
    produktNummern: List<ProduktnummerDTO>,
    produktnummernVerwendung: String?,
    status: Int
  ) {
    // Given
    val minPreisString = if (preisbereich?.minPreis != null) {
      "{\"bruttoBetrag\": ${preisbereich.minPreis.bruttoBetrag}, \"waehrung\": \"${preisbereich.minPreis!!.waehrung}\"}"
    } else {
      null
    }
    val maxPreisString = if (preisbereich?.maxPreis != null) {
      "{\"bruttoBetrag\": ${preisbereich.maxPreis.bruttoBetrag}, \"waehrung\": \"${preisbereich.maxPreis!!.waehrung}\"}"
    } else {
      null
    }
    val preisbereichString = if (preisbereich != null) {
      "{\"minPreis\": $minPreisString, \"maxPreis\": $maxPreisString}"
    } else {
      null
    }
    val produktnummernVerwendungString = if (produktnummernVerwendung != null) {
      "\"$produktnummernVerwendung\""
    } else {
      null
    }
    val invalidRequestBody = """
       {
         "blumensorten": [${blumensorten.joinToString { "{\"name\": \"${it.name}\"}" }}],
         "farben": [${farben.joinToString { "{\"name\": \"${it.name}\"}" }}],
         "klassifikationen": [${klassifikationen.joinToString { "{\"name\":\"${it.name}\",\"id\":\"${it.id}\"}" }}],
         "preisbereich": $preisbereichString,
         "limit": $limit,
         "produktNummern": [${produktNummern.joinToString { "{\"nummer\": \"${it.nummer}\"}" }}],
         "produktnummernVerwendung": $produktnummernVerwendungString
       }
    """.trimIndent()

    // When
    val response = given().`when`().body(
      invalidRequestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte")

    // Then
    response.then().statusCode(status)
  }

  fun produkteResourceMitUserfilterungErwartetResponseCode(requestBody: String, code: Int) {
    val response = given().`when`().body(
      requestBody
    ).contentType(ContentType.JSON).post("/api/finden/produkte/mituserfilterung")

    response.then().statusCode(code)
  }

  fun erstelleVerfügbarkeitenFürProdukt(): List<ProduktVerfügbarkeit> {
    val nächstmöglicheVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(2)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
    )
    val letztmöglicheVerfügbarkeit = erstelleProduktVerfügbarkeit(
      liefertag = Liefertag(LocalDate.now().plusDays(2)),
      bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
    )
    return listOf(nächstmöglicheVerfügbarkeit, letztmöglicheVerfügbarkeit)
  }

  fun erstelleVerfügbaresProdukt(
    produktnummer: String, preis: Preis, streichpreis: Preis
  ): Produkt {
    return erstelleProdukt(
      nummer = produktnummer,
      preis = preis,
      streichpreis = streichpreis,
      verfügbarkeiten = erstelleVerfügbarkeitenFürProdukt()
    )
  }

  fun erstelleVerfügbaresProdukt(
    produktnummer: String
  ): Produkt {
    return erstelleProdukt(
      nummer = produktnummer,
      verfügbarkeiten = erstelleVerfügbarkeitenFürProdukt()
    )
  }

  companion object {
    @JvmStatic
    @Suppress("LongMethod")
    private fun provideCmsProdukteFilterDTO(): Stream<Arguments> = Stream.of(
      // blumensorten farben klassifikationen preisbereich limit produktNummern produktnummernVerwendung status
      Arguments.of(
        CmsProdukteFilterDTO().blumensorten, // blumensorten
        CmsProdukteFilterDTO().farben, // farben
        CmsProdukteFilterDTO().klassifikationen, // klassifikationen
        CmsProdukteFilterDTO().preisbereich, // preisbereich
        CmsProdukteFilterDTO().limit, // limit
        CmsProdukteFilterDTO().produktNummern, // produktNummern
        CmsProdukteFilterDTO().produktnummernVerwendung, // produktnummernVerwendung
        200 // status
      ),
      Arguments.of(
        listOf(SorteDTO(name = "Lilien")),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        null,
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        200
      ),
      Arguments.of(
        listOf(SorteDTO(name = "Something not in '^[a-zA-Z0-9äöüÄÖÜß ]+$'")),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        null,
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        400
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        listOf(FarbeDTO(name = "blau")),
        emptyList<KlassifikationDTO>(),
        null,
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        200
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        listOf(FarbeDTO(name = "Something not in '^[a-zäöüß]+$'")),
        emptyList<KlassifikationDTO>(),
        null,
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        400
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        listOf(KlassifikationDTO(name = "Strauss", id = "11")),
        null,
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        200
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        listOf(KlassifikationDTO(name = "Klassifikation", id = "Something not in '^(\\d)+$'")),
        null,
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        400
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        PreisbereichDTO(minPreis = PreisDTO(bruttoBetrag = BigDecimal(5.0), waehrung = "EUR")),
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        200
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        PreisbereichDTO(minPreis = PreisDTO(bruttoBetrag = BigDecimal(5.0), waehrung = "NOT A WAEHRUNG")),
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        400
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        PreisbereichDTO(maxPreis = PreisDTO(bruttoBetrag = BigDecimal(5.0), waehrung = "EUR")),
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        200
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        PreisbereichDTO(maxPreis = PreisDTO(bruttoBetrag = BigDecimal(5.0), waehrung = "NOT A WAEHRUNG")),
        null,
        emptyList<ProduktnummerDTO>(),
        null,
        400
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        null,
        null,
        listOf(ProduktnummerDTO(nummer = "20003804")),
        null,
        200
      ),
      Arguments.of(
        emptyList<SorteDTO>(),
        emptyList<FarbeDTO>(),
        emptyList<KlassifikationDTO>(),
        null,
        null,
        listOf(ProduktnummerDTO(nummer = "Something not in '^(\\d)+$'")),
        null,
        400
      ),
    )
  }
}
