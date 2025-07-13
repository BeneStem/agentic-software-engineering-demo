package de.blume2000.finden.architecture.onion.adapter

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import de.blume2000.finden.architecture.onion.OnionTest.Companion.ADAPTER_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class AdapterTest {

  @ArchTest
  fun adapterSlices(classes: JavaClasses) =
    slices()
      .matching("$ADAPTER_PACKAGE.")
      .should().notDependOnEachOther()
      .check(classes)
}
