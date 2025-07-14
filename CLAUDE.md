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

The Finden application is designed as a **Self-Contained System (SCS)** - an autonomous unit that includes its own web interface, business logic, and database, designed to handle a specific business capability end-to-end.

### SCS Implementation Principles

- **UI Ownership**: Each SCS MUST include its own web interface, NO shared UI components between SCS boundaries
- **Data Autonomy**: Dedicated database per SCS, NO direct database access between systems
- **Communication Boundaries**: Prefer asynchronous communication between SCS, minimize synchronous API calls
- **Deployment Independence**: Each SCS deployed as complete unit, NO deployment coordination required
- **Security Model**: Authentication and authorization handled by infrastructure - SCS handles input validation and business logic security only

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

#### Security Boundaries

- **Authentication/Authorization**: Handled by infrastructure - SCS MUST NEVER implement
- **SCS Responsibility**: Input validation, data protection, business logic security only
- **FORBIDDEN**: User authentication, session management, role-based access control
- **NO personal data storage** - search operates on anonymous basis only
- **MANDATORY GDPR compliance** - right to be forgotten, data minimization

#### Data Consistency Boundaries

- **NO direct database access** between SCS instances
- **NO shared database schemas** between SCS boundaries
- **NO synchronous cross-SCS data dependencies** for core operations
- **MANDATORY eventual consistency** for cross-SCS data synchronization
- **Product data consistency** - prices and availability must be real-time accurate
- **Search index consistency** - search results must reflect current product state

#### Architecture Boundaries

- **Domain layer MUST NEVER reference** adapter DTOs or external data structures
- **Application layer MUST ONLY depend** on domain interfaces
- **Adapter layer MUST implement** domain interfaces without domain contamination
- **NO synchronous dependencies** on other SCS for core functionality
- **MANDATORY asynchronous communication** for all cross-SCS interactions

## Architecture and Domain Design

### Onion Architecture Implementation

The application follows **Domain-Driven Design (DDD)** with **Onion/Hexagonal Architecture**:

**Onion Architecture package structure:**

- **Domain layer (core):** Contains model entities, value objects, aggregates, domain services, repository interfaces, and domain exceptions
- **Application layer:** Contains application services implementing use cases
- **Adapter layer (outer):** Contains active adapters (REST controllers, message consumers) and passive adapters (database implementations, external service clients)

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
**Key Fields:** classification IDs (categories), order deadlines, delivery dates
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

## Frontend Architecture

### Three-Layer Architecture

- **Presentation (Components):** Vue components, templates, styles - UI rendering only
- **Business Logic (Composables):** Reusable reactive business logic and state management
- **Data Access (API):** HTTP clients, API abstractions, data transformation
- **Dependencies:** Presentation → Composables → API (strict layer dependency)

### Essential Requirements (MANDATORY)

**Vue.js 3 Composition API Only:**
- Use Composition API syntax with reactive functions
- TypeScript interfaces for all Props and Emits
- **FORBIDDEN**: Options API, untyped props/events, Vue 2 patterns

**Component Architecture:**
- Base components (reusable UI), Layout components (structure), Business components (domain-specific)
- Single responsibility per component, business logic in composables
- Semantic HTML, BEM methodology, scoped styling

**State Management:**
- Vuex with TypeScript typing, namespaced modules
- Composables for reactive business logic
- Centralized error handling with try-catch patterns

**Performance:**
- Lazy loading for heavy components
- Shallow references for large arrays, non-reactive objects for performance
- Route-level code splitting

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

- **Rules:** Immutable value objects, no external dependencies
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

**Backend:** PascalCase with suffixes, lowercase packages
**Frontend:** PascalCase components, camelCase composables with prefix, lowercase directories with hyphens
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

**URL Naming:** Nouns not verbs, plural collections, kebab-case, path parameters for IDs, query params for filtering
**HTTP Methods:** GET (retrieve), POST (create/search), PUT (replace), DELETE (remove) 
**Response Format:** Envelope structure with status, data/error, and meta fields, ISO 8601 UTC dates, monetary values as strings
**Versioning & Security:** URL path versioning, input validation, schema sanitization, rate limiting (1000/min per client)
**Finden-Specific Standards:** Product search endpoints with classification/price/availability filters, pagination (1-based, max 100 per page), event-driven integration via Kafka with Avro schemas

## Testing Strategy (MANDATORY)

### Test Requirements

**Framework Stack:** Jest/Vue Test Utils (frontend), JUnit 5/Mockk/TestContainers (backend)

**Universal Standards:**

- BDD structure (Given-When-Then), complete test isolation
- 80% minimum coverage (JaCoCo), no production dependencies in tests
- Public interface testing only, no reflection or private method access

**Test Types:**

