package de.blume2000.finden.architecture.onion.domain

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_PACKAGE
import de.blume2000.finden.architecture.onion.domain.model.RepositoryTest.Companion.REPOSITORY

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class DomainTest {

  @ArchTest
  fun domainClassesBlutgruppe0(classes: JavaClasses) =
    classes()
      .that().resideInAPackage("$DOMAIN_PACKAGE.")
      .and().haveSimpleNameNotEndingWith(REPOSITORY)
      .should().onlyDependOnClassesThat().resideInAnyPackage(
        "$DOMAIN_PACKAGE.",
        "java..",
        "kotlin..",
        "org.jetbrains..",

        "org.jmolecules.ddd.annotation..",

        // WARNING do we want this?
        "javax..",
        "org.hibernate.validator.."
      )
      .check(classes)

  @ArchTest
  fun repositoryClassesBlutgruppe0(classes: JavaClasses) =
    classes()
      .that().resideInAPackage("$DOMAIN_PACKAGE.")
      .and().haveSimpleNameEndingWith(REPOSITORY)
      .should().onlyDependOnClassesThat().resideInAnyPackage(
        "$DOMAIN_PACKAGE.",
        "java..",
        "kotlin..",
        "org.jetbrains..",

        "org.jmolecules.ddd.annotation..",

        // WARNING do we want this?
        "javax..",
        "org.hibernate.validator..",

        "io.smallrye.mutiny.."
      )
      .check(classes)
}
