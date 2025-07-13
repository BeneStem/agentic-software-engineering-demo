resource "kubernetes_deployment" "application_deployment-frontend" {

  metadata {
    name = "${var.application_name}-frontend-deployment"
    namespace = "${var.application_name}-frontend"
    labels = {
      app = "${var.application_name}-frontend"
      team = var.team_key
    }
  }

  spec {
    replicas = 3
    selector {
      match_labels = {
        app = "${var.application_name}-frontend"
      }
    }

    template {
      metadata {
        labels = {
          app = "${var.application_name}-frontend"
          team = var.team_key
        }
        annotations = {
          "prometheus.io/scrape" = "true"
          "prometheus.io/path"   = "/metrics"
          "prometheus.io/port"   = var.application_frontend_port
        }
      }

      spec {
        container {
          name = "${var.application_name}-frontend-container"
          image = "eu.gcr.io/${var.b2k_gcp_project_name}/${var.application_name}-frontend:${var.application_version}"

          resources {
            limits = {
              cpu    = "500m"
              memory = "512Mi"
            }

            requests = {
              cpu    = "100m"
              memory = "128Mi"
            }
          }

          port {
            container_port = var.application_frontend_port
          }

          readiness_probe {
            failure_threshold = 3
            initial_delay_seconds = 5
            period_seconds = 10
            success_threshold = 1
            timeout_seconds = 10

            http_get {
              path = "/health"
              port = var.application_frontend_port
            }
          }

          liveness_probe {
            failure_threshold = 3
            initial_delay_seconds = 5
            period_seconds = 10
            success_threshold = 1
            timeout_seconds = 10

            http_get {
              path = "/health"
              port = var.application_frontend_port
            }
          }

          env {
            name = "ENVIRONMENT"
            value = var.environment
          }
          env {
            name = "B2K_TEAM_KEY"
            value = var.team_key
          }
          env {
            name = "VUE_APPLICATION_NAME"
            value = "${var.application_name}-frontend"
          }
        }
      }
    }
  }
}
