locals {
  # ordering is important, keep values in lexicographical order
  application_unleash_account = {
    instance_id_key: "instance-id"
    project_id_key: "project-id"
  }

  kafka_produkte_read_access = {
    ssl_keystore_password : "ssl-keystore-password"
    ssl_truststore_password : "ssl-truststore-password"
  }

  kafka_verfuegbarkeiten_read_access = {
    ssl_keystore_password : "ssl-keystore-password"
    ssl_truststore_password : "ssl-truststore-password"
  }

  mongo_application_access = {
    db_connectionstring : "db-connectionstring"
    db_password : "db-password"
    db_user : "db-user"
  }

  aiven_schema_registry = {
    user-info: "user-info"
  }
}
