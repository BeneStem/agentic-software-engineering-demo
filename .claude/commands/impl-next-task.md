Implement the next available task using mandatory TDD cycle with sub-agent integration.

This command follows CLAUDE.md's core directives for Test-Driven Development and mandatory sub-agent usage to ensure 100/100 quality standards.

## TDD Implementation Workflow

### 1. **Task Selection & Context Loading**

**Get Next Available Task:**
```bash
# Intelligent task selection based on:
# - Dependencies resolved
# - Priority levels  
# - Current capacity
```

Let me analyze the current project state and select the optimal next task:

- Check task queue with Task Master MCP
- Evaluate task dependencies and blockers
- Load complete task context including:
  - Task description and acceptance criteria
  - Dependencies and related tasks
  - Implementation requirements
  - Test strategy needs

**Set Task In Progress:**
```bash
# Mark selected task as in-progress immediately
# Update Task Master with implementation start time
# Log task selection reasoning
```

### 2. **Mandatory Sub-Agent Integration**

Following CLAUDE.md requirements, automatically deploy specialized agents:

**Pattern Analysis (MANDATORY for new code):**
- Deploy pattern-analyzer agent BEFORE any implementation
- Analyze existing codebase for e3 similar implementations
- Extract naming conventions and architectural patterns
- Generate conformance guidelines
- Document rationale for established patterns

**Quality Assurance (MANDATORY for tests):**
- Deploy quality-assurance-expert agent BEFORE writing tests
- Analyze test requirements and edge cases
- Generate BDD scenarios (Given-When-Then format)
- Define coverage targets and validation criteria
- Provide mocking and test data strategies

**Documentation Research (CONDITIONAL):**
- Deploy documentation-researcher if task involves:
  - API specifications or integrations
  - Architectural decisions
  - Framework patterns or setup guides
  - External system interactions

### 3. **TDD Cycle Implementation**

**MANDATORY TDD WORKFLOW: RED � GREEN � REFACTOR**

#### Phase 1: RED (Failing Test)
```
=4 RED PHASE: Write Failing Test
```

1. **Test Strategy Analysis**
   - Use quality-assurance-expert recommendations
   - Design BDD scenarios for business logic
   - Identify edge cases and boundary conditions
   - Plan test data and mocking strategy

2. **Write Failing Test**
   - Implement Given-When-Then scenarios
   - Focus on business behavior, not implementation details
   - Ensure test fails for the RIGHT reasons
   - Verify test failure before proceeding

3. **Test Validation**
   - Run test to confirm it fails correctly
   - Check error messages are meaningful
   - Ensure no false positives
   - **RULE: DON'T STOP until test fails correctly**

#### Phase 2: GREEN (Minimal Implementation)
```
=� GREEN PHASE: Make Test Pass
```

1. **Implementation Planning**
   - Use pattern-analyzer guidelines for structure
   - Follow established naming conventions
   - Respect architectural boundaries
   - Plan minimal code to pass test

2. **Code Implementation**
   - Write simplest code that passes test
   - Follow CUPID properties (Composable, Unix Philosophy, Predictable, Idiomatic, Domain-based)
   - Maintain clean code principles
   - Respect 12-factor app compliance

3. **Test Validation**
   - Run test to confirm it passes
   - Verify no other tests broken
   - Check implementation meets acceptance criteria
   - **RULE: DON'T STOP until test passes**

#### Phase 3: REFACTOR (Clean & Optimize)
```
= REFACTOR PHASE: Improve Code Quality
```

1. **Code Quality Analysis**
   - Check CUPID property compliance
   - Validate clean code principles
   - Ensure proper separation of concerns
   - Review naming and structure clarity

2. **Refactoring Operations**
   - Extract methods for clarity
   - Remove duplication
   - Improve naming and structure
   - Maintain test coverage during changes

3. **Quality Validation**
   - Re-run all tests
   - Check lint and type errors
   - Validate performance metrics
   - **RULE: DON'T STOP until 100/100 quality achieved**

### 4. **Quality Gates & Validation**

**Local Quality Gates:**
- Unit tests passing (target: 80% coverage)
- Lint checks clean
- Type checking successful
- Build process successful
- CUPID properties validated

**Integration Validation:**
- Component integration tests
- API contract validation
- Database migration tests (if applicable)
- Architecture compliance checks

**Performance & Security:**
- Response time validation (P95 < 300ms)
- Memory usage checks
- Security vulnerability scans
- Input validation tests

### 5. **Progress Documentation**

**Task Updates:**
- Log implementation approach and decisions
- Document any deviations from patterns
- Record blockers and resolutions
- Update estimated completion time

**Knowledge Capture:**
- Save reusable patterns discovered
- Document lessons learned
- Update architectural decisions
- Capture performance insights

### 6. **Completion Protocol**

**Pre-Completion Validation:**
- All tests passing (unit, integration, E2E if applicable)
- Quality score = 100/100
- No security vulnerabilities
- Performance criteria met
- Documentation updated

**Task Status Update:**
- Mark task as `review` (NOT `done`)
- Generate descriptive completion notes
- Link to related tasks/PRs
- Schedule follow-up if needed

**MANDATORY Commit Protocol:**
- **ALWAYS commit immediately after task completion**
- Stage all changes: `git add .`
- Generate conventional commit with task ID and summary
- Use CLAUDE.md commit format with Co-Authored-By attribution
- **RULE: DO NOT mark task complete without committing first**

**Next Steps:**
- Show next available task
- Prepare for code review if needed
