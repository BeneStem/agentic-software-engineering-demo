variable "b2k_unleash_project_id" {
  type = string
  description = "Set automatically from gitlab project 'project-starter' after manually adding it from feature flag settings"
}

variable "b2k_unleash_instance_id" {
  type = string
  description = "Set automatically from gitlab project 'project-starter' after manually adding it from feature flag settings"
}

variable "kafka_produkte_read_ssl_keystore_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "kafka_produkte_read_ssl_truststore_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "kafka_verfuegbarkeiten_read_ssl_keystore_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "kafka_verfuegbarkeiten_read_ssl_truststore_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "application_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_db_user_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_db_user_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_master_public_key" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_master_private_key" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_project_id" {
  type = string
  description = "Set automatically from gitlab group of the team"
}

variable "aiven_schema_registry_user" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "aiven_schema_registry_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "team_key" {
  type = string
  description = "Set automatically from team gitlab group"
}
