# 18. ArchUnit for Architecture Testing

## Status

Accepted

## Context

The Onion/Hexagonal architecture requires strict enforcement of layer dependencies and architectural rules. Requirements include:
- Automated verification of architecture rules
- Prevention of architecture erosion
- Clear feedback on violations
- Integration with CI/CD pipeline
- Documentation of architecture decisions in code

Manual code reviews cannot consistently catch all architecture violations. Static analysis tools don't understand architectural concepts.

## Decision

We will use ArchUnit for architecture testing:
- Test layer dependencies (domain → application → adapter)
- Verify package structure conventions
- Ensure no framework dependencies in domain layer
- Check naming conventions for components
- Validate annotation usage
- Run as part of integration test suite

Example rules:
- Domain layer has no external dependencies
- Repositories are only implemented in adapter layer
- Controllers only exist in adapter layer
- Value objects are immutable

## Consequences

**Positive:**
- Architecture rules enforced automatically
- Prevents architecture erosion over time
- Quick feedback on violations
- Architecture documented as executable tests
- Reduces manual review burden
- Educates team on architecture

**Negative:**
- Additional test maintenance
- Initial setup effort
- Can slow down build
- May require rule adjustments over time
- Learning curve for rule definition

**Neutral:**
- Balance between strictness and flexibility needed
- Regular review of rules importance
- May need exceptions for specific cases