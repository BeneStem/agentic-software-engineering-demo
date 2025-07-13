package de.blume2000.finden.adapter.active.api.tools.produktliste

import de.blume2000.finden.application.ProduktlistenApplicationService
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import de.blume2000.finden.domain.model.produkte.ProduktnummernVerwendung
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.util.sanitize
import mu.KLogging
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("api/finden/tool/produktliste")
class ProduktlisteResource(val produktlistenApplicationService: ProduktlistenApplicationService) {

  companion object : KLogging()

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  fun entferneNichtVerfügbareProduktnummern(produktnummern: List<String>?): List<String> {
    if (produktnummern == null || produktnummern.isEmpty()) {
      return listOf()
    }

    logger.info { "Client request: $produktnummern" }

    val sanitizedProduktnummern =
      produktnummern.filter { it.isNotBlank() }.map { it.sanitize() }.filter { it.matches("[0-9]+".toRegex()) }
    logger.info { "Filtering for: $sanitizedProduktnummern" }

    return when {
      sanitizedProduktnummern.isEmpty() -> listOf()
      else -> {
        val verfügbareProdukte = produktlistenApplicationService.gebeMirAlleVerfuegbarenProdukte(
          CmsProdukteFilter(
            listOf(),
            produktNummern = sanitizedProduktnummern.map { Produktnummer(it) },
            produktnummernVerwendung = ProduktnummernVerwendung.SELEKTIONSBASIS,
            limit = null,
            maxPreis = null,
            minPreis = null,
          ), ProdukteSortierung()
        )
        verfügbareProdukte.map { it.nummer }
      }
    }
  }
}
