terraform {
  backend "gcs" {
    prefix = "finden/service/frontend"
  }
}
