# Component Diagram - Finden Backend Service

## Overview

The Component diagram zooms into the Quarkus Backend container to show its internal structure. The backend follows an Onion/Hexagonal architecture with Domain-Driven Design principles, ensuring clear separation of concerns and maintainability.

## Component Diagram - Backend Service

```mermaid
flowchart TD
    %% Styling
    classDef adapter fill:#e3f2fd,stroke:#0d47a1,stroke-width:2px
    classDef application fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef domain fill:#e8f5e9,stroke:#1b5e20,stroke-width:3px
    classDef infra fill:#fce4ec,stroke:#880e4f,stroke-width:2px
    classDef external fill:#f5f5f5,stroke:#424242,stroke-width:1px
    
    %% External Systems
    Client[REST Client]:::external
    MongoDB[(MongoDB)]:::external
    Kafka[Apache Kafka]:::external
    
    %% Adapter Layer (Outer)
    subgraph Adapter Layer
        subgraph Active Adapters
            ProdukteResource[ProdukteResource<br/><i>REST Controller</i><br/>Product Search API]:::adapter
            ProduktlisteResource[ProduktlisteResource<br/><i>REST Controller</i><br/>Admin Tools API]:::adapter
            HealthCheck[FindenHealthCheck<br/><i>Health Endpoint</i><br/>Liveness/Readiness]:::adapter
        end
        
        subgraph Passive Adapters
            ProduktMongoRepo[ProduktMongoRepository<br/><i>MongoDB Adapter</i><br/>Product Persistence]:::adapter
            FilterwerteMongoRepo[VerfügbareFilterwerteMongoRepository<br/><i>MongoDB Adapter</i><br/>Filter Options Persistence]:::adapter
            KafkaConsumer[Kafka Event Consumer<br/><i>Message Adapter</i><br/>Event Processing]:::adapter
            MetricsAugmentor[MetricsAugmentor<br/><i>Monitoring Adapter</i><br/>Prometheus Metrics]:::adapter
        end
    end
    
    %% Application Layer
    subgraph Application Layer
        ProduktlistenService[ProduktlistenApplicationService<br/><i>Use Case Orchestration</i><br/>Search & Filter Logic]:::application
        VerfuegbarkeitenService[VerfügbarkeitenApplicationService<br/><i>Use Case Orchestration</i><br/>Availability Processing]:::application
        
        subgraph DTOs
            ProduktDTO[ProduktDTO<br/>ProdukteFilterDTO<br/>VerfügbareFilterwerteDTO<br/><i>Data Transfer Objects</i>]:::application
        end
    end
    
    %% Domain Layer (Core)
    subgraph Domain Layer
        subgraph Domain Model
            Produkt[Produkt<br/><i>Aggregate Root</i><br/>Product Entity]:::domain
            Produkte[Produkte<br/><i>Domain Service</i><br/>Product Collection Logic]:::domain
            VerfuegbareFilterwerte[VerfügbareFilterwerte<br/><i>Domain Service</i><br/>Filter Aggregation]:::domain
        end
        
        subgraph Value Objects
            ValueObjects[Produktnummer<br/>Produktname<br/>Klassifikation<br/>Preis<br/>Verfügbarkeit<br/>Liefertag<br/><i>Immutable Values</i>]:::domain
        end
        
        subgraph Domain Repositories
            ProduktRepo[ProduktRepository<br/><i>Interface</i><br/>Product Persistence Port]:::domain
            FilterwerteRepo[VerfügbareFilterwerteRepository<br/><i>Interface</i><br/>Filter Persistence Port]:::domain
        end
        
        subgraph Domain Services
            ProdukteFilter[CmsProdukteFilter<br/>UserProdukteFilter<br/><i>Filter Logic</i>]:::domain
            ProdukteSortierung[ProdukteSortierung<br/><i>Sorting Logic</i>]:::domain
        end
    end
    
    %% Connections - External to Adapters
    Client -->|HTTP/JSON| ProdukteResource
    Client -->|HTTP/JSON| ProduktlisteResource
    Client -->|Health Check| HealthCheck
    
    ProduktMongoRepo -->|MongoDB Protocol| MongoDB
    FilterwerteMongoRepo -->|MongoDB Protocol| MongoDB
    Kafka -->|Events| KafkaConsumer
    
    %% Connections - Adapters to Application
    ProdukteResource -->|DTO| ProduktlistenService
    ProduktlisteResource -->|DTO| ProduktlistenService
    KafkaConsumer -->|DTO| VerfuegbarkeitenService
    
    %% Connections - Application to Domain
    ProduktlistenService -->|Domain Objects| Produkte
    ProduktlistenService -->|Domain Objects| VerfuegbareFilterwerte
    VerfuegbarkeitenService -->|Domain Objects| Produkt
    
    %% Connections - Domain to Repositories
    Produkte -->|Interface| ProduktRepo
    VerfuegbareFilterwerte -->|Interface| FilterwerteRepo
    
    %% Connections - Repository Implementations
    ProduktMongoRepo -.->|Implements| ProduktRepo
    FilterwerteMongoRepo -.->|Implements| FilterwerteRepo
    
    %% Connections - Domain Internal
    Produkte -->|Uses| Produkt
    Produkt -->|Composed of| ValueObjects
    Produkte -->|Uses| ProdukteFilter
    Produkte -->|Uses| ProdukteSortierung
```

