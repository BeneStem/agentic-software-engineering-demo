package de.blume2000.util

import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry
import java.time.OffsetDateTime

class OffsetDateTimeCodecProvider : CodecProvider {
  override fun <T> get(clazz: Class<T>, registry: CodecRegistry): Codec<T>? {
    return if (clazz == OffsetDateTime::class.java) OffsetDateTimeCodec() as Codec<T> else null
  }
}
