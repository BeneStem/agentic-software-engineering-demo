# 19. Terraform for Infrastructure as Code

## Status

Accepted

## Context

The cloud infrastructure needs to be version-controlled, reproducible, and manageable across multiple environments (dev, staging, production). Requirements include:
- Declarative infrastructure definition
- Version control for infrastructure changes
- Reproducible deployments
- Multi-environment support
- GCP service integration
- Team collaboration on infrastructure

Manual infrastructure management leads to configuration drift and deployment errors. ClickOps makes rollbacks difficult.

## Decision

We will use Terraform for all infrastructure management:
- Separate modules for different components (GKE, MongoDB, operations)
- Remote state storage in GCS buckets
- Separate workspaces for environments
- Variable files for environment-specific configuration
- Terraform modules for reusable components
- Version pinning for provider stability

Structure:
```
infrastructure/
├── gke/         # Kubernetes cluster
├── mongo/       # MongoDB Atlas
├── operations/  # Monitoring, alerts
└── service/     # Application deployments
```

## Consequences

**Positive:**
- Infrastructure changes tracked in version control
- Reproducible infrastructure across environments
- Rollback capability for infrastructure changes
- Clear documentation of infrastructure
- Reduced human errors
- Team collaboration through code review

**Negative:**
- State file management complexity
- Risk of state file corruption
- Learning curve for HCL language
- Some GCP features lag in provider support
- Potential for destructive changes
- Long apply times for complex changes

**Neutral:**
- Different workflow from console management
- Need for proper secret management
- Regular provider updates required