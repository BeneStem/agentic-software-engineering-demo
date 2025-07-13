package de.blume2000.util

import java.time.OffsetDateTime
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext
import java.time.Instant
import java.time.ZoneOffset

class OffsetDateTimeCodec : Codec<OffsetDateTime> {
  override fun encode(writer: BsonWriter, value: OffsetDateTime, encoderContext: EncoderContext) {
    writer.writeDateTime(DateTimeUtil.wandleZeitpunktNachUTCUm(value).toInstant().toEpochMilli())
  }

  override fun getEncoderClass(): Class<OffsetDateTime> {
    return OffsetDateTime::class.java
  }

  override fun decode(reader: BsonReader, decoderContext: DecoderContext): OffsetDateTime {
    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(reader.readDateTime()), ZoneOffset.UTC)
  }
}
