terraform {
  backend "gcs" {
    prefix = "finden/service/backend"
  }
}
