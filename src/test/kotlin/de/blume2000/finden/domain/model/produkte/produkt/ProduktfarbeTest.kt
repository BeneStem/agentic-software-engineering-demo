package de.blume2000.finden.domain.model.produkte.produkt

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@Tag("unit")
internal class ProduktfarbeTest {

  @Test
  internal fun `Wirft eine Exception bei Aufruf mit UpperCase Zeichen`() {
    // Given
    // When
    // Then
    expectThrows<ProduktfarbeEnthältUpperCaseZeichenException> {
      Produktfarbe("Grün")
    }
  }

  @Test
  internal fun `Erstellt eine Instanz mit LowerCase Paramter`() {
    // Given
    val produktfarbe = Produktfarbe("Grün".lowercase())
    // When
    // Then
    expectThat(produktfarbe.asString()).isEqualTo("grün")
  }
}
