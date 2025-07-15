package de.blume2000.finden.domain.service.produkte

import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import jakarta.enterprise.context.ApplicationScoped
import mu.KLogging
import org.jmolecules.ddd.annotation.Service

@ApplicationScoped
@Service
class ProduktImportDomainService(private val produktRepository: ProduktRepository) {

  companion object : KLogging()

  fun importiereProdukt(produkt: Produkt) {
    logger.info { "Importiere Produkt '${produkt.nummer.asString()}'" }

    val gespeichertesProdukt = produktRepository.ladeProdukt(produkt.nummer)
    val verfügbarkeiten = gespeichertesProdukt?.verfügbarkeiten ?: emptyList()
    produktRepository.speicherProdukt(produkt.copy(verfügbarkeiten = verfügbarkeiten))
  }

  fun entferneProdukt(nummer: Produktnummer) {
    logger.info { "Entferne Produkt '${nummer.asString()}'" }
    if (produktRepository.ladeProdukt(nummer) != null) {
      produktRepository.entferneProdukt(nummer)
    } else {
      logger.debug { "Produkt '${nummer.asString()}' ist bereits entfernt" }
    }
  }
}
