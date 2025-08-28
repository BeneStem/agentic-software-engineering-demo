package de.blume2000.finden.domain.model.produkte.produkt

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@Tag("unit")
internal class BeschreibungTest {
  @Test
  internal fun `Erstelle Beschreibung sofern der Wert valide ist`() {
    // Given
    val value = "Eine gültige Produktbeschreibung"

    // When
    val beschreibung = Beschreibung(value)

    // Then
    expectThat(beschreibung.asString()).isEqualTo(value)
  }

  @Test
  internal fun `Wenn der Wert leer ist wird keine Beschreibung initialisiert`() {
    // Given
    val leerValue = ""
    val whitespaceValue = "   "

    // Then
    expectThrows<BeschreibungIstLeerException> {
      Beschreibung(leerValue)
    }
    expectThrows<BeschreibungIstLeerException> {
      Beschreibung(whitespaceValue)
    }
  }

  @Test
  internal fun `Wenn der Wert zu lang ist wird keine Beschreibung initialisiert`() {
    // Given
    val zuLangerValue = "a".repeat(501)

    // Then
    expectThrows<BeschreibungZuLangException> {
      Beschreibung(zuLangerValue)
    }
  }

  @Test
  internal fun `Beschreibung mit genau 500 Zeichen ist valide`() {
    // Given
    val value500Zeichen = "a".repeat(500)

    // When
    val beschreibung = Beschreibung(value500Zeichen)

    // Then
    expectThat(beschreibung.asString()).isEqualTo(value500Zeichen)
  }

  @Test
  internal fun `HTML wird aus Beschreibung entfernt`() {
    // Given
    val valueWithHtml = "Ein Text mit <script>alert('xss')</script> HTML"
    val expectedSanitized = "Ein Text mit  HTML"

    // When
    val beschreibung = Beschreibung(valueWithHtml)

    // Then
    expectThat(beschreibung.asString()).isEqualTo(expectedSanitized)
  }

  @Test
  internal fun `HTML entities werden korrekt behandelt und Länge validiert`() {
    // Given - HTML entities that might expand after unescaping
    val valueWithEntities = "&quot;".repeat(100) + "text" // Should be well under 500 after sanitization

    // When
    val beschreibung = Beschreibung(valueWithEntities)

    // Then - Should not throw exception and should be sanitized
    expectThat(beschreibung.asString()).isEqualTo("\"".repeat(100) + "text")
  }
}
