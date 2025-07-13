package de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte

import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.testutils.erstelleBlumensorten
import de.blume2000.finden.testutils.erstelleEuroPreis
import de.blume2000.finden.testutils.erstelleEuroPreisbereich
import de.blume2000.finden.testutils.erstelleKlassifikation
import de.blume2000.finden.testutils.erstelleLiefertage
import de.blume2000.finden.testutils.erstelleProduktfarben
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.stream.Stream

@Tag("unit")
internal class VerfügbareFilterwerteTest {

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt alle Sorten zurück, wenn Sorten im CMS Filter leer sind`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter()
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      sorten = erstelleBlumensorten("Rosen", "Nelken", "Tulpen")
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.sorten).isEqualTo(erstelleBlumensorten("Rosen", "Nelken", "Tulpen"))
  }

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt nur Sorten zurück, die auch im CMS Filter vorhanden sind`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter(
      blumensorten = erstelleBlumensorten("Rosen", "Nelken")
    )
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      sorten = erstelleBlumensorten("Rosen", "Nelken", "Tulpen")
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.sorten).isEqualTo(erstelleBlumensorten("Rosen", "Nelken"))
  }

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt alle Farben zurück, wenn Farben im CMS Filter leer sind`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter()
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      farben = erstelleProduktfarben("gelb", "rot", "blau")
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.farben).isEqualTo(erstelleProduktfarben("gelb", "rot", "blau"))
  }

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt nur Farben zurück, die auch im CMS Filter vorhanden sind`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter(
      farben = erstelleProduktfarben("gelb", "rot")
    )
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      farben = erstelleProduktfarben("gelb", "rot", "blau")
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.farben).isEqualTo(erstelleProduktfarben("gelb", "rot"))
  }

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt alle Klassifikationen zurück, wenn Klassifikationen im CMS Filter leer sind`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter()
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      klassifikationen = listOf(
        erstelleKlassifikation("11", "Strauss"),
        erstelleKlassifikation("13", "Topfpflanze")
      )
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.klassifikationen).isEqualTo(
      listOf(
        erstelleKlassifikation("11", "Strauss"),
        erstelleKlassifikation("13", "Topfpflanze")
      )
    )
  }

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt nur Klassifikationen zurück, die auch im CMS Filter vorhanden sind`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter(
      klassifikationen = listOf(KlassifikationId("11"))
    )
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      klassifikationen = listOf(
        erstelleKlassifikation("11", "Strauss"),
        erstelleKlassifikation("13", "Topfpflanze")
      )
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.klassifikationen).isEqualTo(
      listOf(
        erstelleKlassifikation("11", "Strauss"),
      )
    )
  }

  @Test
  internal fun `schränkeAufCmsFilterwerteEin gibt alle Preisbereich ohne Veränderung zurück, wenn preise im CMS Filter leer ist`() {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter()
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      preisBereich = erstelleEuroPreisbereich(20.0, 250.0)
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.preisBereich).isEqualTo(erstelleEuroPreisbereich(20.0, 250.0))
  }

  @ParameterizedTest(
    name = "schränkeAufCmsFilterwerteEin gibt evtl. eingeschränkten Preisbereich zurück - cmsMinPreis: {0}, cmsMaxPreis: {1}, minPreis: {2}, maxPreis: {3}, expectedMinPreis: {4}, expectedMaxPreis: {5}")
  @MethodSource("providePreisbereichArguments")
  @Suppress("LongParameterList")
  internal fun `schränkeAufCmsFilterwerteEin gibt eingeschränkten Preisbereich zurück, wenn einschränkende Preise im CMS Filter vorhanden sind`(
    cmsMinPreis: Double?,
    cmsMaxPreis: Double?,
    minPreis: Double?,
    maxPreis: Double?,
    expectedMinPreis: Double,
    expectedMaxPreis: Double
  ) {
    // Given
    val cmsProdukteFilter = CmsProdukteFilter(
      minPreis = cmsMinPreis?.let { erstelleEuroPreis(it) },
      maxPreis = cmsMaxPreis?.let { erstelleEuroPreis(it) }
    )
    val verfügbareFilterwerte = erstelleVerfügbareFilterwerte(
      preisBereich = erstelleEuroPreisbereich(minPreis, maxPreis)
    )

    // When
    val eingeschränkteFilterwerte = verfügbareFilterwerte.schränkeAufCmsFilterwerteEin(cmsProdukteFilter)

    // Then
    expectThat(eingeschränkteFilterwerte.preisBereich).isEqualTo(
      erstelleEuroPreisbereich(
        expectedMinPreis,
        expectedMaxPreis
      )
    )
  }

  companion object {
    @JvmStatic
    private fun providePreisbereichArguments(): Stream<Arguments> = Stream.of(
      // cmsMinPreis cmsMaxPreis minPreis maxPreis expectedMinPreis expectedMaxPreis
      Arguments.of(45.0, 100.0, null, null, 45.0, 100.0),
      Arguments.of(45.0, 100.0, null, 250.0, 45.0, 100.0),
      Arguments.of(45.0, 100.0, 20.0, null, 45.0, 100.0),
      Arguments.of(null, null, 20.0, 250.0, 20.0, 250.0),
      Arguments.of(30.0, null, 20.0, 250.0, 30.0, 250.0),
      Arguments.of(null, 100.0, 20.0, 250.0, 20.0, 100.0),
      Arguments.of(45.0, 100.0, 20.0, 250.0, 45.0, 100.0),
    )
  }

  private fun erstelleVerfügbareFilterwerte(
    sorten: List<Blumensorte> = erstelleBlumensorten("Rosen"),
    farben: List<Produktfarbe> = erstelleProduktfarben(),
    preisBereich: PreisBereich = erstelleEuroPreisbereich(15.0, 45.0),
    klassifikationen: List<Klassifikation> = listOf(erstelleKlassifikation("11", "Strauss")),
    liefertage: Liefertage = erstelleLiefertage(morgen = true, übermorgen = true),
  ) = VerfügbareFilterwerte(
    sorten = sorten,
    farben = farben,
    preisBereich = preisBereich,
    klassifikationen = klassifikationen,
    liefertage = liefertage,
  )
}
