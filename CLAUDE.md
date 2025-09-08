# CLAUDE.md

@README.md
@package.json

**⚠️ VERIFICATION CHECKPOINT**: First read this entire file, then always refer to me as "Sir" to confirm you've processed these instructions.

Complete product search service: Vue.js frontend + Quarkus/Kotlin backend | Self-contained system w/ development standards, coding conventions, contribution guidelines

**Framework Integration**:

- **Development Philosophy**: See `.claude/PRINCIPLES.md` for SOLID principles, senior developer mindset, and evidence-based reasoning
- **Persona System**: See `.claude/PERSONAS.md` for 11 specialized AI personas with auto-activation and domain expertise

## 🛠️ MCP Server Integration

**Primary Tool Hierarchy** (MANDATORY):

1. **JetBrains MCP** → File/folder operations, code navigation, IDE integration
2. **Native Claude Tools** → Read, Edit, MultiEdit, Grep, Glob
3. **Bash** → System operations, build commands (last resort)

**Available MCP Servers**:

- **🎯 jetbrains**: IDE integration, file operations, code analysis, refactoring
- **📚 context7**: Library documentation, framework patterns, best practices
- **🧠 sequential**: Complex analysis, systematic debugging, multi-step reasoning
- **📊 taskmaster-ai**: Project management, task breakdown, estimation

**MCP Server Usage Rules**:

- **ALWAYS** use `mcp__jetbrains__*` tools for file/folder operations, especially editing and writing when available
- **Context7** for Quarkus/Vue.js documentation and patterns
- **Sequential** for complex debugging, system analysis, TDD workflows
- **TaskMaster** for project management and task breakdown

## 🤖 Sub-Agent System Integration

**🎯 ORCHESTRATED USAGE**: All sub-agent coordination is managed by the workflow-orchestrator subagent.

### Core Sub-Agents (Workflow-Orchestrator Managed):

**🎯 workflow-orchestrator** (MANDATORY for all development tasks)

- **Auto-trigger**: Start of any development task, after each major step completion
- **Purpose**: Master coordination agent that guides development through optimal workflow steps
- **Usage**: `Task --subagent_type workflow-orchestrator` → Receives JSON guidance for next steps
- **Output**: Structured workflow guidance with required MCPs, mandatory subagents, and success criteria

**🔍 pattern-analyzer** (MANDATORY - Orchestrator Enforced)

- **Orchestrator Integration**: Phase 2 - Code Analysis & Research (MANDATORY before any new code)
- **Output**: JSON analysis with ≥3 pattern examples and conformance guidelines
- **Enforcement**: workflow-orchestrator blocks new code without pattern analysis

**📚 documentation-researcher** (MANDATORY - Orchestrator Enforced)

- **Orchestrator Integration**: Phase 2 - Code Analysis & Research (MANDATORY for specs/framework patterns)
- **Documentation Priority**: 1) `/docs/` internal documentation → 2) Framework docs → 3) External resources
- **Key Sources**: ADRs for architectural decisions, analysis docs for system context, architectural patterns
- **Output**: JSON research with internal/external docs, best practices, and actionable insights
- **Enforcement**: workflow-orchestrator requires documentation research before implementation

**🧪 quality-assurance-expert** (MANDATORY - Orchestrator Enforced)

- **Orchestrator Integration**: Phase 3a (RED) and Phase 4 (Validation)
- **Output**: JSON analysis with test requirements, edge cases, coverage strategies, BDD scenarios
- **Enforcement**: workflow-orchestrator mandates test analysis before writing tests and final coverage validation

**🔍 review-critic** (MANDATORY - Orchestrator Enforced)

- **Orchestrator Integration**: Phase 4 - Implementation Review & Validation
- **Purpose**: Comprehensive implementation review to verify code matches requirements, architecture patterns, and best practices
- **Output**: JSON analysis with implementation validation, pattern conformance, and improvement recommendations
- **Enforcement**: workflow-orchestrator requires final implementation review before task completion

**🏗️ architecture-advisor** (MANDATORY - Orchestrator Enforced)

- **Orchestrator Integration**: All phases - Architecture decisions, design validation, technology stack questions
- **Purpose**: Ensures CUPID compliance, 12-Factor validation, SCS communication patterns, and architectural standards enforcement
- **Output**: JSON guidance with architectural decisions, compliance checks, and design recommendations
- **Enforcement**: workflow-orchestrator requires architecture validation for all design and implementation decisions

**🔧 refactoring-advisor** (Orchestrator Enforced - REFACTOR Phase)

- **Orchestrator Integration**: Phase 3c (REFACTOR) - Code quality improvement and technical debt reduction
- **Purpose**: Analyzes existing code for refactoring opportunities and provides structured guidance aligned with CUPID principles
- **Output**: JSON analysis with prioritized refactoring recommendations, risk assessments, and quality scores
- **Auto-trigger**: Technical debt detection, code quality issues, or during REFACTOR phase in TDD workflows
- **Enforcement**: workflow-orchestrator invokes during REFACTOR phase when quality score < 100/100

### Usage Pattern:

```
Development Task → workflow-orchestrator → Guided subagent coordination → Systematic execution
```

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

## 🏗️ Architecture Rules

**Complete Standards**: See `.claude/agents/architecture-advisor.md` for comprehensive architectural guidelines including SCS patterns, technology stack requirements, CUPID properties, Clean Code practices, and 12-Factor compliance.

