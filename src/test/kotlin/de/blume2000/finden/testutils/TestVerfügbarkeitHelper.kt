package de.blume2000.finden.testutils

import de.blume2000.finden.domain.model.produkte.produkt.Bestellschluss
import de.blume2000.finden.domain.model.produkte.produkt.Liefertag
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.util.DateTimeUtil
import java.time.LocalDate

fun erstelleProduktVerfügbarkeit(
  liefertag: Liefertag = Liefertag(LocalDate.now().plusDays(2)),
  bestellschluss: Bestellschluss = Bestellschluss(DateTimeUtil.erstelleUTCNow().plusDays(1))
) = ProduktVerfügbarkeit(
  liefertag = liefertag,
  bestellschluss = bestellschluss
)
