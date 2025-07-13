data "mongodbatlas_cluster" "mongo_db_cluster" {
  name = var.application_name
  project_id = var.mongo_atlas_project_id
}

resource "kubernetes_secret" "unleash_account" {
  metadata {
    name = "${var.application_name}-unleash-account"
    namespace = "${var.application_name}-backend"

    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }
  }

  data = zipmap(values(local.application_unleash_account), [
    var.b2k_unleash_instance_id,
    var.b2k_unleash_project_id
  ])

  depends_on = [
    kubernetes_namespace.backend-namespace
  ]
}

resource "kubernetes_secret" "kafka_produkte_read_access" {
  metadata {
    name = "kafka-produkte-read-access"
    namespace = "${var.application_name}-backend"

    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }
  }

  data = zipmap(values(local.kafka_produkte_read_access), [
    var.kafka_produkte_read_ssl_keystore_password,
    var.kafka_produkte_read_ssl_truststore_password
  ])

  depends_on = [
    kubernetes_namespace.backend-namespace
  ]
}

resource "kubernetes_secret" "kafka_verfuegbarkeiten_read_access" {
  metadata {
    name = "kafka-verfuegbarkeiten-read-access"
    namespace = "${var.application_name}-backend"

    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }
  }

  data = zipmap(values(local.kafka_verfuegbarkeiten_read_access), [
    var.kafka_verfuegbarkeiten_read_ssl_keystore_password,
    var.kafka_verfuegbarkeiten_read_ssl_truststore_password
  ])

  depends_on = [
    kubernetes_namespace.backend-namespace
  ]
}

resource "kubernetes_secret" "mongo_application_access" {
  metadata {
    name = "mongo-${var.application_name}-access"
    namespace = "${var.application_name}-backend"

    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }
  }

  data = zipmap(values(local.mongo_application_access), [
    data.mongodbatlas_cluster.mongo_db_cluster.connection_strings[0].private_srv,
    var.mongo_atlas_db_user_password,
    var.mongo_atlas_db_user_name
  ])

  depends_on = [
    kubernetes_namespace.backend-namespace
  ]
}

resource "kubernetes_secret" "aiven_schema_registry" {
  metadata {
    name = "aiven-schema-registry"
    namespace = "${var.application_name}-backend"

    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }
  }

  data = zipmap(values(local.aiven_schema_registry), [
    "${var.aiven_schema_registry_user}:${var.aiven_schema_registry_password}"
  ])

  depends_on = [
    kubernetes_namespace.backend-namespace
  ]
}