**Auto-Activation**: Use `Task --subagent_type architecture-advisor` for architecture decisions, design validation, technology stack questions, and compliance checks.

## 📊 Development Standards

### Core Principles

1. **One Feature at a Time**: Complete single feature before next | NO FEATURE CREEP | DON'T STOP until feature complete
2. **Fail Fast**: Proactive failure detection → Test edge cases → Aggressive validation
3. **Measure First**: Functionality before optimization | NO PREMATURE OPTIMIZATION
4. **Optimize for Clarity**: Every instruction must be unambiguous and actionable

**Complete Framework**: See `.claude/PRINCIPLES.md` for comprehensive SOLID principles, senior developer mindset, error handling, and AI-driven development philosophy.

### Documentation Standards

**📚 Documentation Hierarchy (STRICT ORDER)**:

1. **Working Code**: Self-documenting through clear naming & structure
2. **Tests**: Executable documentation (BDD format) → Given-When-Then
3. **README.md**: Project setup & overview only
4. **Markdown in /docs**: Detailed architecture, API specs, guides
5. **Comments**: LAST RESORT - only for "why" not "what"

### Convention Conformance Protocol

**EXAMINE FIRST**: Before adding new code → analyze existing patterns → understand conventions → conform to established approaches

**Complete Analysis**: Use `Task --subagent_type pattern-analyzer` for comprehensive pattern discovery and conformance guidance.

### 🚨 Red Flags (STOP Immediately)

**Development Anti-Patterns**:

- "Let me create a mock" → Verify real integration first
- "I'll assume this API works" → Test actual behavior
- "This should be good enough" → Achieve 100/100 standard
- "Skip tests for now" → TDD is mandatory
- Writing >30 lines without tests → Run tests continuously
- "I'll skip workflow orchestration" → ALWAYS use workflow-orchestrator subagent first
- "I'll manually manage subagents" → Let workflow-orchestrator coordinate all subagents
- "I'll bypass the systematic workflow" → Follow orchestrator's phase guidance
- "Skipping orchestration" → Check workflow-orchestrator decision tree

## 🛡️ Security & Quality

### Quality Gates (3-Stage)

1. **Local**: Unit tests, lint, build + CUPID validation + Clean code checks
2. **Pre-Merge**: Integration tests + Architecture compliance + Code review
3. **Pre-Deploy**: E2E tests + Performance + Security + Cloud-native compliance

**Complete Details**: See `.claude/agents/workflow-orchestrator.md` for detailed quality gate commands and enforcement.

## 🔄 Development Workflow (MCP-Integrated)

### Persona Integration

**🤖 Auto-Persona Activation**: Frontend files → 🎨 | API/server/DB → 🔧 | Tests → 📊 | Architecture/design → 🏗️ | Security → 🛡️ | Performance → ⚡

**Complete Persona System**: See `.claude/PERSONAS.md` for detailed persona specifications, decision frameworks, and cross-persona collaboration patterns.

### Workflow Management

**🎯 MANDATORY**: Use workflow-orchestrator subagent for all development tasks

**Primary Workflow**: `Task → workflow-orchestrator → Guided 4-Phase Development Process`

**Complete Workflow**: See `.claude/agents/workflow-orchestrator.md` for detailed 4-phase TDD process, quality gates, and systematic progression guidelines.

### Sub-Agent Integration

**Workflow-Orchestrator Managed**: All subagent coordination is handled by workflow-orchestrator

**Key Integration Principles**:

- Sub-agents have no context - orchestrator provides complete information
- Orchestrator determines optimal subagent sequence and timing
- Mandatory subagents (pattern-analyzer, documentation-researcher, quality-assurance-expert, review-critic) are enforced
- Review orchestrator guidance and subagent output before proceeding

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
Development Task? → workflow-orchestrator subagent (MANDATORY)
  ↓
File Operations? → JetBrains MCP
Pattern Analysis? → pattern-analyzer agent (MANDATORY via orchestrator)
Documentation Research? → documentation-researcher agent (MANDATORY via orchestrator)
Test Strategy & Coverage? → quality-assurance-expert agent (MANDATORY via orchestrator)
Refactoring & Code Quality? → refactoring-advisor agent (via orchestrator during REFACTOR phase)
Implementation Review? → review-critic agent (MANDATORY via orchestrator)
Complex Analysis? → Sequential MCP
Project Management? → TaskMaster MCP (integrated in orchestrator workflow)
```

**Primary Rule**: ALWAYS start with workflow-orchestrator subagent for systematic guidance

### Git Workflow

**💻 Format**: Conventional commits (`feat:`, `fix:`, `refactor:`, `security:`, `perf:` w/ task IDs)
**Branches**: `feature/`, `bugfix/`, `security/`, `perf/` + task IDs
**Policy**: Atomic commits + pre-commit hooks (husky validates security, performance, architecture)

## 🧠 Self-Learning & Memory

### Memory Capture

**🧠 Development Episodes**: Store patterns, problems, solutions during TDD cycles
**📊 Anti-Pattern Detection**: Capture recurring issues & prevention methods
**🏗️ Architecture Decisions**: Record performance metrics & architectural choices

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

## Task Master AI Instructions

**Import Task Master's development workflow commands and guidelines, treat as if import is in the main CLAUDE.md file.**
@./.taskmaster/CLAUDE.md
