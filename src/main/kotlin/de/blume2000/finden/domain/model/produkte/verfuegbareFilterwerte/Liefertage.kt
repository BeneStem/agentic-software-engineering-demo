package de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte

import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import org.jmolecules.ddd.annotation.Entity

@Entity
data class Liefertage(
  val verfügbareLiefertage: List<Liefertag>
)
