# SuperClaude Subagent Enhancement Plan

## 🎯 Implementation Roadmap

**Phase 1** (Immediate - P0): Core workflow gaps
**Phase 2** (Short-term - P1): Quality & optimization agents
**Phase 3** (Medium-term - P2): Advanced coordination agents
**Phase 4** (Future - P3): Specialized domain agents

---

## 1. Code Intelligence Agents 🔍

### **code-base-researcher** (Existing - P0)

- **Purpose**: Impact analysis for changes and dependency tracking
- **Model**: Sonnet | **Color**: Cyan
- **Tools**: `Read, Grep, Glob, mcp__jetbrains__search_in_files_by_text`
- **Auto-trigger**: Before major changes, refactoring requests
- **Output**: JSON impact analysis with affected files and components
- **Token Efficiency**: Coordinates with pattern-analyzer for consistency

### **refactoring-expert** (Existing - P1)

- **Purpose**: Refactoring proposals with JetBrains MCP analysis
- **Model**: Opus | **Color**: Blue
- **Tools**: `Read, Grep, mcp__jetbrains__get_file_problems, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Technical debt detection, code quality issues
- **Output**: Structured refactoring plan with risk assessment
- **Token Efficiency**: Uses JetBrains MCP vs manual analysis

### **dependency-impact-analyzer** (NEW - P2)

- **Purpose**: Analyze cascading effects of changes across modules
- **Model**: Sonnet | **Color**: Orange
- **Tools**: `Read, Grep, Glob, mcp__jetbrains__search_in_files_by_text, Task`
- **Auto-trigger**: API changes, breaking changes, major refactoring
- **Output**: Dependency graph with breaking change risk scores
- **Token Efficiency**: Delegates to specialized agents for deep analysis

---

## 2. Quality & Testing Agents ✅

### **review-expert** (Existing - P0)

- **Purpose**: Pre-push validation and quality gate compliance
- **Model**: Opus | **Color**: Green
- **Tools**: `Read, Grep, mcp__jetbrains__get_file_problems, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Pre-merge, pre-deploy, quality validation requests
- **Output**: Quality scorecard with CUPID/Clean Code/12-factor compliance
- **Token Efficiency**: Delegates to pattern-analyzer for consistency checks

### **problem-diagnostics-expert** (Existing - P0)

- **Purpose**: Root cause analysis for failing code and performance issues
- **Model**: Sonnet | **Color**: Red
- **Tools**: `Read, Grep, Bash, mcp__jetbrains__get_file_problems, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Error messages, failing tests, debugging requests
- **Output**: Diagnostic report with root causes and fix recommendations
- **Token Efficiency**: Uses JetBrains MCP for IDE-level diagnostics

---

## 3. Documentation & Communication 📚

### **documentation-advisor** (Existing - P1)

- **Purpose**: Documentation quality analysis and brainstorming assistance
- **Model**: Sonnet | **Color**: Purple
- **Tools**: `Read, Grep, mcp__context7__get-library-docs, mcp__sequential__sequentialthinking`
- **Auto-trigger**: API changes, new features, documentation requests, documentation quality reviews
- **Output**: Documentation quality assessment with gaps, recommendations, and improvement strategies
- **Token Efficiency**: Uses Context7 for documentation patterns and Sequential for systematic analysis
- **Approach**: Analyzes what makes good documentation, identifies missing elements, brainstorms improvement strategies (similar to quality-assurance-expert but for documentation)

### **api-design-consultant** (NEW - P2)

- **Purpose**: Research API patterns, discuss contract design, advise on versioning strategies
- **Model**: Sonnet | **Color**: Teal
- **Tools**: `Read, Grep, mcp__context7__get-library-docs, WebSearch`
- **Auto-trigger**: API design discussions, breaking change analysis
- **Output**: API design patterns analysis, versioning strategies, contract recommendations
- **Token Efficiency**: Context7 for API patterns, WebSearch for latest practices
- **Approach**: Discusses API philosophy and best practices without implementing

### **naming-convention-advisor** (NEW - P3)

- **Purpose**: Research naming patterns, suggest consistent conventions, discuss clarity
- **Model**: Haiku | **Color**: Lavender
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking`
- **Auto-trigger**: New file/class/method creation, refactoring discussions
- **Output**: Naming pattern analysis with consistency recommendations
- **Token Efficiency**: Lightweight model for quick naming advice
- **Approach**: Helps discuss what makes names clear and maintainable

