package de.blume2000.finden.architecture.onion.adapter.passive

import com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableFrom
import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage
import com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.PASSIVE_ADAPTER_PACKAGE
import de.blume2000.finden.architecture.onion.domain.model.RepositoryTest.Companion.REPOSITORY
import jakarta.enterprise.context.ApplicationScoped

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class RepositoryTest {

  @ArchTest
  fun repositoryClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(REPOSITORY)
      .and().resideOutsideOfPackage("$DOMAIN_MODEL_PACKAGE.")
      .should().resideInAPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .andShould().implement(assignableFrom(resideInAPackage("$DOMAIN_MODEL_PACKAGE.").and(simpleNameEndingWith(REPOSITORY))))
      .andShould().beAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun repositoryFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(REPOSITORY)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)
}
