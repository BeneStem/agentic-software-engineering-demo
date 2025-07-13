package de.blume2000.finden.domain.model.produkte.produkt

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@Tag("unit")
internal class ProduktnameTest {
  @Test
  internal fun `Erstelle Produktname sofern der Wert valide ist`() {
    // Given
    val value = "ABC123"

    // When
    val produktname = Produktname(value)

    // Then
    expectThat(produktname.asString()).isEqualTo(value)
  }

  @Test
  internal fun `Wenn der Wert leer ist wird keine Produktname initialisiert`() {
    // Given
    val value = ""
    val value2 = "   "

    // Then
    expectThrows<ProduktnameIstLeerException> {
      Produktname(value)
    }
    expectThrows<ProduktnameIstLeerException> {
      Produktname(value2)
    }
  }
}
