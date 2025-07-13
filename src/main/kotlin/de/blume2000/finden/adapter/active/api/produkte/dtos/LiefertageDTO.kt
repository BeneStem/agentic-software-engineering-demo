package de.blume2000.finden.adapter.active.api.produkte.dtos

import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.Liefertage
import de.blume2000.util.ListPattern
import java.time.LocalDate

data class LiefertageDTO(
  @field:ListPattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
  val verfuegbareLiefertage: List<String>
) {
  companion object {
    fun vonLiefertage(liefertage: Liefertage) =
      LiefertageDTO(verfuegbareLiefertage = liefertage.verfügbareLiefertage.map { it.toISOString() })
  }

  fun zuLiefertage() = Liefertage(verfügbareLiefertage = verfuegbareLiefertage.map { Liefertag(LocalDate.parse(it)) })
}
