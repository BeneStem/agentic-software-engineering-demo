# 21. Domain-Driven Design Tactical Patterns

## Status

Accepted

## Context

The product search domain contains complex business logic that needs to be modeled accurately. Requirements include:
- Clear representation of business concepts
- Protection of business invariants
- Ubiquitous language between developers and domain experts
- Handling of complex product classifications and filtering rules
- Maintaining consistency within transactional boundaries
- Expression of business rules in code

Traditional data-centric approaches lead to anemic domain models where business logic leaks into services. The team needs patterns to properly model the product search domain.

## Decision

We will implement Domain-Driven Design tactical patterns:

**Aggregates:**
- `Produkt` as aggregate root maintaining product consistency
- Aggregate boundaries define transaction scope
- No direct references between aggregates, only by ID

**Value Objects:**
- `Produktnummer`, `Produktname`, `Klassifikation` for identity and descriptive concepts
- `Preis`, `Verfügbarkeit`, `Liefertag` for product attributes
- Immutable with validation in constructors
- Equality based on values, not identity

**Domain Services:**
- `Produkte` for collection-level operations
- `ProdukteSortierung` for sorting algorithms
- `VerfügbareFilterwerte` for filter aggregation
- Stateless operations on domain objects

**Domain Events:**
- Events captured for significant state changes
- Used for integration with other bounded contexts
- Enable event sourcing and audit trails

**Repositories:**
- Abstract persistence behind domain interfaces
- One repository per aggregate root
- Methods express domain operations, not CRUD

**Domain Exceptions:**
- `ProduktnummerIstLeerException`, `StreichpreisIstNiedrigerAlsPreisException`
- Express violations of business rules
- Rich error information for debugging

**Ubiquitous Language:**
- German terms for domain concepts (Produkt, Verfügbarkeit, Liefertag)
- Consistent terminology across code and documentation
- Direct mapping from business language to code

## Consequences

**Positive:**
- Business logic properly encapsulated in domain objects
- Clear expression of business rules and invariants
- Reduced complexity through proper boundaries
- Better communication with domain experts
- Protection against invalid states
- Easier to understand and maintain business logic

**Negative:**
- Initial learning curve for DDD patterns
- More classes and abstractions
- Risk of over-engineering simple CRUD operations
- Need for careful aggregate boundary design
- Potential performance impact from object creation

**Neutral:**
- Requires close collaboration with domain experts
- Different mindset from database-centric design
- Regular refinement of domain model needed