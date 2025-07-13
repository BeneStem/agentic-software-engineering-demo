terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.11.0"
    }
  }
  required_version = "1.0.4"
}

provider "google" {
  project = var.b2k_gcp_shared_operations_project_name
  region = var.region
}
