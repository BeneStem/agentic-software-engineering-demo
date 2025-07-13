# Development Guide

This guide outlines the development standards, coding conventions, and contribution guidelines for the Product API
project.

## Table of Contents

- [Java Version Requirements](#java-version-requirements)
- [Architecture and Domain Design](#architecture-and-domain-design)
- [Modern Java Features](#modern-java-features)
- [Functional Programming Patterns](#functional-programming-patterns)
- [Testing Strategy](#testing-strategy)
- [Code Style Guidelines](#code-style-guidelines)
- [Documentation Standards](#documentation-standards)
- [Quality Assurance](#quality-assurance)

## Java Version Requirements

### Java Version

- **Required Version:** Java 24 (OpenJDK 24.0.1)
- **Compatibility:** Java 24+ features are used
- **Preview Features:** Usage recommended

## Architecture and Domain Design

### Clean Architecture Principles

- **Layered Architecture:** Clear separation between controller, service, and domain layers
- **Dependency Inversion:** Higher layers depend on abstractions, not implementations
- **Single Responsibility:** Each component has one reason to change
- **Domain-Driven Design:** Business logic encapsulated in domain entities

### Domain Driven Design

- **Rich Domain Entities:** Business logic lives in domain objects
- **Value Objects:** Immutable objects representing concepts without identity
- **Domain Services:** Complex business operations that don't belong to single entities
- **Repository Pattern:** Abstract data access behind interfaces

### Service Layer Architecture

- **Application Services:** Orchestrate domain operations and handle use cases
- **Domain Services:** Implement complex business rules spanning multiple entities
- **Infrastructure Services:** Handle external dependencies and technical concerns
- **Transaction Management:** Ensure data consistency across operations

### API Design Patterns

- **RESTful Endpoints:** Follow REST conventions for resource management
- **DTO Pattern:** Data Transfer Objects for API boundaries
- **Request/Response Models:** Separate models for input validation and output formatting
- **Error Handling:** Consistent error responses across all endpoints

### REST API Best Practices (MANDATORY)

#### Resource Identification

- **Path Parameters:** Use path parameters to identify resources (e.g., `/products/{id}`)
- **ID Precedence:** Path parameter ID ALWAYS takes precedence over request body ID
- **Consistent Endpoints:** Use consistent URL patterns across all resources

#### HTTP Methods and Semantics

- **GET:** Retrieve resources (idempotent, no side effects)
- **POST:** Create new resources (non-idempotent)
- **PUT:** Update existing resources (idempotent)
- **DELETE:** Remove resources (idempotent)

#### Request/Response Design

- **Validation:** Apply proper validation annotations on DTOs
- **Error Responses:** Use consistent error response format
- **Status Codes:** Return appropriate HTTP status codes
  - 200 OK for successful updates
  - 201 Created for successful creation
  - 404 Not Found for non-existent resources
  - 400 Bad Request for validation errors
  - 409 Conflict for business rule violations

### Validation Strategy

- **Input Validation:** Jakarta Validation annotations on DTOs
- **Business Validation:** Domain rules enforced in entities and services
- **Cross-cutting Validation:** Global exception handling for consistent responses

## Modern Java Features

### Stable Features (Recommended)

#### Records (Java 17 LTS)

- **Immutable Data Carriers:** Use records for DTOs and value objects
- **Automatic Generation:** Equals, hashCode, toString, and accessors generated automatically
- **Compact Syntax:** Minimal boilerplate for data classes
- **Validation:** Use compact constructors for input validation

#### Text Blocks (Java 17 LTS)

- **Multi-line Strings:** Use for JSON templates, SQL queries, or configuration
- **Better Readability:** Eliminates escape sequences for quotes
- **String Formatting:** Combine with .formatted() for dynamic content

#### Local Variable Type Inference (Java 17 LTS)

- **var keyword:** Use when type is obvious from context
- **Improved Readability:** Reduces verbosity in complex generic types
- **Guidelines:** Don't use when it reduces code clarity

### Advanced Features (Recommended)

#### Pattern Matching (Java 21+)

- **instanceof patterns:** Use for type checking and casting
- **Switch expressions:** Modern alternative to traditional switch statements
- **Caution:** Only use when providing clear benefit over traditional approaches

#### Sealed Classes (Java 21+)

- **Controlled Inheritance:** Restrict class hierarchies for domain modeling
- **Use Case:** When you need exhaustive pattern matching
- **Consideration:** Evaluate impact on extensibility

### Preview Features (Evaluate Carefully)

- **Current Policy:** Use preview features only when they provide significant value
- **Stability Risk:** Preview features may change or be removed
- **Team Decision:** Discuss impact on maintainability and future upgrades

### Lombok Usage Guidelines

- **Minimal Usage:** Only when Java language features are insufficient
- **Constructor Injection:** Use @RequiredArgsConstructor for dependency injection
- **Avoid Getters/Setters:** Prefer records for immutable data classes
- **Utility Classes:** Use @UtilityClass only for static utility methods
- **@Getter for Exceptions:** Use @Getter on exception classes that need field access
- **@Getter vs Records:** Use @Getter when class cannot be a record (e.g., extends other class)

#### When to Use @Getter

**Use @Getter for:**
- **Exception Classes:** Classes extending Exception/RuntimeException that need field access
- **Inheritance Scenarios:** Classes that extend other classes (cannot be records)
- **Framework Requirements:** Classes needing special constructor behavior that records can't provide
- **Mutable State:** Classes that need setter methods alongside getters

**Don't Use @Getter for:**
- **Immutable Data:** Use records instead for simple data carriers
- **Value Objects:** Prefer records for domain value objects
- **DTOs:** Use records for simple request/response objects
- **Builder Pattern:** Combine with @Builder, use @Value instead

### Spring Annotations Policy (MANDATORY)

- **No @Bean:** Never use @Bean annotations for dependency configuration
- **No @MockBean:** Never use @MockBean in tests - use @Mock and @InjectMocks instead
- **Direct Annotations:** Use @Service, @Repository, @Component directly on classes
- **Constructor Injection:** Use @RequiredArgsConstructor with final fields
- **Auto-Configuration:** Rely on Spring's component scanning and auto-configuration

## Functional Programming Patterns

### Immutability Principles

- **Immutable Objects:** Use @Value and final fields where possible
- **Immutable Collections:** Prefer List.of(), Set.of(), Map.of() for static collections
- **Builder Pattern:** Use @Builder with @Value for immutable object construction
- **Copy Methods:** Use @With for creating modified copies of immutable objects

### Stream API Usage

- **Data Transformation:** Use map() for transforming elements
- **Filtering:** Apply filter() for conditional selection
- **Reduction:** Use reduce(), collect(), and terminal operations
- **Parallel Processing:** Consider parallelStream() for CPU-intensive operations

### Optional Handling

- **Null Safety:** Replace null checks with Optional
- **Chaining Operations:** Use map(), flatMap(), filter() on Optional
- **Default Values:** Apply orElse(), orElseGet(), orElseThrow()
- **Conditional Execution:** Use ifPresent(), ifPresentOrElse()

### Functional Interfaces

- **Lambda Expressions:** Use lambdas for short, single-use functions
- **Method References:** Apply method references for existing methods
- **Custom Functional Interfaces:** Create domain-specific functional interfaces
- **Composition:** Combine functions using andThen(), compose()

### Error Handling Patterns

- **Try-Catch Alternatives:** Use Optional for recoverable failures
- **Result Types:** Consider Either pattern for success/failure scenarios
- **Exception Transformation:** Convert checked exceptions to unchecked
- **Functional Error Handling:** Chain operations with error propagation

## Testing Strategy

### Test Structure

- **Project Structure:** Test sources should mirror main source structure, following DDD pattern.
- **Separate Packages:** Organize tests by layers: domain, application, infrastructure, presentation.
- **Directory Consistency**: Ensure `src/test` mirrors `src/main` structure exactly.

### Test Coverage Requirements

- **Minimum Coverage:** 80% instruction coverage (enforced by JaCoCo)
- **Service Layer:** 95%+ coverage for business logic
- **Controller Layer:** 90%+ coverage for endpoint testing
- **Model Layer:** 85%+ coverage for domain logic

### Testing Frameworks

- **JUnit 5 Jupiter:** Primary testing framework
- **Mockito:** Mocking dependencies in unit tests
- **Hamcrest:** Fluent assertion library
- **Spring Boot Test:** Integration testing with Spring context
- **TestContainers:** Database integration testing (future)

### Testing Pyramid

- **Unit Tests:** Majority of tests, fast feedback, isolated components
- **Integration Tests:** Controller and service layer integration
- **End-to-End Tests:** Few high-value complete application tests

### Unit Testing Guidelines

#### Service Layer Testing

- **Mock Dependencies:** Use @Mock for external dependencies
- **Inject Mocks:** Use @InjectMocks for service under test
- **Given-When-Then:** Structure tests with clear phases
- **Verify Interactions:** Assert both return values and method calls
- **Exception Testing:** Test error scenarios with assertThrows

#### Controller Layer Testing

- **Standalone MockMvc:** Use MockMvcBuilders.standaloneSetup() to avoid @MockBean
- **Mock Dependencies:** Always use @Mock and @InjectMocks, never @MockBean
- **JSON Path:** Validate response structure and content
- **Status Codes:** Assert correct HTTP status codes

### Integration Testing

#### Full Stack Integration Tests

- **SpringBootTest:** Use full application context
- **Random Port:** Use RANDOM_PORT for web environment
- **TestRestTemplate:** Make real HTTP requests
- **Database Testing:** Use in-memory database for isolation
- **End-to-End Scenarios:** Test complete user workflows

### Test Development Workflow

- **Behavior-Driven Development (BDD):** Structure tests with Given-When-Then phases.
- **Test-Driven Development (TDD):** Red-Green-Refactor cycle with BDD.
- **Small Commits:** Commit frequently, after each phase.
- **Code Review:** Regularly ensure tests meet quality standards.

### Coverage Analysis

- **JaCoCo Reports:** Generate comprehensive coverage reports
- **Coverage Goals:** Line coverage 80%, branch coverage 75%
- **Quality Gates:** Fail build if coverage drops below threshold
- **Coverage Review:** Analyze uncovered code for missing tests

### Refactoring and Cleanup

- **Unused Code:** Routinely check and remove unused methods, classes, imports.
- **Code Optimization:** Simplify complex methods and remove redundancies.
- **Commit Clean Code:** Ensure all changes are clean and tested before committing.

### Test Organization

- **Package Structure:** Mirror main source package structure in test
- **Test Naming:** Descriptive method names explaining behavior
- **Test Categories:** Unit tests, integration tests, end-to-end tests
- **Test Data:** Use builders or factories for consistent test data creation

## Development Process

### Guide Maintenance (MANDATORY)

- **Continuous Update:** Always check if the DEVELOPMENT_GUIDE.md needs updates based on any changes made
- **Real-time Reflection:** Any new patterns, decisions, or practices must be immediately documented
- **Guide-First Approach:** Update the guide before implementing new practices
- **Review Before Commit:** Verify guide accuracy with every development session

### Project Structure Requirements (MANDATORY)

- **DDD/Onion Architecture:** All code must follow proper Domain-Driven Design and Onion Architecture principles
- **Package Organization:** Strict layered structure as follows:
    - `domain/` - Core domain (innermost layer)
        - `model/` - Domain entities and value objects
        - `service/` - Domain services
        - `repository/` - Repository interfaces (ports)
        - `exception/` - Domain exceptions
    - `application/` - Application layer
        - `service/` - Application services (use cases)
        - `dto/` - Data Transfer Objects
    - `adapter/` - Adapters (outermost layer)
        - `in/` - Inbound adapters (driving)
            - `web/` - REST controllers and web exception handlers
        - `out/` - Outbound adapters (driven)
            - `persistence/` - Repository implementations
- **Test Structure:** Test folder structure MUST mirror src folder structure exactly
- **Layer Consistency:** Tests organized by same DDD/Onion layers as main source code
- **Dependency Direction:** Dependencies flow inward (adapters → application → domain)

### Pre-Commit Checklist (MANDATORY)

- **Unused Code Cleanup:** Check and remove unused methods, classes, imports
- **Code Optimization:** Look for classes that could be records, redundant code
- **Import Organization:** Ensure all imports are used and properly ordered
- **Architecture Compliance:** Verify all files are in correct DDD layer packages
- **Test Coverage:** Ensure new code has appropriate test coverage

### Commit Practices (MANDATORY)

- **Frequent Commits:** Commit regularly, not just at end of development session
- **Conventional Commits:** Use conventional commit format (feat:, fix:, refactor:, etc.)
- **Atomic Commits:** Each commit should represent a single logical change
- **Meaningful Messages:** Clear description of what was changed and why
- **Guide Updates:** Include guide updates in commits when applicable

### Testing Methodology (MANDATORY)

- **BDD Structure:** ALL tests MUST use Given-When-Then structure
- **Test Names:** Descriptive method names that read like specifications
- **TDD Process:** MANDATORY Red-Green-Refactor cycle for all development
- **Test First:** Write failing tests before implementing functionality
- **Commit Pattern:** Commit after each TDD phase (Red, Green, Refactor)
- **BDD Language:** Use domain terminology in test descriptions
- **Single Behavior:** Each test must verify exactly one specific behavior
- **Behavior Focus:** Tests MUST focus on observable behavior, not implementation details
- **Public API Testing:** Test through public interfaces, avoid reflection-based tests
- **Test Stability:** Tests should survive refactoring and internal changes
- **Test Isolation:** MANDATORY - Each test must be completely isolated from others

### Test Isolation (MANDATORY)

Test isolation is critical for reliable, maintainable tests. Each test must be completely independent of others.

#### The Problem

- **Shared State:** Tests that modify shared static state can contaminate other tests
- **Test Order Dependency:** Tests pass/fail based on execution order
- **Flaky Tests:** Intermittent failures that are hard to debug
- **False Positives/Negatives:** Tests appear to pass but are actually broken

#### Test Isolation Checklist

- Each test can run independently in any order
- No test depends on state left by previous tests
- All shared state is reset before each test
- Tests don't modify global/static state without cleanup
- Database state is reset between integration tests
- No hardcoded IDs that could conflict between tests

#### Test Utilities (MANDATORY)

- **Test Package Only:** All test-specific utilities must be in `src/test` package
- **No Production Dependencies:** Production code must never depend on test utilities
- **Separation of Concerns:** Keep test helpers separate from production logic
- **Helper Classes:** Create dedicated test helper classes for complex test setup
- **Naming Convention:** Use `*TestHelper` or `*TestUtil` for test utility classes
- **Refactoring Opportunity:** Any method in production code only used for testing should be moved to test utilities
- **Architecture Review:** Test-specific methods in production code indicate architectural issues
- **No Reflection in Tests:** Avoid using reflection to access private fields or methods in tests
- **Public Interface Testing:** Test through public APIs rather than manipulating internal state
- **Instance State Over Static:** Prefer instance-based state management over static state for better testability
- **Exception Testing Pattern:** For testing exception handlers that require complex parameter objects (like MethodArgumentNotValidException), use mock objects instead of reflection to create required dependencies

### Test Isolation

#### What Makes a Good Test

- **Behavior-Driven:** Test what the code DOES, not how it's implemented
- **Business Value:** Each test should verify a business requirement or use case
- **Observable Outcomes:** Focus on inputs and outputs visible to clients
- **Meaningful Assertions:** Verify results that matter to users/business
- **Single Responsibility:** Each test validates one specific behavior

#### Avoid These Anti-Patterns

- **Implementation Testing:** Using reflection to check internal structure (constructors, fields, methods)
- **White-box Testing:** Testing private methods or internal state directly
- **Brittle Tests:** Tests that break when refactoring without changing behavior
- **Structural Assertions:** Checking number of methods, constructors, or class hierarchy
- **Mock Overuse:** Mocking value objects or simple data structures

#### Test Through Public Interface

- **Method Calls:** Test public methods with various inputs
- **State Changes:** Verify observable state changes after operations
- **Return Values:** Assert correct outputs for given inputs
- **Side Effects:** Check interactions with dependencies (via mocks)
- **Exception Behavior:** Verify correct exceptions are thrown

### TDD Development Cycle (MANDATORY)

1. **RED Phase:** Write a failing BDD-style test first
2. **GREEN Phase:** Write minimal code to make test pass
3. **REFACTOR Phase:** Improve code while keeping tests green
4. **COMMIT:** Make atomic commit after each phase
5. **REPEAT:** Continue cycle for all functionality

### Development Workflow (MANDATORY)

1. **Check Guide:** Review relevant sections before starting work
2. **Plan Improvements:** Identify what guide updates might be needed
3. **Plan Changes:** Separate changes into smaller tasks
4. **Write Test (RED):** Create failing BDD test for the task first
5. **Implement (GREEN):** Write minimal code to pass test
6. **Write remaining tests:** Write remaining unit tests for the task
7. **Refactor:** Improve code while maintaining green tests
8. **Repeat:** Follow this TDD cycle for all tasks
9. **Clean Up:** Run pre-commit checklist
10. **Update Guide:** Add any new patterns or decisions to guide
11. **Test:** Ensure all tests, linting and coverage pass
12. **Commit:** Make atomic commits with proper TDD messages
13. **Repeat:** Follow this Development Workflow for all changes

## Code Style Guidelines

### Checkstyle Configuration

- **Style Guide:** Google Java Style Guide
- **Configuration:** `google_checks.xml` (built-in Maven Checkstyle Plugin)
- **Enforcement:** Fails build on violations (`failsOnError: true`)
- **Console Output:** Enabled for immediate feedback
- **Execution Phase:** Validate phase (runs early in build)

### Google Java Style Key Rules

- **Indentation:** 2 spaces (no tabs)
- **Line Length:** 100 characters maximum
- **Braces:** K&R style (opening brace on same line)
- **Imports:** Organize imports, no wildcard imports
- **Naming Conventions:**
    - Classes: PascalCase
    - Methods/Variables: camelCase
    - Constants: UPPER_SNAKE_CASE
    - Packages: lowercase

### Code Quality Tools

- **Checkstyle:** Style and convention validation
- **SpotBugs:** Static analysis for bug detection
- **JaCoCo:** Code coverage reporting

## Dependency Management

### Update Strategy

- **Frequency:** Check for dependency updates monthly
- **Tooling:** Use Maven's versions plugin to identify updates
- **Validation:** Ensure updates do not break existing functionality
- **Review:** Review changelogs for backward compatibility and new features
- **Action:** Update to the latest version if compatible; commit updates as separate changes

### Dependency Update Commands

#### Check for Updates

```bash
# Check for plugin updates
mvn versions:display-plugin-updates

# Check for dependency updates
mvn versions:display-dependency-updates

# Check for property updates (like Spring Boot version)
mvn versions:display-property-updates
```
#### Update Dependencies

```bash
# Update to latest versions (interactive)
mvn versions:use-latest-versions

# Update specific dependency
mvn versions:use-latest-versions -Dincludes=org.springframework.boot:*

# Update parent POM version
mvn versions:update-parent
```
#### Validation Workflow

```bash
# After updating dependencies
mvn clean compile test
mvn jacoco:report

# Check for any issues
mvn checkstyle:check spotbugs:check

# Commit updates
git add pom.xml
git commit -m "chore: update dependencies to latest versions"
```
### Monthly Update Process

1. **Check Updates:** Run dependency update commands
2. **Review Changes:** Check changelogs for breaking changes
3. **Update Incrementally:** Update one dependency group at a time
4. **Test Thoroughly:** Run full test suite after each update
5. **Commit Separately:** Each dependency update gets its own commit
6. **Document Issues:** Note any compatibility problems in commit messages

## Documentation Standards

### No Code Documentation Policy

- **Zero Documentation:** No comments, no inline documentation, no JavaDoc
- **Self-Documenting Code:** Code must be self-explanatory through clear naming and structure
- **Clean Code:** Use meaningful variable names, method names, and class names
- **Intent-Revealing:** Code should communicate its purpose without any explanatory text
- **No Exceptions:** This policy applies to all code: production, tests, configuration

### Development Guide Guidelines

- **No Code Examples:** Code examples must not be included in this guide to keep it focused and concise
- **Principle-Based:** Focus on principles and rules rather than implementation details
- **Living Document:** Update guide with lessons learned during development
- **Mandatory Compliance:** All rules in this guide are mandatory unless explicitly marked otherwise

## Refactoring and Code Quality

### Continuous Improvement Principles

- **Refactoring Mindset:** Always look for opportunities to improve code structure during development
- **Clean Separation:** Production and test code must be strictly separated
- **Architecture Hygiene:** Regularly review code placement and move misplaced functionality
- **Boy Scout Rule:** Leave code cleaner than you found it

### Code Placement Guidelines

- **Production Purity:** Production code should only contain production logic
- **Test Isolation:** Test utilities belong only in test packages
- **Single Purpose:** Each class should have only one reason to exist
- **Layer Respect:** Code must respect architectural layer boundaries

### Refactoring Triggers

- **Misplaced Methods:** Methods in production code only used by tests
- **Test Dependencies:** Production code depending on test utilities
- **Architectural Violations:** Code in wrong layers or packages
- **Unnecessary Complexity:** Over-engineered solutions that can be simplified

## Quality Assurance

### Testing Requirements

- **Unit Tests:** Mandatory for all service methods
- **Integration Tests:** Required for controller endpoints
- **Test Coverage:** Aim for 80%+ coverage

### Testing Stack

- **JUnit 5:** Jupiter for unit testing
- **Mockito:** Mocking framework
- **Hamcrest:** Assertion library
- **Spring Boot Test:** Integration testing

### Build Verification

Before submitting code:

```bash
# Run all quality checks
mvn clean compile
mvn checkstyle:check
mvn spotbugs:check
mvn test
mvn jacoco:report
```

## Additional Standards

### Git Workflow

- **Branch Naming:** `feature/`, `bugfix/`, `hotfix/`
- **Commit Messages:** Follow conventional commits format
- **Pull Requests:** Must pass all quality checks

### Performance Considerations

- TODO

### Security

- Follow OWASP guidelines
- Validate all inputs
- Use proper exception handling to avoid information leakage
