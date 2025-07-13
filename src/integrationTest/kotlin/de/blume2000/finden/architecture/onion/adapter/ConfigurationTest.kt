package de.blume2000.finden.architecture.onion.adapter

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import de.blume2000.finden.architecture.onion.OnionTest.Companion.ADAPTER_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import io.quarkus.arc.config.ConfigProperties

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class ConfigurationTest {

  companion object {
    const val CONFIGURATION = "Configuration"
  }

  @ArchTest
  fun configurationClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(CONFIGURATION)
      .should().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(ConfigProperties::class.java)
      .check(classes)

  @ArchTest
  fun classesAnnotatedWithConfigProperties(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(ConfigProperties::class.java)
      .should().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().haveSimpleNameEndingWith(CONFIGURATION)
      .check(classes)

  @ArchTest
  fun configurationFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(CONFIGURATION)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().notBeFinal()
      .check(classes)
}
