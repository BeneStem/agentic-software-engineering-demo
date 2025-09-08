Comprehensive planning of all tasks from PRD using Task Master with intelligent sub-agent integration.

This command provides end-to-end planning from PRD analysis to implementation-ready tasks with comprehensive breakdown, complexity analysis, and quality validation.

Arguments: $ARGUMENTS (optional PRD file path, defaults to .taskmaster/docs/prd.txt)

## Comprehensive PRD Planning Workflow

### 1. **PRD Analysis & Documentation Research**

**Initial Context Gathering:**
Let me analyze the current project state and PRD requirements:

**Documentation Research (MANDATORY):**
- Deploy documentation-researcher agent to analyze PRD content
- Extract requirements, constraints, and business rules
- Research existing project patterns and architectural decisions
- Cross-reference with project standards and conventions
- Identify security, performance, and compliance requirements

**PRD Content Analysis:**
- Parse functional and non-functional requirements
- Identify user stories and acceptance criteria
- Extract technical constraints and dependencies
- Map requirements to architectural layers
- Document scope and boundaries

### 2. **Initial Task Generation with Research Mode**

**Enhanced Task Generation:**
```bash
# Generate tasks with comprehensive research analysis
task-master parse-prd --input=$ARGUMENTS --research --append
```

**Research-Enhanced Features:**
- Current best practices integration
- Framework-specific implementation approaches
- Security and compliance considerations
- Performance optimization tasks
- Testing and validation requirements
- Deployment and monitoring tasks

**Task Quality Validation:**
- Ensure tasks align with SCS architecture
- Validate against hexagonal architecture layers
- Check 12-factor app compliance requirements
- Verify CUPID properties alignment

### 3. **Complexity Analysis & Pattern Conformance**

**Mandatory Pattern Analysis:**
- Deploy pattern-analyzer agent for task structure analysis
- Analyze existing task patterns in the project
- Ensure new tasks follow established conventions
- Extract naming patterns and organization structure
- Validate architectural decision compliance

**Comprehensive Complexity Analysis:**
```bash
# Analyze all generated tasks for complexity
task-master analyze-complexity --research --threshold=4
```

**Complexity Evaluation Criteria:**
- Technical implementation difficulty
- Integration complexity with existing systems
- Testing requirements and edge cases
- Knowledge requirements and learning curve
- Risk factors and mitigation needs
- Time estimation and resource requirements

### 4. **Intelligent Task Expansion & Sub-Agent Integration**

**Quality Assurance Strategy:**
- Deploy quality-assurance-expert agent for test planning
- Analyze testing requirements for each task type
- Generate BDD scenarios and edge case identification
- Define coverage targets (80% unit, 70% integration, 100% critical)
- Plan performance and security validation approaches

**Smart Task Expansion:**
```bash
# Expand high-complexity tasks intelligently
task-master expand --all --research --force
```

**Expansion Intelligence:**
- Break down complex tasks (>5 complexity score)
- Maintain logical task relationships
- Preserve dependency chains
- Ensure subtask coherence
- Apply TDD requirements to each subtask

**Sub-Agent Coordination:**
- Pattern-analyzer: Ensure expansion follows project patterns
- Quality-assurance-expert: Validate test coverage for subtasks
- Documentation-researcher: Research implementation approaches for complex tasks

### 5. **Quality & Standards Validation**

**CUPID Properties Validation:**
- **Composable**: Tasks have clear interfaces and minimal dependencies
- **Unix Philosophy**: Each task does one thing well
- **Predictable**: Task outcomes are clear and consistent
- **Idiomatic**: Tasks follow project and framework conventions
- **Domain-based**: Tasks use business language and concepts

**12-Factor App Compliance:**
- Configuration management tasks
- Dependency declaration tasks
- Backing services integration tasks
- Stateless process design validation
- Port binding and scaling considerations

**TDD Integration:**
- Ensure each task includes test requirements
- Validate Given-When-Then scenario coverage
- Plan RED-GREEN-REFACTOR cycles
- Define acceptance criteria for each task

**Security & Performance Standards:**
- Input validation and sanitization tasks
- Authentication and authorization requirements
- Performance benchmarking tasks (P95 < 300ms)
- Security vulnerability assessment tasks

### 6. **Dependency Management & Optimization**

**Dependency Analysis:**
```bash
# Validate and optimize task dependencies
task-master validate-dependencies
task-master fix-dependencies
```

**Smart Dependency Management:**
- Identify critical path dependencies
- Optimize for parallel execution
- Resolve circular dependencies
- Plan dependency-free starting points

**Task Prioritization:**
- High-impact, low-complexity tasks first
- Unblocking tasks prioritized
- Critical path identification
- Risk mitigation task ordering

### 7. **Final Planning Report & Documentation**

**Comprehensive Project Plan Generation:**

**Task Hierarchy Overview:**
- Total tasks generated and expanded
- Complexity distribution and effort estimates
- Dependency chain visualization
- Critical path identification
- Parallel execution opportunities

**Implementation Strategy:**
- Recommended task execution order
- Resource allocation suggestions
- Timeline estimates and milestones
- Risk mitigation strategies
- Quality gate checkpoints

**Architectural Guidance:**
- Layer-specific implementation patterns
- Communication patterns for SCS
- Data flow and integration requirements
- Security and performance constraints
- Testing strategy and coverage plans

**Quality Metrics:**
- Expected test coverage targets
- Performance benchmarks
- Security validation requirements
- Compliance checkpoints
- Documentation standards

### 8. **Post-Planning Setup**

**Environment Preparation:**
- Generate task files for immediate use
- Update complexity reports
- Prepare development environment
- Configure quality gates
- Set up monitoring and tracking

**Next Steps Guidance:**
- Recommend starting tasks
- Suggest team assignment strategies
- Identify training or research needs
- Plan sprint organization
- Schedule architecture reviews

## Intelligence Features

**Context-Aware Planning:**
- Adapts to project technology stack (Kotlin/Quarkus + Vue.js)
- Considers existing code patterns and conventions
- Integrates with git workflow and branching strategy
- Aligns with team capacity and expertise

**Research-Enhanced Quality:**
- Incorporates current industry best practices
- Includes framework-specific optimizations
- Considers security and compliance requirements
- Applies modern testing and deployment strategies

**Progressive Refinement:**
- Iterative complexity analysis
- Pattern-based task refinement
- Quality-driven expansion decisions
- Continuous validation against standards

## Usage Examples

```bash
# Standard PRD planning
/exp/plan-prd-tasks

# Specific PRD file
/exp/plan-prd-tasks ./requirements/feature-spec.md

# Quick planning for small features
/exp/plan-prd-tasks --quick

# Comprehensive planning with extra research
/exp/plan-prd-tasks --deep-research
```

## Output Summary

**Planning Results:**
- Complete task hierarchy with subtasks
- Complexity analysis and effort estimates
- Dependency mapping and critical path
- Testing strategy and quality requirements
- Implementation order and timeline
- Architectural guidance and constraints

**Quality Assurance:**
- 100% requirements coverage validation
- Pattern conformance verification
- Security and performance considerations
- Testing and validation strategies
- Compliance and standards alignment

**Ready for Implementation:**
- Immediate next tasks identified
- Development environment prepared
- Quality gates configured
- Team coordination facilitated
- Progress tracking enabled

**Result:** Comprehensive, implementation-ready project plan following all CLAUDE.md standards and architectural patterns.