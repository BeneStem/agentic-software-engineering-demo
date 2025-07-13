package de.blume2000.finden.adapter.active.messaging

open class MessagingException(message: String?) : Exception(message) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }

  override fun toString(): String {
    return "${javaClass.name}([erkunden-finden] '$message')"
  }
}
