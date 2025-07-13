/*
resource "google_monitoring_uptime_check_config" "uptime_check_dev" {
  display_name = "${var.team_key} ${var.application_name} Dev"
  timeout = "1s"
  period = "60s"

  http_check {
    path = "/"
    port = "443"
    use_ssl = true
    validate_ssl = true
    auth_info {
      password = var.frontend_proxy_auth_password
      username = var.frontend_proxy_auth_username
    }
  }

  monitored_resource {
    type = "uptime_url"
    labels = {
      host = "dev.ecom.blume2000.de"
    }
  }
}
*/

/*
resource "google_monitoring_alert_policy" "uptime_alert" {
  display_name = "${var.team_key} ${var.application_name} uptime alerting"
  combiner = "OR"
  conditions {
    display_name = "Uptime check"
    condition_threshold {
      filter = "resource.type=\"uptime_url\" AND metric.type=\"monitoring.googleapis.com/uptime_check/check_passed\" AND metric.label.check_id=\"${google_monitoring_uptime_check_config.uptime_check_dev.uptime_check_id}\""
      duration = "300s"
      comparison = "COMPARISON_GT"
      threshold_value = 1
      aggregations {
        alignment_period = "1200s"
        per_series_aligner = "ALIGN_NEXT_OLDER"
      }
    }
  }
  depends_on = [
    google_monitoring_uptime_check_config.uptime_check_dev
  ]
}
*/
