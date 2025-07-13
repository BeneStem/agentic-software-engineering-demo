resource "kubernetes_namespace" "backend-namespace" {
  metadata {
    annotations = {
      name = "${var.application_name}-backend-namespace"
    }

    labels = {
      app = "${var.application_name}-backend"
      team = var.team_key
    }

    name = "${var.application_name}-backend"
  }
}

resource "kubernetes_namespace" "frontend-namespace" {
  metadata {
    annotations = {
      name = "${var.application_name}-frontend-namespace"
    }

    labels = {
      app = "${var.application_name}-frontend"
      team = var.team_key
    }

    name = "${var.application_name}-frontend"
  }
}
