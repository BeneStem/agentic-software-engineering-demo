# 12. Repository Pattern for Data Access

## Status

Accepted

## Context

The domain layer needs to persist and retrieve aggregates without being coupled to specific database technology. Requirements include:
- Domain independence from persistence technology
- Testability through mocking
- Consistent data access patterns
- Ability to switch databases if needed
- Clear separation of concerns

Direct database access from domain services would violate hexagonal architecture principles and make testing difficult.

## Decision

We will implement the Repository pattern:
- Repository interfaces defined in domain layer
- Implementation classes in adapter layer
- One repository per aggregate root
- Methods express domain concepts, not database operations
- MongoDB-specific code isolated in adapter implementations
- Repository interfaces use domain objects, not database entities

Example:
```kotlin
// Domain layer
interface ProduktRepository {
    fun findByFilter(filter: ProdukteFilter): List<Produkt>
    fun save(produkt: Produkt): Produkt
}

// Adapter layer
class ProduktMongoRepository : ProduktRepository {
    // MongoDB-specific implementation
}
```

## Consequences

**Positive:**
- Domain completely independent of persistence
- Easy to mock repositories for unit testing
- Can switch databases without changing domain
- Consistent data access patterns
- Clear separation of concerns
- Supports Domain-Driven Design practices

**Negative:**
- Additional abstraction layer adds complexity
- Some database features hard to abstract
- Potential for leaky abstractions
- Performance optimizations may be limited
- Extra mapping between domain and persistence models

**Neutral:**
- Need for clear repository method naming conventions
- Balance between generic and specific repository methods
- Transaction management considerations