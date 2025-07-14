# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Table of Contents

- [Self-Contained Systems Architecture](#self-contained-systems-architecture)
- [Technology Stack](#technology-stack)
- [Critical Rules & Constraints](#critical-rules--constraints)
- [Architecture and Domain Design](#architecture-and-domain-design)
- [Core Domain Logic](#core-domain-logic)
- [Frontend Architecture](#frontend-architecture)
- [Code Organization](#code-organization)
- [Frontend-Backend Integration](#frontend-backend-integration)
- [API Guidelines](#api-guidelines)
- [Testing Strategy](#testing-strategy)
- [Performance Standards](#performance-standards)
- [Kotlin Development Guidelines](#kotlin-development-guidelines)
- [Code Quality Standards](#code-quality-standards)
- [Development Workflow](#development-workflow)
- [Code Review Standards](#code-review-standards)
- [Context Engineering & Task Management](#context-engineering--task-management)

## Self-Contained Systems Architecture

### What is a Self-Contained System?

The Finden application is designed as a **Self-Contained System (SCS)** - an architectural approach that provides a middle ground between monolithic and microservices architectures. An SCS is an autonomous unit that includes its own web interface, business logic, and database, designed to handle a specific business capability end-to-end.

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

### Integration with Development Workflow

The SCS architecture directly influences development practices:

- **Feature Development**: Complete features implemented within single system
- **Testing Strategy**: End-to-end testing within SCS boundaries
- **Security Model**: Authentication and authorization handled by infrastructure - SCS handles input validation and business logic security only
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

- **Authentication/Authorization**: Handled by infrastructure - SCS MUST NEVER implement
- **SCS Responsibility**: Input validation, data protection, business logic security only
- **FORBIDDEN**: User authentication, session management, role-based access control

**Data Protection Requirements:**

- **NO personal data storage** - search operates on anonymous basis only
- **NO cross-SCS personal data sharing** - maintain data privacy boundaries
- **MANDATORY GDPR compliance** - right to be forgotten, data minimization, automated user rights fulfillment
- **FORBIDDEN**: Personal data collection without legal basis, indefinite data retention

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

- **Request timeouts**: 30 seconds maximum for any single request
- **Rate limiting**: 1000 requests per minute per client
- **FORBIDDEN**: Unlimited connection creation, blocking network operations

#### Database Operational Limits

**Query Performance Limits:**

- **Index requirements**: ALL production queries MUST use proper indexes
- **Query timeout**: 2 seconds maximum for any single query
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

**Dependencies:** Domain has zero outward dependencies, application depends only on domain, adapters implement domain interfaces.

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

### Essential Requirements (MANDATORY)

**Vue.js 3 Composition API Only:**

- Use `<script setup>` syntax with reactive functions (ref, computed, onMounted)
- TypeScript interfaces for all Props and Emits
- **FORBIDDEN**: Options API, untyped props/events, Vue 2 patterns

**Component Architecture:**

- Base components (reusable UI), Layout components (structure), Business components (domain-specific)
- Single responsibility per component, business logic in composables
- Semantic HTML, BEM methodology, scoped styling

**State Management:**

- Vuex with TypeScript typing, namespaced modules
- Composables for reactive business logic (useProductFilter, useProductTracking)
- Centralized error handling with try-catch patterns

**Performance:**

- Lazy loading with defineAsyncComponent for heavy components
- shallowRef for large arrays, markRaw for non-reactive objects
- Route-level code splitting with dynamic imports

**Multi-App Support:**

- Independent apps (produktliste, tool) with separate build entry points
- Shared component library with clear interface contracts
- Runtime isolation between applications

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

## Frontend-Backend Integration

### API Communication Patterns

**HTTP Client Configuration:**

- Axios with TypeScript interfaces, separate SSR/client instances
- Request/response interceptors for auth tokens and error handling
- Environment-based API URLs, proper timeout configurations

**Data Flow:**

- **Client**: User Interaction → Component → Composable → Vuex → API → Backend
- **SSR**: Browser → Fastify → Vue SSR → API → Quarkus → MongoDB → Response

**Error Handling:**

- Custom ApiError class with status codes
- Centralized error composables for consistent handling
- Proper TypeScript typing for all error patterns

## API Guidelines

### REST API Design Standards (MANDATORY)

**URL Naming:**

- Nouns not verbs, plural collections, kebab-case
- Path parameters for IDs, query params for filtering
- **FORBIDDEN**: Verbs in URLs, camelCase paths, nested resources >2 levels

**HTTP Methods:**

- **GET**: Retrieve (idempotent, cacheable) | **POST**: Create/search operations
- **PUT**: Replace entire resource | **DELETE**: Remove (not used in read-only catalog)
- **FORBIDDEN**: GET with side effects, POST for idempotent operations

**Response Format:**

- Envelope structure with status, data/error, and meta fields
- ISO 8601 UTC dates, monetary values as strings
- Standard status codes: 200/201/204 success, 4xx client errors, 5xx server errors

**Versioning & Security:**

- URL path versioning, semantic versioning, 6-month deprecation
- Input validation, schema sanitization, rate limiting (1000/min per client)

**Finden-Specific Standards:**

- Product search endpoints with classification/price/availability filters
- Pagination: 1-based, max 100 per page, includes totalElements/hasNext flags
- Event-driven integration via Kafka with Avro schemas

## Testing Strategy (MANDATORY)

### Test Requirements

**Framework Stack:** Jest/Vue Test Utils (frontend), JUnit 5/Mockk/TestContainers (backend)

**Universal Standards:**

- BDD structure (Given-When-Then), complete test isolation
- 80% minimum coverage (JaCoCo), no production dependencies in tests
- Public interface testing only, no reflection or private method access

**Test Types:**

- **Unit Tests**: `@Tag("unit")` - Fast, isolated, no external dependencies
- **Integration Tests**: `@Tag("integration")` - TestContainers, real databases
- **E2E Tests**: Playwright for complete user journeys
- **Architecture Tests**: ArchUnit for layer dependency validation

**Frontend Patterns:** Component mounting with mocked stores, MSW for API mocking, data-testid selectors
**Backend Patterns:** @QuarkusTest for full context, TestContainers for real database integration

## Performance Standards

### Critical Performance Requirements (MANDATORY)

#### Performance Limits

- **API Response Time**: P95 < 300ms for standard operations, P95 < 500ms for complex queries
- **Database Query Time**: Average < 200ms, maximum 2 seconds before timeout
- **Memory Usage**: < 80% heap utilization under normal load

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

#### Performance Anti-Patterns (FORBIDDEN)

- **Memory exhaustion patterns**: Full dataset loading, disabled pagination, unbounded collection loading
- **Inefficient algorithms**: O(n²) operations in production, O(n) operations in comparators (use HashMap lookups)
- **Resource leaks**: Unclosed connections, unmanaged object lifecycles
- **Blocking operations**: Synchronous calls that block event loops
- **Poor database patterns**: Queries without proper indexes, full table scans

#### Performance Monitoring

- **Metrics**: Micrometer with Prometheus for application monitoring
- **Alerting**: Memory usage > 85%, response times > thresholds
- **Profiling**: Regular performance analysis of critical paths

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

### Quality Tools

- **Static Analysis:** Detekt for Kotlin code quality
- **Build Quality:** Gradle build verification with Kotlin DSL
- **Dependency Check:** OWASP Dependency Check for vulnerability scanning
- **Coverage:** JaCoCo with 80% minimum threshold
- **Schema Management:** Avro Plugin for Kafka schema handling

### Build Verification (MANDATORY)

**Build verification requirements:**

- Run clean build to ensure compilation success
- Execute Detekt static analysis for code quality
- Run all tests with coverage reporting before any commit

## Development Workflow (MANDATORY)

### SuperClaude Development Workflow

**Available Personas:** Architect, Frontend, Backend, Security, QA, Performance - auto-activate based on file patterns and task context.

**Auto-Activation Rules:**

- `*.tsx|*.jsx|*.css` → Frontend | `*api*|*server*|*db*` → Backend | `*.test.*` → QA
- Architecture/design → Architect | Input validation → Security | Optimization → Performance

### Daily Development Loop (AUTOMATED with SuperClaude Personas)

#### Core TDD Cycle with Intelligent Persona Activation

**0. Conversation Setup:**

- Clear current conversation with `/clear` command

**1. Task Setup:**

- `task-master next` → select prioritized task
- `task-master show <task-id>` → review requirements
- `task-master set-status --id=<task-id> --status=in-progress`
- Auto-persona selection based on context (Frontend/Backend/Security/QA/Performance)

**2. Subtask Iteration (TDD Cycle):**

For each subtask (`<task-id>.1`, `<task-id>.2`, etc.):

a. **RED:** Write failing BDD test (QA persona auto-enhances test strategy)
b. **GREEN:** Minimal code to pass test with persona-guided implementation:

- Frontend subtasks → `--persona-frontend --magic`
- Backend subtasks → `--persona-backend --c7`
- Security subtasks → `--persona-security --strict`
  c. **REFACTOR:** Improve code (auto-quality analysis with refactorer persona)
  d. **DOCUMENT:** `task-master update-subtask --id=<task-id>.<subtask-id> --prompt="notes"`
  e. **COMMIT:** Atomic commit with pre-commit validation (security, performance, architecture)

**3. Task Completion:**

- Integration testing across subtasks
- Final refactoring for consistency
- `task-master set-status --id=<task-id> --status=done`

**4. Commit & Push:**

- Final commit with pre-commit validation (security, performance, architecture)
- Push changes

**Quality Gates (Pre-Commit):**

- Data Security: Input validation & data protection
- Performance: No O(n²) algorithms, memory patterns
- Architecture: Layer coupling compliance
- Testing: 80% coverage minimum

### Git Workflow

- **Conventional Commits:** `feat:`, `fix:`, `refactor:`, `security:`, `perf:` with task IDs
- **Atomic Commits:** Single logical change per commit
- **Branch Naming:** `feature/`, `bugfix/`, `security/`, `perf/`

### Critical Review Priorities

**Priority 1: Data Security** - Input validation, data protection, business logic security (NOT auth/authorization)
**Priority 2: Performance** - Algorithm complexity, memory patterns, database optimization
**Priority 3: Architecture** - Layer coupling, value object immutability, boundary conditions
**Priority 4: Code Quality** - Complexity analysis, duplication elimination, test coverage

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
