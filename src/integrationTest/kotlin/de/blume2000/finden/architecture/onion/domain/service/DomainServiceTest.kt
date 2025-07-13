package de.blume2000.finden.architecture.onion.domain.service

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_SERVICE_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import javax.enterprise.context.ApplicationScoped

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class DomainServiceTest {

  companion object {
    const val DOMAIN_SERVICE = "DomainService"
  }

  @ArchTest
  fun domainServiceSlices(classes: JavaClasses) =
    slices()
      .matching("$DOMAIN_SERVICE_PACKAGE.")
      .should().notDependOnEachOther()
      .check(classes)

  @ArchTest
  fun domainServiceClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(DOMAIN_SERVICE)
      .should().resideInAPackage("$DOMAIN_SERVICE_PACKAGE.")
      .andShould().beAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun classesAnnotatedWithApplicationScoped(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(ApplicationScoped::class.java)
      .and().resideInAPackage("$DOMAIN_SERVICE_PACKAGE.")
      .should().haveSimpleNameEndingWith(DOMAIN_SERVICE)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun domainServiceFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(DOMAIN_SERVICE)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$DOMAIN_SERVICE_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)
}
