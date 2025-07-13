package de.blume2000.finden.domain.model.produkte.produkt.preis

import de.blume2000.finden.domain.model.produkte.ProdukteException

class NegativerPreisException(fehlermeldung: String) : ProdukteException(fehlermeldung)
