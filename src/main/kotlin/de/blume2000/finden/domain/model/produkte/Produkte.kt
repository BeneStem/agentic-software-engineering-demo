package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.adapter.active.api.produkte.dtos.ProduktDTO
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import org.jmolecules.ddd.annotation.AggregateRoot

@AggregateRoot
data class Produkte(private val produkte: List<Produkt>) {
  fun gebeMirAlleProdukte(): List<Produkt> = produkte

  fun zuValideProduktDTOs(): List<ProduktDTO> {
    return produkte.mapNotNull { ProduktDTO.vonProdukt(it) }
  }

  fun sortiere(comperator: Comparator<Produkt>): Produkte {
    return Produkte(produkte.sortedWith(comperator))
  }

  /**
   * @param n Wenn null -> keine beschränkung
   * @return Ein Produkte-Objekt welches höchstens n Produkte repräsentiert
   */
  fun begrenzeAufNEinträge(n: Int?): Produkte {
    return if (n != null && n <= produkte.size) {
      Produkte(produkte.subList(0, n))
    } else {
      this
    }
  }
}
