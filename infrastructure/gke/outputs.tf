output "application_unleash_account" {
  value = {
    name = kubernetes_secret.unleash_account.metadata[0].name
    project_id = local.application_unleash_account.project_id_key
    instance_id = local.application_unleash_account.instance_id_key
  }
}

output "kafka_produkte_access" {
  value = {
    name = kubernetes_secret.kafka_produkte_read_access.metadata[0].name
    ssl_keystore_password = local.kafka_produkte_read_access.ssl_keystore_password
    ssl_truststore_password = local.kafka_produkte_read_access.ssl_truststore_password
  }
}

output "kafka_verfuegbarkeiten_access" {
  value = {
    name = kubernetes_secret.kafka_verfuegbarkeiten_read_access.metadata[0].name
    ssl_keystore_password = local.kafka_verfuegbarkeiten_read_access.ssl_keystore_password
    ssl_truststore_password = local.kafka_verfuegbarkeiten_read_access.ssl_truststore_password
  }
}

output "mongo_application_access" {
  value = {
    name = kubernetes_secret.mongo_application_access.metadata[0].name
    db_user = local.mongo_application_access.db_user
    db_password = local.mongo_application_access.db_password
    db_connection_string = local.mongo_application_access.db_connectionstring
  }
}

output "aiven_schema_registry" {
  value = {
    name = kubernetes_secret.aiven_schema_registry.metadata[0].name
    user-info = local.aiven_schema_registry.user-info
  }
}

