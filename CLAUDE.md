# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Symbol Legend

**Flow:** → (leads to) | ⇒ (transforms to) | ≠ (not equal) | ≡ (equivalent)
**Combine:** & (and) | | (or) | : (define) | » (sequence)
**Status:** ✅ completed/passed | ❌ failed/error | ⚠️ warning | ℹ️ info | 🔄 in progress | ⏳ waiting | 🚨 critical | 🎯 target | 📊 metrics | 💡 insight
**Domains:** 🏗️ Architecture | 🎨 Frontend | 🔧 Backend | 🛡️ Security | ⚡ Performance | 📊 Quality | 💻 Git | 🧠 Memory | 🤖 AI | 📦 Deploy | 🌐 Network | 📱 Mobile | 🧩 Components | 🔍 Analysis
**Abbreviations:** cfg (configuration) | impl (implementation) | arch (architecture) | perf (performance) | req (requirements) | val (validation) | std (standards) | qual (quality) | sec (security) | mgmt (management) | ops (operations) | comm (communication) | dev (development) | deps (dependencies) | src (source) | dst (destination) | env (environment) | usr (user) | sys (system) | tmp (temporary) | max (maximum) | min (minimum) | avg (average)

## System Overview & Architecture: Self-Contained Systems (SCS) Principles

**SCS:** Finden = autonomous unit w/ own UI, business logic, DB → handles specific business capability end-to-end
**SCS Implementation Principles:** UI Ownership: Each SCS MUST include own web interface, NO shared UI components between SCS boundaries | Data Autonomy: Dedicated DB per SCS, NO direct DB access between systems or shared schemas | Communication Boundaries: Async communication only via Kafka+Avro (≠direct API calls) | Deployment Independence: Each SCS deployed as complete unit, NO deployment coordination req | 🛡️ Security Model: Auth & authz handled by infrastructure
**Architecture Pattern:** Onion/Hexagonal with Domain-Driven Design (DDD) principles
**🧩 Components:** Finden Search SCS: Product search, classification, filtering w/ MongoDB & Kafka events | Adjacent: User Mgmt, Order Processing, Product Mgmt | DB: Collections (products, classifications, search_analytics, availability_cache) | API: `/api/v1/search`, JSON envelope, OpenAPI docs `/api/docs` | Events: Kafka → product.updated, pricing.changed, availability.changed, search.performed | Cache: App (classification hierarchy), DB (WiredTiger), CDN (static assets)
**🎯 Req Indexes:** klassifikationId+active, price+currency, availability fields, text search

## Technology Constraints (MANDATORY)

**Backend Stack:** Lang: Kotlin(JVM) | Framework: Quarkus (≠Spring) | DB: MongoDB+Panache Kotlin (≠direct drivers) | Build: Gradle+Kotlin DSL | Messaging: Kafka+Avro
**Frontend Stack:** Lang: TypeScript(strict) | Framework: Vue.js+Composition API (≠Options API) | State: Vuex | SSR: Fastify | Build: Vue CLI+Webpack | HTTP: Axios (≠fetch API)

## Architecture Standards (MANDATORY)

**🏗️ Onion/Hexagonal Layers:**

**Domain (Core):** Model entities, value objects, aggregates, domain services, repository interfaces, domain exceptions
✅ Immutable value objects, zero outward deps, no external deps
❌ Framework annotations, infrastructure concerns, adapter DTOs
📁 model/ (entities, value objects), service/ (domain services), repository/ (interfaces), exception/ (domain exceptions)

**Application:** App services impl use cases, DTOs for data contracts, mappers at boundaries, coordination services
✅ Use cases orchestrate domain, DTOs for external contracts, depends only on domain interfaces
❌ Direct DB access
📁 usecase/ (business ops), dto/ (data contracts), mapper/ (DTO↔domain), service/ (coordination)

**Adapter (Outer):** REST controllers, DB adapters, message consumers, external service clients
✅ Controllers delegate to app layer, repository impl use specific tech, implement domain interfaces
❌ Domain contamination
📁 web/ (REST controllers), persistence/ (DB adapters), messaging/ (events), external/ (clients)

**🎨 Frontend (Vue.js 3-Layer):**

