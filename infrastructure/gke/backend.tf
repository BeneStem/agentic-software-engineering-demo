terraform {
  backend "gcs" {
    prefix  = "finden/gke"
  }
}
