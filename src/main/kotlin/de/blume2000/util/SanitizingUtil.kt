package de.blume2000.util

import org.owasp.html.HtmlPolicyBuilder
import org.unbescape.html.HtmlEscape

private object SanitizingUtil {

  private val DISALLOW_ANYTHING_POLICY = HtmlPolicyBuilder().toFactory()

  fun sanitize(text: String): String = unescapeAgainBecauseSanitizingEscapesText(unescapeAndSanitize(text)).trim()

  private fun unescapeAndSanitize(text: String) = DISALLOW_ANYTHING_POLICY.sanitize(HtmlEscape.unescapeHtml(text))

  private fun unescapeAgainBecauseSanitizingEscapesText(text: String) = HtmlEscape.unescapeHtml(text)
}

fun String.sanitize() = SanitizingUtil.sanitize(this)
