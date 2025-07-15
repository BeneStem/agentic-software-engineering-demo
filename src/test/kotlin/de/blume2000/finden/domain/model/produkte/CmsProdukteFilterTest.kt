package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import jakarta.ws.rs.core.MultivaluedHashMap
import jakarta.ws.rs.core.MultivaluedMap
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactly

@Tag("unit")
internal class CmsProdukteFilterTest {

  @Test
  internal fun `Generiert ProdukteFilter mit Produktnummern`() {
    // Given
    val multivaluedMap: MultivaluedMap<String, String> = MultivaluedHashMap()
    multivaluedMap.add("produktNummern", "10140141,102431,102433,102542")

    // When
    val produktfilter = CmsProdukteFilter.vonQueryParameters(multivaluedMap)

    // Then
    expectThat(produktfilter.produktNummern).containsExactly(
      Produktnummer("10140141"),
      Produktnummer("102431"),
      Produktnummer("102433"),
      Produktnummer("102542")
    )
  }

  @Test
  internal fun `Entfernt Leerzeichen in den Produktnummern`() {
    // Given
    val multivaluedMap: MultivaluedMap<String, String> = MultivaluedHashMap()
    multivaluedMap.add("produktNummern", " 10140141 , 102431 , 102433,102542 ")

    // When
    val produktfilter = CmsProdukteFilter.vonQueryParameters(multivaluedMap)

    // Then
    expectThat(produktfilter.produktNummern).containsExactly(
      Produktnummer("10140141"),
      Produktnummer("102431"),
      Produktnummer("102433"),
      Produktnummer("102542")
    )
  }

  @Test
  internal fun `Entfernt trailing Komma in Produktnummern`() {
    // Given
    val multivaluedMap: MultivaluedMap<String, String> = MultivaluedHashMap()
    multivaluedMap.add("produktNummern", "10140141,102431,102433,102542,")

    // When
    val produktfilter = CmsProdukteFilter.vonQueryParameters(multivaluedMap)

    // Then
    expectThat(produktfilter.produktNummern).containsExactly(
      Produktnummer("10140141"),
      Produktnummer("102431"),
      Produktnummer("102433"),
      Produktnummer("102542")
    )
  }
}
