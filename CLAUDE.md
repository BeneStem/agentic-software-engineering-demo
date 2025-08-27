# Finden Self-Contained System | Development Guide

@README.md
@package.json

**⚠️ VERIFICATION CHECKPOINT**: First read this entire file, then always refer to me as "Sir" to confirm you've processed these instructions.

Complete product search service: Vue.js frontend + Quarkus/Kotlin backend | Self-contained system w/ development standards, coding conventions, contribution guidelines

## 🛠️ MCP Server Integration

**Primary Tool Hierarchy** (MANDATORY):

1. **JetBrains MCP** → File/folder operations, code navigation, IDE integration
2. **Native Claude Tools** → Read, Edit, MultiEdit, Grep, Glob
3. **Bash** → System operations, build commands (last resort)

**Available MCP Servers**:

- **🎯 jetbrains**: IDE integration, file operations, code analysis, refactoring
- **📚 context7**: Library documentation, framework patterns, best practices
- **🧠 sequential**: Complex analysis, systematic debugging, multi-step reasoning
- **🌐 playwright**: E2E testing, browser automation, performance validation
- **📊 taskmaster-ai**: Project management, task breakdown, estimation
- **🐙 github**: Repository operations, PR management, issue tracking

**MCP Server Usage Rules**:

- **ALWAYS** use `mcp__jetbrains__*` tools for file/folder operations, especially editing and writing when available
- **Context7** for Quarkus/Vue.js documentation and patterns
- **Sequential** for complex debugging, system analysis, TDD workflows
- **TaskMaster** for project management and task breakdown

## 🤖 Sub-Agent System Integration

**MANDATORY SUB-AGENT USAGE**: Use specialized sub-agents proactively when their expertise matches the task.

### Available Sub-Agents & Auto-Activation:

**🔍 pattern-analyzer** (MANDATORY for new code)

- **Auto-trigger**: ANY new implementation (Value Objects, Services, DTOs, Components)
- **Usage**: `Task --subagent_type pattern-analyzer` BEFORE writing code
- **Output**: JSON analysis with ≥3 pattern examples and conformance guidelines

## 🔧 CLI Tools & System Commands

**Available System Tools** (verified on this environment):

### Version Management

- **`sdk`** - SDKMan for Java version management | **MANDATORY**: `sdk use java 21.0.8-tem` before Gradle ops
- **`sdk current java`** - Check active Java version | **`sdk list java`** - Show available versions

### Build & Package Management

- **`npm`** - Node.js package manager | **`npm ci`** - Install exact deps | **`npm run [script]`** - Run package.json scripts
- **`./gradlew`** - Gradle wrapper (project-local) | **`./gradlew quarkusDev`** - Start backend dev mode
- **`brew`** - macOS package manager | **`brew ls`** - List installed packages

### Database & Services

- **`mongosh`** - MongoDB Shell | Connect to local/remote MongoDB instances
- **`docker`** - Container management | **`docker-compose`** - Multi-container orchestration
- **`docker-compose -f docker/docker-compose.yaml up -d`** - Start local MongoDB/Kafka

### Development Tools

- **`git`** - Version control | Conventional commits required (`feat:`, `fix:`, `refactor:`)
- **`gh`** - GitHub CLI | **`gh pr create`** - Create PRs | **`gh issue list`** - Manage issues
- **`tree`** - Directory structure visualization | **`tree -I node_modules`** - Exclude dirs
- **`ripgrep (rg)`** - Fast file content search | **`rg "pattern" --type js`** - Search JS files
- **`fd`** - Fast file name search | **`fd "*.vue" src/`** - Find Vue files

### Text Processing & HTTP

- **`jq`** - JSON processing | **`curl | jq '.'`** - Pretty-print JSON responses
- **`curl`** - HTTP client | **`curl -X POST -H "Content-Type: application/json"`**
- **`sed`**, **`awk`**, **`grep`** - Text manipulation (prefer `rg` over `grep`)

### Container & Orchestration

- **`kubectl`** - Kubernetes cluster management | **`kubectl get pods`** - List pods
- **`helm`** - Kubernetes package management | **`helm upgrade --install`**

### Project-Specific Commands

