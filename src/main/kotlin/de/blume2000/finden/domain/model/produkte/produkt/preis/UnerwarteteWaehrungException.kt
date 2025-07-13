package de.blume2000.finden.domain.model.produkte.produkt.preis

import de.blume2000.finden.domain.model.produkte.ProdukteException

class UnerwarteteWaehrungException(fehlermeldung: String) : ProdukteException(fehlermeldung)
