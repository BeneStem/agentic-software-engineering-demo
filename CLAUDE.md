# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Table of Contents

- [Self-Contained Systems Architecture](#self-contained-systems-architecture)
- [Technology Stack](#technology-stack)
- [Architecture and Domain Design](#architecture-and-domain-design)
- [Frontend Development Guidelines](#frontend-development-guidelines)
- [Frontend-Backend Integration](#frontend-backend-integration)
- [Testing Strategy (MANDATORY)](#testing-strategy-mandatory)
- [Frontend Build & Deployment](#frontend-build--deployment)
- [Frontend Performance & Security](#frontend-performance--security)
- [Performance Standards](#performance-standards)
- [Kotlin Development Guidelines](#kotlin-development-guidelines)
- [Code Quality Standards](#code-quality-standards)
- [Development Workflow (MANDATORY)](#development-workflow-mandatory)
- [Additional Standards](#additional-standards)
- [Context Engineering & Task Management](#context-engineering--task-management)

## Self-Contained Systems Architecture

### What is a Self-Contained System?

The Finden application is designed as a **Self-Contained System (SCS)** - an architectural approach that provides a middle ground between monolithic and microservices architectures. An SCS is an autonomous unit that includes its own web interface, business logic, and database, designed to handle a specific business capability end-to-end.

### Self-Contained Systems vs Microservices

#### Key Architectural Differences

| Aspect             | Self-Contained Systems                                 | Microservices                                           |
|--------------------|--------------------------------------------------------|---------------------------------------------------------|
| **Size & Scope**   | Larger, domain-focused systems (5-25 per organization) | Smaller, fine-grained services (100s per organization)  |
| **User Interface** | Includes own web UI and complete user experience       | Business logic only, separate UI layer                  |
| **Communication**  | Minimal or asynchronous communication preferred        | Complex synchronous/asynchronous communication patterns |
| **Team Ownership** | One team owns complete system end-to-end               | Multiple teams coordinate across service boundaries     |
| **Deployment**     | Independent deployment of complete business capability | Coordination required across multiple services          |
| **Database**       | Dedicated database per SCS                             | Shared databases or complex data consistency patterns   |

#### Benefits of SCS Architecture (2024 Perspective)

1. **Reduced Operational Complexity**: Eliminates coordination overhead between multiple microservices
2. **Team Autonomy**: Single team can implement features without cross-team dependencies
3. **Faster Deployment**: Complete business features deployed as single units
4. **End-to-End Ownership**: Teams responsible for full user experience, not just APIs
5. **Simplified Communication**: Minimal inter-service communication reduces network complexity

### Why Finden Qualifies as a Self-Contained System

The Finden system exhibits all characteristics of a well-designed SCS:

#### ✅ **Complete Business Domain**

- **Single Purpose**: Product search and listing functionality
- **Business Autonomy**: Complete e-commerce product discovery capability
- **Domain Boundaries**: Clear separation from other business domains (checkout, inventory, etc.)

#### ✅ **Full Technology Stack**

- **Frontend**: Vue.js 3.2.37 with SSR for complete user interface
- **Backend**: Quarkus/Kotlin application layer with business logic
- **Database**: Dedicated MongoDB instance for product data
- **Infrastructure**: Independent deployment and scaling capabilities

#### ✅ **Autonomous Operations**

- **Independent Deployment**: Frontend and backend can be deployed without external dependencies
- **Self-Contained Data**: Product catalog and search indexes maintained independently
- **Minimal External Communication**: API calls limited to essential integrations only

#### ✅ **Team Ownership Model**

- **Full-Stack Responsibility**: Single team can own frontend, backend, and database
- **Feature Delivery**: New product search features implemented entirely within team boundaries
- **Technology Decisions**: Independent choice of Vue.js, TypeScript, Quarkus, Kotlin, MongoDB

### SCS Implementation Principles (MANDATORY)

#### 1. **UI Ownership**

- Each SCS MUST include its own web interface
- NO shared UI components between SCS boundaries
- Changes contained within single SCS reduce coordination

#### 2. **Data Autonomy**

- Dedicated database per SCS
- NO direct database access between systems
- Data consistency handled within SCS boundaries

#### 3. **Communication Boundaries**

- Prefer asynchronous communication between SCS
- Minimize synchronous API calls
- Event-driven integration where possible

#### 4. **Deployment Independence**

- Each SCS deployed as complete unit
- NO deployment coordination required
- Independent scaling and versioning

#### 5. **Security Boundary (CRITICAL)**

- **Authorization & Authentication HANDLED BY INFRASTRUCTURE**
- SCS consumes pre-validated security context from infrastructure layer
- **NO implementation of auth/authorization within SCS**
- SCS focuses on: input validation, data protection, business logic security
- **Infrastructure provides**: User identity, roles, permissions, session management
- **SCS responsibility**: Consume and trust infrastructure security context

### Integration with Development Workflow

The SCS architecture directly influences development practices:

- **Feature Development**: Complete features implemented within single system
- **Testing Strategy**: End-to-end testing within SCS boundaries
- **Security Model**: Authentication and authorization handled per SCS
- **Performance Optimization**: Full-stack performance monitoring and optimization
- **Quality Gates**: Complete SCS must pass all quality standards before deployment

This architectural approach enables the development practices outlined in subsequent sections while maintaining system modularity and team autonomy.

## Technology Stack

### Backend Stack

- **Language:** Kotlin 2.2.0 (targeting JVM 22)
- **Framework:** Quarkus 3.24.3 (not Spring)
- **Database:** MongoDB with Panache Kotlin
- **Build Tool:** Gradle with Kotlin DSL
- **Testing:** JUnit 5, Mockk, Strikt, TestContainers, ArchUnit
- **Messaging:** Kafka with Avro serialization

### Frontend Stack

- **Language:** TypeScript 4.7.3 (strict mode)
- **Framework:** Vue.js 3.2.37 with Composition API (not Options API)
- **State Management:** Vuex 4.0.2
- **SSR Server:** Fastify 4.2.0
- **Build Tool:** Vue CLI 5.0.4 with Webpack
- **HTTP Client:** Axios for backend communication
- **Testing:** Jest with Vue Test Utils, Playwright for E2E

### Key Constraints

- **Backend:** Use Quarkus CDI (not Spring), MongoDB with Panache (not direct drivers)
- **Frontend:** Vue.js 3 Composition API (not Options API), TypeScript strict mode
- **Architecture:** Onion/Hexagonal with DDD principles, Self-Contained System approach

## Critical Rules & Constraints

### System Boundaries & Absolute Constraints (MANDATORY)

These constraints define **absolute system boundaries** that must NEVER be violated under any circumstances:

#### Security Boundaries (CRITICAL)

**SCS Security Responsibilities (ABSOLUTE):**

- **SCS MUST NEVER implement authentication or authorization** - handled by infrastructure
- **SCS MUST ONLY implement input validation and data protection**
- **SCS MUST trust pre-validated security context** from infrastructure layer
- **SCS MUST protect sensitive data** (product prices, business rules, customer data)
- **FORBIDDEN**: Direct user authentication, session management, role-based access control

**Data Protection Requirements (ABSOLUTE):**

- **NO personal data storage** - search operates on anonymous basis only
- **NO search query logging with personal identifiers** - anonymize all search analytics
- **NO cross-SCS personal data sharing** - maintain data privacy boundaries
- **MANDATORY GDPR compliance** - right to be forgotten, data minimization
- **FORBIDDEN**: Storing personal preferences without explicit consent, cross-border personal data transfer without compliance

#### Performance Boundaries (CRITICAL)

**Absolute Performance Limits (MUST NEVER EXCEED):**

- **API Response Time**: P95 < 300ms for standard operations, P95 < 500ms for complex queries
- **Database Query Time**: Average < 200ms, maximum 2 seconds before timeout
- **Memory Usage**: < 80% heap utilization under normal load
- **Algorithm Complexity**: NO O(n²) or higher complexity operations in production
- **Collection Size**: NO unbounded collection loading - always paginate

**Performance Anti-Patterns (FORBIDDEN):**

- **Memory exhaustion patterns**: Full dataset loading, disabled pagination
- **Inefficient algorithms**: O(n) operations in comparators, nested loops on large datasets
- **Resource leaks**: Unclosed connections, unmanaged object lifecycles
- **Blocking operations**: Synchronous calls that block event loops

#### Data Consistency Boundaries (CRITICAL)

**SCS Data Autonomy (ABSOLUTE):**

- **NO direct database access** between SCS instances
- **NO shared database schemas** between SCS boundaries
- **NO synchronous cross-SCS data dependencies** for core operations
- **MANDATORY eventual consistency** for cross-SCS data synchronization
- **FORBIDDEN**: Cross-SCS transactions, shared data stores, real-time cross-SCS data requirements

**Data Integrity Requirements (ABSOLUTE):**

- **Product data consistency** - prices and availability must be real-time accurate
- **Search index consistency** - search results must reflect current product state
- **Event ordering** - maintain causality in event streams
- **Schema evolution** - backward compatibility required for all data changes

#### Architecture Boundaries (CRITICAL)

**Layer Coupling Constraints (ABSOLUTE):**

- **Domain layer MUST NEVER reference** adapter DTOs or external data structures
- **Application layer MUST ONLY depend** on domain interfaces
- **Adapter layer MUST implement** domain interfaces without domain contamination
- **FORBIDDEN**: Cross-layer dependencies, domain logic in adapters, DTO usage in domain

**SCS Integration Constraints (ABSOLUTE):**

- **NO synchronous dependencies** on other SCS for core functionality
- **NO shared UI components** between SCS boundaries (each SCS owns complete UI)
- **NO deployment coordination** required between SCS instances
- **MANDATORY asynchronous communication** for all cross-SCS interactions
- **FORBIDDEN**: Runtime dependencies on external SCS, shared deployment artifacts

### Breaking Change Policies (MANDATORY)

#### API Breaking Change Policy (CRITICAL)

**API Versioning Rules (ABSOLUTE):**

- **NO breaking changes** to existing API endpoints without versioning
- **Minimum 6-month deprecation period** for API version retirement
- **Backward compatibility** required for at least 2 API versions
- **MANDATORY API contract validation** before deployment

**Breaking Change Examples:**

- **FORBIDDEN**: Removing API fields, changing field types, removing endpoints
- **REQUIRED**: New endpoints for new functionality, additive field changes only
- **MANDATORY**: Version negotiation for clients, graceful degradation

#### Cross-SCS Contract Policy (CRITICAL)

**Event Schema Changes (ABSOLUTE):**

- **NO breaking changes** to published event schemas without coordination
- **Avro schema evolution** required for all event modifications
- **Event versioning** mandatory for schema changes
- **Cross-SCS coordination** required for major event changes

**Integration Contract Policy:**

- **NO changes to event ordering guarantees** without coordination
- **NO changes to event payload structure** without backward compatibility
- **MANDATORY**: Schema registry validation, consumer impact assessment

### Resource & Operational Limits (MANDATORY)

#### Infrastructure Resource Limits (ABSOLUTE)

**Memory Limits (CRITICAL):**

- **Maximum heap size**: 2GB per instance under normal load
- **Memory leak detection**: Automatic alerts at 85% usage
- **Garbage collection**: Maximum 100ms pause times
- **FORBIDDEN**: Memory-intensive operations without streaming, unlimited object creation

**Network Limits (CRITICAL):**

- **Connection pooling**: Maximum 100 concurrent database connections
- **Request timeouts**: 30 seconds maximum for any single request
- **Rate limiting**: 1000 requests per minute per client
- **FORBIDDEN**: Unlimited connection creation, blocking network operations

#### Database Operational Limits (ABSOLUTE)

**Query Performance Limits (CRITICAL):**

- **Index requirements**: ALL production queries MUST use proper indexes
- **Query timeout**: 2 seconds maximum for any single query
- **Connection limits**: Maximum 50 connections per application instance
- **FORBIDDEN**: Full table scans, queries without WHERE clauses on large collections

**Data Size Limits (CRITICAL):**

- **Document size**: Maximum 16MB per MongoDB document
- **Collection size**: Maximum 10M documents before partitioning required
- **Index size**: Maximum 500MB per index
- **FORBIDDEN**: Unbounded document growth, missing data archival strategies

#### Deployment & Environment Constraints (MANDATORY)

**Deployment Safety Rules (ABSOLUTE):**

- **Zero-downtime deployments** required for production
- **Rollback capability** required within 5 minutes
- **Health check endpoints** mandatory for all deployments
- **FORBIDDEN**: Production deployments without testing, deployments during business hours without approval

**Environment Isolation (CRITICAL):**

- **NO production data** in development/staging environments
- **NO shared credentials** between environments
- **NO cross-environment communication** except for monitoring
- **MANDATORY**: Environment-specific configurations, secrets management

### Compliance & Regulatory Constraints (MANDATORY)

#### GDPR Compliance (ABSOLUTE)

**Data Processing Constraints (CRITICAL):**

- **Legal basis required** for all personal data processing
- **Data minimization** - collect only necessary data for product search
- **Storage limitation** - automatic deletion after defined retention periods
- **FORBIDDEN**: Unnecessary personal data collection, indefinite data retention

**User Rights Implementation (ABSOLUTE):**

- **Right to access** - users can request their data
- **Right to rectification** - users can correct their data
- **Right to erasure** - users can request data deletion
- **Right to portability** - users can export their data
- **MANDATORY**: Automated user rights fulfillment, audit trails

#### Regional Compliance (MANDATORY)

**Data Residency Rules (ABSOLUTE):**

- **EU data** must remain within EU boundaries
- **Cross-border transfers** require adequate protection
- **Local law compliance** for all operating regions
- **FORBIDDEN**: Unauthorized data transfers, non-compliant data storage

**Business Compliance (CRITICAL):**

- **Price accuracy** - all displayed prices must be authoritative
- **Availability accuracy** - availability information must be real-time
- **Regional restrictions** - respect local business hour and delivery constraints
- **MANDATORY**: Regular compliance audits, violation reporting

### Emergency Procedures & Override Policies (MANDATORY)

#### Production Emergency Rules (CRITICAL)

**Emergency Override Conditions (ABSOLUTE):**

- **Only for P1 production incidents** affecting core functionality
- **CTO approval required** for constraint overrides
- **Time-limited overrides** - maximum 4 hours
- **MANDATORY**: Incident documentation, post-incident review

**Emergency Response Constraints (ABSOLUTE):**

- **NO data integrity compromises** even during emergencies
- **NO security boundary violations** under any circumstances
- **NO user data exposure** during incident response
- **MANDATORY**: Audit trails for all emergency actions

#### Incident Escalation (MANDATORY)

**Escalation Triggers (ABSOLUTE):**

- **Any constraint violation** automatically triggers incident
- **Security boundary breach** - immediate P1 escalation
- **Data loss or corruption** - immediate CTO notification
- **Cross-SCS cascade failure** - architecture team involvement

**Response Requirements (CRITICAL):**

- **15-minute response time** for P1 incidents
- **1-hour resolution time** for constraint violations
- **Post-incident analysis** for all constraint breaches
- **MANDATORY**: Root cause analysis, prevention measures

## Architecture and Domain Design

### Onion Architecture Implementation

The application follows **Domain-Driven Design (DDD)** with **Onion/Hexagonal Architecture**:

**Onion Architecture package structure:**

- **Domain layer (core):** Contains model entities, value objects, aggregates, domain services, repository interfaces, and domain exceptions
- **Application layer:** Contains application services implementing use cases
- **Adapter layer (outer):** Contains active adapters (REST controllers, message consumers) and passive adapters (database implementations, external service clients)
- **Dependencies:** Domain has zero outward dependencies, application depends only on domain, adapters implement domain interfaces

### Domain-Driven Design Principles (MANDATORY)

#### Rich Domain Model

- **Business logic MUST live in domain entities**, not services
- **Value objects MUST be immutable** with proper validation
- **Aggregates MUST enforce business invariants**
- **Domain services for complex operations** spanning multiple entities

#### Layer Dependencies (ENFORCED BY ARCHUNIT)

- **Domain layer:** Zero outward dependencies
- **Application layer:** Depends only on domain interfaces
- **Adapter layer:** Implements domain interfaces

#### Anti-Corruption Layer (MANDATORY)

- **Domain MUST NOT reference DTOs** from adapter layer
- **External data structures MUST be mapped** at adapter boundaries
- **Domain model protected from external changes**

## Business Logic Documentation

### Product Search Domain Workflows (MANDATORY)

The Finden system implements **core business workflows** for product search and discovery within the e-commerce domain:

#### Product Search Workflow (MANDATORY)

**Primary Search Flow:**

```
User Query → Query Analysis → Classification Matching → Availability Check → Price Calculation → Ranking Algorithm → Result Presentation
```

**Search Components:**

- **Query Analysis**: Parse user input, extract keywords, identify product categories
- **Classification Matching**: Match query against `klassifikationId` hierarchy for product categorization
- **Availability Check**: Filter products based on `verfuegbarkeiten.bestellschlussUTC` and `verfuegbarkeiten.liefertag`
- **Price Calculation**: Apply pricing rules, discounts, and regional variations
- **Ranking Algorithm**: Sort results by relevance, availability, price, and business rules
- **Result Presentation**: Format and paginate results for frontend display

#### Product Filtering Workflow (MANDATORY)

**Filter Application Flow:**

```
Filter Selection → Validation → Database Query Optimization → Result Filtering → UI Update
```

**Filter Types:**

- **Category Filters**: Based on product classification hierarchy
- **Availability Filters**: Filter by delivery dates and order deadlines
- **Price Range Filters**: Apply minimum/maximum price constraints
- **Custom Attribute Filters**: Filter by product-specific attributes

#### Product Detail Workflow (MANDATORY)

**Detail Retrieval Flow:**

```
Product Selection → Authorization Check → Detail Lookup → Availability Calculation → Price Calculation → Related Products → Display
```

**Detail Components:**

- **Product Information**: Complete product data with specifications
- **Real-time Availability**: Current availability status and delivery options
- **Dynamic Pricing**: Current price with applicable discounts
- **Related Products**: Recommendations based on business rules

### Business Rules & Decision Trees (MANDATORY)

#### Price Calculation Rules (CRITICAL)

**Price Decision Tree:**

```
Base Price
├── Apply Regional Pricing (if applicable)
├── Apply Volume Discounts
│   ├── Tier 1: 1-10 units (no discount)
│   ├── Tier 2: 11-50 units (5% discount)
│   └── Tier 3: 50+ units (10% discount)
├── Apply Promotional Discounts (if active)
└── Apply Tax Calculations (region-specific)
```

**Business Rules:**

- **Never show negative prices** - return null for invalid calculations
- **Price consistency** - same price for same product within user session
- **Currency formatting** - always display in user's regional currency
- **Discount precedence** - promotional discounts override volume discounts

#### Availability Calculation Rules (CRITICAL)

**Availability Decision Tree:**

```
Product Request
├── Check bestellschlussUTC (order deadline)
│   ├── Current time > deadline → "Order Deadline Passed"
│   └── Current time ≤ deadline → Continue
├── Check liefertag (delivery date)
│   ├── Delivery date < today → "Past Delivery Date"
│   └── Delivery date ≥ today → "Available"
└── Check inventory levels (if applicable)
    ├── Stock = 0 → "Out of Stock"
    └── Stock > 0 → "In Stock"
```

**Business Rules:**

- **Real-time calculations** - availability checked on every request
- **Timezone handling** - all times calculated in user's timezone
- **Grace periods** - 30-minute grace period for order deadlines
- **Business hours** - consider business hours for same-day delivery

#### Classification Hierarchy Rules (MANDATORY)

**Classification Decision Tree:**

```
Product Classification
├── Primary Category (Level 1)
│   ├── Food & Beverages
│   ├── Electronics
│   └── Household Items
├── Secondary Category (Level 2)
│   ├── Subcategory specific to primary
│   └── Cross-category items
└── Tertiary Attributes (Level 3)
    ├── Brand-specific attributes
    ├── Technical specifications
    └── Regional variations
```

**Business Rules:**

- **Hierarchical inheritance** - products inherit parent category attributes
- **Multi-classification support** - products can belong to multiple categories
- **Classification validation** - invalid klassifikationId returns empty results
- **Search optimization** - index classification hierarchy for fast lookups

### Domain Events & Triggers (MANDATORY)

#### Product Domain Events

**Core Domain Events:**

- **ProductSearchRequested**: Triggered when user performs search
- **ProductFiltered**: Triggered when filters are applied
- **ProductViewed**: Triggered when product detail is accessed
- **PriceCalculated**: Triggered when product price is computed
- **AvailabilityChecked**: Triggered when availability is verified

**Event Payload Examples:**

```kotlin
// ProductSearchRequested Event
data class ProductSearchRequested(
  val userId: String,
  val query: String,
  val filters: Map<String, Any>,
  val timestamp: Instant
)

// PriceCalculated Event
data class PriceCalculated(
  val productId: String,
  val basePrice: BigDecimal,
  val finalPrice: BigDecimal,
  val discountsApplied: List<Discount>,
  val region: String
)
```

#### Cross-SCS Integration Events (MANDATORY)

**Integration Event Types:**

- **ProductAvailabilityChanged**: Notify when product availability updates
- **PriceUpdated**: Notify when product prices change
- **NewProductAdded**: Notify when products are added to catalog
- **ProductDiscontinued**: Notify when products are removed

**Event Publishing Rules:**

- **Asynchronous publishing** - all events published via Kafka
- **Event versioning** - use Avro schemas for backward compatibility
- **Event ordering** - maintain causality for related events
- **Error handling** - dead letter queues for failed event processing

### Business Terminology Glossary (MANDATORY)

#### Core Domain Terms

**Product Terms:**

- **Produkt**: Core product entity with specifications and metadata
- **Klassifikation**: Product classification/category system
- **Verfügbarkeit**: Product availability including order deadlines and delivery dates
- **Bestellschluss**: Order deadline - latest time to place order for scheduled delivery
- **Liefertag**: Delivery date - scheduled date for product delivery

**Search Terms:**

- **Suchquery**: User search query with keywords and filters
- **Filterkriterien**: Search filter criteria for narrowing results
- **Ranking**: Result ordering algorithm based on relevance and business rules
- **Facetten**: Search facets for guided navigation

**Business Process Terms:**

- **Preisberechnung**: Price calculation including discounts and regional pricing
- **Verfügbarkeitsprüfung**: Availability check against current inventory and schedule
- **Bestellabwicklung**: Order processing workflow (handled by separate SCS)
- **Produktkatalog**: Complete product catalog management

**Technical Terms:**

- **API-Gateway**: Infrastructure component handling cross-SCS routing
- **Event-Stream**: Kafka event streaming for cross-SCS communication
- **Suchindex**: Search index optimized for product discovery
- **Cache-Ebene**: Caching layer for performance optimization

### Compliance & Regulatory Rules (MANDATORY)

#### Data Protection Rules (GDPR)

**Personal Data Handling:**

- **Search queries** - log only anonymized search patterns
- **User preferences** - store with explicit consent only
- **Regional restrictions** - apply data residency rules
- **Data retention** - automatically purge personal data after defined periods

**Implementation Requirements:**

- **No personal data in product search** - search operates on anonymous basis
- **User consent tracking** - for personalized recommendations only
- **Right to be forgotten** - clear user data on request
- **Data minimization** - collect only necessary search analytics

#### Business Rules Validation (MANDATORY)

**Critical Business Rules:**

- **Price accuracy** - all prices must be validated against authoritative source
- **Availability accuracy** - real-time availability checks required
- **Regional compliance** - respect regional restrictions and regulations
- **Business hours** - consider business operating hours for delivery calculations

**Validation Implementation:**

- **Input validation** - all search parameters validated at API boundary
- **Business rule enforcement** - domain entities enforce business invariants
- **Audit trails** - maintain audit logs for critical business operations
- **Error handling** - graceful degradation when business rules fail

## Frontend Architecture Analysis

### Frontend Layered Architecture (MANDATORY)

The frontend follows a **three-layer architecture** that complements the backend's Onion Architecture:

**Frontend Architecture Layer Structure:**

- **Presentation Layer (Components):** Vue components, templates, styles, and user interaction handling
- **Business Logic Layer (Composables):** Reusable business logic, state management, and domain operations
- **Data Access Layer (API):** HTTP clients, API abstractions, and data transformation
- **Dependencies:** Presentation depends only on composables, composables depend only on API layer

#### Presentation Layer Principles (MANDATORY)

- **Components MUST handle only UI rendering and user interaction**
- **NO business logic in component script sections** - delegate to composables
- **Template logic MUST be minimal** - use computed properties for complex operations
- **Style scoping MUST prevent CSS bleeding** between components
- **FORBIDDEN:** Direct API calls from components, complex calculations in templates

#### Business Logic Layer Principles (MANDATORY)

- **Composables MUST contain all reusable business logic** and state management
- **State management MUST be reactive** using Vue's reactivity system
- **Business rules MUST be testable** independently of Vue components
- **Cross-cutting concerns MUST be handled** in dedicated composables
- **FORBIDDEN:** Business logic in components, non-reactive state management

#### Data Access Layer Principles (MANDATORY)

- **API layer MUST handle all backend communication** and data transformation
- **HTTP clients MUST be abstracted** behind service interfaces
- **Response transformation MUST occur** at API boundary
- **Error handling MUST be centralized** in API layer
- **FORBIDDEN:** Direct HTTP client usage in composables, unhandled API errors

### Multi-App Architecture (MANDATORY)

The frontend supports **multiple independent applications** within a single codebase:

#### App Separation Principles (MANDATORY)

- **Each app (produktliste, tool) MUST be independently buildable** and deployable
- **App-specific configurations MUST be isolated** per application
- **Build entry points MUST be separate** for each application
- **Runtime isolation MUST be maintained** between applications
- **FORBIDDEN:** Cross-app dependencies at runtime, shared mutable state between apps

#### Shared Component Library (MANDATORY)

- **Shared components MUST have clear interface contracts** with typed props and events
- **Component reusability MUST be maximized** across applications
- **Breaking changes MUST be avoided** in shared component interfaces
- **Component documentation MUST be maintained** for shared library
- **FORBIDDEN:** App-specific logic in shared components, undocumented interface changes

#### Build System Architecture (MANDATORY)

- **Vue CLI MUST support multi-app builds** with separate output directories
- **Environment variables MUST be app-specific** where applicable
- **Code splitting MUST be optimized** per application
- **Bundle analysis MUST be available** for each application
- **FORBIDDEN:** Build configuration coupling between apps, shared build artifacts

### SSR Architecture with Fastify (MANDATORY)

The frontend implements **Server-Side Rendering** using Fastify server:

#### SSR Server Principles (MANDATORY)

- **Fastify server MUST handle SSR rendering** with proper error boundaries
- **Server startup MUST be graceful** with proper health checks
- **Request handling MUST be stateless** across server instances
- **Server shutdown MUST be graceful** with proper cleanup
- **FORBIDDEN:** Stateful server operations, browser-specific APIs in SSR context

#### State Hydration Principles (MANDATORY)

- **Client state MUST match server-rendered state** exactly during hydration
- **Store hydration MUST be atomic** to prevent state inconsistencies
- **Hydration mismatches MUST be handled** gracefully with fallbacks
- **Initial state MUST be serializable** for server-to-client transfer
- **FORBIDDEN:** State mismatches between server and client, non-serializable state

#### HTTP Client Separation (MANDATORY)

- **SSR and client-side MUST use separate HTTP client configurations**
- **SSR HTTP client MUST use internal API URLs** for performance
- **Client-side HTTP client MUST handle browser-specific concerns** (CORS, cookies)
- **Timeout configurations MUST be optimized** per context (SSR vs client)
- **FORBIDDEN:** Shared HTTP client instances between SSR and client contexts

### Component Architecture Patterns (MANDATORY)

Components follow **hierarchical organization** with clear responsibilities:

#### Component Hierarchy Standards (MANDATORY)

- **Base components MUST be framework-agnostic** and highly reusable
- **Layout components MUST handle structural concerns** only
- **Business components MUST encapsulate domain-specific logic**
- **Utility components MUST handle cross-cutting concerns**
- **FORBIDDEN:** Mixed responsibility components, unclear component categorization

#### Single Responsibility Principles (MANDATORY)

- **Each component MUST handle only one concern** or responsibility
- **Component size MUST be limited** to maintainable complexity
- **Component composition MUST be preferred** over inheritance
- **Component interfaces MUST be minimal** and focused
- **FORBIDDEN:** Multi-responsibility components, oversized components

#### Props and Events Interface (MANDATORY)

- **Props interfaces MUST be defined** with explicit TypeScript types
- **Optional properties MUST be marked explicitly** in interfaces
- **Event signatures MUST be specific** rather than generic
- **Default values MUST be provided** using withDefaults
- **FORBIDDEN:** Untyped props/events, function props instead of events, generic event handlers

### State Management Architecture (MANDATORY)

State management follows **Vuex modular patterns** with strict typing:

#### Vuex Module Principles (MANDATORY)

- **Each store module MUST handle only related domain concerns**
- **Module namespacing MUST be enforced** for logical separation
- **State interfaces MUST be comprehensively defined** with all properties
- **Module dependencies MUST be minimized** between modules
- **FORBIDDEN:** Cross-module state dependencies, unnamespaced modules

#### Type Safety Requirements (MANDATORY)

- **ALL store operations MUST be fully typed** with TypeScript
- **ActionContext typing MUST be enforced** for all async actions
- **Getters MUST return typed values** with explicit return types
- **Mutations MUST be strongly typed** with payload interfaces
- **FORBIDDEN:** Untyped store operations, any type usage without justification

#### State Normalization (MANDATORY)

- **State structure MUST be flat** to avoid deeply nested objects
- **Computed state MUST use getters** rather than duplicated state
- **Loading states MUST be handled consistently** across modules
- **Error states MUST be centralized** with standardized error interfaces
- **FORBIDDEN:** Deeply nested state structures, duplicated computed state

### API Layer Architecture (MANDATORY)

The API layer provides **type-safe abstractions** for backend communication:

#### HTTP Client Abstraction (MANDATORY)

- **HTTP clients MUST be abstracted** behind service interfaces
- **SSR and client-side MUST use separate HTTP client instances**
- **Base URL configuration MUST be environment-specific**
- **Request/response interceptors MUST be properly configured**
- **FORBIDDEN:** Direct HTTP client usage outside API layer, shared client instances

#### Type Safety Requirements (MANDATORY)

- **ALL API functions MUST use proper TypeScript interfaces**
- **Request parameters MUST be strongly typed** with validation
- **Response interfaces MUST be comprehensive** with all required fields
- **Error responses MUST be typed** with standardized error structures
- **FORBIDDEN:** Untyped API responses, missing request parameter validation

#### Error Handling Strategy (MANDATORY)

- **Error handling MUST be centralized** in API layer
- **Error types MUST be distinguished** (network, server, business errors)
- **Retry logic MUST be implemented** for transient failures
- **Error recovery MUST be graceful** with fallback mechanisms
- **FORBIDDEN:** Unhandled API errors, inconsistent error handling patterns

#### Response Transformation (MANDATORY)

- **DTOs MUST be transformed to domain models** at API boundaries
- **Data normalization MUST occur** in API layer
- **Response caching MUST be implemented** where appropriate
- **API versioning MUST be supported** for backward compatibility
- **FORBIDDEN:** Raw DTO usage in business logic, unprocessed API responses

## Code Organization

### Project Structure Overview (MANDATORY)

The Finden Self-Contained System follows a **clear hierarchical structure** that separates concerns and maintains architectural boundaries:

```
finden-scs/
├── backend/                          # Quarkus/Kotlin backend application
│   ├── src/main/kotlin/
│   │   ├── domain/                   # Domain layer (core business logic)
│   │   ├── application/              # Application layer (use cases)
│   │   ├── adapter/                  # Adapter layer (external integrations)
│   │   └── infrastructure/           # Infrastructure configuration
│   ├── src/test/kotlin/              # Test packages mirroring main structure
│   └── build.gradle.kts              # Kotlin DSL build configuration
├── frontend/                         # Vue.js frontend applications
│   ├── src/
│   │   ├── apps/                     # Multi-app entry points
│   │   ├── shared/                   # Shared components and utilities
│   │   ├── api/                      # API layer abstractions
│   │   ├── composables/              # Business logic composables
│   │   └── store/                    # Vuex state management
│   ├── public/                       # Static assets
│   └── package.json                  # Frontend dependencies
├── infrastructure/                   # Infrastructure as Code
│   ├── docker/                       # Container configurations
│   ├── kubernetes/                   # K8s deployment manifests
│   └── monitoring/                   # Observability configuration
├── docs/                            # Project documentation
│   ├── architecture/                # Architecture decision records
│   ├── api/                         # API documentation
│   └── business/                    # Business logic documentation
└── .taskmaster/                     # TaskMaster AI integration
    ├── tasks/                       # Task management
    └── docs/                        # PRD and requirements
```

### Backend Code Organization (MANDATORY)

#### Domain Layer Structure (MANDATORY)

**Domain layer package organization:**

```kotlin
domain /
├── model/                           # Domain entities and value objects
│   ├── produkt/                     # Product aggregate
│   │   ├── Produkt.kt              # Product entity
│   │   ├── ProduktId.kt            # Product identifier value object
│   │   ├── Klassifikation.kt       # Classification value object
│   │   └── Verfuegbarkeit.kt       # Availability value object
│   ├── search/                      # Search aggregate
│   │   ├── SearchQuery.kt          # Search query entity
│   │   ├── SearchResult.kt         # Search result aggregate
│   │   └── SearchCriteria.kt       # Search criteria value object
│   └── pricing/                     # Pricing aggregate
│       ├── Price.kt                # Price value object
│       ├── Discount.kt             # Discount entity
│       └── Region.kt               # Regional pricing value object
├── service/                         # Domain services
│   ├── ProduktSearchService.kt     # Product search domain service
│   ├── PricingService.kt           # Pricing calculation service
│   └── AvailabilityService.kt      # Availability checking service
├── repository/                      # Repository interfaces
│   ├── ProduktRepository.kt        # Product repository interface
│   ├── SearchRepository.kt         # Search repository interface
│   └── PricingRepository.kt        # Pricing repository interface
└── exception/                       # Domain exceptions
├── DomainException.kt          # Base domain exception
├── ProduktNotFoundException.kt  # Product-specific exceptions
└── InvalidSearchException.kt   # Search-specific exceptions
```

**Domain Layer Rules (MANDATORY):**

- **Domain entities** contain business logic and enforce invariants
- **Value objects** are immutable and validated in constructors
- **Domain services** handle complex operations spanning multiple entities
- **Repository interfaces** define data access contracts without implementation
- **FORBIDDEN**: External dependencies, framework annotations, infrastructure concerns

#### Application Layer Structure (MANDATORY)

**Application layer package organization:**

```kotlin
application /
├── usecase/                         # Use case implementations
│   ├── search/                      # Search use cases
│   │   ├── SearchProductsUseCase.kt # Product search implementation
│   │   ├── FilterProductsUseCase.kt # Product filtering implementation
│   │   └── GetProductDetailUseCase.kt # Product detail retrieval
│   ├── pricing/                     # Pricing use cases
│   │   ├── CalculatePriceUseCase.kt # Price calculation implementation
│   │   └── ApplyDiscountUseCase.kt  # Discount application
│   └── availability/                # Availability use cases
│       └── CheckAvailabilityUseCase.kt # Availability checking
├── dto/                            # Data transfer objects
│   ├── request/                    # Request DTOs
│   │   ├── SearchRequest.kt        # Search request DTO
│   │   ├── FilterRequest.kt        # Filter request DTO
│   │   └── ProductDetailRequest.kt # Product detail request DTO
│   └── response/                   # Response DTOs
│       ├── SearchResponse.kt       # Search response DTO
│       ├── ProductResponse.kt      # Product response DTO
│       └── AvailabilityResponse.kt # Availability response DTO
├── mapper/                         # DTO to domain mapping
│   ├── ProduktMapper.kt           # Product mapping logic
│   ├── SearchMapper.kt            # Search mapping logic
│   └── PricingMapper.kt           # Pricing mapping logic
└── service/                        # Application services
├── ProductApplicationService.kt # Product application service
├── SearchApplicationService.kt  # Search application service
└── PricingApplicationService.kt # Pricing application service
```

**Application Layer Rules (MANDATORY):**

- **Use cases** orchestrate domain operations for specific business scenarios
- **DTOs** define contracts for external communication
- **Mappers** translate between DTOs and domain objects
- **Application services** coordinate multiple use cases
- **FORBIDDEN**: Business logic in application services, direct database access

#### Adapter Layer Structure (MANDATORY)

**Adapter layer package organization:**

```kotlin
adapter /
├── web/                            # Web adapters (REST controllers)
│   ├── ProductController.kt        # Product REST endpoint
│   ├── SearchController.kt         # Search REST endpoint
│   ├── PricingController.kt        # Pricing REST endpoint
│   └── HealthController.kt         # Health check endpoint
├── persistence/                    # Database adapters
│   ├── mongodb/                    # MongoDB implementations
│   │   ├── MongoProduktRepository.kt # MongoDB product repository
│   │   ├── MongoSearchRepository.kt  # MongoDB search repository
│   │   ├── entity/                   # MongoDB entities
│   │   │   ├── MongoProdukt.kt      # MongoDB product entity
│   │   │   └── MongoSearchResult.kt # MongoDB search result
│   │   └── mapper/                   # Entity mapping
│   │       ├── ProduktEntityMapper.kt # Product entity mapper
│   │       └── SearchEntityMapper.kt  # Search entity mapper
├── messaging/                      # Message adapters
│   ├── kafka/                      # Kafka implementations
│   │   ├── ProductEventPublisher.kt # Product event publisher
│   │   ├── SearchEventPublisher.kt  # Search event publisher
│   │   └── consumer/                 # Event consumers
│   │       ├── PriceUpdateConsumer.kt # Price update consumer
│   │       └── ProductUpdateConsumer.kt # Product update consumer
└── external/                       # External service adapters
├── pricing/                    # External pricing service
│   ├── PricingServiceClient.kt # Pricing service client
│   └── PricingServiceMapper.kt # Pricing service mapper
└── inventory/                  # External inventory service
├── InventoryServiceClient.kt # Inventory service client
└── InventoryServiceMapper.kt # Inventory service mapper
```

**Adapter Layer Rules (MANDATORY):**

- **Controllers** handle HTTP concerns and delegate to application services
- **Repository implementations** provide data access using specific technologies
- **Entity mappers** translate between database entities and domain objects
- **Message adapters** handle cross-SCS communication
- **FORBIDDEN**: Business logic in adapters, domain object contamination

### Frontend Code Organization (MANDATORY)

#### Multi-App Structure (MANDATORY)

**Frontend application organization:**

```typescript
frontend / src /
├── apps /
#
Application
entry
points
│   ├── produktliste /
#
Product
listing
application
│   │   ├── main.ts
#
Entry
point
│   │   ├── App.vue
#
Root
component
│   │   ├── router /
#
App - specific
routing
│   │   └── components /
#
App - specific
components
│   └── tool /
#
Tool
application
│       ├── main.ts
#
Entry
point
│       ├── App.vue
#
Root
component
│       ├── router /
#
App - specific
routing
│       └── components /
#
App - specific
components
├── shared /
#
Shared
components
and
utilities
│   ├── components /
#
Reusable
UI
components
│   │   ├── base /
#
Base
components(buttons, inputs)
│   │   ├── layout /
#
Layout
components(header, nav)
│   │   ├── business /
#
Business
components(product
cards
)
│   │   └── utility /
#
Utility
components(loading, error)
│   ├── styles /
#
Shared
styles
and
design
system
│   │   ├── variables.scss
#
SCSS
variables
│   │   ├── mixins.scss
#
SCSS
mixins
│   │   └── components.scss
#
Component
styles
│   └── utils /
#
Shared
utilities
│       ├── formatters.ts
#
Data
formatting
utilities
│       ├── validators.ts
#
Input
validation
utilities
│       └── constants.ts
#
Application
constants
├── api /
#
API
layer
│   ├── clients /
#
HTTP
client
configurations
│   │   ├── productClient.ts
#
Product
API
client
│   │   ├── searchClient.ts
#
Search
API
client
│   │   └── baseClient.ts
#
Base
HTTP
client
│   ├── services /
#
API
service
abstractions
│   │   ├── ProductService.ts
#
Product
API
service
│   │   ├── SearchService.ts
#
Search
API
service
│   │   └── PricingService.ts
#
Pricing
API
service
│   ├── types /
#
API
type definitions
│   │   ├── ProductTypes.ts
#
Product
API
types
│   │   ├── SearchTypes.ts
#
Search
API
types
│   │   └── ResponseTypes.ts
#
Common
response
types
│   └── mappers /
#
API
response
mappers
│       ├── ProductMapper.ts
#
Product
response
mapper
│       └── SearchMapper.ts
#
Search
response
mapper
├── composables /
#
Business
logic
composables
│   ├── useProductSearch.ts
#
Product
search
composable
│   ├── useProductFilter.ts
#
Product
filtering
composable
│   ├── useProductDetail.ts
#
Product
detail
composable
│   ├── usePricing.ts
#
Pricing
composable
│   └── useAuthentication.ts
#
Authentication
composable
└── store /
#
Vuex
state
management
    ├── index.ts
#
Store
configuration
    ├── modules /
#
Store
modules
    │   ├── products.ts
#
Product
state
module
    │   ├── search.ts
#
Search
state
module
    │   ├── filters.ts
#
Filter
state
module
    │   └── user.ts
#
User
state
module
    └── types /
#
Store
type definitions
        ├── ProductState.ts
#
Product
state
types
        ├── SearchState.ts
#
Search
state
types
        └── RootState.ts
#
Root
state
types
```

**Frontend Layer Rules (MANDATORY):**

- **Apps** contain application-specific entry points and configuration
- **Shared components** provide reusable UI elements across applications
- **API layer** abstracts backend communication with type safety
- **Composables** contain reactive business logic
- **Store modules** manage application state with strict typing
- **FORBIDDEN**: Business logic in components, direct API calls from components

### File Naming Conventions (MANDATORY)

#### Backend Naming Standards (MANDATORY)

**Kotlin file naming:**

- **Entities**: PascalCase with domain suffix (e.g., `Produkt.kt`, `SearchQuery.kt`)
- **Value Objects**: PascalCase with descriptive name (e.g., `ProduktId.kt`, `Price.kt`)
- **Services**: PascalCase with Service suffix (e.g., `ProduktSearchService.kt`)
- **Repositories**: PascalCase with Repository suffix (e.g., `ProduktRepository.kt`)
- **Controllers**: PascalCase with Controller suffix (e.g., `ProductController.kt`)
- **DTOs**: PascalCase with Request/Response suffix (e.g., `SearchRequest.kt`)
- **Tests**: Same as class name with Test suffix (e.g., `ProduktTest.kt`)

**Package naming:**

- **All lowercase** with descriptive names (e.g., `domain.model.produkt`)
- **No abbreviations** except for well-known terms (e.g., `dto`, `api`)
- **Logical grouping** by domain concepts

#### Frontend Naming Standards (MANDATORY)

**TypeScript/Vue file naming:**

- **Components**: PascalCase with descriptive names (e.g., `ProductCard.vue`, `SearchFilter.vue`)
- **Composables**: camelCase with use prefix (e.g., `useProductSearch.ts`)
- **Services**: PascalCase with Service suffix (e.g., `ProductService.ts`)
- **Types**: PascalCase with descriptive names (e.g., `ProductTypes.ts`)
- **Store modules**: camelCase with domain name (e.g., `products.ts`, `search.ts`)
- **Tests**: Same as file name with `.spec` or `.test` suffix

**Directory naming:**

- **All lowercase** with hyphens for multi-word names (e.g., `product-detail`)
- **Descriptive names** that clearly indicate purpose
- **Consistent structure** across all applications

### Module Organization Principles (MANDATORY)

#### Dependency Management (MANDATORY)

**Module dependency rules:**

- **Domain modules** have NO external dependencies
- **Application modules** depend only on domain modules
- **Adapter modules** can depend on application and domain modules
- **Frontend modules** follow layered dependency patterns
- **FORBIDDEN**: Circular dependencies, cross-layer dependencies

#### Cross-SCS Boundaries (MANDATORY)

**Integration module organization:**

- **Event schemas** in separate versioned modules
- **API contracts** defined in shared contract modules
- **Cross-SCS types** isolated in integration packages
- **NO direct module dependencies** between SCS instances
- **FORBIDDEN**: Shared business logic modules, runtime dependencies

#### Module Cohesion Rules (MANDATORY)

**High cohesion principles:**

- **Related functionality** grouped in same module
- **Single responsibility** per module
- **Clear module interfaces** with minimal public APIs
- **Business boundaries** align with module boundaries
- **FORBIDDEN**: God modules, mixed concerns, unclear module purposes

## Frontend Development Guidelines

#### Composition API (MANDATORY)

**All new components MUST use the Composition API with script setup syntax:**

- Import reactive functions (ref, computed, onMounted) from Vue
- Use Vuex store integration with useStore composable
- Define clear TypeScript interfaces for component data structures
- Implement reactive references for component state
- Use computed properties for derived state
- Implement lifecycle hooks for component initialization

**FORBIDDEN practices:**

- Options API for new components (data, methods, computed properties)
- Legacy Vue 2 patterns and syntax
- Mixing Options API and Composition API within same component

#### TypeScript Integration (MANDATORY)

**Strict typing requirements for all components:**

- Define clear Props interfaces with optional properties marked explicitly
- Define Emits interfaces with proper event signatures
- Use withDefaults for component props with default values
- Implement type-safe event emission with proper typing
- Ensure all component interactions are type-checked

**FORBIDDEN practices:**

- Untyped props using array syntax
- Untyped event emissions
- Any type usage without justification
- Bypassing TypeScript type checking

### Component Architecture (MANDATORY)

#### Component Hierarchy Standards

**Organize components in clear functional categories:**

- **Base components:** Reusable UI components (buttons, inputs, form elements)
- **Layout components:** Page structure components (headers, navigation, footers)
- **Business components:** Domain-specific components (Produktkachel for single product display, Produktliste for product grid/list, Produktfilter for filtering interface)
- **Utility components:** Cross-cutting components (loading states, error handling)

#### Props and Events Pattern

**Component interface design requirements:**

- Define clear Props interfaces with readonly data inputs, configuration options, and optional state properties
- Define Emits interfaces with specific event signatures for user actions and state changes
- Separate data input, configuration, and state concerns in prop definitions
- Use specific event types rather than generic handlers

**FORBIDDEN practices:**

- Mixing data and configuration in single prop objects
- Passing functions as props instead of using events
- Generic or unclear prop and event naming

#### Single Responsibility Components

**Each component MUST focus on a single concern:**

- Components should handle only presentation logic for their specific domain
- Avoid combining multiple responsibilities (display, filtering, API calls) in one component
- Use composition to combine multiple single-purpose components
- Maintain clear separation between data, logic, and presentation layers

### Composables & Business Logic (MANDATORY)

#### Composables Pattern

**Implement reusable business logic in composables following these requirements:**

- Create composables in dedicated files with clear naming (useProductFilter, useProductTracking, etc.)
- Define reactive state using ref with proper TypeScript interfaces
- Implement logic functions for state manipulation (applyFilter, clearFilter)
- Return readonly reactive state and manipulation functions
- Use proper component import and destructuring patterns
- Ensure composables are testable and reusable across components

#### State Management Integration

**Vuex integration requirements for typed state management:**

- Define clear state interfaces with all required properties
- Implement namespaced modules for logical separation
- Create typed getters for computed state values
- Define mutations for synchronous state changes
- Implement async actions for API integration and complex operations
- Use proper ActionContext typing for all store actions
- Ensure all store operations are type-safe and follow established patterns

### Template and Styling Standards

#### Template Organization

**Template organization requirements:**

- Use semantic HTML elements (article, header, section, footer) for content structure
- Implement clear CSS class naming with BEM methodology
- Group related template sections with descriptive comments
- Use proper accessibility attributes (alt text, button types)
- Avoid flat, unstructured div-based layouts

#### SCSS/Sass Integration

**SCSS/Sass integration requirements:**

- Use scoped styling to prevent CSS bleeding
- Import design system variables and mixins for consistency
- Implement BEM naming methodology for CSS classes
- Use design system spacing and color variables
- Include responsive design mixins for grid layouts
- Implement state modifiers for component variants (loading, selected, etc.)
- Use smooth transitions for interactive elements

### Error Handling & User Experience

#### Error Boundaries

**Error handling requirements:**

- Implement graceful error boundaries with proper user feedback
- Use reactive error state management with TypeScript typing
- Integrate with Vuex store for centralized error handling
- Use ref() for loading states and error tracking
- Implement try-catch-finally patterns for async operations
- Use computed properties for derived error states
- Use conditional rendering for loading states and product grids
- Implement proper template structure with semantic HTML
- Provide retry buttons for error recovery
- Use semantic loading indicators and messages

### Performance Best Practices

#### Lazy Loading & Code Splitting

**Component lazy loading requirements:**

- Import defineAsyncComponent from Vue for performance optimization
- Lazy load heavy components like ProductDetailsModal and ProductComparison
- Use dynamic imports for code splitting and performance improvement

- Configure route-level code splitting with dynamic imports
- Create routes array with path and lazy-loaded component definitions
- Configure dynamic product detail routes with lazy-loaded components
- Use route parameters for product identification

#### Reactivity Optimization

**Reactivity optimization requirements:**

- Use shallowRef for large arrays that don't need deep reactivity
- Import appropriate Vue reactivity functions (shallowRef, markRaw)

- Mark non-reactive objects using markRaw for performance optimization
- Use computed properties with proper reactive dependencies for expensive calculations
- Implement proper filtering, mapping, and sorting chains for data processing

## Frontend-Backend Integration

### API Communication Patterns

The frontend communicates with the backend through well-defined REST API endpoints using Axios HTTP client with type-safe patterns.

#### HTTP Client Configuration

**Client-side HTTP client configuration requirements:**

- Configure Axios with appropriate base URL, timeout, and default headers
- Implement request interceptors for automatic authentication token injection
- Implement response interceptors for centralized error handling
- Handle 401 authentication errors with proper redirects
- Use environment variables for API configuration

#### SSR HTTP Client Configuration

**SSR HTTP client configuration requirements:**

- Configure separate Axios instance for server-side rendering
- Use appropriate backend API URL with shorter timeout for SSR performance
- Include server identification headers for request tracking
- Separate client-side and server-side HTTP configurations

### API Integration Patterns

#### Product API Integration

**API integration pattern requirements:**

- Define type-safe API functions with proper TypeScript interfaces
- Use const assertions for API endpoint constants
- Separate SSR and client-side API calls with appropriate HTTP clients
- Implement proper parameter typing for filter objects
- Export functions with clear naming conventions for different use cases
- Export API functions with descriptive names and proper parameter typing
- Define comprehensive response interfaces with all required fields
- Implement async functions with proper Promise typing
- Handle response data extraction with type safety

### Data Flow Architecture

#### Client-Side Data Flow

**Client-side data flow:**
User Interaction → Vue Component → Composable → Vuex Store → API Call → Backend → Response → Response Handler → Store Mutation → Component Update → User Interface

#### SSR Data Flow

**SSR data flow:**
Browser Request → Fastify Server → Vue SSR → API Call → Quarkus Backend → MongoDB → JSON Response → Store Hydration → SSR Renderer → HTML Response

### State Management Integration

#### Vuex Store with API Integration

**Vuex store integration requirements:**

- Import Module and ActionContext types from Vuex
- Import API functions for store integration
- Define comprehensive state interfaces with all required properties
- Configure namespaced modules for logical separation
- Implement proper state initialization with all required fields
- Create mutations for SET_LOADING, SET_PRODUCTS, and SET_ERROR
- Implement actions with proper async/await patterns
- Use commit() for state updates and proper error handling
- Implement proper loading state management with commit calls
- Export modules with clear naming conventions

### Error Handling Patterns

#### Centralized Error Handling

**Centralized error handling requirements:**

- Create custom ApiError class extending Error with status codes
- Implement error handling functions that distinguish between server errors, network errors, and unknown errors
- Provide composables for consistent error handling across components
- Integrate with notification systems for user feedback
- Use proper TypeScript typing for error handling patterns

## API Guidelines

### REST API Design Standards (MANDATORY)

The Finden SCS exposes **RESTful APIs** following industry best practices and consistent patterns:

#### URL Structure & Naming (MANDATORY)

**URL Design Principles:**

```
Base URL: https://api.finden-scs.com/v1
Resource Collection: /products
Resource Instance: /products/{productId}
Sub-Resources: /products/{productId}/availability
Action Resources: /search/products
```

**URL Naming Rules (MANDATORY):**

- **Use nouns, not verbs** for resource names (`/products`, not `/getProducts`)
- **Use plural nouns** for collections (`/products`, `/search-results`)
- **Use kebab-case** for multi-word resources (`/search-results`, `/product-categories`)
- **Use path parameters** for resource identification (`/products/{productId}`)
- **Use query parameters** for filtering and pagination (`?page=1&size=20&category=electronics`)
- **FORBIDDEN**: Verbs in URLs, camelCase in paths, nested resources beyond 2 levels

**Resource URL Examples:**

```
GET /v1/products                    # Get product collection
GET /v1/products/{productId}        # Get specific product
GET /v1/products/{productId}/availability # Get product availability
POST /v1/search/products            # Search products
GET /v1/classifications             # Get product classifications
GET /v1/classifications/{classId}/products # Get products in classification
```

#### HTTP Methods & Semantics (MANDATORY)

**HTTP Method Usage (CRITICAL):**

- **GET**: Retrieve resources (idempotent, cacheable, no side effects)
- **POST**: Create resources or perform non-idempotent operations (search, calculations)
- **PUT**: Replace entire resource (idempotent, requires full representation)
- **PATCH**: Partial resource updates (not used in current API)
- **DELETE**: Remove resources (not implemented in read-only product catalog)
- **FORBIDDEN**: GET requests with side effects, POST for idempotent operations

**Status Code Standards (MANDATORY):**

```
Success Codes:
200 OK          - Successful GET, PUT operations
201 Created     - Successful POST creation
204 No Content  - Successful DELETE, empty responses

Client Error Codes:
400 Bad Request     - Invalid request format, validation errors
401 Unauthorized    - Missing or invalid authentication
403 Forbidden      - Valid auth but insufficient permissions
404 Not Found      - Resource does not exist
422 Unprocessable  - Valid format but business rule violations

Server Error Codes:
500 Internal Error  - Unexpected server errors
502 Bad Gateway    - External service failures
503 Service Unavailable - Temporary service overload
504 Gateway Timeout - External service timeout
```

#### Request & Response Format (MANDATORY)

**JSON Request Format Standards:**

```json
// Search Products Request
POST /v1/search/products
{
  "query": "elektronik laptop",
  "filters": {
    "klassifikationId": "electronics.computers",
    "priceRange": {
      "min": 500.00,
      "max": 2000.00
    },
    "availability": {
      "deliveryDateFrom": "2024-01-15",
      "deliveryDateTo": "2024-02-15"
    }
  },
  "pagination": {
    "page": 1,
    "size": 20
  },
  "sorting": {
    "field": "price",
    "direction": "asc"
  }
}
```

**JSON Response Format Standards:**

```json
// Successful Response
{
  "status": "success",
  "data": {
    "products": [
      ...
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "totalElements": 156,
      "totalPages": 8
    }
  },
  "meta": {
    "timestamp": "2024-01-15T10:30:00Z",
    "requestId": "req-12345",
    "version": "1.0"
  }
}

// Error Response
{
  "status": "error",
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid search parameters",
    "details": [
      {
        "field": "priceRange.min",
        "message": "Must be greater than 0"
      }
    ]
  },
  "meta": {
    "timestamp": "2024-01-15T10:30:00Z",
    "requestId": "req-12345",
    "version": "1.0"
  }
}
```

**Response Format Rules (MANDATORY):**

- **Consistent envelope structure** with status, data, error, and meta fields
- **Null field handling** - omit optional null fields from responses
- **Date/time format** - use ISO 8601 UTC format (`2024-01-15T10:30:00Z`)
- **Decimal numbers** - use strings for precise monetary values
- **FORBIDDEN**: Inconsistent response structures, mixed date formats

#### API Versioning Strategy (MANDATORY)

**Versioning Approach (CRITICAL):**

- **URL path versioning** - version included in URL path (`/v1/products`)
- **Semantic versioning** - Major.Minor.Patch format (1.0.0, 1.1.0, 2.0.0)
- **Backward compatibility** - maintain compatibility within major versions
- **Deprecation policy** - 6-month minimum notice for version retirement

**Version Support Policy:**

```
Current Version: v1.2.0
Supported Versions: v1.0.x, v1.1.x, v1.2.x
Deprecated: (none currently)
End-of-Life: (none currently)
```

**Breaking Change Policy (MANDATORY):**

- **Major version bump** required for breaking changes
- **Breaking changes include**: Field removal, type changes, required field additions
- **Non-breaking changes**: Optional field additions, endpoint additions
- **Version negotiation** supported via Accept-Version header

#### Error Handling & Validation (MANDATORY)

**Input Validation Standards (CRITICAL):**

```json
// Validation Error Response
{
  "status": "error",
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Request validation failed",
    "details": [
      {
        "field": "query",
        "value": "",
        "message": "Search query cannot be empty",
        "code": "REQUIRED_FIELD"
      },
      {
        "field": "pagination.size",
        "value": 500,
        "message": "Page size must be between 1 and 100",
        "code": "INVALID_RANGE"
      }
    ]
  }
}
```

**Business Logic Error Standards:**

```json
// Business Logic Error Response
{
  "status": "error",
  "error": {
    "code": "PRODUCT_NOT_AVAILABLE",
    "message": "Product is not available for the requested delivery date",
    "details": {
      "productId": "prod-12345",
      "requestedDeliveryDate": "2024-01-10",
      "nextAvailableDate": "2024-01-20"
    }
  }
}
```

**Error Code Standards (MANDATORY):**

- **VALIDATION_ERROR**: Request format or validation failures
- **AUTHENTICATION_ERROR**: Authentication-related failures
- **AUTHORIZATION_ERROR**: Permission-related failures
- **RESOURCE_NOT_FOUND**: Requested resource does not exist
- **BUSINESS_RULE_VIOLATION**: Business logic constraint violations
- **EXTERNAL_SERVICE_ERROR**: External service integration failures
- **RATE_LIMIT_EXCEEDED**: Client rate limiting violations

#### Pagination & Filtering (MANDATORY)

**Pagination Standards (CRITICAL):**

```json
// Pagination Request
{
  "pagination": {
    "page": 1,
    // 1-based page numbering
    "size": 20
    // Items per page (max 100)
  }
}

// Pagination Response
{
  "data": {
    "products": [
      ...
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "totalElements": 156,
      "totalPages": 8,
      "hasNext": true,
      "hasPrevious": false
    }
  }
}
```

**Filtering Standards (MANDATORY):**

```json
// Complex Filtering Example
{
  "filters": {
    "klassifikationId": "electronics.computers.laptops",
    "priceRange": {
      "min": 500.00,
      "max": 2000.00
    },
    "availability": {
      "deliveryDateFrom": "2024-01-15",
      "deliveryDateTo": "2024-02-15",
      "regions": [
        "DE",
        "AT",
        "CH"
      ]
    },
    "attributes": {
      "brand": [
        "Apple",
        "Dell",
        "Lenovo"
      ],
      "screenSize": {
        "min": 13,
        "max": 17
      }
    }
  }
}
```

**Sorting Standards (MANDATORY):**

```json
// Single Field Sorting
{
  "sorting": {
    "field": "price",
    "direction": "asc"
    // "asc" or "desc"
  }
}

// Multi-Field Sorting
{
  "sorting": [
    {
      "field": "availability.deliveryDate",
      "direction": "asc"
    },
    {
      "field": "price",
      "direction": "asc"
    }
  ]
}
```

### Cross-SCS API Communication (MANDATORY)

#### Integration API Standards (CRITICAL)

**Cross-SCS Request Headers (MANDATORY):**

```
X-SCS-Source: finden-product-search    # Source SCS identifier
X-SCS-Correlation-ID: uuid-12345       # Request correlation for tracing
X-SCS-Version: 1.2.0                   # API version
Content-Type: application/json
Accept: application/json
```

**Cross-SCS Authentication (CRITICAL):**

- **Service-to-service tokens** - JWT tokens for SCS authentication
- **Token validation** - validate tokens at API gateway level
- **Mutual TLS** - for high-security SCS communications
- **NO user authentication** - handled by infrastructure layer
- **FORBIDDEN**: User tokens in SCS-to-SCS communication

#### Event-Driven API Integration (MANDATORY)

**Event Publishing API Standards:**

```json
// Product Event Publication
POST /v1/events/products
{
  "eventType": "ProductAvailabilityChanged",
  "eventVersion": "1.0",
  "source": "finden-product-search",
  "timestamp": "2024-01-15T10:30:00Z",
  "correlationId": "correlation-12345",
  "data": {
    "productId": "prod-12345",
    "oldAvailability": {
      "status": "available",
      "deliveryDate": "2024-01-20"
    },
    "newAvailability": {
      "status": "unavailable",
      "nextAvailableDate": "2024-02-15"
    }
  }
}
```

**Event Schema Evolution (MANDATORY):**

- **Avro schemas** for event payload definitions
- **Schema registry** for centralized schema management
- **Backward compatibility** required for schema changes
- **Event versioning** for breaking schema changes

### API Documentation Standards (MANDATORY)

#### OpenAPI Specification (MANDATORY)

**OpenAPI 3.0 Requirements:**

- **Complete API specification** with all endpoints documented
- **Schema definitions** for all request/response types
- **Example payloads** for all operations
- **Error response documentation** with all possible error codes
- **Authentication schemes** properly documented

**Documentation Structure:**

```yaml
openapi: 3.0.3
info:
  title: Finden Product Search API
  version: 1.2.0
  description: Product search and discovery API for Finden SCS
servers:
  - url: https://api.finden-scs.com/v1
    description: Production server
paths:
  /products:
    get:
      summary: List products
      parameters: [ ... ]
      responses: [ ... ]
  /search/products:
    post:
      summary: Search products
      requestBody: [ ... ]
      responses: [ ... ]
```

#### API Evolution & Deprecation (MANDATORY)

**Deprecation Process (CRITICAL):**

1. **Announce deprecation** - 6 months advance notice
2. **Add deprecation headers** - `Sunset: 2024-07-15T00:00:00Z`
3. **Client migration support** - provide migration guides
4. **Monitor usage** - track deprecated endpoint usage
5. **Gradual shutdown** - throttle deprecated endpoints before removal

**API Evolution Guidelines:**

- **Additive changes** - safe to add new optional fields, new endpoints
- **Modification changes** - require new API version for required fields
- **Removal changes** - require major version bump and deprecation process
- **Client compatibility** - ensure existing clients continue working

### API Security Standards (MANDATORY)

#### Request Validation (CRITICAL)

**Input Sanitization Requirements:**

- **Schema validation** - validate all requests against OpenAPI schema
- **Size limits** - maximum request body size (1MB), parameter lengths
- **Type validation** - strict type checking for all parameters
- **Range validation** - numeric ranges, date ranges, string lengths
- **Pattern validation** - regex patterns for IDs, emails, URLs

**Security Headers (MANDATORY):**

```
Request Security Headers:
Content-Type: application/json
X-Content-Type-Options: nosniff
X-Frame-Options: DENY

Response Security Headers:
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1; mode=block
Strict-Transport-Security: max-age=31536000; includeSubDomains
```

#### Rate Limiting & Throttling (MANDATORY)

**Rate Limiting Rules:**

- **Per-client limits** - 1000 requests per minute per API key
- **Global limits** - 10,000 requests per minute system-wide
- **Burst limits** - Allow 50 requests in 10-second window
- **Graceful degradation** - return 429 Too Many Requests with Retry-After header

**Rate Limiting Response:**

```json
HTTP 429 Too Many Requests
Retry-After: 60

{
"status": "error",
"error": {
"code": "RATE_LIMIT_EXCEEDED",
"message": "API rate limit exceeded",
"details": {
"limit": 1000,
"remaining": 0,
"resetTime": "2024-01-15T10:31:00Z"
}
}
}
```

## Testing Strategy (MANDATORY)

### Framework Stack

#### Frontend Testing

- **Framework:** Jest 28.1.1 with Vue Test Utils and TypeScript support
- **Component Testing:** Vue Test Utils for component mounting and interaction
- **API Mocking:** Mock Service Worker (MSW) for API integration testing
- **E2E Testing:** Playwright for comprehensive user flow testing

#### Backend Testing

- **Unit Testing:** JUnit 5 Jupiter with Kotlin support
- **Mocking:** Mockk 1.14.4 (Kotlin-specific)
- **Assertions:** Strikt 0.35.1 (Kotlin-specific)
- **Integration:** @QuarkusTest with TestContainers 1.21.3 (MongoDB & Kafka)
- **Architecture:** ArchUnit 1.4.1 for layer compliance
- **REST Testing:** REST Assured with Kotlin extensions

### Test Organization & Execution

#### Test Structure (Tag-Based)

- **Unit Tests:** `@Tag("unit")` - Fast, isolated tests with no external dependencies
- **Integration Tests:** `@Tag("integration")` - Tests requiring TestContainers, databases, or external services
- **E2E Tests:** Complete user journeys from frontend to backend
- **Architecture Tests:** ArchUnit validation of layer dependencies

#### Test Execution Strategy

- **Fast Feedback:** `unitTest` gradle task for isolated tests only
- **Comprehensive Testing:** `integrationTest` task for external dependency tests
- **Complete Suite:** `test` task for all tests
- **E2E Testing:** Playwright tests for full user journeys

### Testing Requirements (MANDATORY)

#### Universal Standards

1. **BDD Structure:** ALL tests MUST use Given-When-Then
2. **Test Isolation:** Each test completely independent
3. **Coverage:** 80% minimum, 95% for business logic
4. **No Production Dependencies:** Test utils in test package only
5. **Public Interface Testing:** No reflection or private method testing

#### Frontend Testing Patterns

- **Component Testing:** Mount components with proper props and mock stores
- **API Integration:** Use MSW for API mocking with proper request handlers
- **Composables Testing:** Test reactive state management and function behavior
- **E2E Testing:** Use data-testid selectors and test complete user flows

#### Backend Testing Patterns

- **Unit Tests:** Use @Test annotation with descriptive function names in backticks
- **Integration Tests:** Use @QuarkusTest annotation for full application context
- **Architecture Tests:** Use ArchUnit for layer dependency validation
- **TestContainers:** Real database integration with external services

## Frontend Build & Deployment

### Build Configuration

#### Multi-App Build System

- Configure code splitting with vendor and common chunk separation
- Set chunk naming conventions and priority levels
- Configure development server with appropriate port and API proxy settings
- Enable change origin for cross-origin API requests
- Use environment variables to determine app name and rendering mode
- Configure dynamic entry points based on app type

### SSR Server Deployment

#### Production SSR Server

**Production SSR server requirements:**

- Import and create Fastify server with proper configuration
- Configure host and port based on environment (localhost for dev, 0.0.0.0 for production)
- Implement proper error handling with server logging and process exit
- Handle graceful shutdown with SIGTERM signal processing
- Implement startup error catching with appropriate error logging

#### Docker Configuration

**Frontend Docker configuration requirements:**

- Use multi-stage builds with Node.js Alpine base images
- Separate builder and runtime stages for optimized image size
- Copy package files and install production dependencies only
- Build application artifacts in builder stage
- Copy only necessary files to runtime stage
- Expose appropriate port and define proper start command

### Environment Configuration

#### Environment Variables

**Environment variable requirements:**

- Configure NODE_ENV for proper production settings
- Set API base URLs for client and server communications
- Configure server ports for SSR server
- Set up monitoring endpoints and service identification

## Frontend Performance & Security

### Performance Optimization

#### Bundle Size Optimization

**Bundle size optimization requirements:**

- Enable tree-shaking with usedExports and sideEffects configuration
- Configure code splitting with appropriate chunk limits
- Set up cache groups for framework-specific chunks (Vue, Vuex, Vue Router)
- Separate vendor libraries into dedicated chunks for better caching
- Prioritize framework chunks over generic library chunks
- Optimize initial and async request limits for performance

#### Performance Monitoring

**Performance monitoring requirements:**

- Implement Web Vitals tracking for Core Web Vitals metrics (CLS, FID, FCP, LCP, TTFB)
- Use dynamic imports for performance measurement libraries
- Implement custom metric tracking with Performance API
- Check for browser API availability before usage
- Track application-specific performance metrics

### Security Standards

#### Content Security Policy

**Content Security Policy requirements:**

- Define restrictive default source directives
- Configure script sources with minimal unsafe-inline usage
- Allow specific trusted domains for analytics and external services

#### XSS Prevention

- Use DOMPurify for HTML sanitization with allowed tags and attributes
- Implement input sanitization for user-provided content
- Configure allowed HTML tags (p, br, strong, em, ul, ol, li)
- Set allowed attributes (class only)
- Remove dangerous characters from input strings

#### Authentication Integration

- Create authentication composables for token management
- Store authentication tokens in localStorage
- Implement computed properties for authentication state
- Create async login functions with proper credential handling
- Integrate with authentication API endpoints
- Set token values and localStorage on successful login
- Clear tokens and localStorage on logout
- Return authentication state and functions from composables

## Performance Standards

### Critical Performance Requirements (MANDATORY)

#### Algorithm Complexity Standards

- **NO O(n²) or higher complexity operations** in production code
- **Database operations MUST use proper indexes**
- **Collection operations MUST be optimized** for expected data sizes
- **Memory usage MUST be bounded** - no unlimited data loading

#### Database Performance (MANDATORY)

1. **Query Optimization:**

- **Required MongoDB indexes**: Create indexes on klassifikationId, verfuegbarkeiten.bestellschlussUTC, and verfuegbarkeiten.liefertag for optimal query performance

2. **Pagination Requirements:**

- ALL queries MUST support proper pagination
- NO full collection loading for sorting
- Database-level sorting preferred over in-memory

3. **Connection Management:**

- Connection pooling properly configured
- Query timeout limits set
- Connection leak prevention

#### Memory Management (MANDATORY)

1. **Object Lifecycle:**

- Prefer streaming over full collection materialization
- Immutable objects for value objects
- Proper resource cleanup

2. **Collection Processing:**

- Avoid multiple intermediate collections
- Use sequence operations for large datasets
- Monitor heap usage in large operations

#### Performance Anti-Patterns (FORBIDDEN)

**Forbidden performance anti-patterns:**

- Never disable pagination for in-memory sorting (causes memory exhaustion)
- Avoid O(n) operations in comparators (use HashMap lookups instead)
- Never load entire collections without limits (always use pagination)
- Prevent memory exhaustion through proper collection handling

#### Performance Monitoring (MANDATORY)

- **API Endpoints:** P95 < 300ms for standard operations, P95 < 500ms for complex queries
- **SSR Pages:** < 200ms response time for server-side rendered pages
- **Database Queries:** < 200ms average, with proper indexing on search fields
- **Memory Usage:** < 80% heap under normal load
- **No memory leaks** in long-running operations

## Kotlin Development Guidelines

### Language Standards

#### Immutability (MANDATORY)

- **Use val, not var** for all value object properties
- **Implement validation in init blocks** for value objects
- **Make data classes immutable** by using only val properties
- **FORBIDDEN**: Mutable value objects that break immutability principles

#### Null Safety (MANDATORY)

- Use Kotlin's null safety features properly
- Prefer `?.let` over explicit null checks
- Never use `!!` unless absolutely necessary with clear justification
- Use `Optional` patterns for domain modeling

#### Collection Operations

- **PREFERRED**: Use functional collection operations (filter, map, take)
- **Chain operations** for readable data transformations
- **AVOID**: Imperative loops for simple transformations that can be expressed functionally

### Error Handling

#### Domain Exceptions (MANDATORY)

- **Create specific domain exceptions** with meaningful error messages
- **Inherit from base domain exception class** for consistency
- **Include context values** in exception messages for debugging
- **Implement validation in value object constructors** using require() statements

#### Boundary Validation (CRITICAL)

- **MANDATORY**: Validate all boundary conditions to prevent crashes
- **Check for null values** and negative numbers before processing
- **Use when expressions** for comprehensive condition handling
- **FORBIDDEN**: Missing boundary checks that cause runtime crashes with invalid inputs

### MongoDB Integration

#### Repository Pattern (MANDATORY)

- **Domain layer**: Define repository interfaces with domain types only
- **Adapter layer**: Implement repository interfaces using MongoDB-specific classes
- **Use ApplicationScoped annotation** for CDI repository implementations
- **Inherit from PanacheMongoRepository** for MongoDB data access patterns

#### Entity Mapping (MANDATORY)

- MongoDB entities in adapter layer only
- Clean mapping between MongoProdukt ↔ Produkt
- No MongoDB annotations in domain layer

### Kotlin Annotation Processing Configuration

The project uses sophisticated annotation processing for framework integration:

#### AllOpen Configuration

**AllOpen plugin configuration requirements:**

- Configure CDI annotations (ConfigProperties, ApplicationScoped, Singleton, Dependent)
- Configure JAX-RS endpoints with Path annotation
- Configure persistence annotations for JPA and MongoDB entities
- Configure QuarkusTest annotations for test class compatibility
- Enable framework integration through annotation processing

#### NoArg Configuration

**NoArg plugin configuration requirements:**

- Configure custom domain annotation for no-arg constructor generation
- Configure CDI ConfigProperties for framework compatibility
- Configure persistence entity annotations for JPA and MongoDB
- Enable automatic no-arg constructor generation for framework integration

#### Custom NoArg Annotation

**Custom NoArg annotation requirements:**

- Define annotation with CLASS target and RUNTIME retention
- Apply to domain entities for framework compatibility
- Use with data classes to generate no-arg constructors automatically
- Maintain clean domain code while supporting framework requirements

#### Annotation Processing Benefits

- **Framework Integration**: Seamless CDI and JAX-RS integration
- **Persistence Layer**: Both JPA and MongoDB support configured
- **Testing Support**: QuarkusTest classes get proper initialization
- **Domain Model**: Custom NoArg annotation for clean domain code

## Code Quality Standards

### Critical Bug Prevention (MANDATORY)

#### Boundary Conditions

- **MANDATORY**: Validate all boundary conditions to prevent crashes
- **Use null-safe operators** (?.let) for safe value processing
- **Implement try-catch blocks** for parsing operations that may fail
- **Return null for invalid values** rather than throwing exceptions for negative numbers
- **Throw meaningful exceptions** with context for parsing failures

#### Price Comparison Logic

- **Use proper null handling** with null-checks before value comparisons
- **Avoid contradictory null logic** that returns true for null values in greater-than comparisons
- **Implement consistent comparison logic** that handles nullable parameters correctly

### Architecture Compliance (ENFORCED)

#### Layer Coupling (FORBIDDEN)

- **Domain layer MUST NOT reference adapter DTOs** or external data structures
- **Domain classes should only work with domain types** and interfaces
- **Keep clear separation** between domain model and adapter layer representations
- **Use proper mapping at adapter boundaries** to convert between domain and DTO types

### Quality Tools

- **Static Analysis:** Detekt 1.23.8 for Kotlin
- **Build Quality:** Gradle 8.x build verification with Kotlin DSL
- **Dependency Check:** OWASP Dependency Check 12.1.3 for vulnerability scanning
- **Version Management:** Gradle Versions Plugin 0.52.0 for dependency updates
- **Coverage:** JaCoCo 0.8.7 with 80% minimum threshold
- **Schema Management:** Avro Plugin 1.9.1 for Kafka schema handling

### Build Verification (MANDATORY)

**Build verification requirements:**

- Run clean build to ensure compilation success
- Execute Detekt static analysis for code quality
- Run all tests with coverage reporting before any commit

## Development Workflow (MANDATORY)

### SuperClaude Persona Integration

The development workflow leverages **SuperClaude personas** for specialized expertise and intelligent command selection based on context and task requirements.

#### Available Personas

**Architect Persona (`--persona-architect`)**

- **Core Belief:** "Systems evolve, design for change"
- **Primary Question:** "How will this scale & evolve?"
- **Auto-Flags:** `--think-hard`, `--arch`, `--validate`
- **Commands:** `/analyze --arch`, `/design --api --ddd`, `/build --tdd`

**Frontend Persona (`--persona-frontend`)**

- **Core Belief:** "UX determines product success"
- **Primary Question:** "How does this feel to user?"
- **Auto-Flags:** `--react`, `--interactive`, `--visual`
- **Commands:** `/build --react --magic`, `/test --e2e --pup`, `/analyze --perf`

**Backend Persona (`--persona-backend`)**

- **Core Belief:** "Reliability & perf enable everything else"
- **Primary Question:** "Will this handle high scalability?"
- **Auto-Flags:** `--api`, `--profile`, `--security`
- **Commands:** `/build --api --c7`, `/analyze --perf --profile`, `/scan --security`

**Security Persona (`--persona-security`)**

- **Core Belief:** "Threats exist everywhere, trust must be earned"
- **Primary Question:** "What could go wrong?"
- **SCS Security Scope:** Input validation, data protection, business logic security (NOT auth/authorization)
- **Auto-Flags:** `--security`, `--validate`, `--strict`
- **Commands:** `/scan --security --input-validation`, `/analyze --security --data-protection`, `/build --security`

**QA Persona (`--persona-qa`)**

- **Core Belief:** "Quality cannot be tested in, must be built in"
- **Primary Question:** "How could this break?"
- **Auto-Flags:** `--coverage`, `--validate`, `--strict`
- **Commands:** `/test --coverage --e2e`, `/analyze --quality`, `/scan --validate`

**Performance Persona (`--persona-performance`)**

- **Core Belief:** "Speed is a feature, slowness kills adoption"
- **Primary Question:** "Where is the bottleneck?"
- **Auto-Flags:** `--profile`, `--watch-perf`, `--iterate`
- **Commands:** `/analyze --profile --pup`, `/improve --perf --iterate`, `/test --benchmark`

#### Persona Auto-Activation Rules

**File-Based Activation:**

- `*.tsx|*.jsx|*.css` → Frontend Persona
- `*api*|*server*|*db*` → Backend Persona
- `*.test.*|*.spec.*` → QA Persona
- `*security*|*auth*` → Security Persona
- `*perf*|*benchmark*` → Performance Persona

**Context-Based Activation:**

- Architecture/design tasks → Architect Persona
- Input validation/data protection tasks → Security Persona
- Performance/optimization tasks → Performance Persona
- UI/UX development → Frontend Persona
- **NOTE:** Auth/authorization tasks → Defer to infrastructure (outside SCS scope)

### Daily Development Loop (AUTOMATED with SuperClaude Personas)

#### Core TDD Cycle with Intelligent Persona Activation

##### Task-Level Setup

1. **Task Selection:** `task-master next` → identify prioritized main task

- **Auto-Persona Selection**: Context analysis determines appropriate persona based on task type, file patterns, and requirements

2. **Task Context Review:** `task-master show <task-id>` → understand overall task requirements

- **Intelligent Analysis**: SuperClaude automatically applies `--think` or `--think-hard` based on task complexity
- **Domain Detection**: Auto-activates relevant persona (`--persona-architect` for system design, `--persona-frontend` for UI work, etc.)

3. **Task Status Update:** `task-master set-status --id=<task-id> --status=in-progress`

- **Context Setup**: Persona-specific environment and tools automatically configured

##### Subtask Iteration Loop

4. **For Each Subtask in Task:** Process all subtasks sequentially

a. **Subtask Selection:** Identify next pending subtask (e.g., `<task-id>.1`, `<task-id>.2`)

b. **Subtask Context:** Review specific subtask requirements and acceptance criteria

c. **RED:** Write failing BDD test aligned with subtask requirements

- **Auto-Enhancement**: QA persona (`--persona-qa`) automatically enhances test strategy
- **Smart Testing**: Appropriate test types selected based on context (unit, integration, E2E)

d. **GREEN:** Minimal code to pass subtask test

- **Persona-Guided Implementation**:
  - Frontend subtasks → `--persona-frontend --magic` (UI components)
  - Backend subtasks → `--persona-backend --c7` (API with library docs)
  - Data security subtasks → `--persona-security --strict` (input validation, data protection)
- **Tool Integration**: Automatic MCP tool activation based on needs

e. **REFACTOR:** Improve while keeping tests green

- **Auto-Quality**: Refactorer persona (`--persona-refactorer --scope=subtask --task-id=<task-id>.<subtask-id>`) automatically analyzes code quality for subtask files only
- **Performance Check**: Performance persona (`--persona-performance --profile --scope=subtask --task-id=<task-id>.<subtask-id>`) validates no regressions in subtask-specific code

f. **SUBTASK CONTEXT:** `task-master update-subtask --id=<task-id>.<subtask-id> --prompt="implementation notes"`

- **Intelligent Documentation**: Mentor persona (`--persona-mentor`) enhances context capture
- **Learning Integration**: Automatic knowledge extraction and preservation

g. **SUBTASK COMMIT:** Atomic commit with subtask reference

- **Pre-Commit Validation**: Automatic persona-enhanced quality gates run with subtask scope:
  - Data security scan (`--persona-security --scope=subtask --task-id=<task-id>.<subtask-id>`) - input validation, data protection for subtask files
  - Performance validation (`--persona-performance --scope=subtask --task-id=<task-id>.<subtask-id>`) - validate subtask-specific performance
  - Architecture compliance (`--persona-architect --scope=subtask --task-id=<task-id>.<subtask-id>`) - validate subtask layer compliance

##### Task-Level Completion

5. **Final Quality Check:** Cross-subtask validation and integration testing

- **Integration Testing**: Ensure all subtasks work together properly (`/test --integration --scope=task --task-id=<task-id>`)
- **Task-Level Validation**: Multi-persona final check ensures all quality dimensions met (`--scope=task --task-id=<task-id>`)

6. **Final Refactoring:** Cross-subtask improvements and cleanup if needed

- **Code Integration**: Refactor for consistency across subtasks (`--persona-refactorer --scope=task --task-id=<task-id>`)
- **Performance Optimization**: Task-level performance validation (`--persona-performance --scope=task --task-id=<task-id>`)

7. **Final Integration Commit:** If cross-subtask changes were made

8. **TASK COMPLETE:** `task-master set-status --id=<task-id> --status=done`

- **Completion Validation**: Verify all subtasks are completed and integrated
- **Documentation Update**: Ensure task context reflects final implementation

### Quality Gates (MANDATORY)

#### Persona-Enhanced Pre-Commit Checklist

- [ ] **Performance:** No O(n²) algorithms (`/analyze --profile --persona-performance --scope=task --task-id=<task-id>`)
- [ ] **Architecture:** No layer coupling violations (`/scan --validate --arch --persona-architect --scope=task --task-id=<task-id>`)
- [ ] **Data Security:** Input validation & data protection implemented (`/scan --security --persona-security --scope=task --task-id=<task-id>`)
- [ ] **Frontend:** UI/UX standards met (`/test --e2e --persona-frontend --pup --scope=task --task-id=<task-id>`)
- [ ] **Backend:** API reliability validated (`/test --api --persona-backend --scope=task --task-id=<task-id>`)
- [ ] **Quality:** All tests pass, coverage > 80% (`/test --coverage --persona-qa --scope=task --task-id=<task-id>`)
- [ ] **Boundaries:** All input validation implemented (`/scan --validate --strict --scope=task --task-id=<task-id>`)
- [ ] **Immutability:** Value objects properly immutable (`/analyze --code --persona-refactorer --scope=task --task-id=<task-id>`)
- [ ] **Task Context:** Implementation details logged via `task-master update-subtask` for all subtasks
- [ ] **Status Management:** Task and all subtasks properly updated

### Git Workflow

#### Commit Standards

- **Conventional Commits:** `feat:`, `fix:`, `refactor:`, `security:`, `perf:`
- **Task References:** Include task IDs in commit messages (e.g., "feat: implement JWT auth (task 1.2)")
- **Atomic Commits:** Single logical change per commit
- **Branch Naming:** `feature/`, `bugfix/`, `security/`, `perf/`

### Critical Review Focus with Persona Guidance

#### Priority 1: Data Security (`--persona-security`)

1. **Input validation** - `/scan --security --validate --scope=task --task-id=<task-id>` prevent injection attacks in task-specific code
2. **Data protection** - `/scan --security --data --scope=task --task-id=<task-id>` prevent data exposure in task implementation
3. **Business logic security** - `/analyze --security --business-logic --scope=task --task-id=<task-id>` validate domain rules for task
   **NOTE:** Auth/authorization handled by infrastructure - NOT SCS responsibility

#### Priority 2: Performance (`--persona-performance`)

1. **Algorithm complexity** - `/analyze --profile --complexity --scope=task --task-id=<task-id>` prevent O(n²) operations in task code
2. **Memory patterns** - `/analyze --profile --memory --scope=task --task-id=<task-id>` prevent exhaustion in task implementation
3. **Database optimization** - `/analyze --profile --db --scope=task --task-id=<task-id>` prevent N+1 problems in task queries
4. **Frontend performance** - `/test --benchmark --persona-frontend --scope=task --task-id=<task-id>` validate UI metrics for task features

#### Priority 3: Architecture (`--persona-architect`)

1. **Layer coupling** - `/scan --validate --arch --scope=task --task-id=<task-id>` prevent domain/DTO dependencies in task implementation
2. **Value object immutability** - `/analyze --code --immutable --scope=task --task-id=<task-id>` prevent state corruption in task entities
3. **Boundary conditions** - `/test --edge-cases --persona-qa --scope=task --task-id=<task-id>` validate inputs for task functionality
4. **System scalability** - `/design --scalability --think-hard --scope=task --task-id=<task-id>` ensure task future-proofing

#### Priority 4: Code Quality (`--persona-refactorer`)

1. **Code complexity** - `/analyze --code --complexity --scope=task --task-id=<task-id>` identify refactoring opportunities in task code
2. **Duplication elimination** - `/improve --quality --iterate --scope=task --task-id=<task-id>` reduce code debt in task implementation
3. **Test coverage** - `/test --coverage --persona-qa --scope=task --task-id=<task-id>` ensure comprehensive testing for task
4. **Documentation quality** - `/document --api --persona-mentor --scope=task --task-id=<task-id>` maintain clarity for task APIs

### Essential TaskMaster Commands

#### Core Commands

- `task-master next` - Get next available task
- `task-master show <task-id>` - View main task details and subtasks
- `task-master get-task <task-id>.<subtask-id>` - View specific subtask details
- `task-master set-status --id=<task-id> --status=<status>` - Update main task status
- `task-master set-status --id=<task-id>.<subtask-id> --status=<status>` - Update subtask status
- `task-master update-subtask --id=<task-id>.<subtask-id> --prompt="notes"` - Add implementation notes to subtask
- `task-master update-task --id=<task-id> --prompt="notes"` - Add implementation notes to main task

#### Task Structure

- **Task IDs:** Main tasks (1, 2, 3), Subtasks (1.1, 1.2), Sub-subtasks (1.1.1)
- **Status Values:** pending, in-progress, done, deferred, cancelled, blocked
- **File Structure:** `.taskmaster/tasks/tasks.json`, `.taskmaster/docs/prd.txt`

## Code Review Process

### Review Workflow & Responsibilities (MANDATORY)

The code review process integrates with **SuperClaude personas** to ensure comprehensive quality validation across all system dimensions:

#### Review Types & Triggers (MANDATORY)

**Pull Request Review Requirements:**

- **ALL code changes** require peer review before merge
- **Cross-SCS boundary changes** require architecture team review
- **Security-related changes** require security team review
- **Performance-critical changes** require performance team review
- **Database migrations** require DBA review

**Review Assignment Rules:**

- **Domain expertise** - assign reviewers based on code area (frontend, backend, infrastructure)
- **Cross-training** - include junior reviewers for knowledge sharing
- **Availability** - maximum 24-hour response time for review requests
- **Conflict resolution** - escalate to tech lead for disagreements

#### Persona-Enhanced Review Process (MANDATORY)

**Automated Persona Analysis Integration:**
Before human review, SuperClaude personas automatically analyze changes:

```
1. Context Analysis (--persona-analyzer --think-hard)
   → Identify change scope, complexity, and risk level

2. Architecture Review (--persona-architect --validate)
   → Check layer compliance, dependency patterns, SCS boundaries

3. Security Analysis (--persona-security --strict)
   → Validate input handling, data protection, boundary security

4. Performance Review (--persona-performance --profile)
   → Check algorithm complexity, memory patterns, database queries

5. Quality Assessment (--persona-qa --coverage)
   → Validate test coverage, edge cases, error handling
```

**Persona Review Integration:**

- **Automated analysis runs** before human review assignment
- **Review comments pre-populated** with persona findings
- **Quality gate enforcement** - personas block merge for critical issues
- **Human reviewers focus** on business logic and architectural decisions

### Review Checklist & Standards (MANDATORY)

#### Universal Review Checklist (CRITICAL)

**Code Quality Standards (MANDATORY):**

- [ ] **Code follows established patterns** from existing codebase
- [ ] **Naming conventions** are consistent and descriptive
- [ ] **Function/method size** is manageable (< 50 lines preferred)
- [ ] **Code complexity** is reasonable (avoid deep nesting)
- [ ] **Comments explain why, not what** - focus on business logic rationale
- [ ] **TODO comments** include task references and deadlines
- [ ] **FORBIDDEN**: Dead code, debug statements, hardcoded values

**Architecture Compliance Checklist (CRITICAL):**

- [ ] **Layer boundaries respected** - no domain/DTO mixing
- [ ] **Dependency direction correct** - domain has no outward dependencies
- [ ] **Single responsibility** - classes/modules have clear purpose
- [ ] **Interface segregation** - no fat interfaces with unused methods
- [ ] **SCS boundaries maintained** - no direct cross-SCS dependencies
- [ ] **FORBIDDEN**: Cross-layer dependencies, business logic in adapters

#### Frontend-Specific Review Checklist (MANDATORY)

**Vue.js/TypeScript Standards (MANDATORY):**

- [ ] **Composition API usage** - no Options API in new components
- [ ] **TypeScript typing** - all props, emits, and composables typed
- [ ] **Component responsibility** - single concern per component
- [ ] **Business logic location** - logic in composables, not components
- [ ] **State management** - proper Vuex integration with typed modules
- [ ] **Performance patterns** - lazy loading, code splitting, reactivity optimization
- [ ] **FORBIDDEN**: Business logic in components, untyped API calls

**Frontend Security Checklist (MANDATORY):**

- [ ] **Input sanitization** - all user inputs properly sanitized
- [ ] **XSS prevention** - no innerHTML with unsanitized content
- [ ] **CSRF protection** - proper token handling for state changes
- [ ] **Authentication integration** - correct composable usage
- [ ] **FORBIDDEN**: Direct authentication implementation, unsanitized data display

#### Backend-Specific Review Checklist (MANDATORY)

**Kotlin/Quarkus Standards (MANDATORY):**

- [ ] **Immutability** - value objects are immutable with validation
- [ ] **Null safety** - proper use of Kotlin null safety features
- [ ] **Domain logic location** - business logic in domain entities
- [ ] **Repository pattern** - clean separation of domain and persistence
- [ ] **Exception handling** - proper domain exceptions with context
- [ ] **Collection operations** - functional style preferred over imperative
- [ ] **FORBIDDEN**: Mutable value objects, business logic in controllers

**Performance Review Checklist (CRITICAL):**

- [ ] **Algorithm complexity** - no O(n²) or higher complexity operations
- [ ] **Database queries** - proper indexing and pagination
- [ ] **Memory management** - bounded collections, proper resource cleanup
- [ ] **Connection handling** - proper connection pooling and timeouts
- [ ] **FORBIDDEN**: Full collection loading, memory exhaustion patterns

#### Security Review Checklist (CRITICAL)

**Security Validation Standards (MANDATORY):**

- [ ] **Input validation** - all boundary inputs validated with proper error handling
- [ ] **Data protection** - sensitive data properly handled and encrypted
- [ ] **Authentication boundaries** - no user auth implementation in SCS
- [ ] **Authorization context** - proper consumption of infrastructure auth context
- [ ] **SQL injection prevention** - parameterized queries, no string concatenation
- [ ] **Cross-SCS security** - proper service-to-service authentication
- [ ] **FORBIDDEN**: SQL string concatenation, plaintext sensitive data storage

**GDPR Compliance Checklist (CRITICAL):**

- [ ] **Personal data minimization** - only necessary data collected
- [ ] **Data anonymization** - search analytics properly anonymized
- [ ] **Retention policies** - automatic data deletion implemented
- [ ] **User rights support** - data access/deletion capabilities
- [ ] **FORBIDDEN**: Unnecessary personal data collection, indefinite data retention

### Review Approval Process (MANDATORY)

#### Approval Requirements by Change Type (CRITICAL)

**Standard Changes (Low Risk):**

- **Requirements**: 1 peer reviewer approval
- **Examples**: Bug fixes, minor refactoring, test improvements
- **Criteria**: No architecture changes, no performance impact
- **Timeline**: 24-hour review requirement

**Significant Changes (Medium Risk):**

- **Requirements**: 2 reviewer approvals (1 senior, 1 peer)
- **Examples**: New features, API changes, dependency updates
- **Criteria**: Architecture impact, moderate complexity
- **Timeline**: 48-hour review requirement

**Critical Changes (High Risk):**

- **Requirements**: 3 reviewer approvals (1 tech lead, 1 senior, 1 domain expert)
- **Examples**: Cross-SCS changes, security modifications, performance optimizations
- **Criteria**: System-wide impact, high complexity, security implications
- **Timeline**: 72-hour review requirement with mandatory testing

**Emergency Changes (Production Fix):**

- **Requirements**: 1 senior reviewer + post-merge review
- **Examples**: Production hotfixes, critical security patches
- **Criteria**: P1 production incidents only
- **Timeline**: Immediate review, 4-hour post-merge full review

#### Review Quality Standards (MANDATORY)

**Effective Review Practices (CRITICAL):**

- **Constructive feedback** - suggest improvements, don't just identify problems
- **Code examples** - provide better alternatives when suggesting changes
- **Business context** - understand and validate business requirements
- **Testing verification** - ensure adequate test coverage for changes
- **Documentation updates** - verify docs are updated for public API changes

**Review Communication Standards:**

- **Clear feedback** - specific, actionable comments with code references
- **Respectful tone** - professional communication focused on code quality
- **Timely responses** - acknowledge reviews within SLA timeframes
- **Resolution tracking** - ensure all feedback is addressed before approval

### Cross-SCS Review Coordination (MANDATORY)

#### Integration Change Reviews (CRITICAL)

**Cross-SCS Impact Assessment:**

- **Event schema changes** - coordinate with consuming SCS teams
- **API contract modifications** - validate backward compatibility
- **Performance impact** - assess cross-SCS performance implications
- **Security boundary changes** - validate with security architecture team

**Integration Review Process:**

1. **Technical Review** - standard code review process
2. **Impact Analysis** - assess cross-SCS implications
3. **Stakeholder Notification** - inform affected SCS teams
4. **Integration Testing** - validate cross-SCS functionality
5. **Deployment Coordination** - plan rollout across SCS boundaries

#### Architecture Review Board (MANDATORY)

**Architecture Review Triggers (CRITICAL):**

- **New SCS integration patterns** require architecture board review
- **Technology stack changes** require architecture approval
- **Performance pattern changes** require performance architecture review
- **Security model changes** require security architecture review

**Review Board Composition:**

- **Technical Architect** (required)
- **Security Architect** (for security changes)
- **Performance Engineer** (for performance changes)
- **Domain Expert** (for business logic changes)

### Review Metrics & Continuous Improvement (MANDATORY)

#### Review Quality Metrics (MANDATORY)

**Effectiveness Metrics:**

- **Defect escape rate** - bugs found in production vs. code review
- **Review coverage** - percentage of changes that receive proper review
- **Review turnaround time** - time from PR submission to approval
- **Review participation** - distribution of review load across team members

**Quality Improvement Process:**

- **Monthly review retrospectives** - identify process improvements
- **Review effectiveness analysis** - correlate review quality with bug rates
- **Training identification** - identify areas where reviewers need more expertise
- **Process refinement** - update review checklists based on learnings

#### Automated Quality Gates (MANDATORY)

**Pre-Review Automation:**

- **SuperClaude persona analysis** - automated quality assessment
- **Static analysis** - Detekt, ESLint, type checking
- **Test execution** - all tests must pass before review
- **Performance testing** - automated performance regression detection
- **Security scanning** - automated vulnerability detection

**Review Integration Requirements:**

- **Quality gates must pass** before human review assignment
- **Automated findings** are included in review context
- **Human reviewers focus** on logic, architecture, and business requirements
- **Merge blocked** until all automated and human reviews approve

### Knowledge Sharing & Documentation (MANDATORY)

#### Review as Learning Tool (MANDATORY)

**Knowledge Transfer Objectives:**

- **Cross-training** - expose team members to different code areas
- **Pattern sharing** - identify and propagate good practices
- **Architectural education** - teach SCS and DDD principles through review
- **Quality standards** - reinforce coding standards and conventions

**Documentation Requirements:**

- **Significant decisions** documented in architecture decision records
- **Pattern examples** added to code organization documentation
- **Review lessons learned** captured in team knowledge base
- **Best practices** updated based on review findings

## Additional Standards

### Dependency Management

- **Update Strategy:** Monthly dependency updates
- **Security:** Automated vulnerability scanning
- **Version Strategy:** Latest stable releases preferred
- **Build Reproducibility:** Version locking in gradle.lockfile

### Performance Monitoring

- **Metrics:** Micrometer with Prometheus
- **Response Times:** P95 < 300ms for standard operations, P95 < 500ms for complex queries
- **Resource Usage:** Memory < 80% heap
- **Database:** Query times < 200ms average

### Error Handling

- **Domain Exceptions:** Rich, specific exception types
- **Boundary Validation:** ALL inputs validated
- **Graceful Degradation:** Proper fallback mechanisms
- **Logging:** Structured logging without sensitive data

## Context Engineering & Task Management

### Project Awareness & Context (MANDATORY)

#### AI Behavior Rules

- **Never assume missing context. Ask questions if uncertain.**
- **Never hallucinate libraries or functions** – only use known, verified packages and APIs
- **Always confirm file paths and class names** exist before referencing them in code or tests
- **Maintain context continuity** across Claude sessions using TaskMaster and PRD references

#### Context Engineering Workflow

1. **PRD-Based Development:**

- Create detailed Product Requirements Documents in `.taskmaster/docs/`
- Use PRDs to generate structured task hierarchies
- Maintain traceability from requirements to implementation

2. **Task-Driven Implementation:**

- Break complex features into manageable, testable subtasks
- Use TaskMaster for project planning and progress tracking
- Update task status and implementation notes throughout development

3. **Context Handoff Patterns:**

- Document architectural decisions in task notes
- Reference specific files and line numbers in task updates
- Maintain implementation history for future sessions

### Task Completion Standards (MANDATORY)

- **Mark completed tasks immediately** after finishing implementation
- **Add discovered sub-tasks** to TaskMaster during development
- **Document blockers and solutions** in task notes for future reference
- **Link tasks to commits** using conventional commit format with task IDs

### PRD Integration with DDD Architecture

#### Requirements Analysis

- **Domain Modeling:** Extract aggregates, entities, and value objects from PRDs
- **Use Case Identification:** Map PRD features to application services
- **Architecture Impact:** Assess new requirements against existing Onion Architecture
- **Performance Implications:** Analyze requirements for potential performance bottlenecks

#### Context Engineering Best Practices

- **Ubiquitous Language:** Ensure PRD terminology matches domain model
- **Bounded Context Analysis:** Identify context boundaries from requirements
- **Integration Points:** Plan adapter layer changes for new requirements
- **Test Strategy:** Define acceptance criteria and test approaches in PRDs