```bash
# MANDATORY Java setup
sdk use java 21.0.8-tem

# Backend setup & run
./gradlew downloadSchemas    # Fetch Avro schemas (requires AIVEN_* env vars)
./gradlew quarkusDev        # Start backend on :8081

# Frontend setup & run
npm ci                      # Install exact dependencies
npm run dev                 # Start frontend on :8082

# Local services
docker-compose -f docker/docker-compose.yaml up -d    # Start MongoDB/Kafka
docker-compose -f docker/docker-compose.yaml down     # Stop & remove containers

# Quality gates
./gradlew test detekt build         # Backend: unit tests, lint, build
npm run test                        # Frontend: lint + unit tests
npm run unitTest -- -u             # Update test snapshots
```

**🚨 Tool Hierarchy**: JetBrains MCP > Native Claude Tools > CLI commands above > Raw bash (last resort)

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
**🎯 NEVER STOP UNTIL**: All tests pass | 100/100 achieved | User intent perfectly matched

### Goal Persistence Protocol

**DON'T STOP UNTIL ACHIEVED**:

- Test coverage reaches required thresholds (80% unit, 70% integration, 100% critical)
- All quality gates pass (local, pre-merge, pre-deploy)
- Performance metrics met (P95 < 300ms, Core Web Vitals green)
- Security validations complete (no vulnerabilities, GDPR compliant)

**COMPLETION CRITERIA**: Task incomplete until → Tests pass → Integration verified → Quality validated → Performance confirmed → Security checked → 100/100 achieved

### Hallucination Prevention Protocol

**PERMISSION TO ADMIT UNCERTAINTY**:

- Say "I don't know" when confidence <70%
- State "I need to verify" before assuming
- Request clarification for ambiguous requirements

**VERIFICATION TECHNIQUES**:

- **Quote First**: Extract exact quotes from docs before answering
- **Think Before Answer**: Show chain-of-thought reasoning
- **Prepare to Discuss**: Load full context before complex tasks
- **Cite Sources**: Reference file:line for every claim
- **Test Everything**: Verify through actual execution, not assumptions

**RESTRICTED KNOWLEDGE**: Only use provided files/docs | NO external assumptions | Verify library existence before use

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

## 🏛️ Software Engineering Principles

### CUPID Properties (Joyful Code)

**🧩 Composable**: Small surface area | Minimal dependencies | Intention-revealing | Easy to combine
**🔧 Unix Philosophy**: Do one thing well | Specific, well-defined purpose | Outside-in perspective
**📊 Predictable**: Does what it looks like | Consistent behavior | No surprises | Easy to confirm
**🎯 Idiomatic**: Feels familiar | Language/framework conventions | Team patterns
**🌐 Domain-based**: Minimize cognitive distance | Domain language in code | Business-aligned

### Clean Code Practices

**Self-Documenting Code**: Clear naming > comments | Structure reveals intent | Function size <20 lines
**Boy Scout Rule**: Leave code cleaner than found | Continuous improvement | Refactor ruthlessly
**Comment Philosophy**: Code failure → comment | Explain "why" not "what" | Remove commented code

### 12-Factor App (Cloud-Native)

**☁️ Codebase**: One repo, many deploys | Git as single source
**📦 Dependencies**: Explicit declaration | package.json, build.gradle | No implicit deps
**⚙️ Config**: Environment variables only | NO hardcoded values | Per-environment cfg
**🔗 Backing Services**: MongoDB as attached resource | Kafka as message broker
**🔄 Build/Release/Run**: Strict separation | CI/CD pipeline | Immutable releases
**🎯 Processes**: Stateless execution | NO sticky sessions | Share-nothing
**🌐 Port Binding**: Self-contained services | Export via port binding
**📈 Concurrency**: Horizontal scaling | Process model | Multiple instances
**⚡ Disposability**: Fast startup <10s | Graceful shutdown | Robust against failure
**🔄 Dev/Prod Parity**: Minimize gaps | Same backing services | Same deployment process
**📋 Logs**: Event streams to stdout | Structured logging | NO log files
**🔧 Admin**: One-off processes | Separate admin tasks | Same codebase

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

### CUPID Architecture Integration

**🧩 Composable Design**:

- Small surface area: <5 public methods per class | Minimal external dependencies
- Intention-revealing: Clear naming for classes/methods | Self-documenting interfaces
- Easy combination: Components work together without complex configuration

**🔧 Unix Philosophy Application**:

- Each service does ONE thing well | Clear, specific purpose per module
- Domain services: Single business capability | Controllers: Single endpoint responsibility

**📊 Predictable Behavior**:

- Consistent error handling across layers | Standard response formats
- No hidden side effects | Pure functions in domain layer

**🎯 Idiomatic Implementation**:

- Follow Kotlin/Vue.js conventions | Use established patterns from community
- Consistent naming across codebase | Team-agreed patterns documented

**🌐 Domain-based Organization**:

- Package by feature, not layer | Business concepts in code structure
- Domain language in class/method names | Business rules explicit in code

## 📊 Development Standards

### Core Principles

1. **One Feature at a Time**: Complete single feature before next | NO FEATURE CREEP | DON'T STOP until feature complete
2. **Fail Fast**: Proactive failure detection → Test edge cases → Aggressive validation
3. **Measure First**: Functionality before optimization | NO PREMATURE OPTIMIZATION
4. **Optimize for Clarity**: Every instruction must be unambiguous and actionable

### Documentation Standards

**📚 Documentation Hierarchy (STRICT ORDER)**:

1. **Working Code**: Self-documenting through clear naming & structure
2. **Tests**: Executable documentation (BDD format) → Given-When-Then
3. **README.md**: Project setup & overview only
4. **Markdown in /doc**: Detailed architecture, API specs, guides
5. **Comments**: LAST RESORT - only for "why" not "what"

**📝 Rules**:

- NO comments for obvious code → code should explain itself
- NO commented-out code → delete it (use Git history)
- NO redundant documentation → tests ARE documentation
- Code structure IS documentation → use clear naming & organization
- Comments only when code cannot self-explain after all other options exhausted

**🎯 Comment Standards**:

- Explain intent, not implementation
- Warning of consequences only
- Legal/regulatory requirements only
- Amplification of non-obvious business rules only

### Convention Conformance Protocol

**EXAMINE FIRST**: Before adding new code → analyze existing patterns → understand conventions → conform to established approaches

**🎯 PREPARE TO DISCUSS**: Load all relevant context → Study existing patterns → Understand architectural decisions → Then implement

**Discovery Workflow**:

1. **Pattern Analysis**: Search similar implementations → identify naming patterns → understand architectural decisions
2. **Convention Mapping**: Document discovered conventions → note deviations → understand rationale
3. **Conformance Implementation**: Apply patterns → maintain consistency → extend logically

**PATTERN RECOGNITION REQUIREMENTS**:

- Search for ≥3 similar implementations before writing new code
- Extract common patterns and naming conventions
- Document why existing patterns were chosen
- NEVER deviate without explicit justification

**🔧 Kotlin/Backend Standards**:

- **File Naming**: PascalCase w/ suffixes (`UserService`, `ProductRepository`) + lowercase packages
- **✅ Required**: `val` > `var` | Immutable data classes w/ `copy()` | Safe operators (`?.`, `?:`) | Functional ops | Pure domain functions
- **❌ Forbidden**: Force unwrapping | Imperative loops | Side effects in domain | Resource leaks

**🎨 Vue.js/Frontend Standards**:

- **File Naming**: PascalCase components (`ProductCard.vue`) + camelCase composables w/ `use` prefix + lowercase dirs w/ hyphens
- **✅ Required**: Composition API + TypeScript interfaces | Single responsibility components | Semantic HTML + BEM + scoped styles
- **❌ Forbidden**: Options API | Direct API calls from components | Business logic in components

### 🚨 Red Flags (STOP Immediately)

**Development Anti-Patterns**:

- "Let me create a mock" → Verify real integration first
- "I'll assume this API works" → Test actual behavior
- "This should be good enough" → Achieve 100/100 standard
- "Skip tests for now" → TDD is mandatory
- Writing >30 lines without tests → Run tests continuously
- "I'll manually search for patterns" → USE pattern-analyzer agent
- "Skipping agent delegation" → Check decision tree

**CUPID Violations**:

- Large interfaces (>5 methods) → Break into smaller, composable pieces
- Unclear naming → Make intention-revealing
- Unpredictable behavior → Ensure consistent, obvious outcomes
- Framework-specific code in domain → Keep domain pure
- Technical language in business code → Use domain language

**Clean Code Violations**:

- Adding explanatory comments → Make code self-explaining first
- Commented-out code → Delete it (use Git)
- Long functions (>20 lines) → Break down into smaller functions
- Nested conditions (>3 levels) → Extract methods or early returns

**12-Factor Violations**:

- Hardcoded configuration → Use environment variables
- Stateful services → Make stateless
- Direct file system usage → Treat as attached resource
- Manual deployment steps → Automate everything

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

### 12-Factor App Compliance

**☁️ Cloud-Native Validation Checklist**:

- Codebase: ✅ Single Git repo | ✅ Multiple environment deploys
- Dependencies: ✅ package.json + build.gradle explicit | ❌ NO global dependencies
- Config: ✅ Environment variables | ❌ NO hardcoded config
- Backing Services: ✅ MongoDB/Kafka as attached resources
- Build/Release/Run: ✅ CI/CD pipeline separation | ✅ Immutable releases
- Processes: ✅ Stateless services | ❌ NO session storage
- Port Binding: ✅ Self-contained services | ✅ Export via port binding
- Concurrency: ✅ Horizontal scaling ready | ✅ Process model
- Disposability: ✅ Fast startup/shutdown | ✅ Graceful termination
- Dev/Prod Parity: ✅ Same backing services | ✅ Same deployment
- Logs: ✅ stdout streams | ✅ Structured logging
- Admin: ✅ One-off processes | ✅ Same codebase

### Quality Gates (3-Stage)

1. **Local**: Unit tests, lint, build + CUPID validation + Clean code checks + Documentation hierarchy compliance

- Backend: `unitTest`, `integrationTest`, `detekt`, `build`
- Frontend: `install`, `lint`, `unitTest`, `build`
- Principles: CUPID property check, comment ratio <5%, function size <20 lines

2. **Pre-Merge**: Integration tests + Architecture compliance + 12-Factor validation + Code review

- TestContainers integration tests, arch compliance validation
- CUPID composability check, 12-factor compliance scan
- Code review approval, SCS communication pattern compliance

3. **Pre-Deploy**: E2E tests + Performance + Security + Cloud-native compliance

- E2E tests w/ Playwright, performance tests under load, security validation
- 12-factor deployment validation, stateless service verification
- Docker image build & push, K8s deployment validation

## 🔄 Development Workflow (MCP-Integrated)

### SuperClaude Integration

**🤖 Auto-Persona Activation**: Frontend files → 🎨 | API/server/DB → 🔧 | Tests → 📊 | Architecture/design → 🏗️ | Security → 🛡️ | Performance → ⚡

### Daily TDD + Task Management + MCP Server Loop

**0. Conversation Setup**: Clear context with `/clear` command

**1. Task Setup** (TaskMaster MCP):

- `task-master next` → select prioritized task
- `task-master show <task-id>` → review requirements
- `task-master set-status --id=<task-id> --status=in-progress`
- Auto-persona selection based on file patterns & task context

**2. Code Analysis** (JetBrains MCP):

- `mcp__jetbrains__list_directory_tree` → Explore project structure
- `mcp__jetbrains__search_in_files_by_text` → Find similar implementations
- `mcp__jetbrains__get_file_problems` → Identify existing issues

**3. Subtask Iteration (TDD Cycle)**:
For each subtask (`<task-id>.1`, `<task-id>.2`, etc.):
a. **RED**: Write failing BDD test (Given-When-Then format) → DON'T STOP until test fails correctly
b. **GREEN**: Minimal code to pass test w/ persona-guided implementation → DON'T STOP until test passes
c. **REFACTOR**: Clean code w/ auto-quality analysis → DON'T STOP until quality score = 100
d. **DOCUMENT**: `task-master update-subtask --id=<task-id>.<subtask> --prompt="notes"`
e. **MEMORY**: Capture patterns, problems, solutions in development episode
f. **COMMIT**: Atomic commit w/ pre-commit validation → DON'T STOP until all hooks pass

**4. Validation & Task Completion** (Multiple MCP):

