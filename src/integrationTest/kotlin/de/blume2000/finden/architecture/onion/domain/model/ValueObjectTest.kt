package de.blume2000.finden.architecture.onion.domain.model

import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import de.blume2000.finden.architecture.onion.OnionTest
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE
import org.jmolecules.ddd.annotation.ValueObject

@AnalyzeClasses(packages = [OnionTest.BOUNDED_CONTEXT_PACKAGE],
  importOptions = [ImportOption.DoNotIncludeTests::class, ImportOption.DoNotIncludeArchives::class])
class ValueObjectTest {

  companion object {
    const val VALUE = "value"
  }

  @ArchTest
  fun valueObjectClasses(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(ValueObject::class.java)
      .should().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .check(classes)

  @ArchTest
  fun valueObjectFields(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().areAnnotatedWith(ValueObject::class.java)
      .should().beDeclaredInClassesThat().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .andShould().haveName(VALUE)
      .andShould().beFinal()
      .andShould().bePrivate()
      .andShould().haveRawType(resideInAnyPackage("java..", "kotlin.."))
      .check(classes)
}
