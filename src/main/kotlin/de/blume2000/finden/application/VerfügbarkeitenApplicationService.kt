package de.blume2000.finden.application

import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.NeueVerfügbarkeit
import javax.enterprise.context.ApplicationScoped
import mu.KLogging

@ApplicationScoped
class VerfügbarkeitenApplicationService(private val produktRepository: ProduktRepository) {

  companion object : KLogging()

  fun importiereNeueVerfügbarkeit(neueVerfügbarkeit: NeueVerfügbarkeit) {
    val produktnummer = neueVerfügbarkeit.produktnummer.asString()
    produktRepository.ladeProdukt(Produktnummer(produktnummer))?.let {
      produktRepository.speicherProdukt(
        it.fügeVerfügbarkeitHinzu(neueVerfügbarkeit).entferneAlteVerfügbarkeiten()
      )
    } ?: logger.info { "Produkt $produktnummer existiert nicht in der DB. Verfügbarkeit wird nicht hinzugefügt." }
  }
}