---

## 4. Performance & Optimization ⚡

### **prompt-enhancement-specialist** (Existing - P1)

- **Purpose**: Context optimization and prompt refinement for better AI performance
- **Model**: Sonnet | **Color**: Gold
- **Tools**: `Read, mcp__sequential__sequentialthinking, WebFetch`
- **Auto-trigger**: Context >75%, complex operations, iterative workflows
- **Output**: Enhanced prompts with context optimization recommendations
- **Token Efficiency**: 30-50% reduction through intelligent compression

### **performance-strategy-researcher** (NEW - P2)

- **Purpose**: Research optimization approaches, analyze trade-offs, discuss bottlenecks
- **Model**: Sonnet | **Color**: Magenta
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking, WebSearch`
- **Auto-trigger**: Performance discussions, optimization planning
- **Output**: Performance strategy options with trade-off analysis
- **Token Efficiency**: Research-based optimization guidance
- **Approach**: Discusses performance philosophy before implementation

### **caching-strategy-advisor** (NEW - P2)

- **Purpose**: Research caching patterns, discuss invalidation strategies, advise on levels
- **Model**: Sonnet | **Color**: Amber
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Performance optimization, scalability discussions
- **Output**: Caching strategy analysis with pros/cons for each approach
- **Token Efficiency**: Focused caching research without implementation
- **Approach**: Helps decide what/when/how to cache

---

## 5. Learning & Memory 🧠

### **memory-synthesis-agent** (Existing - P0)

- **Purpose**: Retrospective facilitation and knowledge capture
- **Model**: Haiku | **Color**: Pink
- **Tools**: `Read, Write, TodoWrite, Task`
- **Auto-trigger**: Task completion, retrospectives, significant learnings
- **Output**: Structured memory entries with patterns and improvement recommendations
- **Token Efficiency**: Lightweight model builds reusable knowledge base

---

## 6. Security & Compliance 🛡️

### **security-threat-researcher** (NEW - P1)

- **Purpose**: Research threat models, discuss attack vectors, advise on mitigations
- **Model**: Sonnet | **Color**: Crimson
- **Tools**: `Read, Grep, WebSearch, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Security reviews, threat modeling sessions
- **Output**: Threat analysis with mitigation strategies and risk assessments
- **Token Efficiency**: Focused security research without implementation
- **Approach**: Discusses security concerns without implementing fixes

### **compliance-requirements-advisor** (NEW - P2)

- **Purpose**: Research compliance requirements, discuss implications, advise on approaches
- **Model**: Sonnet | **Color**: Navy
- **Tools**: `Read, WebSearch, mcp__context7__get-library-docs`
- **Auto-trigger**: GDPR/regulatory discussions, data handling decisions
- **Output**: Compliance requirements analysis with implementation implications
- **Token Efficiency**: Research-based compliance guidance
- **Approach**: Helps understand what compliance means for the project

---

## 7. Architecture & Design 🏗️

### **architecture-patterns-researcher** (NEW - P1)

