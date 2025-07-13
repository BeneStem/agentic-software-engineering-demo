package de.blume2000.finden.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING
import com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class GeneralCodingRulesTest {

  @ArchTest
  fun noClassesShouldAccessStandardStreams(classes: JavaClasses) = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.check(classes)

  @ArchTest
  fun noClassesShouldThrowGenericExceptions(classes: JavaClasses) = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.check(classes)

  @ArchTest
  fun noClassesShouldUseStandardLogging(classes: JavaClasses) = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING.check(classes)

  @ArchTest
  fun noClassesShouldUseJodaTime(classes: JavaClasses) = NO_CLASSES_SHOULD_USE_JODATIME.check(classes)

  @ArchTest
  fun noClassesShouldUseFieldInjection(classes: JavaClasses) = NO_CLASSES_SHOULD_USE_FIELD_INJECTION.check(classes)

  @ArchTest
  fun noClassesShouldBeSuffixedWithImpl(classes: JavaClasses) =
    noClasses()
      .should().haveSimpleNameEndingWith("Impl")
      .because("seriously, you can do better than that")
      .check(classes)
}