- **Unit Tests**: Fast, isolated, no external dependencies
- **Integration Tests**: TestContainers, real databases
- **E2E Tests**: Complete user journeys
- **Architecture Tests**: Layer dependency validation

**Frontend Patterns:** Component mounting with mocked stores, API mocking, test selectors
**Backend Patterns:** Full context testing, TestContainers for real database integration

## Performance Standards

### Critical Performance Requirements (MANDATORY)

#### Performance Limits

- **API Response Time**: P95 < 300ms for standard operations, P95 < 500ms for complex queries
- **Database Query Time**: Average < 200ms, maximum 2 seconds before timeout
- **Memory Usage**: < 80% heap utilization under normal load

#### Algorithm Complexity Standards

- **NO O(n²) or higher complexity operations** in production code
- **Database operations MUST use proper indexes**
- **Required MongoDB indexes**: classification IDs, order deadlines, delivery dates
- **Memory usage MUST be bounded** - no unlimited data loading

#### Performance Anti-Patterns (FORBIDDEN)

- **Memory exhaustion**: Full dataset loading, disabled pagination, unbounded collection loading
- **Inefficient algorithms**: O(n²) operations in production, O(n) operations in comparators
- **Resource leaks**: Unclosed connections, unmanaged object lifecycles
- **Poor database patterns**: Queries without proper indexes, full table scans

## Kotlin Development Guidelines

### Language Standards (MANDATORY)

**Immutability:** Use immutable values, validation in init blocks, immutable data classes
**Null Safety:** Use safe operators, avoid force unwrapping unless justified, Optional patterns for domain modeling
**Collections:** Functional operations, chain transformations, avoid imperative loops
**FORBIDDEN:** Mutable value objects, missing null checks, imperative style

### Error Handling (MANDATORY)

**Domain Exceptions:** Specific exceptions with context, inherit from base domain exception class
**Boundary Validation:** Validate all inputs, check nulls/negatives, use when expressions for conditions
**FORBIDDEN:** Missing boundary checks, runtime crashes from invalid inputs

### MongoDB Integration (MANDATORY)

**Repository Pattern:** Domain interfaces with domain types, adapter implementations with MongoDB classes
**Entity Mapping:** MongoDB entities in adapter layer only, clean mapping between layers
**Configuration:** ApplicationScoped CDI, PanacheMongoRepository inheritance, no MongoDB annotations in domain

## Code Quality Standards

### Critical Bug Prevention (MANDATORY)

- **Validate all boundary conditions** to prevent crashes
- **Use null-safe operators** for safe value processing
- **Implement try-catch blocks** for parsing operations that may fail
- **Return null for invalid values** rather than throwing exceptions for negative numbers
- **Use proper null handling** with null-checks before value comparisons

### Quality Tools

- **Static Analysis:** Detekt for Kotlin code quality
- **Dependency Check:** OWASP Dependency Check for vulnerability scanning
- **Schema Management:** Avro Plugin for Kafka schema handling

## Development Workflow (MANDATORY)

### SuperClaude Development Workflow

**Available Personas:** Architect, Frontend, Backend, Security, QA, Performance - auto-activate based on file patterns and task context.
**Auto-Activation Rules:** Frontend files → Frontend | API/server/database files → Backend | Test files → QA | Architecture/design → Architect | Input validation → Security | Optimization → Performance

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

### Git Workflow & Critical Review Priorities

**Git Workflow:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` with task IDs), Atomic Commits (single logical change per commit), Branch Naming (`feature/`, `bugfix/`, `security/`, `perf/`)

**Critical Review Priorities:**

**Priority 1: Data Security** - Input validation, data protection, business logic security (NOT auth/authorization)
**Priority 2: Performance** - Algorithm complexity, memory patterns, database optimization
**Priority 3: Architecture** - Layer coupling, value object immutability, boundary conditions
**Priority 4: Code Quality** - Complexity analysis, duplication elimination, test coverage

## Code Review Standards

### Key Review Standards

**Architecture Compliance:** Layer boundaries respected (no domain/DTO mixing), SCS boundaries maintained (no direct cross-SCS dependencies)
**Frontend Standards:** Composition API usage (no Options API), TypeScript typing (all props, emits, composables typed), business logic in composables, input sanitization and XSS prevention
**Backend Standards:** Immutable value objects with validation, business logic in domain entities, proper exception handling with context
**Security Requirements:** Parameterized queries (no string concatenation), GDPR compliance: data minimization and user rights support

## Context Engineering & Task Management

### Project Awareness & Context (MANDATORY)

#### AI Behavior Rules

- **Never assume missing context. Ask questions if uncertain.**
- **Never hallucinate libraries or functions** – only use known, verified packages and APIs
- **Always confirm file paths and class names** exist before referencing them in code or tests
- **Maintain context continuity** across Claude sessions using TaskMaster and PRD references

#### Task Completion Standards

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