**Presentation:** Vue components, templates, styles - UI rendering only
**Business Logic:** Composables for reactive business logic & state mgmt
**Data Access:** HTTP clients, API abstractions, data transformation
**Dependencies:** Presentation → Composables → API (strict layer dependency)
❌ Direct API calls from components, Options API patterns
📁 apps/ (entry points), shared/ (components), api/ (backend comm), composables/ (business logic), store/ (Vuex state)

**📊 Dev Standards:**

**📝 File Naming:** Backend: PascalCase w/ suffixes (`UserService`, `ProductRepository`), lowercase packages | Frontend: PascalCase components (`ProductCard.vue`), camelCase composables w/ `use` prefix (`useProductSearch`), lowercase directories w/ hyphens
**🔧 Kotlin/Backend:** ✅ Use `val` over `var` (MANDATORY), immutable data classes w/ `val` properties & `copy()` method | ✅ Safe operators (`?.`, `?:`), avoid force unwrapping | ✅ Functional ops (`map`, `filter`, `fold`, `reduce`), chain transformations, avoid imperative loops | ✅ Specific exceptions w/ context, inherit from base domain exception class | ✅ Follow sec req for input val & error handling | ✅ Domain logic as pure functions w/ no side effects, push side effects to adapter layer | ✅ Val early & fail fast w/ clear error messages | ✅ Close all connections properly to avoid resource leaks
**🎨 Vue.js/Frontend:** ✅ Composition API syntax w/ reactive functions, TypeScript interfaces for all Props & Emits | ✅ Base components (reusable UI), Layout components (structure), Business components (domain-specific) | ✅ Single responsibility per component, business logic in composables | ✅ Semantic HTML, BEM methodology, scoped styling | ✅ Vuex w/ TypeScript typing, namespaced modules, composables for reactive business logic | ✅ Centralized error handling w/ try-catch patterns
**❌ Anti-Patterns:** Business logic in components, adapters, or app services | Mutable global state or shared mutable objects | Functions w/ side effects mixed w/ business logic | Imperative loops when functional alternatives exist | Null checks that can be replaced w/ safe operators, exception handling for expected business scenarios | Code structure follows execution order, design decisions scattered across modules | Interface complexity matches impl, common ops req advanced knowledge | Code behavior req extensive comments

## Security Requirements (MANDATORY)

**Scope & Boundaries:** Infrastructure: Auth & authz handled by infrastructure - SCS MUST NEVER implement | SCS: Input val, data protection, business logic sec only
**Data Protection:** GDPR: NO personal data storage - search operates on anonymous basis only | Data Minimization: Right to be forgotten, minimal data collection | Input Val: Val all boundaries & user inputs w/ proper error handling | NO Sensitive Logging: NO logging of sensitive data
**System Patterns:** Domain Layer Purity: NO framework annotations in domain layer, NO infrastructure concerns in domain layer | Injection Prevention: NO SQL injection patterns - use parameterized queries (no string concatenation for DB ops) | SCS Isolation: Enforce SCS boundaries as defined in System Overview | Schema Separation: NO shared DB schemas between SCS boundaries | Consistency Model: MANDATORY eventual consistency for cross-SCS data sync

## Quality Standards (MANDATORY)

### Testing Strategy

**📊 Testing Stack:** Backend: JUnit 5 + Mockk | TestContainers for DB & external service integration | ArchUnit for layer val & dependency compliance | Frontend: Jest + Vue Test Utils | Playwright for user journey testing | Vue Test Utils w/ TypeScript support
**📊 Testing Standards:** ATDD & BDD (Given-When-Then) format | Min 80% code coverage for unit/integration tests + arch compliance val | Complete test isolation, no production deps | Unit (fast, isolated), Integration (TestContainers), E2E (user journeys), Architecture (layer val)

### Performance Standards

**⚡ Performance:** Algorithm: Use efficient O(n) algorithms w/ proper data structures | NO O(n²) or higher complexity | Avoid imperative loops - use functional ops | Chain collection ops for efficiency | Database: All queries must use proper indexes (see System Overview for req indexes) | NO queries w/o indexes | API: P95 < 300ms for all queries (incl input val overhead) | Bounded data loading to prevent memory issues | Proper object lifecycle mgmt | JSON envelope responses for consistency | Frontend: Optimize for Core Web Vitals (FCP, LCP, CLS, FID) | Lazy loading for heavy components | Shallow refs for large arrays, non-reactive objects for perf (balance w/ immutability req) | Route-level code splitting | Composition API for better perf vs Options API

