package de.blume2000.finden.domain.model.produkte.produkt

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

@Tag("unit")
internal class ProduktbildUrlTest {

  @Test
  internal fun `Erstelle ProduktbildUrl mit validen Wert`() {
    // Given
    val bildUrlString =
      "https://res.cloudinary.com/blume2000/image/upload/c_fill,dpr_%d%,f_auto,h_%h%,q_auto,w_%w%/v1/prod/produkte/PRIMAER/20003385"

    // When
    val bildUrl = ProduktbildUrl(bildUrlString)

    // Then
    expectThat(bildUrl.asString()).isEqualTo(bildUrlString)
  }

  @Test
  internal fun `Wenn der Wert leer ist wird keine ProduktbildUrl initialisiert`() {
    // Given
    val bildUrlString = ""
    val bildUrlString2 = "  "

    // Then
    expectThrows<ProduktbildUrlIstLeerException> {
      ProduktbildUrl(bildUrlString)
    }

    expectThrows<ProduktbildUrlIstLeerException> {
      ProduktbildUrl(bildUrlString2)
    }
  }

  @Test
  internal fun `Wenn eine URL mit etwas anderem als https übergeben wird, wird eine UnsicheresProtokollException geworfen`() {
    // Given
    val bildUrlString =
      "http://res.cloudinary.com/blume2000/image/upload/c_fill,dpr_%d%,f_auto,h_%h%,q_auto,w_%w%/v1/prod/produkte/PRIMAER/20003385"

    // Then
    expectThrows<UnsicheresProtokollException> {
      ProduktbildUrl(bildUrlString)
    }
  }
}
