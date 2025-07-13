package de.blume2000.finden.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.context.Dependent
import javax.inject.Singleton

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class DependencyInjectionRulesTest {

  @ArchTest
  fun singletons(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(ApplicationScoped::class.java)
      .or().areAnnotatedWith(Singleton::class.java)
      .or().areAnnotatedWith(Dependent::class.java)
      .should().haveOnlyFinalFields()
      .check(classes)
}
