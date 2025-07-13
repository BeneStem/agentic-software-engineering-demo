package de.blume2000.util

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo

class SanitizingUtilTest {

  @Test
  fun expectToTrimText() {
    expectThat("    i will be remaining   ".sanitize())
      .isEqualTo("i will be remaining")
  }

  @Test
  fun expectToTrimTextWithTagsCompletely() {
    expectThat("<div> </div>".sanitize())
      .isEmpty()
  }

  @Test
  fun expectToSanitizeTextWithBTag() {
    expectThat("<b>i will remain</b>".sanitize())
      .isEqualTo("i will remain")
  }

  @Test
  fun expectToSanitizeTextAndUnescapeAfterwards() {
    expectThat("<b>i will & shall remain</b>".sanitize())
      .isEqualTo("i will & shall remain")
  }

  @Test
  fun expectToSanitizeTextWithScriptTag() {
    expectThat("  <script>i will be completely removed</script>  ".sanitize())
      .isEmpty()
  }

  @Test
  fun expectToSanitizeTextWithHtmlEntities() {
    expectThat("&#x3C;script&#x3E;alert(1)&#x3C;/script&#x3E;".sanitize())
      .isEmpty()
  }
}
