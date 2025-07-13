package de.blume2000.finden.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class DependencyRulesTest {

  @ArchTest
  fun boundedContextSlices(classes: JavaClasses) =
    slices()
      .matching("$BOUNDED_CONTEXT_PACKAGE.")
      .should().beFreeOfCycles()
      .check(classes)

  @ArchTest
  fun noClassesShouldDependUpperPackages(classes: JavaClasses) = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES.check(classes)
}
