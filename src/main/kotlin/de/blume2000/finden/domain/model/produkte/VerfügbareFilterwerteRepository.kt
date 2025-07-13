package de.blume2000.finden.domain.model.produkte

import de.blume2000.finden.domain.model.produkte.verfuegbareFilterwerte.VerfügbareFilterwerte

interface VerfügbareFilterwerteRepository {
  fun ladeVerfügbareFilterWerte(
    cmsProdukteFilter: CmsProdukteFilter,
    userProdukteFilter: UserProdukteFilter
  ): VerfügbareFilterwerte
}
