pluginManagement {
  plugins {
    id("com.gradle.enterprise") version "3.10.2"

    id("io.quarkus") version "2.6.3.Final"
  }
}

// WARNING add build-scan to measure build performance
// gradleEnterprise {
//    buildScan {
//      termsOfServiceUrl = "https://gradle.com/terms-of-service"
//      termsOfServiceAgree = "yes"
//    }
// }

rootProject.name = "finden"
