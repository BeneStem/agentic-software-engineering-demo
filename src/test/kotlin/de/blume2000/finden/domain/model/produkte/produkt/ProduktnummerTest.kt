package de.blume2000.finden.domain.model.produkte.produkt

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@Tag("unit")
internal class ProduktnummerTest {
  @Test
  internal fun `Erstelle Produktnummer sofern der Wert valide ist`() {
    // Given
    val value = "ABC123"

    // When
    val produktnummer = Produktnummer(value)

    // Then
    expectThat(produktnummer.asString()).isEqualTo(value)
  }

  @Test
  internal fun `Wenn der Wert leer ist wird keine Produktnummer initialisiert`() {
    // Given
    val value = ""
    val value2 = "   "

    // Then
    expectThrows<ProduktnummerIstLeerException> {
      Produktnummer(value)
    }
    expectThrows<ProduktnummerIstLeerException> {
      Produktnummer(value2)
    }
  }
}
