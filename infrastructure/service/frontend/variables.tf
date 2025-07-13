variable "b2k_gcp_project_name" {
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

variable "application_frontend_port" {
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

variable "ssl_certificate_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}
