package de.blume2000.finden.domain.model.produkte.produkt.preis

import de.blume2000.finden.domain.model.produkte.ProdukteException

class UnerwarteteWährungException(fehlermeldung: String) : ProdukteException(fehlermeldung)
