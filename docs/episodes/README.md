# Development Episodes

This directory contains development episode files that capture patterns, problems, and solutions discovered during TDD development cycles.

## Purpose

Development episodes serve as a knowledge repository for:
- **Pattern Discovery**: Reusable code patterns found in the codebase
- **Problem-Solution Pairs**: Challenges encountered and how they were resolved
- **Learning Capture**: Insights about the codebase, frameworks, and domain
- **Anti-pattern Prevention**: Documentation of approaches that didn't work
- **Future Optimization**: Knowledge to make future development more efficient

## Episode Creation Process

Episodes are created during **Phase 3e (MEMORY)** of the workflow-orchestrator TDD cycle:

1. **Mandatory Creation**: Every development task must create an episode file
2. **Timing**: After REFACTOR phase, before COMMIT phase
3. **Blocking Requirement**: Phase 4 validation cannot proceed without episode creation
4. **Template Usage**: Use `TEMPLATE.md` as the starting point for all episodes

## File Naming Convention

```
YYYY-MM-DD-HH-MM-task-{task-id}-episode.md
```

**Examples**:
- `2025-01-15-14-30-task-123-episode.md`
- `2025-01-15-16-45-task-124-episode.md`

## Required Sections

Every episode must include:

1. **Episode Metadata**: Date, task ID, developer, duration, TDD phase
2. **Context**: Task description, requirements, initial state
3. **Problem**: Challenges encountered, blockers, assumptions
4. **Solution**: Approach taken, implementation details, trade-offs
5. **Patterns Discovered**: New patterns found, reusable approaches
6. **Lessons Learned**: Key insights, future optimization opportunities
7. **Anti-patterns to Avoid**: What didn't work, pitfalls to prevent
8. **References**: Related episodes, documentation, code references

## Integration with Workflow

### Workflow-Orchestrator Integration
- Episodes are created as part of the systematic TDD workflow
- Phase 3e (MEMORY) is blocked until episode creation is complete
- Quality gates validate episode existence before task completion

### Knowledge Management
- Episodes enable pattern analysis across development cycles
- Support architectural decision making through historical context
- Facilitate knowledge transfer between team members
- Improve development velocity through captured learnings

## Usage Guidelines

### For Developers
1. **Complete All Sections**: Don't skip sections, even if they seem minimal
2. **Be Specific**: Use concrete examples and code references
3. **Focus on Reusability**: Capture knowledge that will benefit future development
4. **Link Related Content**: Reference other episodes, ADRs, and documentation

### For Pattern Analysis
1. **Search Episodes**: Look for similar patterns before implementing new solutions
2. **Extract Conventions**: Identify common approaches across multiple episodes
3. **Build on Learnings**: Use previous episode insights to inform current development

### For Architecture Decisions
1. **Historical Context**: Review episodes for background on previous decisions
2. **Pattern Evolution**: Track how patterns have evolved over time
3. **Decision Validation**: Use episode learnings to validate new architectural choices

## Directory Structure

```
docs/episodes/
├── README.md                                    # This file
├── TEMPLATE.md                                  # Episode template
├── 2025-01-15-14-30-task-123-episode.md       # Individual episodes
├── 2025-01-15-16-45-task-124-episode.md       # ...
└── ...
```

## Quality Standards

Episodes should maintain the same quality standards as other documentation:
- Clear, concise writing
- Accurate technical details
- Proper formatting and structure
- Relevant code references with file:line notation
- Links to related documentation and episodes

## Automation

Episode creation is enforced by the workflow-orchestrator agent:
- **Phase 3e Blocking**: Development cannot proceed without episode creation
- **Template Enforcement**: All required sections must be present
- **Quality Validation**: Episodes are validated during Phase 4 reviews

This systematic approach ensures consistent knowledge capture and enables the development team to build on previous learnings effectively.