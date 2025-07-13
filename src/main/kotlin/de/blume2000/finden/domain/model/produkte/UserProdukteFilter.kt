package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.Liefertage
import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.PreisBereich

data class UserProdukteFilter(
  val klassifikationen: List<Klassifikation>,
  val preisBereich: PreisBereich?,
  val farben: List<Produktfarbe>,
  val blumensorten: List<Blumensorte>,
  val liefertage: Liefertage,
) {
  companion object {
    /**
     * Ein Filter der keine filternde Einschränkung erzeugt
     */
    val NEUTRALER_FILTER =
      UserProdukteFilter(
        klassifikationen = emptyList(),
        preisBereich = null,
        farben = emptyList(),
        blumensorten = emptyList(),
        liefertage = Liefertage(verfügbareLiefertage = emptyList())
      )
  }
}
