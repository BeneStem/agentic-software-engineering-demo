resource "google_monitoring_dashboard" "dashboard" {
  dashboard_json = local.my_dashboard_json
}
