package de.blume2000.finden.architecture.onion.application

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaMethod
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import de.blume2000.finden.architecture.onion.OnionTest.Companion.APPLICATION_SERVICE_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class ApplicationServiceTest {

  companion object {
    const val APPLICATION_SERVICE = "ApplicationService"
    const val EXECUTE = "execute"
  }

  @ArchTest
  fun applicationServiceSlices(classes: JavaClasses) =
    slices()
      .matching("$APPLICATION_SERVICE_PACKAGE.")
      .should().notDependOnEachOther()
      .check(classes)

  @ArchTest
  fun applicationServiceClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(APPLICATION_SERVICE)
      .should().resideInAPackage("$APPLICATION_SERVICE_PACKAGE.")
      .andShould().beAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun classesAnnotatedWithApplicationScoped(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(ApplicationScoped::class.java)
      .and().resideInAPackage("$APPLICATION_SERVICE_PACKAGE.")
      .should().haveSimpleNameEndingWith(APPLICATION_SERVICE)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun applicationServiceFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(APPLICATION_SERVICE)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$APPLICATION_SERVICE_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)

  @ArchTest
  fun applicationServiceMethods(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areDeclaredInClassesThat().haveSimpleNameEndingWith(APPLICATION_SERVICE)
      .should().beDeclaredInClassesThat().resideInAPackage("$APPLICATION_SERVICE_PACKAGE.")
      .andShould().beAnnotatedWith(Transactional::class.java)
      .andShould().haveName(EXECUTE)
      .andShould(HaveParameterNamedCommand())
      .check(classes)

  @ArchTest
  fun methodsAnnotatedWithTransactional(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areAnnotatedWith(Transactional::class.java)
      .should().beDeclaredInClassesThat().haveSimpleNameEndingWith(APPLICATION_SERVICE)
      .andShould().beDeclaredInClassesThat().resideInAPackage("$APPLICATION_SERVICE_PACKAGE.")
      .andShould().haveName(EXECUTE)
      .andShould(HaveParameterNamedCommand())
      .check(classes)

  private class HaveParameterNamedCommand : ArchCondition<JavaMethod>("have a single parameter named 'Command'") {
    override fun check(method: JavaMethod, events: ConditionEvents) {
      if (method.rawParameterTypes.size < 1L || method.rawParameterTypes.stream().noneMatch { it.simpleName.endsWith("Command") }) {
        events.add(SimpleConditionEvent.violated(method, "${method.fullName}: does not have a single parameter that is named 'Command'"))
      }
    }
  }
}