## Layer Descriptions

### Domain Layer (Core)

The innermost layer containing pure business logic with no external dependencies.

#### Domain Model
- **Produkt** (Aggregate Root)
  - Central entity representing a product
  - Maintains business invariants
  - Composed of value objects
  - Methods: `validate()`, `applyDiscount()`, `checkAvailability()`

- **Produkte** (Domain Service)
  - Collection-level operations on products
  - Implements complex business rules
  - Methods: `search()`, `filter()`, `sort()`, `aggregate()`

- **VerfügbareFilterwerte** (Domain Service)
  - Aggregates available filter options
  - Calculates filter counts
  - Methods: `extractFromProducts()`, `groupByCategory()`

#### Value Objects (Immutable)
- **Produktnummer**: Product identifier with validation
- **Produktname**: Product name with length constraints
- **Klassifikation**: Category hierarchy (KlassifikationId, KlassifikationName)
- **Preis**: Price with currency and discount information
- **Verfügbarkeit**: Stock status and delivery information
- **Liefertag**: Delivery date calculations
- **Produktfarbe**: Color information with validation
- **ProduktbildUrl**: Image URL with protocol validation

#### Domain Repositories (Interfaces)
- **ProduktRepository**
  - `findByFilter(filter: ProdukteFilter): List<Produkt>`
  - `findByIds(ids: List<Produktnummer>): List<Produkt>`
  - `save(produkt: Produkt): Produkt`
  - `deleteById(id: Produktnummer)`

- **VerfügbareFilterwerteRepository**
  - `findAvailableFilters(products: List<Produkt>): VerfügbareFilterwerte`
  - `findPriceRanges(): List<PreisBereich>`

#### Domain Services
- **CmsProdukteFilter**: CMS-driven filter configurations
- **UserProdukteFilter**: User-selected filter criteria
- **ProdukteSortierung**: Sorting strategies (price, name, availability)
- **ProduktComperatorByBaseListOccurence**: Custom sorting logic

### Application Layer

Orchestrates use cases and coordinates between domain and infrastructure.

#### Application Services
- **ProduktlistenApplicationService**
  - Orchestrates product search use cases
  - Transforms between domain objects and DTOs
  - Handles transaction boundaries
  - Key Methods:
    - `searchProducts(filter: ProdukteFilterDTO): ProdukteMitVerfügbarenFilterwertenDTO`
    - `getProductDetails(id: String): ProduktDTO`
    - `getAvailableFilters(): VerfügbareFilterwerteDTO`

- **VerfügbarkeitenApplicationService**
  - Processes availability update events
  - Synchronizes stock information
  - Key Methods:
    - `updateAvailability(event: AvailabilityEvent)`
    - `processStockChange(productId: String, newStock: Int)`

