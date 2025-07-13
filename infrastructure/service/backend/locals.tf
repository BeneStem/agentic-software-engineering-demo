data "terraform_remote_state" "gke" {
  backend = "gcs"
  config = {
    bucket = var.terraform_state_bucket
    prefix = "finden/gke"
  }
}

locals {
  load_balancer_backend_port  = 80
  load_balancer_frontend_port = 80
  app_name                    = "${var.application_name}-backend"
}
