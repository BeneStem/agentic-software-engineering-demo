package de.blume2000.finden.adapter.active

import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse

import jakarta.enterprise.context.ApplicationScoped

import org.eclipse.microprofile.health.Liveness

@Liveness
@ApplicationScoped
class FindenHealthCheck : HealthCheck {
  override fun call(): HealthCheckResponse {
    return HealthCheckResponse.up("Finden Health Check")
  }
}
