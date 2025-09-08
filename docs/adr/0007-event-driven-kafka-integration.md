# 7. Event-Driven Integration via Apache Kafka

## Status

Accepted

## Context

The Finden system needs to stay synchronized with other systems in the e-commerce platform (Product Management, Pricing Service, Availability Service) without creating tight coupling. Requirements include:
- Real-time updates for product changes, pricing, and availability
- Ability to handle high volume of events
- Resilience to downstream system failures
- Audit trail of all changes
- Support for replay and reprocessing

Direct API calls would create tight coupling and synchronous dependencies. Database integration violates SCS boundaries. Event-driven architecture provides loose coupling.

## Decision

We will use Apache Kafka with Confluent Schema Registry for all inter-system communication:
- Avro schemas for event serialization with schema evolution
- Topics: product.events, pricing.events, availability.events, search.analytics
- Consumer groups for scalability
- At-least-once delivery guarantee with idempotent processing
- Dead letter queues for error handling
- Event sourcing patterns for audit trail

## Consequences

**Positive:**
- Loose coupling between systems enables independent evolution
- High throughput for event processing
- Built-in durability and fault tolerance
- Event replay capability for disaster recovery
- Scalable through consumer groups and partitioning
- Schema registry ensures compatibility

**Negative:**
- Operational complexity of Kafka infrastructure
- Eventually consistent data model
- Debugging distributed systems is complex
- Additional latency for data synchronization
- Requires careful offset management
- Schema evolution must be backward compatible

**Neutral:**
- Different programming model from request-response
- Monitoring and alerting for event processing
- Need for event ordering guarantees in some cases