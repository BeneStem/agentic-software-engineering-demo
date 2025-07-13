package de.blume2000.finden.domain.model.produkte

enum class ProduktnummernVerwendung {
  KEINE, AUSSCHLUSSLISTE, SELEKTIONSBASIS;

  companion object {
    fun vonString(storyBlokValue: String) = when (storyBlokValue) {
      "ausblenden" -> AUSSCHLUSSLISTE
      "selektionsbasis" -> SELEKTIONSBASIS
      else -> KEINE
    }
  }
}
