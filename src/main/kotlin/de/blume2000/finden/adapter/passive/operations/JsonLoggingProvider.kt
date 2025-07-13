package de.blume2000.finden.adapter.passive.operations

import io.quarkiverse.loggingjson.JsonGenerator
import io.quarkiverse.loggingjson.JsonProvider
import io.quarkiverse.loggingjson.JsonWritingUtils
import io.quarkiverse.loggingjson.StringBuilderWriter
import io.quarkus.arc.Priority
import io.quarkus.arc.Unremovable
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logmanager.ExtFormatter
import org.jboss.logmanager.ExtLogRecord
import java.io.IOException
import java.io.PrintWriter
import javax.enterprise.inject.Alternative
import javax.inject.Singleton
import javax.interceptor.Interceptor.Priority.PLATFORM_AFTER

@Singleton
@Alternative
@Priority(PLATFORM_AFTER)
@Unremovable
@Suppress("unused")
class JsonLoggingProvider(
    @ConfigProperty(name = "b2k.gcp-project.environment")
    private val environment: String,
    @ConfigProperty(name = "b2k.team.key")
    private val teamKey: String,
    @ConfigProperty(name = "quarkus.application.name")
    private val applicationName: String
) : JsonProvider, ExtFormatter() {

  @Throws(IOException::class)
  override fun writeTo(generator: JsonGenerator, event: ExtLogRecord) {
    writeMessage(generator, event)
    writeSourceLocation(generator, event)
    writeLabels(generator)
  }

  private fun writeMessage(generator: JsonGenerator, event: ExtLogRecord) {
    val stringBuilderWriter = StringBuilderWriter().append(formatMessage(event))
    event.thrown?.let {
      stringBuilderWriter.append('\n')
      it.printStackTrace(PrintWriter(stringBuilderWriter))
      writeLevel(generator, "ERROR")
    } ?: writeLevel(generator, severityFor(event.level.intValue()))
    JsonWritingUtils.writeStringField(generator, "message", stringBuilderWriter.toString().trim())
  }

  private fun writeLevel(generator: JsonGenerator, severity: String) {
    JsonWritingUtils.writeStringField(generator, "severity", severity)
    if (severity == "ERROR") {
      JsonWritingUtils.writeStringField(
        generator, "@type",
        "type.googleapis.com/google.devtools.clouderrorreporting.v1beta1.ReportedErrorEvent"
      )
    }
  }

  private fun severityFor(levelValue: Int) =
    when (levelValue) {
      1000 -> "ERROR"
      900 -> "WARNING"
      800 -> "INFO"
      700 -> "INFO"
      500 -> "DEBUG"
      400 -> "DEBUG"
      300 -> "DEBUG"
      else -> "DEFAULT"
    }

  private fun writeSourceLocation(generator: JsonGenerator, event: ExtLogRecord) {
    generator.writeObjectFieldStart("sourceLocation")
    generator.writeStringField("file", event.loggerClassName)
    event.sourceMethodName?.let {
      generator.writeStringField("function", it)
      generator.writeStringField("line", event.sourceLineNumber.toString())
    }
    generator.writeEndObject()
  }

  private fun writeLabels(generator: JsonGenerator) {
    generator.writeObjectFieldStart("labels")
    generator.writeStringField("environment", environment)
    generator.writeStringField("team", teamKey)
    generator.writeStringField("application", applicationName)
    generator.writeEndObject()
  }

  override fun format(extLogRecord: ExtLogRecord?) = null
}
