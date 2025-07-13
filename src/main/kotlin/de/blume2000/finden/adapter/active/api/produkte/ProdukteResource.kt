package de.blume2000.finden.adapter.active.api.produkte

import de.blume2000.finden.adapter.active.api.produkte.dtos.CmsProdukteFilterDTO
import de.blume2000.finden.adapter.active.api.produkte.dtos.ProdukteFilterDTO
import de.blume2000.finden.application.ProduktlistenApplicationService
import de.blume2000.finden.domain.model.produkte.CmsProdukteFilter
import de.blume2000.finden.domain.model.produkte.ProdukteSortierung
import io.micrometer.core.annotation.Counted
import io.micrometer.core.annotation.Timed
import javax.validation.Validator
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.UriInfo
import mu.KLogging

enum class ValidationStatus {
  OK,
  BAD_REQUEST,
}

@Path("/api/finden/produkte")
class ProdukteResource(
  private val produktlistenApplicationService: ProduktlistenApplicationService,
  private val validator: Validator
) {

  companion object : KLogging()

  // TODO remove get endpoint when the frontend using the post endpoint is deployed
  @GET
  @Produces("application/json")
  @Counted(value = "http/findenProdukteApi/requests/count")
  @Timed(value = "http/findenProdukteApi/requests/timer", percentiles = [0.50, 0.95, 0.99])
  @Deprecated("Use the Post endpoint")
  fun holeProdukte(@Context uriInfo: UriInfo) = produktlistenApplicationService.gebeMirAlleVerfuegbarenProdukte(
    cmsProdukteFilter = CmsProdukteFilter.vonQueryParameters(uriInfo.queryParameters),
    produkteSortierung = ProdukteSortierung()
  )

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  @Counted(value = "http/holeProdukte/requests/count")
  @Timed(value = "http/holeProdukte/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeProdukte(cmsProdukteFilterDTO: CmsProdukteFilterDTO) =
    when (validate(cmsProdukteFilterDTO, "holeProdukte")) {
      ValidationStatus.BAD_REQUEST -> Response.status(Status.BAD_REQUEST).build()
      ValidationStatus.OK -> Response.ok(
        produktlistenApplicationService.gebeMirAlleVerfuegbarenProdukte(
          cmsProdukteFilter = cmsProdukteFilterDTO.zuProdukteFilter(),
          produkteSortierung = ProdukteSortierung()
        )
      ).build()
    }

  @POST
  @Path("/mituserfilterung")
  @Consumes("application/json")
  @Produces("application/json")
  @Counted(value = "http/holeProdukteMitVerfuegbarenFilterwerten/requests/count")
  @Timed(value = "http/holeProdukteMitVerfuegbarenFilterwerten/requests/timer", percentiles = [0.50, 0.95, 0.99])
  fun holeProdukteMitVerfügbarenFilterwerten(produkteFilterDTO: ProdukteFilterDTO) =
    when (validate(produkteFilterDTO, "holeProdukteMitVerfügbarenFilterwerten")) {
      ValidationStatus.BAD_REQUEST -> Response.status(Status.BAD_REQUEST).build()
      ValidationStatus.OK -> Response.ok(
        produktlistenApplicationService.gebeMirAlleVerfuegbarenProdukteUndFilterwerte(
          produkteFilterDTO
        )
      ).build()
    }

  fun <FilterDTO> validate(dto: FilterDTO, endpunkt: String): ValidationStatus {
    val violations = validator.validate(dto)
    return if (violations.isNotEmpty()) {
      violations.forEach { logger.warn { "[HTTP 400 - $endpunkt] ${it.propertyPath} ${it.message}: '${it.invalidValue}'" } }
      ValidationStatus.BAD_REQUEST
    } else {
      ValidationStatus.OK
    }
  }
}
