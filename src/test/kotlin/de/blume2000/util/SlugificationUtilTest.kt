package de.blume2000.util

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

class SlugificationUtilTest {

  @Test
  fun expectToSlugifyStringWithUmlauts() {
    expectThat("Schöne neue Röhrenjeans in Größe 42".slugify())
      .isEqualTo("schoene-neue-roehrenjeans-in-groesse-42")
  }

  @Test
  fun expectToSlugifyStringWithForeignChars() {
    expectThat("Haup(t)hose_+*~#'/-\"'un[d]so--Wahns{i}n.n;".slugify())
      .isEqualTo("haup-t-hose_-un-d-so-wahns-i-n-n")
  }

  @Test
  fun expectToReturnEmptyStringForEmptyInput() {
    expectThat("".slugify())
      .isEmpty()
  }
}
