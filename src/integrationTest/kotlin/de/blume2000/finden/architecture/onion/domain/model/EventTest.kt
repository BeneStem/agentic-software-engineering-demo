package de.blume2000.finden.architecture.onion.domain.model

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import de.blume2000.finden.architecture.onion.OnionTest
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE

@AnalyzeClasses(packages = [OnionTest.BOUNDED_CONTEXT_PACKAGE],
  importOptions = [ImportOption.DoNotIncludeTests::class, ImportOption.DoNotIncludeArchives::class])
class EventTest {

  companion object {
    const val EVENT = "Event"
  }

  @ArchTest
  fun eventClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(EVENT)
      .should().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .check(classes)

  @ArchTest
  fun eventFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(EVENT)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)
}
