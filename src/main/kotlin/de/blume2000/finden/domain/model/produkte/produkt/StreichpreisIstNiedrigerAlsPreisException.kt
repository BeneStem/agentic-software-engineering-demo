package de.blume2000.finden.domain.model.produkte.produkt

import de.blume2000.finden.domain.model.produkte.ProdukteException

class StreichpreisIstNiedrigerAlsPreisException(fehlermeldung: String) : ProdukteException(fehlermeldung)
