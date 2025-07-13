package de.blume2000.finden.adapter.active.api.produkte.dtos

data class ProdukteMitVerfügbarenFilterwertenDTO(
  val produkte: List<ProduktDTO>,
  val verfuegbareFilterwerte: VerfügbareFilterwerteDTO?
)
