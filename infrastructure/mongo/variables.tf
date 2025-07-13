variable "atlas_region" {
  type = string
  # TODO should be at ecom group level
  description = "Set automatically from gitlab-ci.yml job"
}

variable "environment" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_project_id" {
  type = string
  description = "Set automatically from gitlab group of the team"
}

variable "mongo_atlas_master_public_key" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "mongo_atlas_master_private_key" {
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

variable "application_name" {
  type = string
  description = "Set automatically from gitlab-ci.yml job"
}

variable "team_key" {
  type = string
  description = "Set automatically from team gitlab group"
}
