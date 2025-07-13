package de.blume2000.finden.architecture.onion.domain.model

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import de.blume2000.finden.architecture.onion.OnionTest
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.PASSIVE_ADAPTER_PACKAGE
import org.jmolecules.ddd.annotation.Repository

@AnalyzeClasses(packages = [OnionTest.BOUNDED_CONTEXT_PACKAGE],
  importOptions = [ImportOption.DoNotIncludeTests::class, ImportOption.DoNotIncludeArchives::class])
class RepositoryTest {

  companion object {
    const val REPOSITORY = "Repository"
  }

  @ArchTest
  fun repositoryClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(REPOSITORY)
      .and().resideOutsideOfPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .should().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .andShould().beAnnotatedWith(Repository::class.java)
      .andShould().beInterfaces()
      .check(classes)

  @ArchTest
  fun classesAnnotatedWithRepository(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(Repository::class.java)
      .should().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .andShould().haveSimpleNameEndingWith(REPOSITORY)
      .andShould().beInterfaces()
      .check(classes)
}
