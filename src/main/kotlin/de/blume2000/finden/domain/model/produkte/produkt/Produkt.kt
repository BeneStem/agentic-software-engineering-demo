package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import mu.KLogging
import org.jmolecules.ddd.annotation.Entity

@Entity
data class Produkt(
  val nummer: Produktnummer,
  val name: Produktname,
  val preis: Preis,
  val streichpreis: Preis,
  val klassifikation: Klassifikation,
  val bildUrl: ProduktbildUrl,
  val pdsUrlSeoName: ProduktdetailseiteUrlSeoName,
  val farben: List<Produktfarbe>,
  val blumensorten: List<Blumensorte>,
  val verfügbarkeiten: List<ProduktVerfügbarkeit>,
  val beschreibung: Beschreibung? = null
) {
  init {
    if (!streichpreis.istGrößerAlsOderGleich(preis)) {
      throw StreichpreisIstNiedrigerAlsPreisException(
        "Bei Produktnummer ${nummer.asString()} ist der Streichpreis(${streichpreis.asString()}) niedriger als der Verkaufspreis(${preis.asString()})"
      )
    }
  }

  companion object : KLogging()

  fun fügeVerfügbarkeitHinzu(neueVerfügbarkeit: NeueVerfügbarkeit): Produkt {
    logger.info { "Füge Verfügbarkeit für Produkt '${neueVerfügbarkeit.produktnummer.asString()}' hinzu" }
    if (neueVerfügbarkeit.produktnummer.asString() != nummer.asString()) {
      logger.warn { "Die Produktnummer der neuen Verfügbarkeit (${neueVerfügbarkeit.produktnummer.asString()}) stimmt nicht überein (Sollte ${nummer.asString()} sein)" }
      return this.copy()
    }

    val restlicheVerfügbarkeiten = entferneVerfügbarkeitFürLiefertag(neueVerfügbarkeit.liefertag)

    return if (neueVerfügbarkeit.istVerfügbar()) {
      this.copy(verfügbarkeiten = restlicheVerfügbarkeiten.plus(neueVerfügbarkeit.zuProduktVerfügbarkeit()))
    } else {
      this.copy(verfügbarkeiten = restlicheVerfügbarkeiten)
    }
  }

  fun entferneAlteVerfügbarkeiten() =
    this.copy(verfügbarkeiten = verfügbarkeiten.filter { !it.bestellschluss.istAbgelaufen() })

  private fun entferneVerfügbarkeitFürLiefertag(liefertag: Liefertag) =
    verfügbarkeiten.filter { it.liefertag != liefertag }

  fun holeNächstmöglicheVerfügbarkeit() =
    sortiereVerfügbarkeitenNachBestellschluss().firstOrNull { !it.bestellschluss.istAbgelaufen() }

  fun holeLetztmöglicheVerfügbarkeit() =
    sortiereVerfügbarkeitenNachBestellschluss().lastOrNull { !it.bestellschluss.istAbgelaufen() }

  private fun sortiereVerfügbarkeitenNachBestellschluss() =
    verfügbarkeiten.sortedWith(compareBy({ it.liefertag.value }, { it.bestellschluss.value }))

  fun getProduktDetailseiteUrlPfad(): String {

    return "/p/" + pdsUrlSeoName.asString()
  }
}
