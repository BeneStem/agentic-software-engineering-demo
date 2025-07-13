package de.blume2000.finden.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class UtilLayerTest {

  companion object {
    const val UTIL = "Util"
  }

  @ArchTest
  fun utilClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(UTIL)
      .should().resideOutsideOfPackage("$BOUNDED_CONTEXT_PACKAGE.")
      .check(classes)
}
