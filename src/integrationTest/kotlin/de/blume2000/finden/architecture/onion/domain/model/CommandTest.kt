package de.blume2000.finden.architecture.onion.domain.model

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import de.blume2000.finden.architecture.onion.OnionTest
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE

@AnalyzeClasses(packages = [OnionTest.BOUNDED_CONTEXT_PACKAGE],
  importOptions = [ImportOption.DoNotIncludeTests::class, ImportOption.DoNotIncludeArchives::class])
class CommandTest {

  companion object {
    const val COMMAND = "Command"
  }

  @ArchTest
  fun commandClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(COMMAND)
      .should().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .check(classes)

  @ArchTest
  fun commandFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(COMMAND)
      .and().doNotHaveName(OnionTest.COMPANION)
      .and().doNotHaveName(OnionTest.INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)
}
