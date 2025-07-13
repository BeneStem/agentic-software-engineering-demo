package de.blume2000.finden.application

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import de.blume2000.finden.adapter.passive.database.produkte.MongoProdukt
import de.blume2000.finden.testutils.erstelleAvroProdukt
import de.blume2000.util.TestContainers
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
internal class ProduktÄnderungApplicationServiceIntegrationTest {
  @Inject
  private lateinit var mongoClient: MongoClient

  @Inject
  private lateinit var produktÄnderungApplicationService: ProduktÄnderungApplicationService

  private lateinit var mongoCollection: MongoCollection<Document>

  @BeforeEach
  fun cleanup() {
    mongoCollection = mongoClient.getDatabase("finden-test").getCollection(MongoProdukt.MONGO_COLLECTION)
    mongoCollection.drop()
  }

  @Test
  internal fun `Alle Felder des Avroprodukts werden persistiert`() {
    // Given
    val avroProdukt = erstelleAvroProdukt()

    expectThat(mongoCollection.find().toList().size).isEqualTo(0)

    // When
    produktÄnderungApplicationService.importiereNeueProduktDaten(avroProdukt)

    // Then
    val produkteAfterImport = mongoCollection.find(MongoProdukt::class.java).toList().map { it.nachProdukt() }
    expectThat(produkteAfterImport.size).isEqualTo(1)
    val importedProdukt = produkteAfterImport.get(0)

    expectThat(importedProdukt.nummer.asString()).isEqualTo(avroProdukt.produktNummer)
    expectThat(importedProdukt.name.asString()).isEqualTo(avroProdukt.produktNameShops)
    expectThat(importedProdukt.preis.bruttoBetrag.toString()).isEqualTo(avroProdukt.preis.bruttoBetrag)
    expectThat(importedProdukt.streichpreis.bruttoBetrag.toString()).isEqualTo(avroProdukt.uvp.bruttoBetrag)
    expectThat(importedProdukt.klassifikation.id.value).isEqualTo(avroProdukt.klassifikation3.id.toString())
    expectThat(importedProdukt.bildUrl.asString()).isEqualTo(avroProdukt.produktBild.baseLink)
    expectThat(importedProdukt.pdsUrlSeoName.asString()).isEqualTo(avroProdukt.urlName)
    expectThat(importedProdukt.farben.map { it.toString() }).isEqualTo(avroProdukt.artikelFarben)
    expectThat(importedProdukt.blumensorten.map { it.toString() }).isEqualTo(avroProdukt.blumenSorten)
  }
}
