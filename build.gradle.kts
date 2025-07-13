import com.github.benmanes.gradle.versions.reporter.PlainTextReporter
import com.github.benmanes.gradle.versions.reporter.result.Result
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import groovy.json.JsonSlurper
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.owasp.dependencycheck.gradle.extension.AnalyzerExtension
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

group = "de.blume2000"
version = System.getenv("QUARKUS_APPLICATION_VERSION") ?: "local"

plugins {
  idea
  kotlin("jvm") version "1.6.21"
  kotlin("plugin.allopen") version "1.6.21"
  kotlin("plugin.noarg") version "1.6.21"
  id("io.quarkus") version "2.6.3.Final"

  jacoco

  id("io.gitlab.arturbosch.detekt") version "1.20.0"

  id("org.owasp.dependencycheck") version "7.1.1"

  id("com.github.ben-manes.versions") version "0.42.0"

  id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0"
}

repositories {
  maven(url = "https://packages.confluent.io/maven/")
  mavenCentral()
}

dependencies {
  implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:2.6.3.Final"))

  implementation("io.quarkus:quarkus-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

  implementation("org.jmolecules:jmolecules-ddd:1.5.0")
  implementation("io.quarkus:quarkus-arc")

  implementation("io.quarkus:quarkus-resteasy-mutiny")
  implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
  implementation("io.quarkus:quarkus-elytron-security-properties-file")

  implementation("io.quarkus:quarkus-resteasy-jackson")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
  implementation("com.christophsturm:filepeek:0.1.3")

  implementation("io.quarkus:quarkus-hibernate-validator")

  implementation("org.unbescape:unbescape:1.1.6.RELEASE")
  implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20220608.1") {
    exclude("com.google.guava", "guava")
  }
  implementation("com.github.slugify:slugify:2.5")

  implementation("io.quarkus:quarkus-mongodb-panache-kotlin") {
    description = "Native compilation only works with Entity Id: ObjectId"
  }
  implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")

  implementation("io.github.microutils:kotlin-logging-jvm:2.1.23") {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
  }
  implementation("org.jboss.logmanager:log4j2-jboss-logmanager")
  implementation("io.quarkiverse.loggingjson:quarkus-logging-json:1.1.1") {
    exclude("io.quarkus", "quarkus-core")
    exclude("io.quarkus", "quarkus-jackson")
  }
  implementation("io.quarkus:quarkus-smallrye-health")
  implementation("io.quarkus:quarkus-micrometer")
  implementation("io.quarkiverse.micrometer.registry:quarkus-micrometer-registry-stackdriver:2.3.0")
  implementation("io.quarkus:quarkus-micrometer-registry-prometheus")

  implementation("io.quarkus:quarkus-smallrye-openapi")

  implementation("no.finn.unleash:unleash-client-java:4.4.1")

  implementation("io.quarkus:quarkus-config-yaml")

  implementation("io.confluent:kafka-avro-serializer:7.1.0")
  implementation("org.apache.avro:avro:1.11.0")

  testImplementation("io.quarkus:quarkus-junit5")
  testImplementation("io.strikt:strikt-mockk:0.34.1") {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
  }
  testImplementation("io.mockk:mockk:1.12.4")
  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions") {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
  }
  testImplementation("com.tngtech.archunit:archunit-junit5:0.23.1") {
    description =
      "JavaClass now provides generic type information, i.e. there is now JavaClass.getTypeParameters() (see #398). Furthermore JavaClass now reports type parameter bounds as Dependencies (see #484)"
  }
  testImplementation("org.testcontainers:mongodb:1.21.3")
  testImplementation("org.testcontainers:kafka:1.21.3")
  testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.20.0")

  // Sentry
  implementation("io.quarkus:quarkus-logging-sentry")
}

allOpen {
  annotation("io.quarkus.arc.config.ConfigProperties")
  annotation("javax.ws.rs.Path")
  annotation("javax.enterprise.context.ApplicationScoped")
  annotation("javax.inject.Singleton")
  annotation("javax.enterprise.context.Dependent")
  annotation("javax.persistence.Entity")
  annotation("io.quarkus.mongodb.panache.MongoEntity")
  annotation("io.quarkus.test.junit.QuarkusTest")
}

noArg {
  annotation("de.blume2000.util.NoArg")
  annotation("io.quarkus.arc.config.ConfigProperties")
  annotation("javax.persistence.Entity")
  annotation("io.quarkus.mongodb.panache.MongoEntity")
}

configure<DetektExtension> {
  config = files("$rootDir/detekt-config.yml")
}

jacoco {
  toolVersion = "0.8.7"
}

dependencyCheck {
  analyzers(closureOf<AnalyzerExtension> {
    assemblyEnabled = false
    failBuildOnCVSS = 5F
    failOnError = true
    suppressionFile = "./gradle/config/suppressions.xml"
  })
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

sourceSets {
  create("integrationTest") {
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
      kotlin.srcDir("$projectDir/src/integrationTest/kotlin")
      resources.srcDir("$projectDir/src/integrationTest/resources")
      compileClasspath += sourceSets["main"].output + sourceSets["test"].output + configurations["testRuntimeClasspath"]
      runtimeClasspath += output + compileClasspath + sourceSets["test"].runtimeClasspath
    }
  }
}

tasks {
  withType<Detekt> {
    jvmTarget = JavaVersion.VERSION_11.toString()
  }

  withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    kotlinOptions.apiVersion = "1.5"
    kotlinOptions.javaParameters = true
  }

  register<Test>("unitTest") {
    useJUnitPlatform {
      includeTags("unit")
    }
    group = "verification"
    finalizedBy(jacocoTestReport)
  }
  register<Test>("integrationTest") {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    useJUnitPlatform {
      includeTags("integration")
    }
    group = "verification"
    finalizedBy(jacocoTestReport)
  }

  register("downloadSchemas") {
    val schemasToDownload = listOf("produkte-value", "verfuegbarkeiten-value")

    schemasToDownload.forEach {
      val request = URL("${System.getenv("AIVEN_SCHEMA_REGISTRY_URL")}/subjects/$it/versions/latest").openConnection()
      val schemaDir = "$projectDir/src/main/avro/downloaded_schemas"

      request.setRequestProperty("Authorization", "Basic ${Base64.getEncoder().encodeToString("${System.getenv("AIVEN_SCHEMA_REGISTRY_USER")}:${System.getenv("AIVEN_SCHEMA_REGISTRY_PASSWORD")}".toByteArray())}")

      val jsonResponse = JsonSlurper().parseText(String((request.content as InputStream).readAllBytes())) as Map<*, *>

      Files.createDirectories(Paths.get(schemaDir))
      File(schemaDir, "$it.avsc").writeText(jsonResponse["schema"].toString())
    }
  }

  jacocoTestReport {
    executionData.setFrom(fileTree(layout.buildDirectory).include("/jacoco/*.exec"))
    reports {
      xml.required.set(true)
    }
  }

  named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    fun isStable(version: String): Boolean {
      val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
      val regex = "^[0-9,.v-]+(-r)?$".toRegex()
      return stableKeyword || regex.matches(version)
    }
    rejectVersionIf {
      isStable(currentVersion) && !isStable(candidate.version)
    }
    outputFormatter = closureOf<Result> {
      PlainTextReporter(project, System.getProperty("revision"), gradleReleaseChannel).write(System.out, this)
      if (!this.outdated.dependencies.isEmpty()) {
        throw GradleException("Abort, there are dependencies to update.")
      }
    }
  }
}
