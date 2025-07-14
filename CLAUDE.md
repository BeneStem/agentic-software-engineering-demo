# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Table of Contents

- [System Architecture](#system-architecture)
- [Technology Stack](#technology-stack)
- [Architecture & Code Standards](#architecture--code-standards)
- [Security Requirements](#security-requirements)
- [Red Flag Standards](#red-flag-standards)
- [Quality Standards](#quality-standards)
- [Development Workflow](#development-workflow)
- [Context & Task Management](#context--task-management)

## System Architecture

### Self-Contained Systems (SCS) Principles

The Finden application is designed as a **Self-Contained System (SCS)** - an autonomous unit that includes its own web interface, business logic, and database, designed to handle a specific business capability end-to-end.

**SCS Implementation Principles:**

- **UI Ownership**: Each SCS MUST include its own web interface, NO shared UI components between SCS boundaries
- **Data Autonomy**: Dedicated database per SCS, NO direct database access between systems
- **Communication Boundaries**: Prefer asynchronous communication between SCS, minimize synchronous API calls
- **Deployment Independence**: Each SCS deployed as complete unit, NO deployment coordination required
- **Security Model**: Authentication and authorization handled by infrastructure (see "Security Requirements" section)

### Onion/Hexagonal Architecture (MANDATORY)

The application follows **Domain-Driven Design (DDD)** with **Onion/Hexagonal Architecture**.

**Implementation Details:** Complete layer structure, dependencies, and SCS communication patterns defined in "Architecture & Code Standards" section.

### Essential Infrastructure

**SCS Boundaries:**

- **Finden Search SCS**: Product search, classification, filtering with MongoDB and Kafka events
- **Adjacent Systems**: User Management, Order Processing, Product Management (communicate via Kafka only)

**Infrastructure Components:**

- **Database**: MongoDB collections - products, classifications, search_analytics, availability_cache
- **Required Indexes**: klassifikationId+active, price+currency, availability fields, text search
- **API**: Base `/api/v1/search`, JSON envelope responses, OpenAPI docs at `/api/docs`
- **Events**: Kafka topics - product.updated, pricing.changed, availability.changed, search.performed
- **Caching**: Application (classification hierarchy), Database (WiredTiger), CDN (static assets)

## Technology Stack

### Core Technology Stack (MANDATORY)

**Backend Stack:**

- **Language**: Kotlin (JVM)
- **Framework**: Quarkus (NOT Spring)
- **Database**: MongoDB with Panache Kotlin (NOT direct drivers)
- **Build**: Gradle with Kotlin DSL
- **Messaging**: Kafka with Avro serialization

**Frontend Stack:**

- **Language**: TypeScript (strict mode)
- **Framework**: Vue.js with Composition API (NOT Options API)
- **State**: Vuex
- **SSR**: Fastify
- **Build**: Vue CLI with Webpack
- **HTTP**: Axios (NOT fetch API)

**Integration Stack:**

- **Cross-SCS Communication**: Kafka with Avro (NOT direct API calls)
- **Architecture**: Onion/Hexagonal with DDD principles, Self-Contained System approach

### Technology Constraints (FORBIDDEN)

- Spring Framework (use Quarkus)
- Vue.js Options API (use Composition API)
- Direct MongoDB drivers (use Panache)
- fetch API (use Axios)
- Synchronous cross-SCS communication (use Kafka)
- Direct cross-SCS database access

### Architecture & Code Standards (MANDATORY)

**Onion/Hexagonal Architecture Layers:**

- **Domain Layer (Core):** Model entities, value objects, aggregates, domain services, repository interfaces, domain exceptions
  - **Rules:** Immutable value objects, zero outward dependencies, no external dependencies
  - **FORBIDDEN:** Framework annotations, infrastructure concerns, adapter DTOs or external data structures

- **Application Layer:** Application services implementing use cases, DTOs for data contracts, mappers at boundaries, coordination services
  - **Rules:** Use cases orchestrate domain, DTOs for external contracts, depends only on domain interfaces
  - **FORBIDDEN:** Business logic in application services, direct database access

- **Adapter Layer (Outer):** REST controllers, database adapters, message consumers, external service clients
  - **Rules:** Controllers delegate to application layer, repository implementations use specific tech, implement domain interfaces
  - **FORBIDDEN:** Business logic in adapters, domain contamination

**Layer Dependencies & Boundaries:**

- Domain has zero outward dependencies
- Application depends only on domain
- Adapters implement domain interfaces
- NO cross-layer dependencies
- NO business logic in controllers or adapters

**SCS Communication Patterns:**

- NO synchronous cross-SCS calls (use Kafka with Avro)
- NO direct cross-SCS database access
- MANDATORY asynchronous communication for all cross-SCS interactions
- NO shared business logic modules between SCS boundaries
- **Adjacent Systems:** User Management, Order Processing, Product Management (Kafka-only communication)

**Frontend Architecture (Vue.js 3-Layer):**

- **Presentation Layer:** Vue components, templates, styles - UI rendering only
- **Business Logic Layer:** Composables for reactive business logic and state management
- **Data Access Layer:** HTTP clients, API abstractions, data transformation
- **Dependencies:** Presentation → Composables → API (strict layer dependency)
- **FORBIDDEN:** Business logic in components, direct API calls from components, Options API patterns

**Backend Code Organization:**

- **Domain Layer:** model/ (entities, value objects), service/ (domain services), repository/ (interfaces), exception/ (domain exceptions)
- **Application Layer:** usecase/ (business operations), dto/ (data contracts), mapper/ (DTO↔domain), service/ (coordination)
- **Adapter Layer:** web/ (REST controllers), persistence/ (database adapters), messaging/ (events), external/ (clients)

**Frontend Code Organization:**

- **Multi-App Structure:** apps/ (entry points), shared/ (components), api/ (backend communication), composables/ (business logic), store/ (Vuex state)
- **Rules:** Apps contain config, shared provides reusable UI, API abstracts backend, composables contain reactive logic
- **FORBIDDEN:** Business logic in components, direct API calls from components

**File Naming Standards:**

- **Backend:** PascalCase with suffixes, lowercase packages
- **Frontend:** PascalCase components, camelCase composables with prefix, lowercase directories with hyphens

**Kotlin Development Standards:**

- **Immutability:** Use immutable values, validation in init blocks, immutable data classes
- **Null Safety:** Use safe operators, avoid force unwrapping unless justified, Optional patterns for domain modeling
- **Collections:** Functional operations, chain transformations, avoid imperative loops
- **Error Handling:** Specific exceptions with context, inherit from base domain exception class
- **Boundary Validation:** Validate all inputs, check nulls/negatives, use when expressions for conditions

**MongoDB Integration:**

- **Repository Pattern:** Domain interfaces with domain types, adapter implementations with MongoDB classes
- **Entity Mapping:** MongoDB entities in adapter layer only, clean mapping between layers
- **Configuration:** ApplicationScoped CDI, PanacheMongoRepository inheritance, no MongoDB annotations in domain

### Security Requirements (MANDATORY)

**Security Scope & Boundaries:**

- **Infrastructure Responsibility:** Authentication and authorization handled by infrastructure - SCS MUST NEVER implement
- **SCS Responsibility:** Input validation, data protection, business logic security only

**Data Protection & Privacy:**

- **GDPR Compliance:** NO personal data storage - search operates on anonymous basis only
- **Data Minimization:** Right to be forgotten, minimal data collection
- **Secure Queries:** Parameterized queries - no string concatenation for database operations
- **Input Validation:** Validate all boundaries and user inputs
- **NO Sensitive Logging:** NO logging of sensitive data

**System Security Patterns:**

- **Domain Layer Purity:** NO framework annotations in domain layer, NO infrastructure concerns in domain layer
- **Injection Prevention:** NO SQL injection patterns - use parameterized queries
- **Resource Management:** Close all connections properly to avoid resource leaks

**Data Consistency & Isolation:**

- **SCS Isolation:** NO direct database access between SCS instances
- **Schema Separation:** NO shared database schemas between SCS boundaries
- **Consistency Model:** MANDATORY eventual consistency for cross-SCS data synchronization

**Vue.js Development Standards:**

- **Composition API Only:** Use Composition API syntax with reactive functions, TypeScript interfaces for all Props and Emits
- **Component Architecture:** Base components (reusable UI), Layout components (structure), Business components (domain-specific)
- **Single Responsibility:** Single responsibility per component, business logic in composables
- **Styling:** Semantic HTML, BEM methodology, scoped styling
- **State Management:** Vuex with TypeScript typing, namespaced modules, composables for reactive business logic
- **Error Handling:** Centralized error handling with try-catch patterns
- **FORBIDDEN:** Options API, untyped props/events, Vue 2 patterns

**Project Structure:**

- **Standard SCS layout:** backend/ (Quarkus/Kotlin), frontend/ (Vue.js), infrastructure/, docs/, .taskmaster/

### Red Flag Standards (MANDATORY)

**Design & Architecture Red Flags:**

**Red Flag #1: Shallow Module** - Interface complexity matches implementation complexity

- Interface simplicity: Module interfaces MUST be significantly simpler than implementations
- Implementation hiding: Public APIs MUST hide internal complexity from callers
- Minimal surface area: Expose only essential methods and properties

**Red Flag #2: Information Leakage** - Design decisions scattered across multiple modules

- Centralized decisions: Each design decision should be encapsulated in one place
- Clear ownership: Modules should own their internal design choices

**Red Flag #3: Temporal Decomposition** - Code structure follows execution order instead of information hiding

- Information-based structure: Organize by what information is hidden, not execution order
- Logical grouping: Group related functionality together regardless of when it executes

**Red Flag #4: Overexposure** - Common operations require knowledge of advanced features

- Progressive disclosure: Common use cases should not require advanced feature knowledge
- Sensible defaults: Provide reasonable defaults to minimize configuration

**Red Flag #7: Special-General Mixture** - Business logic mixed with general-purpose utilities

- Clear boundaries: Separate domain-specific logic from general-purpose utilities
- Dependency direction: Special-purpose code should depend on general-purpose, not vice versa

**Method & Interface Red Flags:**

**Red Flag #5: Pass-Through Method** - Methods that only delegate without adding value

- Value-adding methods: Every method must add meaningful functionality beyond delegation
- Justified abstraction: Only create wrapper methods with clear abstraction benefits

**Red Flag #8: Conjoined Methods** - Methods requiring deep understanding of each other

- Self-contained methods: Each method should be understandable without deep knowledge of others
- Minimal dependencies: Reduce coupling between methods within the same class

**Red Flag #10: Implementation Contamination** - Interface documentation reveals implementation details

- User-focused documentation: Interface docs describe WHAT and WHY, not HOW
- Behavioral contracts: Document expected behavior, preconditions, and postconditions

**Code Quality Red Flags:**

**Red Flag #6: Repetition** - Nontrivial code duplicated beyond 3% threshold

- Extract common logic: Immediately extract any duplicated nontrivial code
- Shared utilities: Create utility functions for common operations

**Red Flag #9: Comment Repeats Code** - Comments state what code obviously does

- Value-adding comments: Comments must explain WHY, not WHAT the code does
- Context provision: Explain reasoning behind design decisions

**Red Flag #11: Vague Name** - Names like "data", "info", "manager", "handler"

- Descriptive names: Names must clearly indicate purpose and scope
- Domain language: Use terminology from business domain, not technical jargon

**Red Flag #12: Hard to Pick Name** - Multiple responsibilities making naming difficult

- Single responsibility: Hard-to-name entities often have multiple responsibilities
- Refactoring trigger: Naming difficulty should trigger design review

**Red Flag #13: Hard to Describe** - Incomplete documentation requiring guesswork

- Complete behavioral description: All public methods must have complete documentation
- Edge case coverage: Document all important edge cases and error conditions

**Red Flag #14: Nonobvious Code** - Code behavior cannot be understood without extensive comments

- Expressive code: Code should clearly express its intent without requiring comments
- Readable algorithms: Complex algorithms should be broken down into understandable steps

**Red Flag Checklist for Code Review:**

- [ ] **Shallow Module (#1):** Interface complexity matches implementation complexity
- [ ] **Information Leakage (#2):** Design decisions scattered across multiple modules
- [ ] **Temporal Decomposition (#3):** Code structure follows execution order instead of information hiding
- [ ] **Overexposure (#4):** Common operations require knowledge of advanced features
- [ ] **Pass-Through Method (#5):** Methods that only delegate without adding value
- [ ] **Repetition (#6):** Nontrivial code duplicated beyond 3% threshold
- [ ] **Special-General Mixture (#7):** Business logic mixed with general-purpose utilities
- [ ] **Conjoined Methods (#8):** Methods requiring deep understanding of each other
- [ ] **Comment Repeats Code (#9):** Comments state what code obviously does
- [ ] **Implementation Contamination (#10):** Interface documentation reveals implementation details
- [ ] **Vague Name (#11):** Names like "data", "info", "manager", "handler"
- [ ] **Hard to Pick Name (#12):** Multiple responsibilities making naming difficult
- [ ] **Hard to Describe (#13):** Incomplete documentation requiring guesswork
- [ ] **Nonobvious Code (#14):** Code behavior cannot be understood without extensive comments

**Red Flag Resolution Process:**

1. **Immediate Flag:** Stop review if critical red flags found
2. **Root Cause Analysis:** Identify why the red flag exists
3. **Refactoring Plan:** Create specific plan to address the anti-pattern
4. **Test Coverage:** Ensure tests exist before refactoring
5. **Incremental Fix:** Address in small, testable increments

## Quality Standards

### Testing Strategy (MANDATORY)

**Backend Testing Stack:**

- **Unit Testing**: JUnit 5 + Mockk for fast, isolated tests
- **Integration Testing**: TestContainers for database and external service integration
- **Architecture Testing**: ArchUnit for layer validation and dependency compliance

**Frontend Testing Stack:**

- **Unit Testing**: Jest + Vue Test Utils for component and composable testing
- **E2E Testing**: Playwright for user journey testing
- **Component Testing**: Vue Test Utils with TypeScript support

**Testing Standards:**

- **Structure**: BDD (Given-When-Then) format for all tests
- **Coverage**: Minimum 80% code coverage across all layers
- **Isolation**: Complete test isolation, no production dependencies
- **Types**: Unit (fast, isolated), Integration (TestContainers), E2E (user journeys), Architecture (layer validation)

### Performance Standards (MANDATORY)

**Algorithm Complexity:**

- Use efficient O(n) algorithms with proper data structures
- NO O(n²) or higher complexity algorithms
- Avoid imperative loops - use functional operations
- Chain collection operations for efficiency

**Database Performance:**

- All queries must use proper indexes
- Required indexes: klassifikationId+active, price+currency, availability fields, text search
- NO queries without indexes
- Use parameterized queries to prevent injection and improve performance

**API Performance:**

- Keep API response times under 500ms for standard queries
- Bounded data loading to prevent memory issues
- Proper object lifecycle management
- JSON envelope responses for consistency

**Frontend Performance:**

- Optimize for Core Web Vitals (FCP, LCP, CLS, FID)
- Lazy loading for heavy components
- Shallow references for large arrays, non-reactive objects for performance
- Route-level code splitting
- Composition API for better performance vs Options API

**Resource Management:**

- Close all connections properly to avoid resource leaks
- Proper memory management to avoid memory leaks
- Efficient collection operations with chaining
- Proper MongoDB connection lifecycle management

### Quality Gates (MANDATORY)

**Pre-Commit Gates:**

- Linting and code formatting
- Unit tests with 80% coverage
- Security checks (input validation, data protection)
- Performance validation (no O(n²) algorithms, memory patterns)

**Pre-Merge Gates:**

- Integration tests with TestContainers
- Architecture compliance (layer coupling validation)
- Code review approval
- SCS communication pattern compliance

**Pre-Deploy Gates:**

- E2E tests with Playwright
- Performance tests under load
- Security validation
- Database index optimization verification

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

### Git Workflow & Critical Review Priorities

**Git Workflow:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` with task IDs), Atomic Commits (single logical change per commit), Branch Naming (`feature/`, `bugfix/`, `security/`, `perf/`)

### Code Review Standards (MANDATORY)

**Critical Review Priorities:**

1. **Data Security** - Input validation, data protection, business logic security (NOT auth/authorization)
2. **Performance** - Algorithm complexity, memory patterns, database optimization
3. **Architecture** - Layer coupling, value object immutability, boundary conditions
4. **Code Quality** - Complexity analysis, duplication elimination, test coverage

**Review Focus Areas:**

- Architecture layer compliance (domain/application/adapter boundaries)
- SCS communication patterns (Kafka-only, no direct calls)
- Security scope (input validation, no auth implementation)
- Performance standards (O(n) algorithms, proper indexing)

**Red Flag Review Process:**

- Use Red Flag Checklist (see "Red Flag Standards" section for complete 14-point checklist)
- Apply Red Flag Resolution Process for any identified issues
- Ensure all quality gates pass before approval

## Context & Task Management

### Project Awareness & Context (MANDATORY)

**AI Behavior Rules:**

- **Never assume missing context. Ask questions if uncertain.**
- **Never hallucinate libraries or functions** – only use known, verified packages and APIs
- **Always confirm file paths and class names** exist before referencing them in code or tests
- **Maintain context continuity** across Claude sessions using TaskMaster and PRD references

**Task Completion Standards:**

- **Mark completed tasks immediately** after finishing implementation
- **Add discovered sub-tasks** to TaskMaster during development
- **Document blockers and solutions** in task notes for future reference
- **Link tasks to commits** using conventional commit format with task IDs

### PRD Integration with DDD Architecture

**Requirements Analysis:**

- **Domain Modeling:** Extract aggregates, entities, and value objects from PRDs
- **Use Case Identification:** Map PRD features to application services
- **Architecture Impact:** Assess new requirements against existing Onion Architecture
- **Performance Implications:** Analyze requirements for potential performance bottlenecks

**Context Engineering Best Practices:**

- **Ubiquitous Language:** Ensure PRD terminology matches domain model
- **Bounded Context Analysis:** Identify context boundaries from requirements
- **Integration Points:** Plan adapter layer changes for new requirements
- **Test Strategy:** Define acceptance criteria and test approaches in PRDs

---

*Finden Development Guide v2.0 | Self-Contained System | Specific implementation guidelines | Evidence-based practices*
