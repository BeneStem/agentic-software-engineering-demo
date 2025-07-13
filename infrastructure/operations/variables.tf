variable "b2k_gcp_project_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "b2k_gcp_shared_operations_project_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "region" {
  type = string
  description = "Set automatically from gitlab group 'ecom'"
}

variable "team_key" {
  type = string
  description = "Set automatically from team gitlab group"
}

variable "application_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "frontend_proxy_auth_password" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "frontend_proxy_auth_username" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "slack_auth_token" {
  type        = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "email_notification_channel" {
  type        = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "environment" {
  type        = string
  description = "Set automatically from gitlab-ci.yml job"
}

