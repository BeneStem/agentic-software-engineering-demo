resource "kubernetes_service" "internal-load-balancer" {
  metadata {
    name        = "${local.app_name}-load-balancer-internal"
    namespace   = local.app_name
    labels      = {
      app  = local.app_name
      team = var.team_key
    }
    annotations = {
      "external-dns.alpha.kubernetes.io/hostname" = "${var.environment}.${local.app_name}.${var.team_key}.ecom.blume2000.de"
      "networking.gke.io/load-balancer-type"      = "Internal"
    }
  }

  spec {
    selector = {
      app = local.app_name
    }

    port {
      name        = "${local.app_name}-load-balancer-port"
      protocol    = "TCP"
      port        = local.load_balancer_frontend_port
      target_port = var.application_frontend_port
    }

    type = "LoadBalancer"
  }

  depends_on = [
    kubernetes_deployment.application_deployment-frontend
  ]
}