- Sequential: Complex debugging if needed
- JetBrains: Code quality analysis
- Integration testing across subtasks
- Final refactoring for consistency
- Store completion insights & learnings
- `task-master set-status --id=<task-id> --status=done`

### Sub-Agent Best Practices

**Performance Benefits**:

- pattern-analyzer: Ensures consistency, prevents deviations

**Integration Rules**:

- Sub-agents have no context - provide complete information
- Use specific, detailed prompts
- Review agent output before proceeding
- Chain agents for complex workflows

### AI Behavior & Context Management

**🤖 AI Rules**: Never assume context (ask questions) | Never hallucinate libraries (verify first) | Confirm paths/classes exist | Mark tasks complete immediately | Document blockers

**GUIDING PRINCIPLES**:

- **Brutal Honesty First**: State uncertainties, blockers, and failures immediately
- **Test Driven Development**: Never write code without failing test first
- **One Feature at a Time**: Complete current task before starting next
- **Break Things Internally**: Fail fast in development, never in production
- **Optimize Only After It Works**: Functionality first, optimization second
- **Never Mark Complete Until Perfect**: 100/100 or document why not
- **Maintain Full Context**: Preserve all relevant information across operations

**Context Optimization**:

- Use `--uc` for token optimization when context >75%
- Apply `--delegate` for large codebase analysis (>50 files)
- Use `--wave-mode` for complex multi-stage operations
- Leverage `--seq` for systematic analysis & debugging
- Use `--c7` for documentation & framework patterns

### MCP Server Decision Tree

```
File Operations? → JetBrains MCP
Documentation/Patterns? → Context7 MCP
Complex Analysis? → Sequential MCP
Project Management? → TaskMaster MCP
```

### Git Workflow

**💻 Format**: Conventional commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` w/ task IDs)
**Branches**: `feature/`, `bugfix/`, `security/`, `perf/` + task IDs
**Policy**: Atomic commits + pre-commit hooks (husky validates security, performance, architecture)

## 🧠 Self-Learning & Memory

### Memory Capture

**🧠 Development Episodes**: Store patterns, problems, solutions during TDD cycles
**📊 Anti-Pattern Detection**: Capture recurring issues & prevention methods
**🏗️ Architecture Decisions**: Record performance metrics & architectural choices

**🏛️ Principle Violation Tracking**:

- **CUPID Violations**: Non-composable interfaces, unclear naming, unpredictable behavior
- **Clean Code Issues**: Excessive comments, long functions, unclear intent
- **12-Factor Deviations**: Configuration hardcoding, stateful services, manual processes
- **Documentation Failures**: Comments before refactoring, missing tests, poor structure

**🎯 Pattern Recognition Enhancement**:

- Track successful CUPID implementations for reuse
- Monitor clean code practices adoption rates
- Record 12-factor compliance improvements over time
- Analyze documentation hierarchy effectiveness

### CLAUDE.md Evolution

**Self-Learning Cycle**:

- Weekly analysis of memory patterns (>3 occurrences)
- Auto-update FORBIDDEN Anti-Patterns based on real issues
- Refine standards based on proven practices
- Generate recommendations for team review

**Principle Evolution Tracking**:

- CUPID property adherence metrics and improvement suggestions
- Clean code practice effectiveness measurement
- 12-factor compliance gaps and resolution patterns
- Documentation hierarchy violations and corrections

**Context Management**: Maintain full context across operations | Use consistent UUIDs for improved artifacts | Strictly adhere to language/framework requirements

## 🔍 Troubleshooting

### Quick Diagnostics

**Environment Check**: Java version (`sdk current java` = 21.0.8-tem) | Docker running | Ports 8080-8082 available

### Debug Process

**STOP → INVESTIGATE → SIMPLIFY → CLARIFY → SEARCH**

1. Stop coding
2. Use debugger/logs
3. `mcp__jetbrains__get_file_problems` → Analyze issues
4. `mcp__jetbrains__search_in_files_by_text` → Find patterns
5. Write minimal test
6. Ask for help

### When to Escalate (>30 min stuck)

Document: Goal + attempts + actual vs expected results + environment + next steps needed

---

*Finden Development Guide v12.0 | Self-Contained System | Optimized for SuperClaude | Token-efficient | Evidence-based practices*
