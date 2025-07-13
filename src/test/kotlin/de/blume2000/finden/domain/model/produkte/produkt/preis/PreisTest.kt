package de.blume2000.finden.domain.model.produkte.produkt.preis

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale

@Tag("unit")
internal class PreisTest {
  @Test
  internal fun `Liefert den Preis`() {
    // Given
    val preis = Preis(BigDecimal("10.00"), Currency.getInstance(Locale.GERMANY))

    // When
    val preisAsString = preis.asString()

    // Then
    expectThat(preisAsString).isEqualTo("10,00")
  }

  @Test
  internal fun `Ein negativer Preis wird nicht initialisiert`() {
    // Given
    val betrag = BigDecimal("-10.00")
    val währung = Currency.getInstance(Locale.GERMANY)

    // Then
    expectThrows<NegativerPreisException> {
      Preis(betrag, währung)
    }
  }

  @Test
  internal fun `Bei einer unerwarteten Währung wird der Preis nicht initialisiert`() {
    // Given
    val betrag = BigDecimal("10.00")
    val währung = Currency.getInstance(Locale.CHINA)

    // Then
    expectThrows<UnerwarteteWaehrungException> {
      Preis(betrag, währung)
    }
  }

  @Test
  internal fun `Wenn der Preis in istGrößerAlsOderGleich größer ist als der verglichene Preis wird true zurück gegeben`() {
    // Given
    val preis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))
    val vergleichsPreis = Preis(BigDecimal.valueOf(100), Currency.getInstance(Locale.GERMANY))

    // When
    val ergebnis = preis.istGrößerAlsOderGleich(vergleichsPreis)

    // Then
    expectThat(ergebnis).isTrue()
  }

  @Test
  internal fun `Wenn der Preis in istGrößerAlsOderGleich identisch ist mit dem verglichenen Preis wird true zurück gegeben`() {
    // Given
    val preis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))
    val vergleichsPreis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))

    // When
    val ergebnis = preis.istGrößerAlsOderGleich(vergleichsPreis)

    // Then
    expectThat(ergebnis).isTrue()
  }

  @Test
  internal fun `Wenn der Preis in istGrößerAlsOderGleich kleiner ist als der verglichene Preis wird false zurück gegeben`() {
    // Given
    val preis = Preis(BigDecimal.valueOf(100), Currency.getInstance(Locale.GERMANY))
    val vergleichsPreis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))

    // When
    val ergebnis = preis.istGrößerAlsOderGleich(vergleichsPreis)

    // Then
    expectThat(ergebnis).isFalse()
  }

  @Test
  internal fun `Wenn der Preis in istGrößerAls größer ist als der verglichene Preis wird true zurück gegeben`() {
    // Given
    val preis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))
    val vergleichsPreis = Preis(BigDecimal.valueOf(100), Currency.getInstance(Locale.GERMANY))

    // When
    val ergebnis = preis.istGrößerAls(vergleichsPreis)

    // Then
    expectThat(ergebnis).isTrue()
  }

  @Test
  internal fun `Wenn der Preis in istGrößerAls identisch ist mit dem verglichenen Preis wird false zurück gegeben`() {
    // Given
    val preis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))
    val vergleichsPreis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))

    // When
    val ergebnis = preis.istGrößerAls(vergleichsPreis)

    // Then
    expectThat(ergebnis).isFalse()
  }

  @Test
  internal fun `Wenn der Preis in istGrößerAls kleiner ist als der verglichene Preis wird false zurück gegeben`() {
    // Given
    val preis = Preis(BigDecimal.valueOf(100), Currency.getInstance(Locale.GERMANY))
    val vergleichsPreis = Preis(BigDecimal.valueOf(200), Currency.getInstance(Locale.GERMANY))

    // When
    val ergebnis = preis.istGrößerAls(vergleichsPreis)

    // Then
    expectThat(ergebnis).isFalse()
  }
}
