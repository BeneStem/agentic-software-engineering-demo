# 2. Use Quarkus Over Spring Boot

## Status

Accepted

## Context

The backend service requires a JVM-based framework that supports cloud-native development patterns. Key requirements include:
- Fast startup times for container-based deployments
- Low memory footprint to optimize cloud resource costs
- Native compilation support for improved performance
- Reactive programming model for handling high concurrent loads
- Strong Kubernetes integration for cloud deployment

Spring Boot is the traditional choice with a large ecosystem, but it has higher memory consumption and slower startup times. The team evaluated both Spring Boot and Quarkus for the backend implementation.

## Decision

We will use Quarkus 3.x as the backend framework instead of Spring Boot. The implementation will leverage:
- Quarkus native compilation with GraalVM
- Reactive programming with Mutiny
- Built-in Kubernetes manifest generation
- Quarkus extensions for MongoDB, Kafka, and REST endpoints
- Development mode with live reload capabilities

## Consequences

**Positive:**
- Startup time reduced by 10x compared to traditional JVM applications
- Memory footprint reduced by 50%, lowering operational costs
- Native compilation enables sub-second startup in containers
- Built-in Kubernetes integration simplifies deployment
- Development mode provides excellent developer experience
- Optimized for containerized environments

**Negative:**
- Smaller ecosystem compared to Spring Boot
- Team requires training on Quarkus-specific patterns
- Less community support and fewer third-party libraries
- Some reflection-based libraries may not work with native compilation
- Debugging native images can be more complex

**Neutral:**
- Different configuration approach using application.yml
- CDI-based dependency injection instead of Spring's approach
- Testing requires Quarkus-specific test annotations