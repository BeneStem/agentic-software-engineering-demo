resource "google_monitoring_alert_policy" "alert_policy_error_codes" {
  combiner     = "OR"
  display_name = "${title(var.team_key)} - ${title(var.application_name)} - 500er Error Codes ${upper(var.environment)}"
  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Response Code 5** Anzahl Backend > 0"
    condition_threshold {
      filter          = "metric.type=\"custom.googleapis.com/http/server/requests/count\" resource.type=\"generic_node\" resource.label.\"namespace\"=\"finden-backend\" metric.label.\"status\"=monitoring.regex.full_match(\"5.*\") resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\""
      duration        = "0s"
      comparison      = "COMPARISON_GT"
      threshold_value = 0
      aggregations {
        alignment_period   = "60s"
        per_series_aligner = "ALIGN_SUM"
        group_by_fields = [ "metric.application" ]
      }
    }
  }

  notification_channels = [
    var.email_notification_channel
  ]

  user_labels = {
    team = var.team_key
    app = "${var.application_name}-backend-500-alert"
  }
}

resource "google_monitoring_alert_policy" "alert_policy_kafka_consumer_business_error" {
  display_name = "${title(var.team_key)} - ${title(var.application_name)} - Kafka Consumer Business Error ${upper(var.environment)}"
  combiner     = "OR"
  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Business Errors im Produkt Consumer > 0"
    condition_threshold {
      filter          = "metric.type=\"custom.googleapis.com/produktimport/counter\" resource.type=\"generic_node\" resource.label.\"namespace\"=\"finden-backend\" metric.label.\"result\"=\"business-error\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\""
      duration        = "0s"
      comparison      = "COMPARISON_GT"
      threshold_value = 0
      aggregations {
        alignment_period   = "60s"
        per_series_aligner = "ALIGN_SUM"
        group_by_fields = [ "metric.application" ]
      }
    }
  }

  notification_channels = [
    var.email_notification_channel
  ]

  user_labels = {
    team = var.team_key
    app = "${var.application_name}-backend-kafka-business-alert"
  }
}

resource "google_monitoring_alert_policy" "alert_policy_kafka_consumer_technical_error" {
  display_name = "${title(var.team_key)} - ${title(var.application_name)} - Kafka Consumer Technical Error ${upper(var.environment)}"
  combiner     = "OR"
 conditions {
   display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Technical Errors im Produkt Consumer > 0"
   condition_threshold {
     filter          = "metric.type=\"custom.googleapis.com/produktimport/counter\" resource.type=\"generic_node\" resource.label.\"namespace\"=\"finden-backend\" metric.label.\"result\"=\"technical-error\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\""
     duration        = "0s"
     comparison      = "COMPARISON_GT"
     threshold_value = 0
     aggregations {
       alignment_period   = "60s"
       per_series_aligner = "ALIGN_SUM"
       group_by_fields = [ "metric.application" ]
     }
   }
 }

  notification_channels = [
    var.email_notification_channel
  ]

  user_labels = {
    team = var.team_key
    app  = "${var.application_name}-backend-kafka-technical-alert"
  }
}


resource "google_monitoring_alert_policy" "response_times_alert" {
  display_name = "${title(var.team_key)} - ${title(var.application_name)} - Antwortzeiten ${upper(var.environment)}"
  combiner     = "OR"
  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Antwortzeiten Backend > 1s"
    condition_threshold {
      filter          = "metric.type=\"custom.googleapis.com/http/info/requests/timer/p99\" resource.type=\"generic_node\" resource.label.\"namespace\"=\"${lower(var.application_name)}-backend\" resource.labels.project_id=\"${var.b2k_gcp_project_name}\""
      duration        = "60s"
      comparison      = "COMPARISON_GT"
      threshold_value = 1000
      aggregations {
        alignment_period     = "60s"
        per_series_aligner   = "ALIGN_MAX"
        cross_series_reducer = "REDUCE_MAX"
        group_by_fields      = [
          "metric.application"
        ]
      }
    }
  }

  notification_channels = [
    var.email_notification_channel
  ]

  user_labels = {
    team = var.team_key
  }
}

