# 17. TestContainers for Integration Testing

## Status

Accepted

## Context

Integration tests need to verify the system's interaction with external dependencies like MongoDB and Kafka. Requirements include:
- Realistic test environments
- Isolated test execution
- Reproducible test conditions
- CI/CD pipeline compatibility
- No shared test infrastructure
- Fast test execution

Mocking external dependencies doesn't catch integration issues. Shared test databases cause test interference. In-memory substitutes don't match production behavior.

## Decision

We will use TestContainers for integration testing:
- MongoDB TestContainer for database tests
- Kafka TestContainer for messaging tests
- Automatic container lifecycle management
- Isolated containers per test class
- Real MongoDB and Kafka instances
- Configuration through `@QuarkusTest` and `@TestProfile`

## Consequences

**Positive:**
- Tests run against real dependencies
- Complete test isolation
- No test data contamination
- Reproducible across environments
- Catches real integration issues
- Works in CI/CD pipelines

**Negative:**
- Slower than unit tests
- Requires Docker installation
- More resource intensive
- Container startup time overhead
- Potential flakiness from container issues
- Debugging can be more complex

**Neutral:**
- Different test patterns required
- Container version management needed
- Network configuration considerations