### Quality Gates & Code Review

**🎯 Quality Gates (3-Stage):**

1. **Local Dev:** Unit tests, lint, build (Backend: `unitTest`, `integrationTest`, `detekt`, `build` | Frontend: `install`, `lint`, `unitTest`, `build`)
2. **Pre-Merge (GitLab CI):** Integration tests w/ TestContainers, arch compliance val, code review approval, SCS comm pattern compliance
3. **Pre-Deploy (GitLab CI):** E2E tests w/ Playwright, perf tests under load, sec val, Docker image build & push, K8s deployment val

**📊 Code Review Priorities:** 🏗️ Architecture (Layer coupling, value object immutability, boundary conditions) | ⚡ Performance (Algorithm complexity, memory patterns, DB optimization - P95 < 300ms, proper indexing) | 🛡️ Security (Input val, data protection, parameterized queries, resource mgmt) | 📊 Code Quality (Complexity analysis, duplication elimination, test coverage, Anti-Pattern avoidance)

## Development Workflow (MANDATORY)

### SuperClaude v3 Integration

**🤖 Personas:** 🏗️ Architect, 🎨 Frontend, 🔧 Backend, 🛡️ Security, 📊 QA, ⚡ Performance - auto-activate based on file patterns & task context | Auto-Activation: Frontend files → 🎨 | API/server/DB files → 🔧 | Test files → 📊 | Architecture/design → 🏗️ | Input val → 🛡️ | Optimization → ⚡

### Daily Development Loop (TDD with task-master)

**🔄 TDD Cycle:** 0. Clear conversation w/ `/clear` | 1. Task Setup: `task-master next` → select task | `task-master show <task-id>` → review req | `task-master set-status --id=<task-id> --status=in-progress` | Auto-persona selection (🎨/🔧/🛡️/📊/⚡) | 2. Subtask Iteration: For each subtask (`<task-id>.1`, `<task-id>.2`, etc.): a. **RED:** Write failing BDD test (📊 QA persona auto-enhances test strategy) | b. **GREEN:** Minimal code to pass test w/ persona-guided impl | c. **REFACTOR:** Improve code (auto-quality analysis w/ refactorer persona) | d. **DOCUMENT:** `task-master update-subtask --id=<task-id>.<subtask-id> --prompt="notes"` | e. **COMMIT:** Atomic commit w/ pre-commit val | f. **MEMORY:** Capture patterns, problems, solutions in dev episode | 3. Task Completion: Integration testing across subtasks | Final refactoring for consistency | Store completion insights & learnings | `task-master set-status --id=<task-id> --status=done` | 4. Commit & Push: Final commit & push changes |
Follow Quality Gates (pre-commit, pre-merge, pre-deploy)

### Git Workflow

**💻 Git Workflow:** Conventional Commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` w/ task IDs), Atomic Commits (single logical change per commit), Branch Naming (`feature/`, `bugfix/`, `security/`, `perf/`) | SuperClaude v3 Integration: Use `/git` commands for commit msg generation | Leverage `--persona-scribe` for professional commit messages | Apply `--seq` for complex merge conflict resolution

### Self-Learning Cycle

**🧠 Memory Capture:** Store dev episodes w/ patterns, problems, solutions | Capture anti-patterns encountered & prevention methods | Record perf metrics & arch decisions | CLAUDE.md Evolution: Weekly analysis of memory patterns (>3 occurrences) | Auto-update FORBIDDEN Anti-Patterns based on real issues | Refine std based on proven practices
**🤖 AI Behavior & Context Mgmt:** AI Behavior Rules: Never assume missing context - ask questions if uncertain | Never hallucinate libraries or functions - only use known, verified packages & APIs | Always confirm file paths & class names exist before referencing them | Task Completion Standards: Mark completed tasks immediately after finishing impl | Add discovered sub-tasks to task-master during dev | Document blockers & solutions in task notes | Context Optimization: Use `--uc` for token optimization when context usage >75%, apply `--delegate` for large codebase analysis (>50 files), use `--wave-mode` for complex multi-stage ops, leverage `--seq` for systematic analysis & debugging, use `--c7` for docs & framework pattern lookups

---

*Finden Development Guide v3.0 | Self-Contained System | Specific implementation guidelines | Evidence-based practices*