resource "google_monitoring_alert_policy" "response_codes_alert" {
  display_name = "${title(var.team_key)} - ${title(var.application_name)} - Response Codes"
  combiner     = "OR"

  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Response Code 404 Anzahl Frontend > 10"
    condition_threshold {
      filter          = "resource.type=\"global\" metric.type=\"custom.googleapis.com/opentelemetry/finden.ssr.responseCode\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\" metric.label.\"status\"=\"404\""
      duration        = "60s"
      comparison      = "COMPARISON_GT"
      threshold_value = 5
      aggregations {
        alignment_period     = "60s"
        per_series_aligner   = "ALIGN_SUM"
        cross_series_reducer = "REDUCE_SUM"
        group_by_fields      = [
          "metric.status"
        ]
      }
    }
  }

  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Response Code 5** Anzahl Backend > 0"
    condition_threshold {
      filter          = "resource.type=\"generic_node\" metric.type=\"custom.googleapis.com/http/server/requests/count\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\" metric.label.\"status\"=monitoring.regex.full_match(\"5.*\") resource.label.\"namespace\"=\"${lower(var.application_name)}-backend\"  metric.label.\"uri\"!=\"/q/\""
      duration        = "60s"
      comparison      = "COMPARISON_GT"
      threshold_value = 0
      aggregations {
        alignment_period     = "60s"
        per_series_aligner   = "ALIGN_SUM"
        cross_series_reducer = "REDUCE_SUM"
        group_by_fields      = [
          "metric.status",
          "metric.method",
          "metric.uri"
        ]
      }
    }
  }

  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Response Code 5** Anzahl Frontend > 0"
    condition_threshold {
      filter          = "resource.type=\"global\" metric.type=\"custom.googleapis.com/opentelemetry/finden.ssr.responseCode\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\" metric.label.\"status\"=monitoring.regex.full_match(\"5.*\")"
      duration        = "60s"
      comparison      = "COMPARISON_GT"
      threshold_value = 0
      aggregations {
        alignment_period     = "60s"
        per_series_aligner   = "ALIGN_SUM"
        cross_series_reducer = "REDUCE_SUM"
        group_by_fields      = [
          "metric.status"
        ]
      }
    }
  }

  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Response Code 4** (ohne 404) Anzahl Backend > 0"
    condition_threshold {
      filter          = "resource.type=\"generic_node\" metric.type=\"custom.googleapis.com/http/server/requests/count\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\" metric.label.\"status\"=monitoring.regex.full_match(\"4.*\") metric.label.\"status\"!=\"404\" resource.label.\"namespace\"=\"${lower(var.application_name)}-backend\"  metric.label.\"uri\"!=\"/q/\""
      duration        = "60s"
      comparison      = "COMPARISON_GT"
      threshold_value = 0
      aggregations {
        alignment_period     = "60s"
        per_series_aligner   = "ALIGN_SUM"
        cross_series_reducer = "REDUCE_SUM"
        group_by_fields      = [
          "metric.status",
          "metric.method",
          "metric.uri"
        ]
      }
    }
  }

  conditions {
    display_name = "[${upper(var.environment)}] ${title(var.application_name)}: Response Code 4** (ohne 404) Anzahl Frontend > 0"
    condition_threshold {
      filter          = "resource.type=\"global\" metric.type=\"custom.googleapis.com/opentelemetry/finden.ssr.responseCode\" resource.label.\"project_id\"=\"${var.b2k_gcp_project_name}\" metric.label.\"status\"=monitoring.regex.full_match(\"4.*\") metric.label.\"status\"!=\"404\""
      duration        = "180s"
      comparison      = "COMPARISON_GT"
      threshold_value = 0
      aggregations {
        alignment_period     = "60s"
        per_series_aligner   = "ALIGN_SUM"
        cross_series_reducer = "REDUCE_SUM"
        group_by_fields      = [
          "metric.status"
        ]
      }
    }
  }

  notification_channels = [
    var.email_notification_channel
  ]

  user_labels = {
    team = var.team_key
  }
}
