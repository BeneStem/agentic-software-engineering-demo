package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import de.blume2000.finden.testutils.erstelleProdukt
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

@Tag("unit")
internal class ProduktTest {
  @Test
  internal fun `Wirft eine StreichpreisIstNiedrigerAlsPreisException wenn der Streichpreis kleiner ist als der Preis`() {
    // Then
    expectThrows<StreichpreisIstNiedrigerAlsPreisException> {
      erstelleProdukt(
        preis = Preis(BigDecimal.TEN, Currency.getInstance(Locale.GERMANY)),
        streichpreis = Preis(BigDecimal.ONE, Currency.getInstance(Locale.GERMANY))
      )
    }
  }

  @Test
  internal fun `verwende pdsUrl`() {
    val pdsPfad = "20002902-beste-mama"
    // GIVEN
    val produktMitPdsUrl = erstelleProdukt(pdsUrl = ProduktdetailseiteUrlSeoName(pdsPfad))
    // THEN
    expectThat(produktMitPdsUrl.getProduktDetailseiteUrlPfad()).isEqualTo(
      "/p/" +
        pdsPfad
    )
  }

  @Test
  internal fun `Erstelle Produkt mit Beschreibung`() {
    // Given
    val beschreibung = Beschreibung("Eine schöne Produktbeschreibung")

    // When
    val produkt = erstelleProdukt(beschreibung = beschreibung)

    // Then
    expectThat(produkt.beschreibung).isEqualTo(beschreibung)
  }

  @Test
  internal fun `Erstelle Produkt ohne Beschreibung`() {
    // Given/When
    val produkt = erstelleProdukt()

    // Then
    expectThat(produkt.beschreibung).isEqualTo(null)
  }
}
