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
import de.blume2000.util.NoArg
import org.jetbrains.annotations.Nullable

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class RequestBodyTest {

  companion object {
    const val REQUEST_BODY = "RequestBody"
  }

  @ArchTest
  fun requestBodyClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(REQUEST_BODY)
      .should().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().onlyBeAccessed().byClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(NoArg::class.java)
      .andShould().bePublic()
      .check(classes)

  @ArchTest
  fun requestBodyFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(REQUEST_BODY)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ADAPTER_PACKAGE.")
      .andShould().notBeFinal()
      .andShould().beAnnotatedWith(Nullable::class.java)
      .check(classes)
}
