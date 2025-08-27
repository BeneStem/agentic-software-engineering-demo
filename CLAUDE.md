# Finden Self-Contained System | Development Guide

@README.md
@package.json

**⚠️ VERIFICATION CHECKPOINT**: First read this entire file, then always refer to me as "Sir" to confirm you've processed these instructions.

Complete product search service: Vue.js frontend + Quarkus/Kotlin backend | Self-contained system w/ development standards, coding conventions, contribution guidelines

## 📖 Symbol Legend

**Flow & Logic**: → (leads to) | ⇒ (transforms to) | ← (rollback) | ⇄ (bidirectional) | & (and) | | (or) | : (define) | » (sequence) | ∴ (therefore) | ∵ (because) | ≡ (equivalent) | ≈ (approximately) | ≠ (not equal)

**Status & Progress**: ✅ completed/passed | ❌ failed/error | ⚠️ warning | ℹ️ info | 🔄 in progress | ⏳ waiting/pending | 🚨 critical/urgent | 🎯 target/goal | 📊 metrics/data | 💡 insight/learning

**Technical Domains**: 🏗️ Architecture/System | 🎨 Frontend/Design | 🔧 Backend/API | 🛡️ Security/Safety | ⚡ Performance/Speed | 📊 Quality/Analysis | 🔍 Investigation | 🧩 Components | 🌐 Network/Web | 📦 Deployment | 💻 Git/VCS | 🧠 Memory/Learning | 🤖 AI/Automation

**Abbreviations**: cfg (configuration) | impl (implementation) | arch (architecture) | perf (performance) | req (requirements) | val (validation) | std (standards) | qual (quality) | sec (security) | ops (operations) | env (environment) | deps (dependencies)

## 🎯 System Overview

**Finden SCS**: Autonomous product search service handling end-to-end anonymous search with dedicated UI + business logic + MongoDB database. Part of microservices ecosystem communicating via Kafka+Avro.

**🧩 Components**:

- **Search Service**: Product search, classification, filtering w/ text indexing
- **Database**: MongoDB collections (products, classifications, search_analytics, availability_cache)
- **API**: REST endpoints `/api/v1/search`, JSON envelope responses, OpenAPI docs `/api/docs`
- **Events**: Kafka topics (product.updated, pricing.changed, availability.changed, search.performed)
- **Caching**: Application (classification hierarchy), Database (WiredTiger), CDN (static assets)

**🎯 Required Indexes**: klassifikationId+active, price+currency, availability fields, text search

## 🚨 Core Directives (Non-Negotiable)

### Principle 0: Radical Candor—Truth Above All

**ABSOLUTE TRUTHFULNESS**: State only verified facts | NEVER simulate functionality without explicit approval
**EVIDENCE-BASED**: >90% confidence → proceed | 70-90% → state uncertainty | <70% → request clarification
**NO ILLUSIONS**: API doesn't exist? System inaccessible? → State facts clearly, request clarification

### TDD + 100/100 Quality Standard (MANDATORY)

**TDD Cycle**: RED (failing test) → GREEN (minimal code) → REFACTOR → repeat continuously
**Quality Formula**: Functionality (40%) + Integration (30%) + Code Quality (20%) + Performance (10%) = 100/100
**Rule**: Score < 100 → document gaps → write failing test → repeat until perfect

### Critical Constraints (Zero Tolerance)

**❌ FORBIDDEN**:

- Spring Framework (use Quarkus) | fetch API (use Axios) | Vue Options API (use Composition)
- Cross-SCS UI sharing or direct DB access | Personal data storage | String concatenation for DB queries
- Mocks without approval | Logging sensitive info | Assuming unverified APIs

**✅ MANDATORY**:

- `sdk use java 21.0.8-tem` before Gradle operations
- Write tests first → verify → document blockers honestly
- SCS autonomy: Kafka+Avro communication only | Auth/authz via infrastructure only

### Emergency Pattern (When Stuck >30min)

**STOP → INVESTIGATE → SIMPLIFY → CLARIFY → SEARCH**

