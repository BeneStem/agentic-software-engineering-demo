resource "kubernetes_deployment" "application-deployment" {

  metadata {
    name = "${var.application_name}-backend-deployment"
    namespace = "${var.application_name}-backend"
    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }
  }

  spec {
    replicas = 3
    selector {
      match_labels = {
        app = "${var.application_name}-backend"
      }
    }

    template {
      metadata {
        labels = {
          app = "${var.application_name}-backend"
          team = var.team_key
        }
        annotations = {
          "prometheus.io/scrape" = "true"
          "prometheus.io/path"   = "/q/metrics"
          "prometheus.io/port"   = var.application_backend_port
        }
      }

      spec {
        container {
          name = "${var.application_name}-backend-container"
          image = "eu.gcr.io/${var.b2k_gcp_project_name}/${var.application_name}-backend:${var.application_version}"

          resources {
            limits = {
              cpu    = "500m"
              memory = "1536Mi"
            }

            requests = {
              cpu    = "250m"
              memory = "512Mi"
            }
          }

          port {
            container_port = var.application_backend_port
          }

          readiness_probe {
            failure_threshold = 3
            initial_delay_seconds = 40
            period_seconds = 10
            success_threshold = 1
            timeout_seconds = 1

            http_get {
              path = "/q/health/ready"
              port = var.application_backend_port
            }
          }

          liveness_probe {
            failure_threshold = 3
            initial_delay_seconds = 40
            period_seconds = 10
            success_threshold = 1
            timeout_seconds = 1

            http_get {
              path = "/q/health/live"
              port = var.application_backend_port
            }
          }

          env {
            name = "B2K_ZONE"
            value = var.zone
          }

          env {
            name = "B2K_TEAM_KEY"
            value = var.team_key
          }

          env {
            name = "B2K_GCP_PROJECT_NAME"
            value = var.b2k_gcp_project_name
          }

          env {
            name = "B2K_HOST"
            value = var.b2k_host
          }

          env {
            name  = "B2K_GCP_PROJECT_ENVIRONMENT"
            value = var.environment
          }

          env {
            name  = "QUARKUS_APPLICATION_NAME"
            value = "${var.application_name}-backend"
          }

          env {
            name  = "QUARKUS_LOG_SENTRY_DSN"
            value = var.sentry_dsn
          }

          env {
            name  = "QUARKUS_APPLICATION_VERSION"
            value = var.application_version
          }

          env {
            name = "B2K_UNLEASH_PROJECT_ID"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.application_unleash_account.project_id
                name = data.terraform_remote_state.gke.outputs.application_unleash_account.name
              }
            }
          }

          env {
            name = "B2K_UNLEASH_INSTANCE_ID"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.application_unleash_account.instance_id
                name = data.terraform_remote_state.gke.outputs.application_unleash_account.name
              }
            }
          }

          env {
            name = "KAFKA_BOOTSTRAP_SERVERS"
            value = var.produkte_kafka_bootstrap_servers
          }

          env {
            name = "MP_MESSAGING_INCOMING_PRODUKTE_SSL_KEYSTORE_LOCATION"
            value = var.erkunden_kafka_produkte_ssl_keystore_location
          }

          env {
            name = "MP_MESSAGING_INCOMING_PRODUKTE_SSL_KEYSTORE_PASSWORD"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.kafka_produkte_access.ssl_keystore_password
                name = data.terraform_remote_state.gke.outputs.kafka_produkte_access.name
              }
            }
          }

          env {
            name = "MP_MESSAGING_INCOMING_PRODUKTE_SSL_TRUSTSTORE_LOCATION"
            value = var.erkunden_kafka_produkte_ssl_truststore_location
          }

          env {
            name = "MP_MESSAGING_INCOMING_PRODUKTE_SSL_TRUSTSTORE_PASSWORD"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.kafka_produkte_access.ssl_truststore_password
                name = data.terraform_remote_state.gke.outputs.kafka_produkte_access.name
              }
            }
          }

          env {
            name = "MP_MESSAGING_INCOMING_VERFUEGBARKEITEN_SSL_KEYSTORE_LOCATION"
            value = var.erkunden_kafka_verfuegbarkeiten_ssl_keystore_location
          }

          env {
            name = "MP_MESSAGING_INCOMING_VERFUEGBARKEITEN_SSL_KEYSTORE_PASSWORD"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.kafka_verfuegbarkeiten_access.ssl_keystore_password
                name = data.terraform_remote_state.gke.outputs.kafka_verfuegbarkeiten_access.name
              }
            }
          }

          env {
            name = "MP_MESSAGING_INCOMING_VERFUEGBARKEITEN_SSL_TRUSTSTORE_LOCATION"
            value = var.erkunden_kafka_verfuegbarkeiten_ssl_truststore_location
          }

          env {
            name = "MP_MESSAGING_INCOMING_VERFUEGBARKEITEN_SSL_TRUSTSTORE_PASSWORD"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.kafka_verfuegbarkeiten_access.ssl_truststore_password
                name = data.terraform_remote_state.gke.outputs.kafka_verfuegbarkeiten_access.name
              }
            }
          }

          env {
            name = "QUARKUS_MONGODB_CONNECTION_STRING"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.mongo_application_access.db_connection_string
                name = data.terraform_remote_state.gke.outputs.mongo_application_access.name
              }
            }
          }

          env {
            name = "QUARKUS_MONGODB_CREDENTIALS_USERNAME"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.mongo_application_access.db_user
                name = data.terraform_remote_state.gke.outputs.mongo_application_access.name
              }
            }
          }

          env {
            name = "QUARKUS_MONGODB_CREDENTIALS_PASSWORD"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.mongo_application_access.db_password
                name = data.terraform_remote_state.gke.outputs.mongo_application_access.name
              }
            }
          }

          env {
            name = "KAFKA_SCHEMA_REGISTRY_URL"
            value = var.kafka_schema_registry_url
          }

          env {
            name = "MP_MESSAGING_INCOMING_PRODUKTE_BASIC_AUTH_USER_INFO"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.aiven_schema_registry.user-info
                name = data.terraform_remote_state.gke.outputs.aiven_schema_registry.name
              }
            }
          }

          env {
            name = "MP_MESSAGING_INCOMING_VERFUEGBARKEITEN_BASIC_AUTH_USER_INFO"
            value_from {
              secret_key_ref {
                key = data.terraform_remote_state.gke.outputs.aiven_schema_registry.user-info
                name = data.terraform_remote_state.gke.outputs.aiven_schema_registry.name
              }
            }
          }
        }
      }
    }
  }
}
