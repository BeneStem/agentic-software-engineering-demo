terraform {
  backend "gcs" {
    prefix = "finden/operations"
  }
}