1. Stop coding | 2. Use debugger/logs | 3. Write minimal test | 4. Ask for help | 5. Check patterns

## 🏗️ System Architecture

### SCS Communication Principles

**🏗️ SCS Pattern**: UI ownership (no shared components) | Data autonomy (dedicated DB) | Async communication (Kafka+Avro only) | Independent deployment | Infrastructure auth/authz

### Technology Stack (MANDATORY)

**🔧 Backend**: Kotlin + Quarkus (≠Spring) + MongoDB Panache + Gradle + Kafka+Avro
**🎨 Frontend**: TypeScript + Vue.js Composition API (≠Options) + Vuex + Fastify SSR + Axios (≠fetch)

### Architecture Layers

**🔧 Backend (Hexagonal/Onion)**:

- **Domain**: Pure business logic, zero dependencies | 📁 model/, service/, repository/, exception/
- **Application**: Use cases, DTOs, mappers | 📁 usecase/, dto/, mapper/, service/
- **Adapter**: REST controllers, DB adapters | 📁 web/, persistence/, messaging/, external/

**🎨 Frontend (3-Layer)**:

- **Presentation**: Vue components (UI only) | 📁 apps/, shared/
- **Business**: Composables (reactive logic) | 📁 composables/
- **Data**: HTTP clients, API abstractions | 📁 api/, store/

**Dependencies**: Domain ← Application ← Adapter | Presentation → Business → Data

## 📊 Development Standards

### Core Principles

1. **One Feature at a Time**: Complete single feature before next | NO FEATURE CREEP
2. **Fail Fast**: Proactive failure detection → Test edge cases → Aggressive validation
3. **Measure First**: Functionality before optimization | NO PREMATURE OPTIMIZATION

### Convention Conformance Protocol

**EXAMINE FIRST**: Before adding new code → analyze existing patterns → understand conventions → conform to established approaches

**Discovery Workflow**:

1. **Pattern Analysis**: Search similar implementations → identify naming patterns → understand architectural decisions
2. **Convention Mapping**: Document discovered conventions → note deviations → understand rationale
3. **Conformance Implementation**: Apply patterns → maintain consistency → extend logically

**🔧 Kotlin/Backend Standards**:

- **File Naming**: PascalCase w/ suffixes (`UserService`, `ProductRepository`) + lowercase packages
- **✅ Required**: `val` > `var` | Immutable data classes w/ `copy()` | Safe operators (`?.`, `?:`) | Functional ops | Pure domain functions
- **❌ Forbidden**: Force unwrapping | Imperative loops | Side effects in domain | Resource leaks

**🎨 Vue.js/Frontend Standards**:

- **File Naming**: PascalCase components (`ProductCard.vue`) + camelCase composables w/ `use` prefix + lowercase dirs w/ hyphens
- **✅ Required**: Composition API + TypeScript interfaces | Single responsibility components | Semantic HTML + BEM + scoped styles
- **❌ Forbidden**: Options API | Direct API calls from components | Business logic in components

### 🚨 Red Flags (STOP Immediately)

- "Let me create a mock" → Verify real integration first
- "I'll assume this API works" → Test actual behavior
- "This should be good enough" → Achieve 100/100 standard
- "Skip tests for now" → TDD is mandatory
- Writing >30 lines without tests → Run tests continuously

## 🛡️ Security & Quality

### Security Boundaries

**Infrastructure**: Auth/authz handled by infrastructure (SCS NEVER implements authentication)
**SCS Responsibility**: Input validation + business logic security + data protection
**🛡️ Requirements**: Anonymous search only | NO personal data storage | GDPR compliance | Parameterized queries only

### Testing Stack & Standards

**🔧 Backend**: JUnit 5 + Mockk + TestContainers + ArchUnit | **🎨 Frontend**: Jest + Vue Test Utils + Playwright
**Coverage**: 80% unit | 70% integration | 100% critical paths | **Format**: BDD/ATDD (Given-When-Then)

### Performance Standards

