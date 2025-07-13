package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Produktname
import de.blume2000.finden.testutils.erstelleProdukt
import de.blume2000.finden.testutils.erstelleProduktliste
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.containsExactly
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo

@Tag("unit")
internal class ProdukteTest {

  @Test
  internal fun `Gibt die Liste zurück, die im Constructor übergeben wurde`() {
    // Given
    val testProdukte = erstelleProduktliste()
    val produkte = Produkte(testProdukte)

    // When
    val alleProdukte = produkte.gebeMirAlleProdukte()

    // Then
    expectThat(alleProdukte).isEqualTo(testProdukte)
  }

  @Test
  internal fun `begrenzung einer leeren Produktliste macht keinen unterschied`() {
    // Given
    val produkteLeer = Produkte(emptyList())

    // That
    val mitNullBegrenzt = produkteLeer.begrenzeAufNEinträge(null)
    val auf0Begrenzt = produkteLeer.begrenzeAufNEinträge(0)
    val auf1Begrenzt = produkteLeer.begrenzeAufNEinträge(1)
    val aufNBegrenzt = produkteLeer.begrenzeAufNEinträge(42)

    expectThat(mitNullBegrenzt.gebeMirAlleProdukte()).isEqualTo(emptyList())
    expectThat(auf0Begrenzt.gebeMirAlleProdukte()).isEqualTo(emptyList())
    expectThat(auf1Begrenzt.gebeMirAlleProdukte()).isEqualTo(emptyList())
    expectThat(aufNBegrenzt.gebeMirAlleProdukte()).isEqualTo(emptyList())
  }

  @Test
  internal fun `begrenzung einer Produktliste mit einträgen funktioniert wie erwartet`() {
    // Given
    val produkt1 = erstelleProdukt(nummer = "1")
    val produkt2 = erstelleProdukt(nummer = "2")
    val produktliste = listOf(produkt1, produkt2)
    val dreiProdukte = Produkte(produktliste)

    // That
    val dreiProdukteMitNullBegrenzt = dreiProdukte.begrenzeAufNEinträge(null)
    val dreiProdukteAuf0Begrenzt = dreiProdukte.begrenzeAufNEinträge(0)
    val dreiProdukteAuf1Begrenzt = dreiProdukte.begrenzeAufNEinträge(1)
    val dreiProdukteAuf2Begrenzt = dreiProdukte.begrenzeAufNEinträge(2)
    val dreiProdukteAuf3Begrenzt = dreiProdukte.begrenzeAufNEinträge(3)

    expectThat(dreiProdukteMitNullBegrenzt.gebeMirAlleProdukte()).containsExactly(produkt1, produkt2)
    expectThat(dreiProdukteAuf0Begrenzt.gebeMirAlleProdukte()).isEqualTo(emptyList())
    expectThat(dreiProdukteAuf1Begrenzt.gebeMirAlleProdukte()).containsExactly(produkt1)
    expectThat(dreiProdukteAuf2Begrenzt.gebeMirAlleProdukte()).containsExactly(produkt1, produkt2)
    expectThat(dreiProdukteAuf3Begrenzt.gebeMirAlleProdukte()).containsExactly(produkt1, produkt2)
  }

  @Test
  @Disabled
  internal fun `Gebe mir eine nach Liefbarkeit sortiere Liste zurück`() {
    // Given
    val testProdukte = listOf(
      erstelleProdukt(name = "1. Produkt"),
      erstelleProdukt(name = "3. Produkt"),
      erstelleProdukt(name = "4. Produkt"),
      erstelleProdukt(name = "2. Produkt")
    )
    val produkte = Produkte(testProdukte)

    // When
    val sortierteProdukte = produkte.gebeMirAlleProdukte()

    // Then
    expectThat(sortierteProdukte).hasSize(4)
    expectThat(sortierteProdukte[0].name).isEqualTo(Produktname("1. Produkt"))
    expectThat(sortierteProdukte[1].name).isEqualTo(Produktname("2. Produkt"))
    expectThat(sortierteProdukte[2].name).isEqualTo(Produktname("3. Produkt"))
    expectThat(sortierteProdukte[3].name).isEqualTo(Produktname("4. Produkt"))
  }

  // TODO BS NEXT move to MongoRepository
  @Test
  @Disabled
  internal fun `Gibt nur Produkte aus, bei denen 'lieferbar bis' in er Zukunft liegt`() {
    // Given
    val testProdukte = listOf(
      erstelleProdukt(name = "Lieferbares Produkt"),
      erstelleProdukt(name = "Nicht lieferbares Produkt")
    )
    val produkte = Produkte(testProdukte)

    // When
    val sortierteProdukte = produkte.gebeMirAlleProdukte()

    // Then
    expectThat(sortierteProdukte).hasSize(1)
    expectThat(sortierteProdukte).contains(testProdukte[0])
  }
}
