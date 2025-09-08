# 13. Structured JSON Logging

## Status

Accepted

## Context

The distributed system needs comprehensive logging for debugging, monitoring, and compliance. Requirements include:
- Machine-readable logs for automated analysis
- Correlation across distributed requests
- Consistent log format across services
- Integration with log aggregation tools
- Queryable logs for troubleshooting
- Performance metrics extraction

Traditional text logs are difficult to parse and analyze programmatically. Structured logging enables better observability.

## Decision

We will implement structured JSON logging:
- All logs output in JSON format
- Quarkus Logging JSON for backend
- Pino for Node.js SSR server
- Standard fields: timestamp, level, service, correlation_id, message
- Additional context fields as needed
- No sensitive data in logs (GDPR compliance)
- Log levels: ERROR, WARN, INFO, DEBUG

## Consequences

**Positive:**
- Machine-readable logs enable automated analysis
- Easy integration with log aggregation tools
- Powerful querying and filtering capabilities
- Correlation IDs enable distributed tracing
- Consistent format across all services
- Metrics can be extracted from logs

**Negative:**
- Less human-readable in raw format
- Larger log volume due to JSON structure
- Requires log viewer for local development
- Careful attention needed to avoid logging sensitive data
- Performance impact of serialization

**Neutral:**
- Different logging patterns from traditional approaches
- Need for log retention policies
- Monitoring storage costs important