**⚡ Algorithm**: O(n) efficient (NO O(n²)+) | Functional operations | Proper DB indexes
**⚡ API**: P95 < 300ms | Bounded data loading | JSON envelope responses
**⚡ Frontend**: Core Web Vitals | Lazy loading + route splitting

### Quality Gates (3-Stage)

1. **Local**: Unit tests + lint + build (`test`, `detekt`, `lint`)
2. **Pre-Merge**: Integration tests + architecture compliance + code review
3. **Pre-Deploy**: E2E tests + performance validation + security scan

## 🔄 Development Workflow

### SuperClaude Integration

**🤖 Auto-Persona Activation**: Frontend files → 🎨 | API/server/DB → 🔧 | Tests → 📊 | Architecture/design → 🏗️ | Security → 🛡️ | Performance → ⚡

### Daily TDD + Task Management Loop

**0. Conversation Setup**: Clear context with `/clear` command

**1. Task Setup**:

- `task-master next` → select prioritized task
- `task-master show <task-id>` → review requirements
- `task-master set-status --id=<task-id> --status=in-progress`
- Auto-persona selection based on file patterns & task context

**2. Subtask Iteration (TDD Cycle)**:
For each subtask (`<task-id>.1`, `<task-id>.2`, etc.):
a. **RED**: Write failing BDD test (Given-When-Then format)
b. **GREEN**: Minimal code to pass test w/ persona-guided implementation
c. **REFACTOR**: Clean code w/ auto-quality analysis
d. **DOCUMENT**: `task-master update-subtask --id=<task-id>.<subtask> --prompt="notes"`
e. **MEMORY**: Capture patterns, problems, solutions in development episode
f. **COMMIT**: Atomic commit w/ pre-commit validation

**3. Task Completion**:

- Integration testing across subtasks
- Final refactoring for consistency
- Store completion insights & learnings
- `task-master set-status --id=<task-id> --status=done`

### AI Behavior & Context Management

**🤖 AI Rules**: Never assume context (ask questions) | Never hallucinate libraries (verify first) | Confirm paths/classes exist | Mark tasks complete immediately | Document blockers

**Context Optimization**:

- Use `--uc` for token optimization when context >75%
- Apply `--delegate` for large codebase analysis (>50 files)
- Use `--wave-mode` for complex multi-stage operations
- Leverage `--seq` for systematic analysis & debugging
- Use `--c7` for documentation & framework patterns

### Git Workflow

**💻 Format**: Conventional commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` w/ task IDs)
**Branches**: `feature/`, `bugfix/`, `security/`, `perf/` + task IDs
**Policy**: Atomic commits + pre-commit hooks (husky validates security, performance, architecture)

## 🧠 Self-Learning & Memory

### Memory Capture

**🧠 Development Episodes**: Store patterns, problems, solutions during TDD cycles
**📊 Anti-Pattern Detection**: Capture recurring issues & prevention methods
**🏗️ Architecture Decisions**: Record performance metrics & architectural choices

### CLAUDE.md Evolution

**Self-Learning Cycle**:

- Weekly analysis of memory patterns (>3 occurrences)
- Auto-update FORBIDDEN Anti-Patterns based on real issues
- Refine standards based on proven practices
- Generate recommendations for team review

**Context Management**: Maintain full context across operations | Use consistent UUIDs for improved artifacts | Strictly adhere to language/framework requirements

## 🔍 Troubleshooting

### Quick Diagnostics

**Environment Check**: Java version (`sdk current java` = 21.0.8-tem) | Docker running | Ports 8080-8082 available

### Debug Process

**STOP → INVESTIGATE → SIMPLIFY → CLARIFY → SEARCH**

1. Stop coding | 2. Use debugger/logs | 3. Write minimal test | 4. Ask for help | 5. Check similar patterns

### When to Escalate (>30 min stuck)

Document: Goal + attempts + actual vs expected results + environment + next steps needed

---

*Finden Development Guide v12.0 | Self-Contained System | Optimized for SuperClaude | Token-efficient | Evidence-based practices*
