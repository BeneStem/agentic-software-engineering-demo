package de.blume2000.util

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ListPatternValidator : ConstraintValidator<ListPattern, List<String>> {

  var pattern: String? = null

  override fun initialize(constraintAnnotation: ListPattern) {
    pattern = constraintAnnotation.regexp
  }

  override fun isValid(values: List<String>, context: ConstraintValidatorContext): Boolean {
    val regex = pattern?.toRegex() ?: return false
    return values.all { regex.matches(it) }
  }
}
