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
import javax.enterprise.inject.Produces
import javax.inject.Singleton

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class FactoryTest {

  companion object {
    const val FACTORY = "Factory"
  }

  @ArchTest
  fun notCallFactoryClasses(classes: JavaClasses) =
    noClasses()
      .that().resideInAPackage("$BOUNDED_CONTEXT_PACKAGE.")
      .and().haveSimpleNameNotEndingWith(FACTORY)
      .should().accessClassesThat().haveSimpleNameEndingWith(FACTORY)
      .check(classes)

  @ArchTest
  fun factoryClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(FACTORY)
      .should().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(Dependent::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun factoryFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(FACTORY)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)

  @ArchTest
  fun factoryMethods(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areAnnotatedWith(Singleton::class.java)
      .or().areAnnotatedWith(Produces::class.java)
      .should().beDeclaredInClassesThat().haveSimpleNameEndingWith(FACTORY)
      .andShould().beDeclaredInClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beDeclaredInClassesThat().areAnnotatedWith(Dependent::class.java)
      .check(classes)
}
