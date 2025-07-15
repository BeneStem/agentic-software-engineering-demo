package de.blume2000.finden.architecture.onion.adapter.active

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
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import de.blume2000.finden.architecture.onion.OnionTest.Companion.ACTIVE_ADAPTER_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.BOUNDED_CONTEXT_PACKAGE
import de.blume2000.finden.architecture.onion.OnionTest.Companion.COMPANION
import de.blume2000.finden.architecture.onion.OnionTest.Companion.INSTANCE
import org.eclipse.microprofile.reactive.messaging.Incoming
import jakarta.enterprise.context.ApplicationScoped

@AnalyzeClasses(packages = [BOUNDED_CONTEXT_PACKAGE],
  importOptions = [DoNotIncludeTests::class, DoNotIncludeArchives::class])
class ConsumerTest {

  companion object {
    const val CONSUMER = "Consumer"
    const val CONSUME = "consume"
  }

  @ArchTest
  fun notCallConsumerClasses(classes: JavaClasses) =
    noClasses()
      .that().resideInAPackage("$BOUNDED_CONTEXT_PACKAGE.")
      .and().haveSimpleNameNotEndingWith(CONSUMER)
      .should().accessClassesThat().haveSimpleNameEndingWith(CONSUMER)
      .check(classes)

  @ArchTest
  fun consumerClasses(classes: JavaClasses) =
    classes()
      .that().haveSimpleNameEndingWith(CONSUMER)
      .should().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveOnlyFinalFields()
      .check(classes)

  @ArchTest
  fun consumerFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().haveSimpleNameEndingWith(CONSUMER)
      .and().doNotHaveName(COMPANION)
      .and().doNotHaveName(INSTANCE)
      .should().beDeclaredInClassesThat().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .check(classes)

  @ArchTest
  fun consumerMethods(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areDeclaredInClassesThat().haveSimpleNameEndingWith(CONSUMER)
      .should().beDeclaredInClassesThat().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beAnnotatedWith(Incoming::class.java)
      .andShould().haveName(CONSUME)
      .andShould(HaveParameterNamedMessagePayload())
      .check(classes)

  @ArchTest
  fun methodsAnnotatedWithIncoming(classes: JavaClasses) =
    methods()
      .that().arePublic()
      .and().areAnnotatedWith(Incoming::class.java)
      .should().beDeclaredInClassesThat().haveSimpleNameEndingWith(CONSUMER)
      .andShould().beDeclaredInClassesThat().resideInAPackage("$ACTIVE_ADAPTER_PACKAGE.")
      .andShould().beDeclaredInClassesThat().areAnnotatedWith(ApplicationScoped::class.java)
      .andShould().haveName(CONSUME)
      .andShould(HaveParameterNamedMessagePayload())
      .check(classes)

  private class HaveParameterNamedMessagePayload : ArchCondition<JavaMethod>("have a single parameter named 'MessagePayload'") {
    override fun check(method: JavaMethod, events: ConditionEvents) {
      if (method.rawParameterTypes.size < 1L || method.rawParameterTypes.stream().noneMatch { it.simpleName.endsWith("MessagePayload") }) {
        events.add(SimpleConditionEvent.violated(method, "${method.fullName}: does not have a single parameter that is named 'MessagePayload'"))
      }
    }
  }
}
