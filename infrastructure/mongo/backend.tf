terraform {
  backend "gcs" {
    prefix = "finden/mongo"
  }
}
