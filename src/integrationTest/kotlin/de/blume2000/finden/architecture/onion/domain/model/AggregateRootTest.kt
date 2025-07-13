package de.blume2000.finden.architecture.onion.domain.model

import com.tngtech.archunit.base.DescribedPredicate.not
import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage
import com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields
import de.blume2000.finden.architecture.onion.OnionTest
import de.blume2000.finden.architecture.onion.OnionTest.Companion.DOMAIN_MODEL_PACKAGE
import de.blume2000.finden.architecture.onion.domain.model.CommandTest.Companion.COMMAND
import de.blume2000.finden.architecture.onion.domain.model.EventTest.Companion.EVENT
import de.blume2000.finden.architecture.onion.domain.model.RepositoryTest.Companion.REPOSITORY
import org.jmolecules.ddd.annotation.AggregateRoot

@AnalyzeClasses(packages = [OnionTest.BOUNDED_CONTEXT_PACKAGE],
  importOptions = [ImportOption.DoNotIncludeTests::class, ImportOption.DoNotIncludeArchives::class])
class AggregateRootTest {

  @ArchTest
  fun aggregateRootClasses(classes: JavaClasses) =
    classes()
      .that().areAnnotatedWith(AggregateRoot::class.java)
      .should().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .check(classes)

  @ArchTest
  fun aggregateRootField(classes: JavaClasses) =
    fields()
      .that().areDeclaredInClassesThat().areAnnotatedWith(AggregateRoot::class.java)
      .should().beDeclaredInClassesThat().resideInAPackage("$DOMAIN_MODEL_PACKAGE.")
      .andShould().beFinal()
      .andShould().bePrivate()
      .andShould().haveRawType(resideInAPackage("$DOMAIN_MODEL_PACKAGE.").and(not(simpleNameEndingWith(REPOSITORY))
        .and(not(simpleNameEndingWith(COMMAND)))
        .and(not(simpleNameEndingWith(EVENT)))))
      .check(classes)
}
