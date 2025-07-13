package de.blume2000.finden.architecture.onion.adapter

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import de.blume2000.finden.architecture.onion.OnionTest.Companion.ADAPTER_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import javax.enterprise.context.Dependent

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class AugmentorTest {

  companion object {
    const val AUGMENTOR = "Augmentor"
    const val STARTUP = "startup"
    const val SHUTDOWN = "startup"
  }

  @ArchTest
  fun notCallAugmentorClasses(classes: JavaClasses) =
    noClasses()
      .that().resideInAPackage("$BOUNDED_CONTEXT_PACKAGE.")
      .and().haveSimpleNameEndingWith(AUGMENTOR)
      .should().accessClassesThat().haveSimpleNameEndingWith(AUGMENTOR)
      .check(classes)

  @ArchTest
  fun augmentorClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(AUGMENTOR)
      .should().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(Dependent::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun augmentorFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(AUGMENTOR)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)

  @ArchTest
  fun augmentorMethods(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areDeclaredInClassesThat().haveSimpleNameEndingWith(AUGMENTOR)
      .should().beDeclaredInClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().haveName(STARTUP)
      .orShould().haveName(SHUTDOWN)
      .check(classes)
}
