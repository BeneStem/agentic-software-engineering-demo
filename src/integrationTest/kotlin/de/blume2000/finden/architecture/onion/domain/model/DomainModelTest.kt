package de.blume2000.finden.architecture.onion.domain.model

import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.properties.HasType.Predicates.rawType
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE
import de.blume2000.finden.architecture.onion.domain.model.CommandTest.Companion.COMMAND
import de.blume2000.finden.architecture.onion.domain.model.EventTest.Companion.EVENT

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class DomainModelTest {

  @ArchTest
  fun dddClasses(classes: JavaClasses) =
    classes()
      .that().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .and().haveSimpleNameNotEndingWith(COMMAND)
      .and().haveSimpleNameNotEndingWith(EVENT)
      .should().beAnnotatedWith(rawType(resideInAPackage("org.jmolecules.ddd.annotation")))
      .check(classes)
}
