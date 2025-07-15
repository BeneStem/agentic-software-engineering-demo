package de.blume2000.util

import com.github.slugify.Slugify
import java.util.Locale

private object SlugificationUtil {

  private val slugify = Slugify.builder()
    .locale(Locale.GERMAN)
    .build()

  fun slugify(text: String): String = slugify.slugify(text)
}

fun String.slugify() = SlugificationUtil.slugify(this)
