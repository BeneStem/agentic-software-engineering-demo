package de.blume2000.finden.architecture.onion

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures.onionArchitecture
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class OnionTest {

  companion object {
    const val BOUNDED_CONTEXT_PACKAGE = "de.blume2000.finden."
    const val DOMAIN_PACKAGE = "${BOUNDED_CONTEXT_PACKAGE}domain."
    const val DOMAIN_MODEL_PACKAGE = "${DOMAIN_PACKAGE}model."
    const val DOMAIN_SERVICE_PACKAGE = "${DOMAIN_PACKAGE}service."
    const val APPLICATION_SERVICE_PACKAGE = "${BOUNDED_CONTEXT_PACKAGE}application."
    const val ADAPTER_PACKAGE = "${BOUNDED_CONTEXT_PACKAGE}adapter."
    const val ACTIVE_ADAPTER_PACKAGE = "${ADAPTER_PACKAGE}active."
    const val PASSIVE_ADAPTER_PACKAGE = "${ADAPTER_PACKAGE}passive."

    const val COMPANION = "Companion"
    const val INSTANCE = "INSTANCE"
  }

  @ArchTest
  fun onionArchitecture(classes: JavaClasses) =
    onionArchitecture()
      .domainModels("$DOMAIN_MODEL_PACKAGE.")
      .domainServices("$DOMAIN_SERVICE_PACKAGE.")
      .applicationServices("$APPLICATION_SERVICE_PACKAGE.")
      .adapter("active", "$ACTIVE_ADAPTER_PACKAGE.")
      .adapter("passive", "$PASSIVE_ADAPTER_PACKAGE.")
      .check(classes)
}