- **Purpose**: Research architectural patterns, discuss trade-offs, advise on approaches
- **Model**: Opus | **Color**: Indigo
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking, mcp__context7__get-library-docs`
- **Auto-trigger**: Architecture decisions, pattern discussions, design reviews
- **Output**: Pattern analysis with trade-offs, applicability assessment
- **Token Efficiency**: Deep architectural research with pattern coordination
- **Approach**: Discusses architectural philosophy and pattern fitness

### **database-schema-advisor** (NEW - P2)

- **Purpose**: Research schema patterns, discuss normalization, advise on indexing
- **Model**: Sonnet | **Color**: Emerald
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Database design, schema changes, query optimization
- **Output**: Schema design analysis with performance implications
- **Token Efficiency**: Database-focused research and analysis
- **Approach**: Helps discuss data modeling decisions

### **event-flow-analyst** (NEW - P2)

- **Purpose**: Analyze event flows, research patterns, discuss choreography vs orchestration
- **Model**: Sonnet | **Color**: Coral
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Kafka/event-driven discussions, async pattern decisions
- **Output**: Event flow analysis with pattern recommendations
- **Token Efficiency**: Event-driven architecture research
- **Approach**: Helps understand event-driven implications

---

## 8. Development Workflow 🔄

### **code-review-discussion-facilitator** (NEW - P1)

- **Purpose**: Research review best practices, facilitate discussions, suggest focus areas
- **Model**: Sonnet | **Color**: Olive
- **Tools**: `Read, Grep, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Code review preparation, PR discussions
- **Output**: Review checklist with discussion points and focus areas
- **Token Efficiency**: Structured review guidance
- **Approach**: Facilitates productive review discussions

### **technical-debt-analyst** (NEW - P2)

- **Purpose**: Research debt patterns, analyze impact, discuss prioritization
- **Model**: Sonnet | **Color**: Bronze
- **Tools**: `Read, Grep, mcp__jetbrains__get_file_problems`
- **Auto-trigger**: Refactoring discussions, debt prioritization
- **Output**: Technical debt analysis with impact assessment and prioritization
- **Token Efficiency**: Systematic debt analysis with JetBrains integration
- **Approach**: Helps decide what debt to address when

### **migration-strategy-consultant** (NEW - P3)

- **Purpose**: Research migration patterns, discuss strategies, advise on approaches
- **Model**: Sonnet | **Color**: Copper
- **Tools**: `Read, WebSearch, mcp__sequential__sequentialthinking`
- **Auto-trigger**: Version upgrades, framework migrations, dependency updates
- **Output**: Migration strategy analysis with risk assessment
- **Token Efficiency**: Migration-focused research and risk analysis
- **Approach**: Discusses migration philosophy and approaches

---

## 🎯 Token Optimization Strategy

### Agent Coordination Patterns

- **Primary-Secondary**: Main agent delegates to subagents to reduce token usage of main agent
- **Progressive Enhancement**: Start simple → escalate to complex only when needed
- **Memory Reuse**: Synthesis agent builds reusable knowledge patterns
- **Context Sharing**: Share analysis results across related operations

### Expected Token Savings

- **Agent Coordination**: 30-40% reduction through delegation
- **Pattern Reuse**: 20-30% reduction through memory synthesis
- **Context Optimization**: 25-35% reduction through prompt enhancement
- **Research Delegation**: 40-50% reduction by delegating research to specialized advisors
- **Parallel Research**: Multiple advisors can research simultaneously
- **Knowledge Caching**: Research results reusable across sessions

### Implementation Priority Matrix

```
P0 (Critical - 2 weeks):
code-base-researcher, review-expert, problem-diagnostics-expert, memory-synthesis-agent

P1 (High - 4 weeks):
refactoring-expert, documentation-advisor, prompt-enhancement-specialist,
test-strategy-advisor, security-threat-researcher, architecture-patterns-researcher,
code-review-discussion-facilitator

P2 (Medium - 8 weeks):
dependency-impact-analyzer, api-design-consultant, performance-strategy-researcher,
caching-strategy-advisor, compliance-requirements-advisor, database-schema-advisor,
event-flow-analyst, technical-debt-analyst

P3 (Low - 12+ weeks):
naming-convention-advisor, migration-strategy-consultant
```

---

## 2. Update MCPs

- **Firecrawl**: Enhanced web scraping capabilities for documentation research
