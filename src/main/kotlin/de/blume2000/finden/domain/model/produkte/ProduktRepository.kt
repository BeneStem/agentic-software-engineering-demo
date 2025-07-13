package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer

interface ProduktRepository {
  fun ladeVerfügbareProdukte(
    cmsProdukteFilter: CmsProdukteFilter,
    userProdukteFilter: UserProdukteFilter,
    produkteSortierung: ProdukteSortierung,
  ): Produkte

  fun ladeProdukt(nummer: Produktnummer): Produkt?
  fun speicherProdukt(produkt: Produkt)
  fun entferneProdukt(nummer: Produktnummer)
  fun zähleProdukte(): Long
}
