package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import java.util.Currency
import java.util.Locale
import java.util.function.Function
import jakarta.ws.rs.core.MultivaluedMap

data class CmsProdukteFilter(
  val klassifikationen: List<KlassifikationId> = emptyList(),
  val minPreis: Preis? = null,
  val maxPreis: Preis? = null,
  val produktNummern: List<Produktnummer> = emptyList(),
  val produktnummernVerwendung: ProduktnummernVerwendung = ProduktnummernVerwendung.KEINE,
  private var limit: Int? = null,
  val farben: List<Produktfarbe> = emptyList(),
  val blumensorten: List<Blumensorte> = emptyList(),
) {
  fun disableLimit() {
    limit = null
  }

  fun getLimit(): Int? {
    return limit
  }

  companion object {
    // TODO remove when removing ProduktRessource get endpoint
    @Deprecated("Removed when GET endpoint is obsolet")
    fun vonQueryParameters(parameters: MultivaluedMap<String, String>) = CmsProdukteFilter(
      klassifikationen = parseKlassifikationIds(parameters),
      minPreis = parsePreis(parameters, "minPreis"),
      maxPreis = parsePreis(parameters, "maxPreis"),
      produktNummern = parseProduktnummern(parameters),
      produktnummernVerwendung = parseProduktnummernVerwendung(parameters),
      limit = parseLimit(parameters),
      farben = parseFarben(parameters),
      blumensorten = parseBlumensorten(parameters)
    )

    private fun parseKlassifikationIds(parameters: MultivaluedMap<String, String>) =
      defaultOnBlank(parameters.getFirst("klassifikation3"), emptyList()) {
        it.split(",").map { idString -> KlassifikationId(idString) }
      }

    private fun parsePreis(parameters: MultivaluedMap<String, String>, key: String) =
      defaultOnBlank(parameters.getFirst(key), null) {
        Preis(it.toBigDecimal(), Currency.getInstance(Locale.GERMANY))
      }

    private fun parseProduktnummern(parameters: MultivaluedMap<String, String>) =
      defaultOnBlank(parameters.getFirst("produktNummern"), emptyList()) { produktNummernString ->
        produktNummernString.split(",").filter { it.isNotBlank() }.map { Produktnummer(it) }
      }

    private fun parseProduktnummernVerwendung(parameters: MultivaluedMap<String, String>) =
      defaultOnBlank(parameters.getFirst("produktnummernVerwendung"), ProduktnummernVerwendung.KEINE) {
        ProduktnummernVerwendung.vonString(it)
      }

    private fun parseLimit(parameters: MultivaluedMap<String, String>) =
      defaultOnBlank(parameters.getFirst("limit"), null) {
        it.toInt()
      }

    private fun parseFarben(parameters: MultivaluedMap<String, String>) =
      defaultOnBlank(parameters.getFirst("farben"), emptyList()) { farbenString ->
        farbenString.split(",").map { Produktfarbe(it) }
      }

    private fun parseBlumensorten(parameters: MultivaluedMap<String, String>) =
      defaultOnBlank(parameters.getFirst("blumensorten"), emptyList()) { blumensortenString ->
        blumensortenString.split(",").map { Blumensorte(it) }
      }

    private fun <T> defaultOnBlank(value: String?, default: T, valueTransformer: Function<String, T>): T =
      if (value.isNullOrBlank()) default else (valueTransformer.apply(value))
  }
}
