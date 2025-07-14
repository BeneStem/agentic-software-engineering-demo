# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Table of Contents

- [Self-Contained Systems Architecture](#self-contained-systems-architecture)
- [Technology Stack](#technology-stack)
- [Architecture and Domain Design](#architecture-and-domain-design)
- [Frontend Development Guidelines](#frontend-development-guidelines)
- [Frontend-Backend Integration](#frontend-backend-integration)
- [Testing Strategy](#testing-strategy)
- [Frontend Build & Deployment](#frontend-build--deployment)
- [Frontend Performance & Security](#frontend-performance--security)
- [Performance Standards](#performance-standards)
- [Kotlin Development Guidelines](#kotlin-development-guidelines)
- [Code Quality Standards](#code-quality-standards)
- [Development Workflow](#development-workflow)
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

### SCS Implementation Principles

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

#### 5. **Security Boundary**

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

### System Boundaries & Core Constraints

Key constraints that define system boundaries:

#### Security Boundaries

**SCS Security Responsibilities:**

- **SCS MUST NEVER implement authentication or authorization** - handled by infrastructure
- **SCS MUST ONLY implement input validation and data protection**
- **SCS MUST trust pre-validated security context** from infrastructure layer
- **SCS MUST protect sensitive data** (product prices, business rules, customer data)
- **FORBIDDEN**: Direct user authentication, session management, role-based access control

**Data Protection Requirements:**

- **NO personal data storage** - search operates on anonymous basis only
- **NO search query logging with personal identifiers** - anonymize all search analytics
- **NO cross-SCS personal data sharing** - maintain data privacy boundaries
- **MANDATORY GDPR compliance** - right to be forgotten, data minimization
- **FORBIDDEN**: Storing personal preferences without explicit consent, cross-border personal data transfer without compliance

#### Performance Boundaries

**Performance Limits:**

- **API Response Time**: P95 < 300ms for standard operations, P95 < 500ms for complex queries
- **Database Query Time**: Average < 200ms, maximum 2 seconds before timeout
- **Memory Usage**: < 80% heap utilization under normal load
- **Algorithm Complexity**: NO O(n²) or higher complexity operations in production
- **Collection Size**: NO unbounded collection loading - always paginate

**Performance Anti-Patterns to Avoid:**

- **Memory exhaustion patterns**: Full dataset loading, disabled pagination
- **Inefficient algorithms**: O(n) operations in comparators, nested loops on large datasets
- **Resource leaks**: Unclosed connections, unmanaged object lifecycles
- **Blocking operations**: Synchronous calls that block event loops

#### Data Consistency Boundaries

**SCS Data Autonomy:**

- **NO direct database access** between SCS instances
- **NO shared database schemas** between SCS boundaries
- **NO synchronous cross-SCS data dependencies** for core operations
- **MANDATORY eventual consistency** for cross-SCS data synchronization
- **FORBIDDEN**: Cross-SCS transactions, shared data stores, real-time cross-SCS data requirements

**Data Integrity Requirements:**

- **Product data consistency** - prices and availability must be real-time accurate
- **Search index consistency** - search results must reflect current product state
- **Event ordering** - maintain causality in event streams
- **Schema evolution** - backward compatibility required for all data changes

#### Architecture Boundaries

**Layer Coupling Constraints:**

- **Domain layer MUST NEVER reference** adapter DTOs or external data structures
- **Application layer MUST ONLY depend** on domain interfaces
- **Adapter layer MUST implement** domain interfaces without domain contamination
- **FORBIDDEN**: Cross-layer dependencies, domain logic in adapters, DTO usage in domain

**SCS Integration Constraints:**

- **NO synchronous dependencies** on other SCS for core functionality
- **NO shared UI components** between SCS boundaries (each SCS owns complete UI)
- **NO deployment coordination** required between SCS instances
- **MANDATORY asynchronous communication** for all cross-SCS interactions
- **FORBIDDEN**: Runtime dependencies on external SCS, shared deployment artifacts

### Breaking Change Policies

#### API Breaking Change Policy

**API Versioning Rules:**

- **NO breaking changes** to existing API endpoints without versioning
- **Minimum 6-month deprecation period** for API version retirement
- **Backward compatibility** required for at least 2 API versions
- **MANDATORY API contract validation** before deployment

**Breaking Change Examples:**

- **FORBIDDEN**: Removing API fields, changing field types, removing endpoints
- **REQUIRED**: New endpoints for new functionality, additive field changes only
- **MANDATORY**: Version negotiation for clients, graceful degradation

#### Cross-SCS Contract Policy

**Event Schema Changes:**

- **NO breaking changes** to published event schemas without coordination
- **Avro schema evolution** required for all event modifications
- **Event versioning** mandatory for schema changes
- **Cross-SCS coordination** required for major event changes

**Integration Contract Policy:**

- **NO changes to event ordering guarantees** without coordination
- **NO changes to event payload structure** without backward compatibility
- **MANDATORY**: Schema registry validation, consumer impact assessment

### Resource & Operational Limits

#### Infrastructure Resource Limits

**Memory Limits:**

- **Maximum heap size**: 2GB per instance under normal load
- **Memory leak detection**: Automatic alerts at 85% usage
- **Garbage collection**: Maximum 100ms pause times
- **FORBIDDEN**: Memory-intensive operations without streaming, unlimited object creation

**Network Limits:**

- **Connection pooling**: Maximum 50 connections per application instance
- **Request timeouts**: 30 seconds maximum for any single request
- **Rate limiting**: 1000 requests per minute per client
- **FORBIDDEN**: Unlimited connection creation, blocking network operations

#### Database Operational Limits

**Query Performance Limits:**

- **Index requirements**: ALL production queries MUST use proper indexes
- **Query timeout**: 2 seconds maximum for any single query
- **Connection limits**: 50 connections per application instance (see Network Limits section)
- **FORBIDDEN**: Full table scans, queries without WHERE clauses on large collections

**Data Size Limits:**

- **Document size**: Maximum 16MB per MongoDB document
- **Collection size**: Maximum 10M documents before partitioning required
- **Index size**: Maximum 500MB per index
- **FORBIDDEN**: Unbounded document growth, missing data archival strategies

#### Deployment & Environment Constraints

**Deployment Safety Rules:**

- **Zero-downtime deployments** required for production
- **Rollback capability** required within 5 minutes
- **Health check endpoints** mandatory for all deployments
- **FORBIDDEN**: Production deployments without testing, deployments during business hours without approval

**Environment Isolation:**

- **NO production data** in development/staging environments
- **NO shared credentials** between environments
- **NO cross-environment communication** except for monitoring
- **MANDATORY**: Environment-specific configurations, secrets management

### Compliance & Regulatory Constraints

#### GDPR Compliance

**Data Processing Constraints:**

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

#### Regional Compliance

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

#### Layer Dependencies

**Dependencies:** See Architecture Boundaries section for detailed layer coupling constraints and ArchUnit enforcement.

#### Anti-Corruption Layer

- **Domain MUST NOT reference DTOs** from adapter layer
- **External data structures MUST be mapped** at adapter boundaries
- **Domain model protected from external changes**

## Core Domain Logic

**Search Operations:** Query analysis → classification matching → availability/price calculations → ranking → presentation
**Key Fields:** `klassifikationId` (categories), `verfuegbarkeiten.bestellschlussUTC` (order deadlines), `verfuegbarkeiten.liefertag` (delivery dates)
**Business Rules:** Real-time pricing, availability checks, hierarchical product classification

### Business Rules & Decision Trees (MANDATORY)

#### Price Calculation Rules

**Price Calculation:** Base price → regional pricing → volume discounts → promotional discounts → tax calculations

**Business Rules:**

- **Never show negative prices** - return null for invalid calculations
- **Price consistency** - same price for same product within user session
- **Currency formatting** - always display in user's regional currency
- **Discount precedence** - promotional discounts override volume discounts

#### Availability Calculation Rules

**Availability Calculation:** Check order deadline → delivery date → inventory levels → determine status

**Business Rules:**

- **Real-time calculations** - availability checked on every request
- **Timezone handling** - all times calculated in user's timezone
- **Grace periods** - 30-minute grace period for order deadlines
- **Business hours** - consider business hours for same-day delivery

#### Classification Hierarchy Rules

**Classification Hierarchy:** Primary categories → Secondary subcategories → Tertiary attributes (3-level hierarchy)

**Business Rules:**

- **Hierarchical inheritance** - products inherit parent category attributes
- **Multi-classification support** - products can belong to multiple categories
- **Classification validation** - invalid klassifikationId returns empty results
- **Search optimization** - index classification hierarchy for fast lookups

### Domain Events

**Event Publishing:** Asynchronous via Kafka with Avro schemas. Key events include search operations, price calculations, and cross-SCS integration events for availability/price changes.

### Key Domain Concepts

**Core Entities:** Produkt (product), Klassifikation (category), Verfügbarkeit (availability with order deadlines and delivery dates)
**Technical Integration:** API-Gateway, Event-Stream (Kafka), Search indexes, Caching layer

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

#### Business Rules Validation

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

## Frontend Architecture

### Three-Layer Architecture

**Layer Structure:**
- **Presentation (Components):** Vue components, templates, styles - UI rendering only
- **Business Logic (Composables):** Reusable reactive business logic and state management  
- **Data Access (API):** HTTP clients, API abstractions, data transformation
- **Dependencies:** Presentation → Composables → API (strict layer dependency)

### Key Frontend Principles

**Component Architecture:**
- Business logic in composables, not components
- Components handle only UI rendering and user interaction
- TypeScript typing for all props, emits, and composables

**Multi-App Support:**
- Independent apps (produktliste, tool) with separate build entry points
- Shared component library with clear interface contracts
- Runtime isolation between applications

**SSR with Fastify:**
- Server-side rendering with proper error boundaries  
- Separate HTTP client configurations for SSR vs client-side
- State hydration with serializable initial state

### Component Standards

**Component Hierarchy:**
- Base components (framework-agnostic, reusable)
- Layout components (structural concerns only)  
- Business components (domain-specific logic)
- Single responsibility per component

**State Management:**
- Vuex modular patterns with strict TypeScript typing
- Flat state structures, namespaced modules
- Typed actions, mutations, and getters

**API Layer:**
- Type-safe abstractions for backend communication
- Separate HTTP client instances for SSR vs client-side
- Centralized error handling with retry logic
- DTO transformation at API boundaries

## Code Organization

### Project Structure Overview (MANDATORY)

**Standard SCS layout:** backend/ (Quarkus/Kotlin), frontend/ (Vue.js), infrastructure/, docs/, .taskmaster/

### Backend Code Organization (MANDATORY)

**Domain Layer:** model/ (entities, value objects), service/ (domain services), repository/ (interfaces), exception/ (domain exceptions)
- **Rules:** Business logic in entities, immutable value objects, no external dependencies
- **FORBIDDEN:** Framework annotations in domain, infrastructure concerns

**Application Layer:** usecase/ (business operations), dto/ (data contracts), mapper/ (DTO↔domain), service/ (coordination)
- **Rules:** Use cases orchestrate domain, DTOs for external contracts, mappers at boundaries
- **FORBIDDEN:** Business logic in application services, direct database access

**Adapter Layer:** web/ (REST controllers), persistence/ (database adapters), messaging/ (events), external/ (clients)
- **Rules:** Controllers delegate to application, repository implementations use specific tech
- **FORBIDDEN:** Business logic in adapters, domain contamination

### Frontend Code Organization (MANDATORY)

**Multi-App Structure:** apps/ (entry points), shared/ (components), api/ (backend communication), composables/ (business logic), store/ (Vuex state)
- **Rules:** Apps contain config, shared provides reusable UI, API abstracts backend, composables contain reactive logic
- **FORBIDDEN:** Business logic in components, direct API calls from components

### File Naming & Dependencies (MANDATORY)

**Backend:** PascalCase with suffixes (.kt, Service, Repository, Controller, Request/Response), lowercase packages
**Frontend:** PascalCase components (.vue), camelCase composables (use prefix), lowercase directories with hyphens
**Dependencies:** Domain→Application→Adapter (strict hierarchy), no circular dependencies, no cross-SCS runtime dependencies
**FORBIDDEN:** Cross-layer dependencies, shared business logic modules

## Frontend Development Guidelines

#### Composition API

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

#### TypeScript Integration

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

#### URL Structure & Naming

**URL Design Principles:**

Standard REST URL structure with base URL, resource collections, resource instances, sub-resources, and action resources.

**URL Naming Rules (MANDATORY):**

- **Use nouns, not verbs** for resource names (`/products`, not `/getProducts`)
- **Use plural nouns** for collections (`/products`, `/search-results`)
- **Use kebab-case** for multi-word resources (`/search-results`, `/product-categories`)
- **Use path parameters** for resource identification (`/products/{productId}`)
- **Use query parameters** for filtering and pagination (`?page=1&size=20&category=electronics`)
- **FORBIDDEN**: Verbs in URLs, camelCase in paths, nested resources beyond 2 levels

**Resource URL Examples:**

Standard REST endpoints for product collections, individual products, availability, search operations, and classification hierarchies.

#### HTTP Methods & Semantics

**HTTP Method Usage (CRITICAL):**

- **GET**: Retrieve resources (idempotent, cacheable, no side effects)
- **POST**: Create resources or perform non-idempotent operations (search, calculations)
- **PUT**: Replace entire resource (idempotent, requires full representation)
- **PATCH**: Partial resource updates (not used in current API)
- **DELETE**: Remove resources (not implemented in read-only product catalog)
- **FORBIDDEN**: GET requests with side effects, POST for idempotent operations

**Status Code Standards (MANDATORY):**

Standard HTTP status codes: 200/201/204 for success, 400/401/403/404/422 for client errors, 500/502/503/504 for server errors.

#### Request & Response Format

**JSON Request Format Standards:**

**Request Format**: JSON with query, filters (classification, price range, availability), pagination, and sorting fields.

**JSON Response Format Standards:**

**Response Format**: Envelope structure with status, data/error, and meta fields. Success responses include data and pagination. Error responses include error codes and details.

**Response Format Rules (MANDATORY):**

- **Consistent envelope structure** with status, data, error, and meta fields
- **Null field handling** - omit optional null fields from responses
- **Date/time format** - use ISO 8601 UTC format (`2024-01-15T10:30:00Z`)
- **Decimal numbers** - use strings for precise monetary values
- **FORBIDDEN**: Inconsistent response structures, mixed date formats

#### API Versioning Strategy

**Versioning Approach (CRITICAL):**

- **URL path versioning** - version included in URL path (`/v1/products`)
- **Semantic versioning** - Major.Minor.Patch format (1.0.0, 1.1.0, 2.0.0)
- **Backward compatibility** - maintain compatibility within major versions
- **Deprecation policy** - 6-month minimum notice for version retirement

**Version Support Policy:**

Multiple version support with semantic versioning, deprecation notices, and end-of-life management.

**Breaking Change Policy (MANDATORY):**

- **Major version bump** required for breaking changes
- **Breaking changes include**: Field removal, type changes, required field additions
- **Non-breaking changes**: Optional field additions, endpoint additions
- **Version negotiation** supported via Accept-Version header

#### Error Handling & Validation

**Input Validation Standards (CRITICAL):**

Structured error responses with status, error code, message, and field-specific validation details.

**Business Logic Error Standards:**

Business rule violation responses with specific error codes and contextual details for resolution.

**Error Code Standards (MANDATORY):**

- **VALIDATION_ERROR**: Request format or validation failures
- **AUTHENTICATION_ERROR**: Authentication-related failures
- **AUTHORIZATION_ERROR**: Permission-related failures
- **RESOURCE_NOT_FOUND**: Requested resource does not exist
- **BUSINESS_RULE_VIOLATION**: Business logic constraint violations
- **EXTERNAL_SERVICE_ERROR**: External service integration failures
- **RATE_LIMIT_EXCEEDED**: Client rate limiting violations

#### Pagination & Filtering

**Pagination Standards (CRITICAL):**

**Pagination**: Request with page/size fields (1-based, max 100 per page). Response includes totalElements, totalPages, hasNext/hasPrevious flags.

**Filtering Standards (MANDATORY):**

**Filtering**: Supports classification, price ranges, availability windows, regions, and custom attributes with ranges or arrays.

**Sorting Standards (MANDATORY):**

**Sorting**: Single field with direction ("asc"/"desc") or array of multiple field/direction objects for complex sorting.

### Cross-SCS API Communication (MANDATORY)

#### Integration API Standards

**Cross-SCS Request Headers (MANDATORY):**

Required headers for SCS communication: source identifier, correlation ID, API version, and content type.

**Cross-SCS Authentication (CRITICAL):**

- **Service-to-service tokens** - JWT tokens for SCS authentication
- **Token validation** - validate tokens at API gateway level
- **Mutual TLS** - for high-security SCS communications
- **NO user authentication** - handled by infrastructure layer
- **FORBIDDEN**: User tokens in SCS-to-SCS communication

#### Event-Driven API Integration

**Event Publishing API Standards:**

Product events with event type, version, source, timestamp, correlation ID, and structured data payload for availability changes.

**Event Schema Evolution (MANDATORY):**

- **Avro schemas** for event payload definitions
- **Schema registry** for centralized schema management
- **Backward compatibility** required for schema changes
- **Event versioning** for breaking schema changes

### API Documentation & Security

**Documentation Standards:**
- OpenAPI 3.0 specification with complete endpoint documentation
- 6-month deprecation process with sunset headers

**Security Standards:**
- Schema validation and input sanitization for all requests
- Security headers for content type validation and XSS prevention  
- Rate limiting: 1000 requests/minute per client, 10,000 system-wide

## Testing Strategy (MANDATORY)

### Framework Stack

**Framework Stack**: Uses technology stack defined in earlier sections (Jest/Vue Test Utils for frontend, JUnit 5/Mockk/TestContainers for backend).

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
3. **Coverage:** 80% minimum (JaCoCo with minimum threshold)
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

**Note:** Authentication handled by infrastructure layer - SCS consumes pre-validated security context. No auth implementation needed in SCS code.

## Performance Standards

### Critical Performance Requirements (MANDATORY)

#### Algorithm Complexity Standards

- **NO O(n²) or higher complexity operations** in production code
- **Database operations MUST use proper indexes**
- **Collection operations MUST be optimized** for expected data sizes
- **Memory usage MUST be bounded** - no unlimited data loading

#### Database Performance

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

#### Memory Management

1. **Object Lifecycle:**

- Prefer streaming over full collection materialization
- Immutable objects for value objects
- Proper resource cleanup

2. **Collection Processing:**

- Avoid multiple intermediate collections
- Use sequence operations for large datasets
- Monitor heap usage in large operations

#### Performance Anti-Patterns

**Forbidden performance anti-patterns:**

- Never disable pagination for in-memory sorting (causes memory exhaustion)
- Avoid O(n) operations in comparators (use HashMap lookups instead)
- Never load entire collections without limits (always use pagination)
- Prevent memory exhaustion through proper collection handling

#### Performance Monitoring

Consolidated performance standards defined in Performance Standards section.

## Kotlin Development Guidelines

### Language Standards (MANDATORY)

**Immutability:** Use val (not var), validation in init blocks, immutable data classes
**Null Safety:** Use `?.let`, avoid `!!` unless justified, Optional patterns for domain modeling
**Collections:** Functional operations (filter, map, take), chain transformations, avoid imperative loops
**FORBIDDEN:** Mutable value objects, missing null checks, imperative style

### Error Handling (MANDATORY)

**Domain Exceptions:** Specific exceptions with context, inherit from base domain exception class
**Boundary Validation:** Validate all inputs, check nulls/negatives, use when expressions for conditions
**FORBIDDEN:** Missing boundary checks, runtime crashes from invalid inputs

### MongoDB Integration (MANDATORY)

**Repository Pattern:** Domain interfaces with domain types, adapter implementations with MongoDB classes
**Entity Mapping:** MongoDB entities in adapter layer only, clean MongoProdukt ↔ Produkt mapping
**Configuration:** ApplicationScoped CDI, PanacheMongoRepository inheritance, no MongoDB annotations in domain

### Annotation Processing (MANDATORY)

**AllOpen/NoArg Configuration:** CDI annotations, JAX-RS endpoints, persistence entities, QuarkusTest compatibility
**Custom NoArg Annotation:** CLASS target, RUNTIME retention, framework compatibility for domain entities
**Benefits:** Seamless CDI/JAX-RS integration, JPA/MongoDB support, clean domain code

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

#### Layer Coupling

See Architecture Boundaries section for detailed layer coupling constraints.

### Quality Tools

- **Static Analysis:** Detekt 1.23.8 for Kotlin
- **Build Quality:** Gradle 8.x build verification with Kotlin DSL
- **Dependency Check:** OWASP Dependency Check 12.1.3 for vulnerability scanning
- **Version Management:** Gradle Versions Plugin 0.52.0 for dependency updates
- **Coverage:** JaCoCo 0.8.7 with 80% minimum threshold (see Testing Strategy section)
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

**Available Personas:** Architect (system design), Frontend (UI/UX), Backend (API/performance), Security (input validation/data protection), QA (testing/quality), Performance (optimization)

**Auto-Activation:** Personas activate automatically based on file patterns and task context.

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

#### Task-Level Setup

1. **Task Selection:** `task-master next` → identify prioritized main task

- **Auto-Persona Selection**: Context analysis determines appropriate persona based on task type, file patterns, and requirements

2. **Task Context Review:** `task-master show <task-id>` → understand overall task requirements

- **Intelligent Analysis**: SuperClaude automatically applies `--think` or `--think-hard` based on task complexity
- **Domain Detection**: Auto-activates relevant persona (`--persona-architect` for system design, `--persona-frontend` for UI work, etc.)

3. **Task Status Update:** `task-master set-status --id=<task-id> --status=in-progress`

- **Context Setup**: Persona-specific environment and tools automatically configured

#### Subtask Iteration Loop

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

#### Task-Level Completion

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
- [ ] **Quality:** All tests pass, 80% coverage minimum (`/test --coverage --persona-qa --scope=task --task-id=<task-id>`)
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

## Code Review Standards

### Key Review Standards

**Architecture Compliance:**
- Layer boundaries respected (no domain/DTO mixing)
- Dependency direction correct (domain has no outward dependencies)
- SCS boundaries maintained (no direct cross-SCS dependencies)

**Frontend Standards:**
- Composition API usage (no Options API in new components)
- TypeScript typing (all props, emits, and composables typed)
- Business logic in composables, not components
- Input sanitization and XSS prevention

**Backend Standards:**
- Immutable value objects with validation
- Business logic in domain entities
- Proper exception handling with context
- Algorithm complexity: no O(n²) or higher operations

**Security Requirements:**
- Input validation at all boundaries
- No user auth implementation in SCS (handled by infrastructure)
- Parameterized queries (no string concatenation)
- GDPR compliance: data minimization and user rights support


## Additional Standards

### Dependency Management

- **Update Strategy:** Monthly dependency updates
- **Security:** Automated vulnerability scanning
- **Version Strategy:** Latest stable releases preferred
- **Build Reproducibility:** Version locking in gradle.lockfile

### Performance Monitoring

- **Metrics:** Micrometer with Prometheus  
- **API Response Times:** P95 < 300ms standard operations, P95 < 500ms complex queries
- **SSR Pages:** < 200ms response time
- **Database Queries:** < 200ms average with proper indexing
- **Memory Usage:** < 80% heap under normal load
- **Connection Limits:** 50 connections per application instance

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
