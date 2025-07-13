# Development Guide

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Backend (Product Search Service) built with Quarkus/Kotlin.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Architecture and Domain Design](#architecture-and-domain-design)
- [Security Requirements](#security-requirements)
- [Performance Standards](#performance-standards)
- [Kotlin Development Guidelines](#kotlin-development-guidelines)
- [Testing Strategy](#testing-strategy)
- [Code Quality Standards](#code-quality-standards)
- [Development Process](#development-process)
- [Context Engineering & Task Management](#context-engineering--task-management)
- [Task Master Integration](#task-master-integration)
- [AI Development Workflow](#ai-development-workflow)

## Technology Stack

### Core Technologies

- **Language:** Kotlin 2.2.0 (kotlin-stdlib-jdk8:2.2.0 explicitly pinned, targeting JVM 22)
- **Framework:** Quarkus 3.24.3 with BOM platform enforcement
- **Build Tool:** Gradle 8.x with Kotlin DSL
- **Database:** MongoDB with Panache Kotlin
- **Dependency Injection:** Quarkus CDI (not Spring)
- **Messaging:** Kafka with Avro serialization (Confluent 8.0.0)

### Key Dependencies

- **Domain Modeling:** jMolecules DDD 1.10.0
- **Database:** MongoDB Panache Kotlin
- **Testing:** JUnit 5, Mockk 1.14.4, Strikt 0.35.1, TestContainers 1.21.3
- **Validation:** Hibernate Validator
- **Monitoring:** Micrometer with Stackdriver 3.3.1, Prometheus
- **Logging:** Kotlin Logging 3.0.5, JSON Logging 3.1.0
- **Serialization:** Jackson with Kotlin module
- **Security:** OWASP HTML Sanitizer 20240325.1, Dependency Check 12.1.3, Elytron Security
- **Architecture Testing:** ArchUnit 1.4.1
- **Build Plugins:** Quarkus Plugin 3.24.3, Detekt 1.23.8, Avro Plugin 1.9.1
- **Feature Flags:** Unleash Client 4.4.1
- **Reactive:** Smallrye Fault Tolerance, Reactive Messaging

### Version Requirements

- **Java Runtime:** JVM 22+ (currently targeting 22)
- **Kotlin:** 2.2.0+ (prefer latest patch releases)
- **Gradle:** 8.x+ with Kotlin DSL

## Architecture and Domain Design

### Onion Architecture Implementation

The application follows **Domain-Driven Design (DDD)** with **Onion/Hexagonal Architecture**:

```
de.blume2000.finden/
├── domain/                   # Core domain (innermost layer)
│   ├── model/               # Domain entities, value objects, aggregates
│   ├── service/             # Domain services  
│   ├── repository/          # Repository interfaces (ports)
│   └── exception/           # Domain exceptions
├── application/             # Application layer (use cases)
│   └── service/             # Application services
└── adapter/                 # Adapters (outermost layer)
    ├── active/              # Inbound adapters (driving)
    │   ├── api/             # REST controllers
    │   └── messaging/       # Message consumers
    └── passive/             # Outbound adapters (driven)
        ├── database/        # Repository implementations
        └── messaging/       # External service clients
```

### Domain-Driven Design Principles (MANDATORY)

#### Rich Domain Model
- **Business logic MUST live in domain entities**, not services
- **Value objects MUST be immutable** with proper validation
- **Aggregates MUST enforce business invariants**
- **Domain services for complex operations** spanning multiple entities

#### Layer Dependencies (ENFORCED BY ARCHUNIT)
- **Domain layer:** Zero outward dependencies
- **Application layer:** Depends only on domain interfaces
- **Adapter layer:** Implements domain interfaces

#### Anti-Corruption Layer (MANDATORY)
- **Domain MUST NOT reference DTOs** from adapter layer
- **External data structures MUST be mapped** at adapter boundaries
- **Domain model protected from external changes**

## Security Requirements

### Authentication & Authorization (CRITICAL - PRODUCTION BLOCKER)

#### Current Status: NOT IMPLEMENTED ⚠️
The application currently has **ZERO authentication/authorization**. This MUST be implemented before any production deployment.

#### Mandatory Security Implementation

```kotlin
// Required Quarkus 3.24.3 Security Dependencies
implementation("io.quarkus:quarkus-security-jpa")
implementation("io.quarkus:quarkus-oidc") // For OAuth2/JWT
implementation("io.quarkus:quarkus-smallrye-jwt") // JWT processing
implementation("io.quarkus:quarkus-security") // Core security

// All endpoints MUST be secured
@RolesAllowed("USER", "ADMIN")
@Path("/api/finden/produkte")
class ProdukteResource {
    
    @GET
    @RolesAllowed("USER") 
    fun getProducts(@Context securityContext: SecurityContext): Response {
        val principal = securityContext.userPrincipal
        // Implementation with user context
    }
    
    @POST
    @RolesAllowed("ADMIN")
    fun createProduct(product: ProduktDTO): Response {
        // Admin-only functionality
    }
}
```

#### Complete Security Configuration

```yaml
# application.yml - Quarkus 3.24.3 Security Configuration
quarkus:
  oidc:
    auth-server-url: ${AUTH_SERVER_URL:http://localhost:8080/auth/realms/finden}
    client-id: finden-backend
    client-secret: ${CLIENT_SECRET}
    token:
      issuer: ${TOKEN_ISSUER:http://localhost:8080/auth/realms/finden}
      audience: finden-api
    
  smallrye-jwt:
    enabled: true
    
  security:
    jpa:
      enabled: true
      roles-mapping:
        USER: users
        ADMIN: administrators
    
  datasource:
    # For JPA security if needed alongside MongoDB
    security:
      username: ${DB_SECURITY_USER}
      password: ${DB_SECURITY_PASSWORD}
```

#### Security Standards (MANDATORY)

1. **Authentication Required:**
   - JWT-based authentication for all API endpoints
   - No public endpoints without explicit security review
   - Proper session management

2. **CORS Configuration:**
   ```yaml
   quarkus:
     http:
       cors:
         ~: true  # Enable CORS
         origins:
           - https://finden.blume2000.de
           - https://admin.blume2000.de
         methods: 
           - GET
           - POST
           - PUT
           - DELETE
           - OPTIONS
         headers:
           - Content-Type
           - Authorization
           - X-Requested-With
         exposed-headers:
           - Location
           - X-Total-Count
         access-control-max-age: 86400
         access-control-allow-credentials: true
   ```

3. **Input Validation:**
   - ALL inputs MUST be validated using Bean Validation
   - HTML sanitization for user content (already implemented)
   - SQL/NoSQL injection prevention

4. **OWASP Top 10 Compliance:**
   - Regular dependency vulnerability scans (OWASP dependency check configured)
   - Secure headers implementation
   - Error handling without information disclosure

#### Security Review Checklist (MANDATORY)

Before ANY production deployment:
- [ ] Authentication system implemented
- [ ] All endpoints secured with role-based access
- [ ] CORS properly configured  
- [ ] Input validation comprehensive
- [ ] Error messages don't leak sensitive data
- [ ] Dependencies scanned for vulnerabilities
- [ ] Security headers configured

#### Security Testing with TestContainers

```kotlin
@QuarkusTest
@QuarkusTestResource(KeycloakTestResourceLifecycleManager::class)
internal class SecurityIntegrationTest {
    
    @Test
    fun `should reject unauthenticated requests`() {
        given()
            .`when`().get("/api/finden/produkte")
            .then()
            .statusCode(401)
    }
    
    @Test
    fun `should accept authenticated user requests`() {
        val token = generateTestToken("USER")
        given()
            .auth().oauth2(token)
            .`when`().get("/api/finden/produkte")
            .then()
            .statusCode(200)
    }
    
    @Test
    fun `should reject unauthorized admin operations`() {
        val userToken = generateTestToken("USER")
        given()
            .auth().oauth2(userToken)
            .contentType(ContentType.JSON)
            .body(validProductDto())
            .`when`().post("/api/finden/produkte")
            .then()
            .statusCode(403) // Forbidden
    }
}

// Custom TestContainer for Keycloak
class KeycloakTestResourceLifecycleManager : QuarkusTestResourceLifecycleManager {
    private val keycloak = KeycloakContainer("quay.io/keycloak/keycloak:23.0")
        .withRealmImportFile("/keycloak-realm.json")
        
    override fun start(): Map<String, String> {
        keycloak.start()
        return mapOf(
            "quarkus.oidc.auth-server-url" to keycloak.authServerUrl + "/realms/finden",
            "quarkus.oidc.client-id" to "finden-backend"
        )
    }
}
```

## Performance Standards

### Critical Performance Requirements (MANDATORY)

#### Algorithm Complexity Standards
- **NO O(n²) or higher complexity operations** in production code
- **Database operations MUST use proper indexes**
- **Collection operations MUST be optimized** for expected data sizes
- **Memory usage MUST be bounded** - no unlimited data loading

#### Database Performance (MANDATORY)

1. **Query Optimization:**
   ```javascript
   // Required MongoDB indexes
   db.products.createIndex({ 
       "klassifikationId": 1, 
       "verfuegbarkeiten.bestellschlussUTC": 1,
       "verfuegbarkeiten.liefertag": 1 
   })
   ```

2. **Pagination Requirements:**
   - ALL queries MUST support proper pagination
   - NO full collection loading for sorting
   - Database-level sorting preferred over in-memory

3. **Connection Management:**
   - Connection pooling properly configured
   - Query timeout limits set
   - Connection leak prevention

#### Memory Management (MANDATORY)

1. **Object Lifecycle:**
   - Prefer streaming over full collection materialization
   - Immutable objects for value objects
   - Proper resource cleanup

2. **Collection Processing:**
   - Avoid multiple intermediate collections
   - Use sequence operations for large datasets
   - Monitor heap usage in large operations

#### Performance Anti-Patterns (FORBIDDEN)

```kotlin
// ❌ FORBIDDEN: Disabling pagination for in-memory sorting
if (inMemorySortierungNötig) {
    cmsProdukteFilter.disableLimit() // CAUSES MEMORY EXHAUSTION
}

// ❌ FORBIDDEN: O(n) operations in comparators
val index = sortingBase.indexOf(element) // USE HASHMAP INSTEAD

// ❌ FORBIDDEN: Loading entire collections without limits
return collection.findAll().toList() // USE PAGINATION
```

#### Performance Monitoring (MANDATORY)

- Response time P95 < 500ms for all endpoints
- Memory usage < 80% heap under normal load
- Database query time < 200ms average
- No memory leaks in long-running operations

#### JVM 22 Performance Tuning (RECOMMENDED)

```yaml
# application.yml - Quarkus JVM optimization for Java 22
quarkus:
  native:
    enabled: false  # Use JVM mode for development
  
  jib:
    jvm-arguments:
      - "-XX:+UseZGC"                    # ZGC for low-latency GC (Java 22)
      - "-XX:+UnlockExperimentalVMOptions"
      - "-XX:MaxGCPauseMillis=10"        # Target 10ms GC pauses
      - "-Xms512m"                       # Initial heap size
      - "-Xmx2g"                         # Maximum heap size
      - "-XX:MetaspaceSize=128m"         # Metaspace for class loading
      - "-XX:MaxMetaspaceSize=256m"
      - "-XX:+UseStringDeduplication"    # String memory optimization
      - "-XX:+UseCompressedOops"         # Compressed object pointers
      - "-XX:+AlwaysPreTouch"            # Pre-touch heap pages
      - "-Djava.net.useSystemProxies=true"
      - "-Dquarkus.http.io-threads=8"    # Optimize for CPU cores
```

```bash
# Development JVM arguments for optimal performance
export JAVA_TOOL_OPTIONS="-XX:+UseZGC -XX:MaxGCPauseMillis=10 -Xms512m -Xmx2g -XX:+UseStringDeduplication"

# Production deployment with Docker
docker run -e JAVA_OPTS="-XX:+UseZGC -XX:MaxGCPauseMillis=10 -Xms1g -Xmx4g" finden-backend:latest
```

#### Quarkus 3.24.3 Specific Optimizations

```yaml
# application.yml - Framework optimizations
quarkus:
  thread-pool:
    core-threads: 8
    max-threads: 64
    queue-size: 1000
    
  http:
    io-threads: 8              # Match CPU cores
    worker-threads: 64         # For blocking operations
    
  mongodb:
    connection-string: ${MONGODB_URI}
    max-pool-size: 50          # Connection pool optimization
    min-pool-size: 5
    max-connection-idle-time: 300s
    max-connection-life-time: 1800s
    
  kafka:
    consumer:
      fetch-min-bytes: 1024    # Batch processing optimization
      max-poll-records: 500
    producer:
      batch-size: 16384
      linger-ms: 5
      
  micrometer:
    binder:
      jvm: true                # Enable JVM metrics
      system: true             # Enable system metrics
```

## Kotlin Development Guidelines

### Language Standards

#### Immutability (MANDATORY)
```kotlin
// ✅ CORRECT: Immutable value objects
data class Produktnummer(
    private val value: String  // val, not var
) {
    init {
        require(value.isNotBlank()) { "Product number cannot be blank" }
    }
}

// ❌ FORBIDDEN: Mutable value objects
data class Produktnummer(
    private var value: String  // Breaks immutability
)
```

#### Null Safety (MANDATORY)
- Use Kotlin's null safety features properly
- Prefer `?.let` over explicit null checks
- Never use `!!` unless absolutely necessary with clear justification
- Use `Optional` patterns for domain modeling

#### Collection Operations
```kotlin
// ✅ PREFERRED: Functional collection operations
products.filter { it.isAvailable() }
    .map { it.toDTO() }
    .take(limit)

// ❌ AVOID: Imperative loops for simple transformations
val result = mutableListOf<ProductDTO>()
for (product in products) {
    if (product.isAvailable()) {
        result.add(product.toDTO())
    }
}
```

### Error Handling

#### Domain Exceptions (MANDATORY)
```kotlin
// ✅ CORRECT: Specific domain exceptions
class ProduktnummerIstLeerException(value: String) : ProdukteException(
    "Product number cannot be empty: '$value'"
)

// ✅ CORRECT: Validation in value objects
data class Preis(private val bruttoBetrag: BigDecimal) {
    init {
        require(bruttoBetrag >= BigDecimal.ZERO) { 
            "Price cannot be negative: $bruttoBetrag" 
        }
    }
}
```

#### Boundary Validation (CRITICAL)
```kotlin
// ✅ MANDATORY: Validate boundaries to prevent crashes
fun begrenzeAufNEinträge(n: Int?): Produkte {
    return when {
        n == null || n < 0 -> this
        n <= produkte.size -> Produkte(produkte.subList(0, n))
        else -> this
    }
}

// ❌ FORBIDDEN: No boundary checks (causes crashes)
fun begrenzeAufNEinträge(n: Int?): Produkte {
    return if (n != null && n <= produkte.size) {
        Produkte(produkte.subList(0, n)) // Crashes with negative n
    } else this
}
```

### MongoDB Integration

#### Repository Pattern (MANDATORY)
```kotlin
// ✅ Domain layer: Interface
interface ProduktRepository {
    fun ladeVerfügbareProdukte(filter: CmsProdukteFilter): Produkte
    fun ladeProdukt(nummer: Produktnummer): Produkt?
}

// ✅ Adapter layer: Implementation
@ApplicationScoped
class ProduktMongoRepository : ProduktRepository, PanacheMongoRepository<MongoProdukt>
```

#### Entity Mapping (MANDATORY)
- MongoDB entities in adapter layer only
- Clean mapping between MongoProdukt ↔ Produkt
- No MongoDB annotations in domain layer

## Testing Strategy

### Testing Framework Stack

- **Unit Testing:** JUnit 5 Jupiter
- **Mocking:** Mockk 1.14.4 (Kotlin-specific, not Mockito)
- **Assertions:** Strikt 0.35.1 (Kotlin-specific)
- **Integration:** @QuarkusTest with TestContainers 1.21.3 (MongoDB & Kafka)
- **Architecture:** ArchUnit 1.4.1 for layer compliance
- **REST Testing:** REST Assured with Kotlin extensions

### Test Structure (MANDATORY)

```
src/test/kotlin/de/blume2000/finden/
├── adapter/
│   ├── active/api/          # REST endpoint tests
│   └── passive/database/    # Repository implementation tests
├── application/             # Application service tests  
├── domain/                  # Domain logic tests
└── testutils/               # Test helpers (separate from production)
```

### Testing Patterns (MANDATORY)

#### Unit Tests
```kotlin
@Test
fun `should throw exception when product number is empty`() {
    // Given-When-Then structure
    expectThrows<ProduktnummerIstLeerException> {
        Produktnummer("")
    }
}
```

#### Integration Tests
```kotlin
@QuarkusTest
@QuarkusTestResource(TestContainers::class)
@Tag("integration")
internal class ProduktlistenApplicationServiceTest {
    
    @Inject
    lateinit var service: ProduktlistenApplicationService
    
    @Test
    fun `should return available products with filters`() {
        // Real database integration testing
    }
}
```

#### Architecture Tests (MANDATORY)
```kotlin
@Test
fun `domain layer should not depend on adapters`() {
    noClasses()
        .that().resideInAPackage("..domain..")
        .should().dependOnClassesThat()
        .resideInAPackage("..adapter..")
        .check(importedClasses)
}
```

### Test Requirements (MANDATORY)

1. **BDD Structure:** ALL tests MUST use Given-When-Then
2. **Test Isolation:** Each test completely independent
3. **Coverage:** 80% minimum, 95% for business logic
4. **No Production Dependencies:** Test utils in test package only
5. **Public Interface Testing:** No reflection or private method testing

## Code Quality Standards

### Critical Bug Prevention (MANDATORY)

#### Boundary Conditions
```kotlin
// ✅ MANDATORY: Validate all boundary conditions
fun parseLimit(value: String?): Int? {
    return value?.let { 
        try {
            val parsed = it.toInt()
            if (parsed < 0) null else parsed
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid limit: $value")
        }
    }
}
```

#### Price Comparison Logic
```kotlin
// ✅ CORRECT: Proper null handling in comparisons
fun istGrößerAls(other: Preis?): Boolean {
    return other != null && this.bruttoBetrag > other.bruttoBetrag
}

// ❌ FORBIDDEN: Contradictory null logic
fun istGrößerAls(other: Preis?): Boolean {
    return other == null || this.bruttoBetrag > other.bruttoBetrag // Wrong!
}
```

### Architecture Compliance (ENFORCED)

#### Layer Coupling (FORBIDDEN)
```kotlin
// ❌ FORBIDDEN: Domain referencing adapter DTOs
class Produkte {
    fun zuValideProduktDTOs(): List<ProduktDTO> // DTO in domain!
}

// ✅ CORRECT: Domain only references domain types
class Produkte {
    fun toValidProducts(): List<Produkt>
}
```

### Quality Tools

- **Static Analysis:** Detekt 1.23.8 for Kotlin
- **Build Quality:** Gradle 8.x build verification with Kotlin DSL
- **Dependency Check:** OWASP Dependency Check 12.1.3 for vulnerability scanning
- **Version Management:** Gradle Versions Plugin 0.52.0 for dependency updates
- **Coverage:** JaCoCo 0.8.7 with 80% minimum threshold
- **Schema Management:** Avro Plugin 1.9.1 for Kafka schema handling

### Build Verification (MANDATORY)

```bash
# Required before any commit
./gradlew clean build
./gradlew detekt
./gradlew test jacocoTestReport
```

## Development Process

### AI-Assisted Development Integration (MANDATORY)

This project integrates TaskMaster AI for comprehensive development workflow management alongside traditional development practices.

#### TaskMaster-First Development Approach
- **PRD-Based Planning:** All major features start with Product Requirements Documents
- **AI Task Generation:** Use `task-master parse-prd` to generate structured task lists
- **Complexity Analysis:** Apply `task-master analyze-complexity --research` for informed planning
- **Iterative Expansion:** Break complex tasks into manageable subtasks automatically
- **Progress Tracking:** Maintain development context through task annotations

### Enhanced TDD Workflow (MANDATORY)

#### TaskMaster Integration Points
1. **Task Selection:** `task-master next` → identify prioritized work
2. **Context Review:** `task-master show <id>` → understand requirements
3. **Status Update:** `task-master set-status --id=<id> --status=in-progress`

#### Core TDD Cycle
4. **RED:** Write failing BDD test aligned with task requirements
5. **GREEN:** Minimal code to pass test
6. **REFACTOR:** Improve while keeping tests green
7. **CONTEXT:** `task-master update-subtask --id=<id> --prompt="implementation notes"`
8. **COMMIT:** Atomic commit with task reference
9. **COMPLETE:** `task-master set-status --id=<id> --status=done`

### Enhanced Pre-Commit Checklist (MANDATORY)

#### Analysis-Informed Quality Gates
- [ ] **Security:** No endpoints without authentication (addresses critical analysis finding)
- [ ] **Performance:** No O(n²) algorithms or unbounded operations (prevents identified bottlenecks)
- [ ] **Architecture:** No layer coupling violations (maintains DDD boundaries)
- [ ] **Quality:** All tests pass, coverage > 80%
- [ ] **Boundaries:** All input validation implemented (prevents runtime crashes)
- [ ] **Immutability:** Value objects properly immutable

#### TaskMaster Integration Checks
- [ ] **Task Context:** Implementation details logged via `task-master update-subtask`
- [ ] **Status Management:** Task status properly updated
- [ ] **Dependency Validation:** `task-master validate-dependencies` passes
- [ ] **Analysis Alignment:** Changes align with comprehensive analysis recommendations

### Enhanced Git Workflow

#### TaskMaster-Informed Commits
- **Conventional Commits:** `feat:`, `fix:`, `refactor:`, `security:`, `perf:`
- **Task References:** Include task IDs in commit messages (e.g., "feat: implement JWT auth (task 1.2)")
- **Analysis Context:** Reference analysis findings in commit descriptions
- **Atomic Commits:** Single logical change per commit
- **Branch Naming:** `feature/`, `bugfix/`, `security/`, `perf/`

#### Analysis-Driven Branch Examples
```bash
# Security implementation branches (from analysis findings)
git checkout -b security/implement-jwt-authentication
git checkout -b security/fix-cors-configuration

# Performance optimization branches (from analysis findings)
git checkout -b perf/optimize-sorting-algorithm
git checkout -b perf/implement-database-indexes

# Architecture improvement branches
git checkout -b refactor/remove-dto-coupling
git checkout -b refactor/fix-value-object-mutability
```

### Critical Code Review Focus (Analysis-Enhanced)

#### Priority 1: Security (Analysis Score: 3/10)
1. **Authentication gaps** - ensure all endpoints secured
2. **CORS misconfiguration** - validate origin restrictions
3. **Input validation** - prevent injection attacks

#### Priority 2: Performance (Analysis Score: 5/10)
1. **O(n²) algorithms** - especially in sorting operations
2. **Memory exhaustion patterns** - full dataset loading
3. **Database query optimization** - prevent N+1 problems

#### Priority 3: Architecture (Analysis Score: 8/10)
1. **Layer coupling violations** - domain/DTO dependencies
2. **Value object immutability** - prevent state corruption
3. **Boundary condition handling** - negative values, null inputs

### Workflow Integration Summary

The enhanced development process integrates TaskMaster commands at key points in the traditional TDD cycle. Detailed workflows and examples are provided in the [AI Development Workflow](#ai-development-workflow) section.

### Build Commands

```bash
# Run tests
./gradlew test integrationTest

# Check code quality
./gradlew detekt spotbugsMain

# Generate coverage report
./gradlew jacocoTestReport

# Full build verification
./gradlew clean build
```

## Additional Standards

### Dependency Management

- **Update Strategy:** Monthly dependency updates
- **Security:** Automated vulnerability scanning
- **Version Strategy:** Latest stable releases preferred
- **Build Reproducibility:** Version locking in gradle.lockfile

### Performance Monitoring

- **Metrics:** Micrometer with Prometheus
- **Response Times:** P95 < 500ms target
- **Resource Usage:** Memory < 80% heap
- **Database:** Query times < 200ms average

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

## Task Master Integration

### Essential Commands

#### Core Workflow Commands

```bash
# Project Setup
task-master init                                    # Initialize Task Master in current project
task-master parse-prd .taskmaster/docs/prd.txt      # Generate tasks from PRD document
task-master models --setup                        # Configure AI models interactively

# Daily Development Workflow
task-master list                                   # Show all tasks with status
task-master next                                   # Get next available task to work on
task-master show <id>                             # View detailed task information (e.g., task-master show 1.2)
task-master set-status --id=<id> --status=done    # Mark task complete

# Task Management
task-master add-task --prompt="description" --research        # Add new task with AI assistance
task-master expand --id=<id> --research --force              # Break task into subtasks
task-master update-task --id=<id> --prompt="changes"         # Update specific task
task-master update --from=<id> --prompt="changes"            # Update multiple tasks from ID onwards
task-master update-subtask --id=<id> --prompt="notes"        # Add implementation notes to subtask

# Analysis & Planning
task-master analyze-complexity --research          # Analyze task complexity
task-master complexity-report                      # View complexity analysis
task-master expand --all --research               # Expand all eligible tasks

# Dependencies & Organization
task-master add-dependency --id=<id> --depends-on=<id>       # Add task dependency
task-master move --from=<id> --to=<id>                       # Reorganize task hierarchy
task-master validate-dependencies                            # Check for dependency issues
task-master generate                                         # Update task markdown files (usually auto-called)
```

### Project Structure Integration

#### Core Files
- `.taskmaster/tasks/tasks.json` - Main task data file (auto-managed)
- `.taskmaster/config.json` - AI model configuration (use `task-master models` to modify)
- `.taskmaster/docs/prd.txt` - Product Requirements Document for parsing
- `.taskmaster/tasks/*.txt` - Individual task files (auto-generated from tasks.json)
- `.env` - API keys for CLI usage

#### Claude Code Integration Files
- `CLAUDE.md` - Auto-loaded context for Claude Code (this file)
- `.claude/settings.json` - Claude Code tool allowlist and preferences
- `.claude/commands/` - Custom slash commands for repeated workflows
- `.mcp.json` - MCP server configuration (project-specific)

#### Integration with Existing Structure
```
project/
├── .taskmaster/
│   ├── tasks/              # Task files directory
│   │   ├── tasks.json      # Main task database
│   │   ├── task-1.md      # Individual task files
│   │   └── task-2.md
│   ├── docs/              # Documentation directory
│   │   ├── prd.txt        # Product requirements
│   ├── reports/           # Analysis reports directory
│   │   └── task-complexity-report.json
│   ├── templates/         # Template files
│   │   └── example_prd.txt  # Example PRD template
│   └── config.json        # AI models & settings
├── .claude/
│   ├── settings.json      # Claude Code configuration
│   └── commands/         # Custom slash commands
├── PRPs/                 # Context engineering patterns (existing)
├── doc/                  # Technical documentation (existing)
├── src/                  # Source code (existing)
├── .env                  # API keys
├── .mcp.json            # MCP configuration
└── CLAUDE.md            # This file - auto-loaded by Claude Code
```

### Task Structure & Management

#### Task ID Format
- Main tasks: `1`, `2`, `3`, etc.
- Subtasks: `1.1`, `1.2`, `2.1`, etc.
- Sub-subtasks: `1.1.1`, `1.1.2`, etc.

#### Task Status Values
- `pending` - Ready to work on
- `in-progress` - Currently being worked on
- `done` - Completed and verified
- `deferred` - Postponed
- `cancelled` - No longer needed
- `blocked` - Waiting on external factors

#### Task Integration with Development Standards

```json
{
  "id": "1.2",
  "title": "Implement JWT authentication system",
  "description": "Add secure authentication to resolve critical security gap",
  "status": "pending",
  "priority": "high",
  "dependencies": ["1.1"],
  "details": "Must address critical security requirements from analysis. Use Quarkus Security with JWT tokens. Ensure all endpoints protected.",
  "testStrategy": "Unit tests for auth functions, integration tests for login flow, security penetration testing",
  "subtasks": []
}
```

### Gradle Integration

TaskMaster works seamlessly with existing Gradle workflow:

```bash
# Run TaskMaster commands alongside Gradle
task-master next                    # Get next task
./gradlew test                     # Run tests for current task
task-master update-subtask --id=1.2 --prompt="Tests passing, ready for implementation"
./gradlew clean build              # Full build verification
task-master set-status --id=1.2 --status=done  # Mark complete
```

### Git Integration Patterns

```bash
# Reference tasks in commits
git commit -m "feat: implement JWT auth system (task 1.2)

- Add Quarkus Security dependencies
- Implement authentication endpoints
- Add security configuration
- Tests: AuthenticationServiceTest passes

Addresses critical security gap identified in comprehensive analysis.

Resolves: task 1.2"

# Create PR for completed task
gh pr create --title "Complete task 1.2: JWT authentication" \
  --body "Implements secure authentication system as specified in task 1.2

**Changes:**
- JWT-based authentication
- Security configuration 
- All endpoints protected
- Comprehensive test coverage

**Security Review:**
- [ ] Authentication system implemented ✅
- [ ] All endpoints secured ✅  
- [ ] CORS properly configured ✅
- [ ] Input validation comprehensive ✅

Closes: task 1.2"
```

## AI Development Workflow

### Standard Development Workflow

#### 1. Project Initialization
```bash
# Initialize Task Master for existing project
task-master init

# Create or obtain PRD, then parse it
# PRDs can reference existing analysis documents
task-master parse-prd .taskmaster/docs/security-implementation-prd.txt

# Analyze complexity and expand tasks
task-master analyze-complexity --research
task-master expand --all --research
```

#### 2. AI-Assisted Planning Phase
```bash
# Generate tasks from PRDs that reference analysis findings
task-master parse-prd .taskmaster/docs/performance-optimization-prd.txt --append

# Use research mode for complex technical decisions
task-master expand --id=1 --research --num=5

# Validate task dependencies align with architecture
task-master validate-dependencies
```

#### 3. Daily Development Loop
```bash
# Start each development session
task-master next                           # Find next available task
task-master show <id>                     # Review task details and acceptance criteria

# During implementation (following existing TDD workflow)
# RED Phase
task-master set-status --id=<id> --status=in-progress
task-master update-subtask --id=<id> --prompt="Starting TDD - writing failing test for [feature]"

# GREEN Phase  
task-master update-subtask --id=<id> --prompt="Test passing with minimal implementation"

# REFACTOR Phase
task-master update-subtask --id=<id> --prompt="Refactored for performance/quality, all tests green"

# Complete tasks
task-master set-status --id=<id> --status=done
```

#### 4. Multi-Claude Session Coordination

For complex projects requiring parallel development:

```bash
# Terminal 1: Main implementation (security features)
cd project && claude
task-master show 1.2  # JWT authentication implementation

# Terminal 2: Performance optimization (separate branch)  
cd project && git checkout -b perf/optimization
claude
task-master show 3.1  # Database index optimization

# Terminal 3: Testing and validation
cd project && claude  
task-master show 2.1  # Integration test development
```

### Context Engineering Patterns

#### PRD Creation from Analysis Findings

Transform analysis findings into actionable PRDs:

```markdown
# Security Implementation PRD

## Background
Comprehensive analysis identified critical security gaps requiring immediate attention.

## Requirements

### R1: Authentication System (CRITICAL)
- Implement JWT-based authentication for all API endpoints
- Use Quarkus Security with role-based access control
- Priority: CRITICAL (production blocker)
- References: COMPREHENSIVE_ANALYSIS_REPORT.md Section 4.1

### R2: CORS Configuration (HIGH)  
- Replace permissive CORS with restrictive policy
- Configure domain-specific origins
- Priority: HIGH
- References: COMPREHENSIVE_ANALYSIS_REPORT.md Section 4.5

## Architecture Impact
- Add security layer to existing Onion Architecture
- Integrate with existing Quarkus CDI patterns
- Maintain performance standards (P95 < 500ms)
```

#### Task Generation Workflow

```bash
# 1. Create PRD based on analysis findings
echo "# Performance Optimization PRD..." > .taskmaster/docs/performance-prd.txt

# 2. Parse PRD to generate structured tasks
task-master parse-prd .taskmaster/docs/performance-prd.txt --append

# 3. Analyze and expand critical tasks
task-master analyze-complexity --from=10 --to=15 --research
task-master expand --id=11 --research --force  # Critical O(n²) algorithm fix

# 4. Validate against architecture standards
task-master validate-dependencies
```

### Complex Workflow Integration

#### Performance Optimization Example

```bash
# 1. Generate tasks from performance analysis
task-master parse-prd .taskmaster/docs/performance-optimization-prd.txt --append

# 2. Expand critical performance tasks
task-master expand --id=11 --research --num=4  # O(n²) algorithm fix
task-master expand --id=12 --research --num=3  # Database index optimization

# 3. Work through systematically with TDD
task-master next                # Get: 11.1 - Fix sorting algorithm complexity
task-master show 11.1          # Review: Implement HashMap-based lookup
task-master set-status --id=11.1 --status=in-progress

# TDD Implementation with context tracking
task-master update-subtask --id=11.1 --prompt="RED: Added failing test for O(1) product lookup in comparator"
# ... implement solution ...
task-master update-subtask --id=11.1 --prompt="GREEN: HashMap lookup implementation passes tests, O(n²) → O(n log n)"
# ... refactor ...
task-master update-subtask --id=11.1 --prompt="REFACTOR: Clean code, performance tests confirm 70% improvement"

task-master set-status --id=11.1 --status=done
```

#### Security Implementation Example

```bash
# 1. Address critical security findings
task-master add-task --prompt="Implement JWT authentication system to resolve critical security gap identified in comprehensive analysis" --research

# 2. Expand into implementation subtasks
task-master expand --id=20 --research --num=6

# 3. Follow security-first development
task-master show 20.1  # Add Quarkus Security dependencies
task-master show 20.2  # Implement authentication endpoints
task-master show 20.3  # Configure CORS restrictions
task-master show 20.4  # Add role-based access control
task-master show 20.5  # Security integration tests
task-master show 20.6  # Security penetration testing

# 4. Track security compliance
task-master update-subtask --id=20.3 --prompt="CORS configured per security analysis recommendations, restricted to specific domains"
```

### Integration with Existing Development Standards

#### Combining AI Workflow with Existing Standards

The TaskMaster workflow integrates seamlessly with existing development standards:

1. **Security Standards Integration:**
   - TaskMaster tasks reference security checklist items
   - PRDs map to OWASP Top 10 compliance requirements
   - Task completion tied to security review completion

2. **Performance Standards Integration:**
   - Tasks include performance criteria (P95 < 500ms)
   - Complexity analysis prevents O(n²) implementations
   - Performance testing integrated into task completion

3. **Architecture Standards Integration:**
   - Tasks validate against Onion Architecture principles
   - PRDs ensure DDD pattern compliance
   - Task updates reference specific architectural layers

4. **Quality Standards Integration:**
   - TDD workflow tracked through TaskMaster updates
   - Coverage requirements tied to task completion
   - Code review checklist integrated with task validation

---

## Summary

This guide integrates **AI-assisted development workflow** with comprehensive codebase analysis findings to establish a complete development ecosystem for the Finden Backend.

### Technology Foundation
- **Runtime:** Kotlin 2.2.0 targeting JVM 22
- **Framework:** Quarkus 3.24.3 
- **Build System:** Gradle 8.x with Kotlin DSL
- **Architecture:** Domain-Driven Design with Onion/Hexagonal pattern
- **AI Integration:** TaskMaster with Claude Code for context-driven development

### Workflow Integration
- **Context Engineering:** PRD-based task generation and management
- **Task Master AI:** Automated complexity analysis and task expansion
- **Claude Code:** Real-time development assistance with project context
- **Analysis-Driven Development:** Quality gates based on comprehensive security and performance analysis

### Critical Security & Performance Gates
Based on comprehensive analysis findings (System Health Score: 6.2/10):

**PRODUCTION BLOCKERS:**
- ⚠️ **NO DEPLOYMENT** without JWT authentication system (Security Score: 3/10)
- ⚠️ **NO O(n²) ALGORITHMS** especially in sorting operations (Performance Score: 5/10)
- ⚠️ **NO MEMORY EXHAUSTION** patterns from full dataset loading
- ⚠️ **NO LAYER COUPLING** violations (enforced by ArchUnit)

**QUALITY STANDARDS:**
- 🛡️ **All endpoints secured** with role-based authentication
- ⚡ **Database operations optimized** with proper indexing
- 🏗️ **Architecture compliance** maintained through testing
- 🧪 **80%+ test coverage** with BDD structure
- 🔍 **Input validation comprehensive** at all boundaries

### Development Workflow Summary
The integrated AI-assisted workflow combines traditional DDD/TDD practices with TaskMaster for **production-ready code quality** and **enhanced developer velocity** through automated planning, context preservation, and analysis-driven quality gates.