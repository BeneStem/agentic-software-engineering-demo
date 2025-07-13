terraform {
  required_providers {
    google     = {
      source  = "hashicorp/google"
      version = "4.11.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "2.8.0"
    }
  }
  required_version = "1.0.4"
}

provider "google" {
  project = var.b2k_gcp_project_name
  region = var.region
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}
