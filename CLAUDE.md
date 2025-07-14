# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Table of Contents

- [System Overview & Architecture](#system-overview--architecture)
- [Technology Constraints](#technology-constraints)
- [Architecture Standards](#architecture-standards)
- [Security Requirements](#security-requirements)
- [Red Flag Standards](#red-flag-standards)
- [Quality Standards](#quality-standards)
- [Development Workflow](#development-workflow)
- [Context & Task Management](#context--task-management)

## System Overview & Architecture

### Self-Contained Systems (SCS) Principles

The Finden application is designed as a **Self-Contained System (SCS)** - an autonomous unit that includes its own web interface, business logic, and database, designed to handle a specific business capability end-to-end.

**SCS Implementation Principles:**

- **UI Ownership**: Each SCS MUST include its own web interface, NO shared UI components between SCS boundaries
- **Data Autonomy**: Dedicated database per SCS, NO direct database access between systems
- **Communication Boundaries**: Asynchronous communication only via Kafka with Avro (NOT direct API calls)
- **Deployment Independence**: Each SCS deployed as complete unit, NO deployment coordination required
- **Security Model**: Authentication and authorization handled by infrastructure
- **Data Isolation**: NO direct database access between SCS instances, NO shared database schemas

**Architecture Pattern:** Onion/Hexagonal with Domain-Driven Design (DDD) principles

**System Components:**

- **Finden Search SCS**: Product search, classification, filtering with MongoDB and Kafka events
- **Adjacent Systems**: User Management, Order Processing, Product Management
- **Database**: Collections (products, classifications, search_analytics, availability_cache)
- **API**: Base `/api/v1/search`, JSON envelope responses, OpenAPI docs at `/api/docs`
- **Events**: Kafka topics - product.updated, pricing.changed, availability.changed, search.performed
- **Caching**: Application (classification hierarchy), Database (WiredTiger), CDN (static assets)

**Required Indexes**: klassifikationId+active, price+currency, availability fields, text search

## Technology Constraints (MANDATORY)

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

**FORBIDDEN Technologies:**

- Spring Framework (use Quarkus)
- Vue.js Options API (use Composition API)
- Direct MongoDB drivers (use Panache)
- fetch API (use Axios)

## Architecture Standards (MANDATORY)

**Onion/Hexagonal Architecture Layers:**

- **Domain Layer (Core):** Model entities, value objects, aggregates, domain services, repository interfaces, domain exceptions
  - **Rules:** Immutable value objects, zero outward dependencies, no external dependencies
  - **FORBIDDEN:** Framework annotations, infrastructure concerns, adapter DTOs or external data structures
  - **Code Organization:** model/ (entities, value objects), service/ (domain services), repository/ (interfaces), exception/ (domain exceptions)

- **Application Layer:** Application services implementing use cases, DTOs for data contracts, mappers at boundaries, coordination services
  - **Rules:** Use cases orchestrate domain, DTOs for external contracts, depends only on domain interfaces
  - **FORBIDDEN:** Business logic in application services, direct database access
  - **Code Organization:** usecase/ (business operations), dto/ (data contracts), mapper/ (DTO↔domain), service/ (coordination)

- **Adapter Layer (Outer):** REST controllers, database adapters, message consumers, external service clients
  - **Rules:** Controllers delegate to application layer, repository implementations use specific tech, implement domain interfaces
  - **FORBIDDEN:** Business logic in adapters, domain contamination
  - **Code Organization:** web/ (REST controllers), persistence/ (database adapters), messaging/ (events), external/ (clients)

**Frontend Architecture (Vue.js 3-Layer):**

- **Presentation Layer:** Vue components, templates, styles - UI rendering only
- **Business Logic Layer:** Composables for reactive business logic and state management
- **Data Access Layer:** HTTP clients, API abstractions, data transformation
- **Dependencies:** Presentation → Composables → API (strict layer dependency)
- **FORBIDDEN:** Business logic in components, direct API calls from components, Options API patterns
- **Code Organization:** apps/ (entry points), shared/ (components), api/ (backend communication), composables/ (business logic), store/ (Vuex state)

**File Naming Standards:**

- **Backend:** PascalCase with suffixes (e.g., `UserService`, `ProductRepository`), lowercase packages
- **Frontend:** PascalCase components (e.g., `ProductCard.vue`), camelCase composables with `use` prefix (e.g., `useProductSearch`), lowercase directories with hyphens

**Development Standards:**

**Kotlin/Backend:**

- **Immutability:** Use `val` over `var` (MANDATORY), immutable data classes with `val` properties and `copy()` method
- **Null Safety:** Use safe operators (`?.`, `?:`), avoid force unwrapping unless justified, Optional patterns for domain modeling
- **Collections:** Functional operations (`map`, `filter`, `fold`, `reduce`), chain transformations, avoid imperative loops
- **Error Handling:** Specific exceptions with context, inherit from base domain exception class
- **Input Validation:** Validate all inputs, check nulls/negatives, use when expressions for conditions
- **Pure Functions:** Domain logic as pure functions with no side effects, push side effects to adapter layer
- **Fail Fast:** Validate early and fail fast with clear error messages
- **Resource Management:** Close all connections properly to avoid resource leaks

**Vue.js/Frontend:**

- **Composition API Only:** Use Composition API syntax with reactive functions, TypeScript interfaces for all Props and Emits
- **Component Architecture:** Base components (reusable UI), Layout components (structure), Business components (domain-specific)
- **Single Responsibility:** Single responsibility per component, business logic in composables
- **Styling:** Semantic HTML, BEM methodology, scoped styling
- **State Management:** Vuex with TypeScript typing, namespaced modules, composables for reactive business logic
- **Error Handling:** Centralized error handling with try-catch patterns

**FORBIDDEN Anti-Patterns:**

- Mutable global state or shared mutable objects
- Functions with side effects mixed with business logic
- Imperative loops when functional alternatives exist
- Null checks that can be replaced with safe operators
- Exception handling for expected business scenarios
- Options API, untyped props/events, Vue 2 patterns
- Business logic in components or adapters

## Security Requirements (MANDATORY)

**Security Scope & Boundaries:**

- **Infrastructure Responsibility:** Authentication and authorization handled by infrastructure - SCS MUST NEVER implement
- **SCS Responsibility:** Input validation, data protection, business logic security only

**Data Protection & Privacy:**

- **GDPR Compliance:** NO personal data storage - search operates on anonymous basis only
- **Data Minimization:** Right to be forgotten, minimal data collection
- **Input Validation:** Validate all boundaries and user inputs with proper error handling
- **NO Sensitive Logging:** NO logging of sensitive data

**System Security Patterns:**

- **Domain Layer Purity:** NO framework annotations in domain layer, NO infrastructure concerns in domain layer
- **Injection Prevention:** NO SQL injection patterns - use parameterized queries (no string concatenation for database operations)
- **SCS Isolation:** NO direct database access between SCS instances
- **Schema Separation:** NO shared database schemas between SCS boundaries
- **Consistency Model:** MANDATORY eventual consistency for cross-SCS data synchronization

### Red Flag Standards (MANDATORY)

**Red Flag Checklist for Code Review:**

- [ ] **Shallow Module (#1):** Interface complexity matches implementation - interfaces MUST be simpler than implementations
- [ ] **Information Leakage (#2):** Design decisions scattered across modules - encapsulate each decision in one place
- [ ] **Temporal Decomposition (#3):** Code structure follows execution order - organize by information hiding, not execution order
- [ ] **Overexposure (#4):** Common operations require advanced knowledge - provide reasonable defaults
- [ ] **Pass-Through Method (#5):** Methods only delegate without value - every method must add meaningful functionality
- [ ] **Repetition (#6):** Nontrivial code duplicated >3% - immediately extract duplicated code
- [ ] **Special-General Mixture (#7):** Business logic mixed with utilities - separate domain-specific from general-purpose
- [ ] **Conjoined Methods (#8):** Methods require deep understanding of each other - each method should be independently understandable
- [ ] **Comment Repeats Code (#9):** Comments state what code does - comments must explain WHY, not WHAT
- [ ] **Implementation Contamination (#10):** Interface docs reveal implementation - describe WHAT and WHY, not HOW
- [ ] **Vague Name (#11):** Names like "data", "info", "manager" - use clear business domain terminology
- [ ] **Hard to Pick Name (#12):** Multiple responsibilities make naming difficult - hard-to-name entities should trigger design review
- [ ] **Hard to Describe (#13):** Incomplete documentation requiring guesswork - document all public methods including edge cases
- [ ] **Nonobvious Code (#14):** Code behavior requires extensive comments - code should clearly express intent

## Quality Standards (MANDATORY)

### Testing Strategy

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
- **Coverage**: Minimum 80% code coverage for unit/integration tests (architecture tests validate layer compliance)
- **Isolation**: Complete test isolation, no production dependencies
- **Types**: Unit (fast, isolated), Integration (TestContainers), E2E (user journeys), Architecture (layer validation)

### Performance Standards

**Algorithm Complexity:**

- Use efficient O(n) algorithms with proper data structures
- NO O(n²) or higher complexity algorithms
- Avoid imperative loops - use functional operations
- Chain collection operations for efficiency

**Database Performance:**

- All queries must use proper indexes (klassifikationId+active, price+currency, availability fields, text search)
- NO queries without indexes

**API Performance:**

- API response times P95 < 300ms for all queries (including input validation overhead)
- Bounded data loading to prevent memory issues
- Proper object lifecycle management
- JSON envelope responses for consistency

**Frontend Performance:**

- Optimize for Core Web Vitals (FCP, LCP, CLS, FID)
- Lazy loading for heavy components
- Shallow references for large arrays, non-reactive objects for performance (balance with immutability requirements)
- Route-level code splitting
- Composition API for better performance vs Options API

**Resource Management:**

- Close all connections properly to avoid resource leaks
- Proper memory management to avoid memory leaks
- Efficient collection operations with chaining

### Quality Gates & Code Review

**Quality Gates (3-Stage Process):**

1. **Local Development**: Unit tests, lint, build (Backend: `unitTest`, `integrationTest`, `detekt`, `build` | Frontend: `install`, `lint`, `unitTest`, `build`)

2. **Pre-Merge (GitLab CI)**: Integration tests with TestContainers, architecture compliance validation, code review approval, SCS communication pattern compliance

3. **Pre-Deploy (GitLab CI)**: E2E tests with Playwright, performance tests under load, security validation, Docker image build and push, Kubernetes deployment validation

**Code Review Priorities:**

1. **Architecture** - Layer coupling, value object immutability, boundary conditions
2. **Performance** - Algorithm complexity, memory patterns, database optimization
3. **Code Quality** - Complexity analysis, duplication elimination, test coverage
4. **Security** - Input validation, data protection, parameterized queries, resource management

**Review Focus Areas:**

- Architecture layer compliance (domain/application/adapter boundaries)
- Performance standards compliance (P95 < 300ms, proper indexing)
- Security requirements compliance (input validation, parameterized queries)
- Use Red Flag Checklist (complete 14-point checklist)
- Ensure all quality gates pass before approval

## Development Workflow (MANDATORY)

### SuperClaude v3 Integration

**Available Personas:** Architect, Frontend, Backend, Security, QA, Performance - auto-activate based on file patterns and task context.
**Auto-Activation Rules:** Frontend files → Frontend | API/server/database files → Backend | Test files → QA | Architecture/design → Architect | Input validation → Security | Optimization → Performance
**SuperClaude v3 Integration**: Use persona flags, MCP servers, and thinking modes as needed for development tasks.

### Daily Development Loop (TDD with Task-Master)

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

**Pre-Push Validation:** Follow Quality Gates (pre-commit, pre-merge, pre-deploy)

### Git Workflow

**Git Workflow:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` with task IDs), Atomic Commits (single logical change per commit), Branch Naming (`feature/`, `bugfix/`, `security/`, `perf/`)

**SuperClaude v3 Git Integration:**

- Use `/git` commands for commit message generation
- Leverage `--persona-scribe` for professional commit messages
- Apply `--seq` for complex merge conflict resolution

### AI Behavior & Context Management

**AI Behavior Rules:** Never assume missing context - ask questions if uncertain. Never hallucinate libraries or functions - only use known, verified packages and APIs. Always confirm file paths and class names exist before referencing them.

**Task Completion Standards:** Mark completed tasks immediately after finishing implementation. Add discovered sub-tasks to TaskMaster during development. Document blockers and solutions in task notes.

**PRD Integration:** Extract domain entities from PRDs, map features to application services, assess architecture impact, ensure PRD terminology matches domain model, identify bounded context boundaries.

**Context Optimization:** Use `--uc` for token optimization when context usage >75%, apply `--delegate` for large codebase analysis (>50 files), use `--wave-mode` for complex multi-stage operations, leverage `--seq` for systematic analysis and debugging, use `--c7` for documentation and framework pattern lookups.

---

*Finden Development Guide v3.0 | Self-Contained System | Specific implementation guidelines | Evidence-based practices*
