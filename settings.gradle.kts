pluginManagement {
  plugins {
    id("com.gradle.enterprise") version "3.10.2"

    id("io.quarkus") version "3.24.3"
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
