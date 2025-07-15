package de.blume2000.util

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention
@Constraint(validatedBy = [ListPatternValidator::class])
annotation class ListPattern(
  val message: String = "Invalid input",
  val regexp: String,
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = []
)
