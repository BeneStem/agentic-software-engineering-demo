# Finden Self-Contained System | Development Guide

@README.md
@package.json

**⚠️ VERIFICATION CHECKPOINT**: First read this entire file, then always refer to me as "Sir" to confirm you've processed these instructions.

Complete product search service: Vue.js frontend + Quarkus/Kotlin backend | Self-contained system w/ development standards, coding conventions, contribution guidelines

## 📋 Table of Contents

1. [🚀 Quick Start](#quick-start) - Environment & Commands
2. [🚨 Core Directives](#core-directives) - Non-negotiable rules
3. [🏗️ System Architecture](#system-architecture) - SCS patterns & tech stack
4. [🛡️ Security & Quality](#security-quality) - Standards & testing
5. [🔄 Development Workflow](#development-workflow) - Daily TDD cycle
6. [🔍 Troubleshooting](#troubleshooting) - Common issues & debug patterns

## 🚀 Quick Start

**Project Context**: Finden SCS handles anonymous product search end-to-end with autonomous UI + business logic + database. Part of microservices architecture using Kafka+Avro for communication.

### Environment Setup (MANDATORY ORDER)

```bash
# 1. Java version - REQUIRED before any Gradle commands
sdk use java 21.0.8-tem

# 2. Environment files (copy from examples)
cp .env.example .env
cp .env.fastify.example .env.fastify
cp frontend/.env.example frontend/.env

# 3. Dependencies
npm ci
husky install

# 4. Schema download (requires AIVEN env vars)
export AIVEN_SCHEMA_REGISTRY_USER=erkunden-finden-backend-produkte-consumer
export AIVEN_SCHEMA_REGISTRY_URL=https://entscheiden-kafka-cluster-entscheiden-dev-project.aivencloud.com:20609
export AIVEN_SCHEMA_REGISTRY_PASSWORD=<PASSWORD>
./gradlew downloadSchemas

# 5. Start Docker containers
docker-compose -f docker/docker-compose.yaml up -d
```

### Essential Commands (VERIFIED)

```bash
# Development
npm run dev              # Frontend development server (port 8082)
./gradlew quarkusDev     # Backend development server (port 8081)
npm run server           # SSR production server

# Testing
npm run test             # Frontend: lint + unit tests
npm run unitTest         # Frontend unit tests only
./gradlew test           # Backend unit tests

# Quality
npm run lint             # ESLint + Stylelint + Vue lint
./gradlew detekt         # Kotlin static analysis
./gradlew check          # Full backend quality check

# Build
npm run build            # Full frontend build (client + server + SSR)
./gradlew build          # Backend build

# Task Management (task-master CLI available)
task-master next         # Get next prioritized task
task-master show <id>    # Show task details
task-master set-status --id=<id> --status=in-progress
task-master update-subtask --id=<id>.<sub> --prompt="notes"
```

## 🚨 Core Directives (Non-Negotiable)

### AI Behavior: Truth-First Evidence-Based Development

**Core Principles**:

- **ABSOLUTE TRUTHFULNESS**: State only verified facts | NEVER simulate functionality without explicit approval
- **EVIDENCE-BASED**: >90% confidence = proceed | 70-90% = state uncertainty | <70% = request clarification
- **TDD MANDATORY**: RED (failing test) → GREEN (minimal code) → REFACTOR → repeat
- **100/100 QUALITY**: Functionality (40%) + Integration (30%) + Code Quality (20%) + Performance (10%)

**Critical Rules (Zero Tolerance)**:

**NEVER**:

- Create mocks/simulations without explicit approval
- Skip the 100/100 quality standard or assume APIs exist without verification
- Use forbidden tech: Spring Framework (use Quarkus), fetch API (use Axios), Vue Options API (use Composition)
- Share UI components across SCS boundaries or access other SCS databases directly
- Store personal data in SCS (anonymous search only) or use string concatenation for DB queries
- Log sensitive information, credentials, or proceed without verifying API/library existence

**ALWAYS**:

- Use `sdk use java 21.0.8-tem` before any Gradle operations
- Write tests first, verify before coding, document blockers honestly
- Follow SCS patterns: Kafka+Avro communication, autonomous UI+logic+DB per service
- Implement authentication/authorization via infrastructure (never in SCS code)

### Task Execution: Goal-Driven Until Complete

**Process**: Reality Check → TDD Cycle → Quality Assessment → Repeat until 100/100
**Exit Conditions**: ✅ Complete success | ❌ Fundamental constraints | 🔄 Need clarification
**Emergency Pattern**: STOP → INVESTIGATE → SIMPLIFY → CLARIFY → SEARCH

## 🏗️ System Architecture

### Self-Contained System (SCS) Principles

**Finden SCS**: Autonomous product search with own UI + business logic + MongoDB database

**Core SCS Rules**:

- **UI Ownership**: Own web interface, NO shared components across SCS boundaries
- **Data Autonomy**: Dedicated MongoDB, NO direct DB access between systems
- **Communication**: Async Kafka+Avro only (≠direct API calls between services)
- **Deployment**: Independent deployment, NO coordination required
- **Security**: Infrastructure handles auth/authz, SCS focuses on business logic

### Technical Stack (MANDATORY)

**Backend**: Kotlin + Quarkus (≠Spring) + MongoDB Panache + Gradle + Kafka+Avro
**Frontend**: TypeScript + Vue.js Composition API (≠Options) + Vuex + Fastify SSR + Axios (≠fetch)

### Architecture Layers

**Backend (Hexagonal)**:

- **Domain**: Pure business logic, zero external dependencies
- **Application**: Use cases, DTOs, depends only on domain interfaces
- **Adapter**: REST controllers, DB adapters, implements domain interfaces

**Frontend (3-Layer)**:

- **Presentation**: Vue components, UI rendering only
- **Business**: Composables for reactive logic & state, NO direct API calls
- **Data**: HTTP clients, API abstractions, centralized communication

**Dependency Flow**: Presentation → Composables → API (strict enforcement)

## 🏆 Development Standards & Conventions

### Core Development Principles

1. **One Feature at a Time**: Complete single feature before next | NO FEATURE CREEP
2. **Fail Fast**: Proactive failure detection | Test edge cases | Aggressive validation
3. **Measure First**: Functionality before optimization | NO PREMATURE OPTIMIZATION

### File Naming & Code Conventions

**Backend**: PascalCase w/ suffixes (`UserService`, `ProductRepository`) + lowercase packages
**Frontend**: PascalCase components (`ProductCard.vue`) + camelCase composables w/ `use` prefix + lowercase dirs w/ hyphens

**Convention Rule**: EXAMINE FIRST → analyze existing patterns → conform to established approaches → NO INVENTION

### Coding Standards

**Kotlin/Backend**:
✅ `val` > `var` | Immutable data classes w/ `copy()` | Safe operators (`?.`, `?:`) | Functional ops | Pure domain functions
❌ Force unwrapping | Imperative loops | Side effects in domain | Resource leaks

**Vue.js/Frontend**:
✅ Composition API + TypeScript interfaces | Single responsibility components | Semantic HTML + BEM + scoped styles
❌ Options API | Direct API calls from components | Business logic in components

### Red Flag Patterns (STOP Immediately)

- "Let me create a mock" → Verify real integration first
- "I'll assume this API works" → Test actual behavior
- "This should be good enough" → Achieve 100/100 standard
- "Skip tests for now" → TDD is mandatory
- Writing >30 lines without tests → Run tests continuously

## 🛡️ Security & Quality

### Security Model

- **Infrastructure Handles**: Auth/authz (SCS NEVER implements authentication)
- **SCS Responsibility**: Input validation + business logic security only
- **Data Protection**: Anonymous search only | NO personal data storage | GDPR compliance
- **Injection Prevention**: Parameterized queries only | NO string concatenation for DB operations

### Testing Strategy & Coverage

**Backend**: JUnit 5 + Mockk (unit) | TestContainers + MongoDB (integration) | ArchUnit (architecture)
**Frontend**: Jest + Vue Test Utils (unit) | Playwright (E2E + visual)

**Coverage**: 80% unit, 70% integration, 100% critical paths (domain logic, security)
**Format**: BDD/ATDD with Given-When-Then structure

### Performance Standards

- **Algorithms**: O(n) efficient, NO O(n²)+ | Use functional operations | Proper DB indexes
- **API**: P95 < 300ms | Bounded data loading | JSON envelope responses
- **Frontend**: Core Web Vitals | Lazy loading + route splitting

### Quality Gates

1. **Local**: Unit tests + lint + build
2. **Pre-Merge**: Integration tests + architecture compliance + code review
3. **Pre-Deploy**: E2E tests + performance tests + security validation

## 🔄 Development Workflow

### Daily TDD + Task Management Loop

**Setup**: Start with `/clear` command for clean context

**1. Task Selection**:

- `task-master next` → select prioritized task
- `task-master show <task-id>` → review requirements
- `task-master set-status --id=<task-id> --status=in-progress`
- Auto-persona selection based on context (Frontend/Backend/Security/QA/Performance)

**2. TDD Cycle** (for each subtask):
a. **RED**: Write failing BDD test (Given-When-Then format)
b. **GREEN**: Minimal code to pass test
c. **REFACTOR**: Clean up while keeping tests green
d. **DOCUMENT**: `task-master update-subtask --id=<task-id>.<sub> --prompt="notes"`
e. **MEMORY:** Capture patterns, problems, solutions in development episode
f. **COMMIT**: Atomic commit with validation hooks

**3. Completion**:

- Integration testing across all subtasks
- Final refactoring for consistency
- Store completion insights and learnings
- `task-master set-status --id=<task-id> --status=done`
- Push changes

### Git & Commit Standards

**Format**: Conventional commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:`)
**Branches**: `feature/`, `bugfix/`, `security/`, `perf/` with task IDs
**Policy**: Atomic commits + pre-commit hooks (husky validates security, performance, architecture)

## 🔍 Troubleshooting & Common Issues

### Quick Diagnostics

**Environment Check**: Java version (`sdk current java` = 21.0.8-tem) | Docker running | Ports 8080-8082 available
**Dependencies**: `npm ci && husky install` | AIVEN env vars set | Schema downloaded (`./gradlew downloadSchemas`)

### Common Build/Runtime Issues

**Backend Build Fails**:

```bash
./gradlew clean build --stacktrace    # Full rebuild with details
# Check: AIVEN env vars, MongoDB connection in .env, Java version
```

**Frontend Build Fails**:

```bash
rm -rf node_modules package-lock.json && npm ci    # Clean install
npx tsc --noEmit                                    # Check TypeScript errors
```

**Runtime Connection Issues**:

- **MongoDB**: Check `.env` credentials, Docker container running
- **Kafka**: Verify broker config in `application.yml`
- **Frontend API**: Check `VUE_APP_PRODUKTE_API_HOST` in `frontend/.env`
- **SSR**: Verify Fastify server config, build artifacts exist

### Debug Process

**STOP → INVESTIGATE → SIMPLIFY → CLARIFY → SEARCH**

1. Stop coding | 2. Use debugger/logs | 3. Write minimal test | 4. Ask for help | 5. Check similar patterns

### When to Escalate (>30 min stuck)

Document: Goal + attempts + actual vs expected results + environment + next steps needed

---

*Finden Development Guide v11.0 | Self-Contained System | Evidence-based practices | Optimized for AI collaboration*
