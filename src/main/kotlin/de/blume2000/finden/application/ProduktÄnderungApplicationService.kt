package de.blume2000.finden.application

import de.blume2000.avro.produkte.AvroProdukt
import de.blume2000.finden.domain.model.produkte.ProduktRepository
import de.blume2000.finden.domain.model.produkte.produkt.Blumensorte
import de.blume2000.finden.domain.model.produkte.produkt.Klassifikation
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationId
import de.blume2000.finden.domain.model.produkte.produkt.KlassifikationName
import de.blume2000.finden.domain.model.produkte.produkt.Produkt
import de.blume2000.finden.domain.model.produkte.produkt.ProduktVerfügbarkeit
import de.blume2000.finden.domain.model.produkte.produkt.ProduktbildUrl
import de.blume2000.finden.domain.model.produkte.produkt.ProduktdetailseiteUrlSeoName
import de.blume2000.finden.domain.model.produkte.produkt.Produktfarbe
import de.blume2000.finden.domain.model.produkte.produkt.Produktname
import de.blume2000.finden.domain.model.produkte.produkt.Produktnummer
import de.blume2000.finden.domain.model.produkte.produkt.preis.Preis
import mu.KLogging
import java.math.BigDecimal
import java.util.Currency
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ProduktÄnderungApplicationService(
  private val produktRepository: ProduktRepository
) {

  companion object : KLogging()

  fun importiereNeueProduktDaten(avroProdukt: AvroProdukt) {
    logger.info { "Importiere Produkt '${avroProdukt.getProduktNummer()}'" }
    val gespeichertesProdukt = produktRepository.ladeProdukt(Produktnummer(avroProdukt.getProduktNummer()))
    val verfügbarkeiten = gespeichertesProdukt?.verfügbarkeiten ?: emptyList()
    produktRepository.speicherProdukt(erstelleDomainProdukt(avroProdukt, verfügbarkeiten))
  }

  fun entferneProdukt(avroProdukt: AvroProdukt) {
    val produktnummer = Produktnummer(avroProdukt.getProduktNummer())
    logger.info { "Entferne Produkt '${produktnummer.asString()}'" }
    produktRepository.ladeProdukt(produktnummer)?.let {
      produktRepository.entferneProdukt(produktnummer)
    } ?: logger.debug { "Produkt '${produktnummer.asString()}' ist bereits entfernt" }
  }

  private fun erstelleDomainProdukt(avroProdukt: AvroProdukt, verfügbarkeiten: List<ProduktVerfügbarkeit>): Produkt {
    val uvp = avroProdukt.getUvp()
    val streichpreis: Preis = when (uvp != null) {
      true -> Preis(BigDecimal(uvp.getBruttoBetrag()), Currency.getInstance(uvp.getWaehrung()))
      else -> Preis(
        BigDecimal(avroProdukt.getPreis().getBruttoBetrag()),
        Currency.getInstance(avroProdukt.getPreis().getWaehrung())
      )
    }

    return Produkt(
      nummer = Produktnummer(avroProdukt.getProduktNummer()),
      name = Produktname(avroProdukt.getProduktNameShops()),
      preis = Preis(
        BigDecimal(avroProdukt.getPreis().getBruttoBetrag()),
        Currency.getInstance(avroProdukt.getPreis().getWaehrung())
      ),
      streichpreis = streichpreis,
      klassifikation = Klassifikation(
        KlassifikationId(avroProdukt.getKlassifikation3().getId()),
        KlassifikationName(avroProdukt.getKlassifikation3().getName())
      ),
      bildUrl = ProduktbildUrl(avroProdukt.getProduktBild().getBaseLink()),
      pdsUrlSeoName = ProduktdetailseiteUrlSeoName(avroProdukt.urlName),
      farben = avroProdukt.getArtikelFarben().map { Produktfarbe(it.lowercase()) },
      blumensorten = avroProdukt.getBlumenSorten().map { Blumensorte(it) },
      verfügbarkeiten = verfügbarkeiten
    )
  }
}
