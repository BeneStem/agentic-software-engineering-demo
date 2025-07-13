variable "b2k_gcp_project_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "b2k_host" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "region" {
  type = string
  description = "Set automatically from gitlab group 'ecom'"
}

variable "zone" {
  type = string
  description = "Set automatically from gitlab group 'ecom'"
}

variable "environment" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "application_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "application_version" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "application_backend_port" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "terraform_state_bucket" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "team_key" {
  type = string
  description = "Set automatically from team gitlab group"
}

variable "produkte_kafka_bootstrap_servers" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "kafka_schema_registry_url" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "erkunden_kafka_produkte_ssl_keystore_location" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "erkunden_kafka_produkte_ssl_truststore_location" {
  type        = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "erkunden_kafka_verfuegbarkeiten_ssl_keystore_location" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "erkunden_kafka_verfuegbarkeiten_ssl_truststore_location" {
  type        = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "ssl_certificate_name" {
  type        = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "sentry_dsn" {
  type        = string
  description = "Set automatically from gitlab project variables"
}