#### Data Transfer Objects (DTOs)
- **ProduktDTO**: External representation of products
- **ProdukteFilterDTO**: Filter criteria from API
- **VerfügbareFilterwerteDTO**: Available filter options
- **KlassifikationDTO**: Category information
- **PreisDTO**: Price information
- **LiefertageDTO**: Delivery options
- **FarbeDTO**: Color options

### Adapter Layer (Outer)

Handles external communication and infrastructure concerns.

#### Active Adapters (Inbound)

- **ProdukteResource** (REST Controller)
  - Endpoint: `/api/v1/products`
  - Operations: Search, filter, paginate
  - OpenAPI documented
  - Input validation
  - Error handling

- **ProduktlisteResource** (Admin Tools)
  - Endpoint: `/api/tools/produktliste`
  - Administrative operations
  - Bulk updates
  - Cache management

- **FindenHealthCheck**
  - Endpoints: `/q/health/live`, `/q/health/ready`
  - Database connectivity checks
  - Kafka consumer status
  - Resource availability

#### Passive Adapters (Outbound)

- **ProduktMongoRepository**
  - MongoDB Panache implementation
  - Implements ProduktRepository interface
  - Handles:
    - Document mapping (Domain ↔ Mongo)
    - Query building
    - Index management
    - Aggregation pipelines

- **VerfügbareFilterwerteMongoRepository**
  - MongoDB aggregation for filters
  - Efficient counting queries
  - Cache optimization

- **Kafka Event Consumer**
  - Consumes product events
  - Avro deserialization
  - Error handling and retries
  - Dead letter queue management

- **MetricsAugmentor**
  - Prometheus metrics collection
  - Custom business metrics
  - Performance monitoring
  - SLA tracking

## Key Design Patterns

### Hexagonal Architecture Patterns
- **Ports and Adapters**: Domain defines ports (interfaces), adapters implement them
- **Dependency Inversion**: Outer layers depend on inner layers
- **Interface Segregation**: Specific interfaces for different concerns

### Domain-Driven Design Patterns
- **Aggregate Root**: Produkt as consistency boundary
- **Value Objects**: Immutable domain concepts
- **Domain Services**: Complex business logic
- **Repository Pattern**: Abstract data access

### Technical Patterns
- **DTO Pattern**: Decoupling external representation
- **Factory Pattern**: Object creation (Augmentors)
- **Strategy Pattern**: Sorting and filtering strategies
- **Observer Pattern**: Event processing

## Component Interactions

### Search Request Flow
1. **REST Request** → ProdukteResource
2. **Validation** → Input sanitization and validation
3. **DTO Mapping** → Convert to domain filter objects
4. **Service Call** → ProduktlistenApplicationService
5. **Domain Logic** → Produkte.search() with filters
6. **Repository** → ProduktRepository.findByFilter()
7. **MongoDB Query** → ProduktMongoRepository executes
8. **Result Mapping** → Domain objects to DTOs
9. **Response** → JSON response to client

### Event Processing Flow
1. **Kafka Event** → Consumer receives message
2. **Deserialization** → Avro to domain event
3. **Service Call** → VerfügbarkeitenApplicationService
4. **Domain Update** → Produkt.updateAvailability()
5. **Persistence** → ProduktRepository.save()
6. **Acknowledgment** → Commit Kafka offset

## Quality Attributes

### Maintainability
- Clear layer boundaries
- Single responsibility per component
- Dependency injection
- Interface-based programming

### Testability
- Domain logic without infrastructure
- Repository interfaces for mocking
- DTO validation separate from domain
- Integration tests with TestContainers

### Performance
- Efficient MongoDB queries with indexes
- Reactive programming model
- Connection pooling
- Caching strategies

### Security
- Input validation at boundaries
- Sanitization in adapters
- No domain pollution
- Principle of least privilege

---

*The Component diagram details the internal structure of the backend service. For deployment architecture, see the [Deployment Diagram](./deployment.md).*