resource "google_logging_metric" "logging_warning_metric" {
  project = var.b2k_gcp_project_name
  name = "${var.application_name}/backend/logging/warning/count"
  filter = "resource.type=k8s_container AND resource.labels.container_name=${var.application_name}-backend-container AND severity=WARNING"

  metric_descriptor {
    metric_kind = "DELTA"
    value_type = "INT64"
  }
}

resource "google_logging_metric" "logging_error_metric" {
  project = var.b2k_gcp_project_name
  name = "${var.application_name}/backend/logging/error/count"
  filter = "resource.type=k8s_container AND resource.labels.container_name=${var.application_name}-backend-container AND severity=ERROR"

  metric_descriptor {
    metric_kind = "DELTA"
    value_type = "INT64"
  }
}

resource "google_logging_metric" "logging_warning_metric_frontend" {
  project = var.b2k_gcp_project_name
  name = "${var.application_name}/frontend/logging/warning/count"
  filter = "resource.type=k8s_container AND resource.labels.container_name=${var.application_name}-frontend-container AND severity=WARNING"

  metric_descriptor {
    metric_kind = "DELTA"
    value_type = "INT64"
  }
}

resource "google_logging_metric" "logging_error_metric_frontend" {
  project = var.b2k_gcp_project_name
  name = "${var.application_name}/frontend/logging/error/count"
  filter = "resource.type=k8s_container AND resource.labels.container_name=${var.application_name}-frontend-container AND severity=ERROR"

  metric_descriptor {
    metric_kind = "DELTA"
    value_type = "INT64"
  }
}
