package de.blume2000.util

import com.github.slugify.Slugify

private object SlugificationUtil {

  private val slugify = Slugify()

  fun slugify(text: String): String = slugify.slugify(text)
}

fun String.slugify() = SlugificationUtil.slugify(this)
