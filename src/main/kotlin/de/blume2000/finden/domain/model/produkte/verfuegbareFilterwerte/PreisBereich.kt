package de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte

import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import org.jmolecules.ddd.annotation.Entity

@Entity
data class PreisBereich(
  val minPreis: Preis?,
  val maxPreis: Preis?
)
