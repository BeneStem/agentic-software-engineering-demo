locals {
  load_balancer_backend_port  = 80
  load_balancer_frontend_port = 80
  app_name                    = "${var.application_name}-frontend"
}
