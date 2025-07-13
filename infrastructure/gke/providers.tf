terraform {
  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "0.7.0"
    }
    kubernetes   = {
      source  = "hashicorp/kubernetes"
      version = "2.8.0"
    }
  }
  required_version = "1.0.4"
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

provider "mongodbatlas" {
  public_key = var.mongo_atlas_master_public_key
  private_key = var.mongo_atlas_master_private_key
}
