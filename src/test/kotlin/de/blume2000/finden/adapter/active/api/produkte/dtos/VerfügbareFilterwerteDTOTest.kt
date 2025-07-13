package de.blume2000.finden.adapter.active.api.produkte.dtos

import java.math.BigDecimal
import java.time.LocalDate
import java.util.Currency
import java.util.Locale
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@Tag("unit")
class VerfügbareFilterwerteDTOTest {

  @Test
  internal fun `entferneFilterMitNurEinemFilterwert gibt alle Sorten zurück, wenn mehr als eine verfügbar ist`() {
    // Given
    val verfügbareFilterwerteDTO = erstelleVerfügbareFilterwerteDTO(
      sorten = listOf(SorteDTO("Rosen"), SorteDTO("Nelken"))
    )

    // When
    val filterwerteDTO = verfügbareFilterwerteDTO.entferneFilterMitNurEinemFilterwert()

    // Then
    expectThat(filterwerteDTO.sorten).isEqualTo(listOf(SorteDTO("Rosen"), SorteDTO("Nelken")))
  }

  @Test
  internal fun `entferneFilterMitNurEinemFilterwert gibt leere Liste für Sorten zurück, wenn nur eine verfügbar ist`() {
    // Given
    val verfügbareFilterwerteDTO = erstelleVerfügbareFilterwerteDTO(
      sorten = listOf(SorteDTO("Rosen"))
    )

    // When
    val filterwerteDTO = verfügbareFilterwerteDTO.entferneFilterMitNurEinemFilterwert()

    // Then
    expectThat(filterwerteDTO.sorten).isEqualTo(emptyList())
  }

  private fun erstelleVerfügbareFilterwerteDTO(
    sorten: List<SorteDTO> = listOf(SorteDTO("Rosen")),
    farben: List<FarbeDTO> = listOf(FarbeDTO("rot")),
    preisbereich: PreisbereichDTO = PreisbereichDTO(
      minPreis = PreisDTO(
        bruttoBetrag = BigDecimal(25.0),
        waehrung = Currency.getInstance(Locale.GERMANY).currencyCode
      ),
      maxPreis = PreisDTO(bruttoBetrag = BigDecimal(50.0), waehrung = Currency.getInstance(Locale.GERMANY).currencyCode)
    ),
    klassifikationen: List<KlassifikationDTO> = listOf(KlassifikationDTO(name = "Strauss", id = "11")),
    liefertage: LiefertageDTO = LiefertageDTO(
      verfuegbareLiefertage = listOf(
        LocalDate.now().plusDays(1).toString(),
        LocalDate.now().plusDays(1).toString()
      )
    )
  ) = VerfügbareFilterwerteDTO(
    sorten = sorten,
    farben = farben,
    preisbereich = preisbereich,
    klassifikationen = klassifikationen,
    liefertage = liefertage,
  )
}
