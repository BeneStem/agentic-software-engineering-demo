package de.blume2000.finden.architecture.onion.adapter.active

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import de.blume2000.finden.architecture.onion.OnionTest.Companion.ACTIVE_ADAPTER_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.HEAD
import javax.ws.rs.OPTIONS
import javax.ws.rs.PATCH
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class ResourceTest {

  companion object {
    const val RESOURCE = "Resource"
  }

  @ArchTest
  fun notCallResourceClasses(classes: JavaClasses) =
    noClasses()
      .that().resideInAPackage("$BOUNDED_CONTEXT_PACKAGE.")
      .and().haveSimpleNameNotEndingWith(RESOURCE)
      .should().accessClassesThat().haveSimpleNameEndingWith(RESOURCE)
      .check(classes)

  @ArchTest
  fun resourceClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(RESOURCE)
      .should().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().haveOnlyFinalFields()
      .andShould().beAnnotatedWith(Path::class.java)
      .orShould().beAnnotatedWith(Produces::class.java)
      .orShould().beAnnotatedWith(Consumes::class.java)
      .check(classes)

  @ArchTest
  fun classesAnnotatedWithPathOrProduces(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(Path::class.java)
      .or().areAnnotatedWith(Produces::class.java)
      .or().areAnnotatedWith(Consumes::class.java)
      .should().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().haveSimpleNameEndingWith(RESOURCE)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun resourceFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(RESOURCE)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)

  @ArchTest
  fun resourceMethods(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areDeclaredInClassesThat().haveSimpleNameEndingWith(RESOURCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(GET::class.java)
      .orShould().beAnnotatedWith(HEAD::class.java)
      .orShould().beAnnotatedWith(OPTIONS::class.java)
      .orShould().beAnnotatedWith(POST::class.java)
      .orShould().beAnnotatedWith(PUT::class.java)
      .orShould().beAnnotatedWith(PATCH::class.java)
      .orShould().beAnnotatedWith(DELETE::class.java)
      .check(classes)

  @ArchTest
  fun methodsAnnotatedWithAMapping(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areAnnotatedWith(GET::class.java)
      .or().areAnnotatedWith(HEAD::class.java)
      .or().areAnnotatedWith(OPTIONS::class.java)
      .or().areAnnotatedWith(POST::class.java)
      .or().areAnnotatedWith(PUT::class.java)
      .or().areAnnotatedWith(PATCH::class.java)
      .or().areAnnotatedWith(DELETE::class.java)
      .should().beDeclaredInClassesThat().haveSimpleNameEndingWith(RESOURCE)
      .andShould().beDeclaredInClassesThat().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beDeclaredInClassesThat().areAnnotatedWith(Path::class.java)
      .check(classes)
}
