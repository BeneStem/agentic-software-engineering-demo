package de.blume2000.finden.architecture.onion.adapter.passive

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeArchives
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.PASSIVE_ADAPTER_PACKAGE
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class ProducerTest {

  companion object {
    const val PRODUCER = "Producer"
    const val PRODUCE = "produce"
  }

  @ArchTest
  fun producerClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(PRODUCER)
      .should().resideInAPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun producerFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(PRODUCER)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)

  @ArchTest
  fun producerMethods(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areDeclaredInClassesThat().haveSimpleNameEndingWith(PRODUCER)
      .should().beDeclaredInClassesThat().resideInAPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(Outgoing::class.java)
      .andShould().haveName(PRODUCE)
      .check(classes)

  @ArchTest
  fun methodsAnnotatedWithOutgoing(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areAnnotatedWith(Outgoing::class.java)
      .should().beDeclaredInClassesThat().haveSimpleNameEndingWith(PRODUCER)
      .andShould().beDeclaredInClassesThat().resideInAPackage("$PASSIVE_ADAPTER_PACKAGE.")
      .andShould().beDeclaredInClassesThat().areAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveName(PRODUCE)
      .check(classes)
}
