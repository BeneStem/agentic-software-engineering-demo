# 10. Immutable Value Objects in Domain Layer

## Status

Accepted

## Context

The domain model needs to ensure data integrity and thread safety while following Domain-Driven Design principles. Requirements include:
- Thread-safe domain objects for concurrent operations
- Clear expression of domain concepts
- Prevention of invalid state
- Easy testing and reasoning about code
- Reduced side effects and bugs

Mutable objects can lead to unexpected state changes, especially in concurrent environments. The functional programming paradigm promotes immutability for safer code.

## Decision

We will implement all value objects as immutable:
- Kotlin data classes with only `val` properties
- Use `copy()` method for creating modified instances
- Validation in constructors to ensure invariants
- No setters or mutable state
- Collections wrapped in immutable interfaces
- Domain exceptions for invalid state attempts

Examples:
- Produktnummer, Produktname, Klassifikation
- Preis, Verfügbarkeit, Liefertag
- All domain value objects follow this pattern

## Consequences

**Positive:**
- Thread safety guaranteed without synchronization
- Easier reasoning about code behavior
- No defensive copying needed
- Reduced bugs from unexpected mutations
- Better support for functional programming patterns
- Natural fit with event sourcing patterns

**Negative:**
- More object creation and garbage collection
- Potential performance impact for large objects
- Learning curve for developers used to mutable objects
- More verbose when many fields need updating
- Memory overhead from object copying

**Neutral:**
- Different patterns for updating state
- Builder pattern may be needed for complex objects
- Careful design of object granularity important