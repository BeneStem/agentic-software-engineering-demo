# Development Guide: Finden Self-Contained System

This guide outlines the development standards, coding conventions, and contribution guidelines for the Finden Self-Contained System - a complete product search service built with Vue.js frontend and Quarkus/Kotlin backend.

## Table of Contents

- [Self-Contained Systems Architecture](#self-contained-systems-architecture)
- [Technology Stack](#technology-stack)
- [Architecture and Domain Design](#architecture-and-domain-design)
- [Frontend Technology Stack](#frontend-technology-stack)
- [Frontend Development Guidelines](#frontend-development-guidelines)
- [Frontend-Backend Integration](#frontend-backend-integration)
- [Frontend Testing Strategy](#frontend-testing-strategy)
- [Frontend Build & Deployment](#frontend-build--deployment)
- [Frontend Performance & Security](#frontend-performance--security)
- [Performance Standards](#performance-standards)
- [Kotlin Development Guidelines](#kotlin-development-guidelines)
- [Testing Strategy](#testing-strategy)
- [Code Quality Standards](#code-quality-standards)
- [Development Process](#development-process)
- [Context Engineering & Task Management](#context-engineering--task-management)
- [Task Master Integration](#task-master-integration)
- [AI Development Workflow](#ai-development-workflow)

## Self-Contained Systems Architecture

### What is a Self-Contained System?

The Finden application is designed as a **Self-Contained System (SCS)** - an architectural approach that provides a middle ground between monolithic and microservices architectures. An SCS is an autonomous unit that includes its own web interface, business logic, and database, designed to handle a specific business capability end-to-end.

### Self-Contained Systems vs Microservices

#### Key Architectural Differences

| Aspect | Self-Contained Systems | Microservices |
|--------|----------------------|---------------|
| **Size & Scope** | Larger, domain-focused systems (5-25 per organization) | Smaller, fine-grained services (100s per organization) |
| **User Interface** | Includes own web UI and complete user experience | Business logic only, separate UI layer |
| **Communication** | Minimal or asynchronous communication preferred | Complex synchronous/asynchronous communication patterns |
| **Team Ownership** | One team owns complete system end-to-end | Multiple teams coordinate across service boundaries |
| **Deployment** | Independent deployment of complete business capability | Coordination required across multiple services |
| **Database** | Dedicated database per SCS | Shared databases or complex data consistency patterns |

#### Benefits of SCS Architecture (2024 Perspective)

1. **Reduced Operational Complexity**: Eliminates coordination overhead between multiple microservices
2. **Team Autonomy**: Single team can implement features without cross-team dependencies
3. **Faster Deployment**: Complete business features deployed as single units
4. **End-to-End Ownership**: Teams responsible for full user experience, not just APIs
5. **Simplified Communication**: Minimal inter-service communication reduces network complexity

### Why Finden Qualifies as a Self-Contained System

The Finden system exhibits all characteristics of a well-designed SCS:

#### ✅ **Complete Business Domain**
- **Single Purpose**: Product search and listing functionality
- **Business Autonomy**: Complete e-commerce product discovery capability
- **Domain Boundaries**: Clear separation from other business domains (checkout, inventory, etc.)

#### ✅ **Full Technology Stack**
- **Frontend**: Vue.js 3.2.37 with SSR for complete user interface
- **Backend**: Quarkus/Kotlin application layer with business logic
- **Database**: Dedicated MongoDB instance for product data
- **Infrastructure**: Independent deployment and scaling capabilities

#### ✅ **Autonomous Operations**
- **Independent Deployment**: Frontend and backend can be deployed without external dependencies
- **Self-Contained Data**: Product catalog and search indexes maintained independently
- **Minimal External Communication**: API calls limited to essential integrations only

#### ✅ **Team Ownership Model**
- **Full-Stack Responsibility**: Single team can own frontend, backend, and database
- **Feature Delivery**: New product search features implemented entirely within team boundaries
- **Technology Decisions**: Independent choice of Vue.js, TypeScript, Quarkus, Kotlin, MongoDB

### SCS Implementation Principles (MANDATORY)

#### 1. **UI Ownership**
- Each SCS MUST include its own web interface
- NO shared UI components between SCS boundaries
- Changes contained within single SCS reduce coordination

#### 2. **Data Autonomy**
- Dedicated database per SCS
- NO direct database access between systems
- Data consistency handled within SCS boundaries

#### 3. **Communication Boundaries**
- Prefer asynchronous communication between SCS
- Minimize synchronous API calls
- Event-driven integration where possible

#### 4. **Deployment Independence**
- Each SCS deployed as complete unit
- NO deployment coordination required
- Independent scaling and versioning

### Integration with Development Workflow

The SCS architecture directly influences development practices:

- **Feature Development**: Complete features implemented within single system
- **Testing Strategy**: End-to-end testing within SCS boundaries
- **Security Model**: Authentication and authorization handled per SCS
- **Performance Optimization**: Full-stack performance monitoring and optimization
- **Quality Gates**: Complete SCS must pass all quality standards before deployment

This architectural approach enables the development practices outlined in subsequent sections while maintaining system modularity and team autonomy.

## Technology Stack

### Core Technologies

- **Language:** Kotlin 2.2.0 (kotlin-stdlib-jdk8:2.2.0 explicitly pinned, targeting JVM 22)
- **Framework:** Quarkus 3.24.3 with BOM platform enforcement
- **Build Tool:** Gradle 8.x with Kotlin DSL
- **Database:** MongoDB with Panache Kotlin
- **Dependency Injection:** Quarkus CDI (not Spring)
- **Messaging:** Kafka with Avro serialization (Confluent 8.0.0)

### Key Dependencies

#### Core Framework & Language
- **Domain Modeling:** jMolecules DDD 1.10.0
- **Database:** MongoDB Panache Kotlin
- **Dependency Injection:** Quarkus CDI (Arc)
- **Serialization:** Jackson with Kotlin module 2.13.3

#### Testing & Quality Assurance
- **Testing:** JUnit 5, Mockk 1.14.4, Strikt 0.35.1, TestContainers 1.21.3
- **Validation:** Hibernate Validator
- **Architecture Testing:** ArchUnit 1.4.1
- **Build Plugins:** Quarkus Plugin 3.24.3, Detekt 1.23.8, Avro Plugin 1.9.1

#### Monitoring & Observability
- **Monitoring:** Micrometer with Stackdriver 3.3.1, Prometheus
- **Logging:** Kotlin Logging 3.0.5, JSON Logging 3.1.0
- **Error Tracking:** Sentry 2.5.4.Final for centralized error monitoring

#### Security & Validation
- **Security:** OWASP HTML Sanitizer 20240325.1, Dependency Check 12.1.3, Elytron Security
- **Input Sanitization:** Unbescape 1.1.6.RELEASE for additional string processing

#### Messaging & Integration
- **Reactive:** Smallrye Fault Tolerance, Reactive Messaging
- **Kafka:** Confluent Kafka Avro Serializer 8.0.0, Apache Avro 1.11.0
- **Feature Flags:** Unleash Client 4.4.1

#### Utility Libraries
- **File Operations:** FileSeek 0.1.3 for file processing utilities
- **Text Processing:** Slugify 3.0.7 for URL-friendly string generation
- **Date/Time Testing:** Jackson Datatype JSR310 for test serialization

### Version Requirements

- **Java Runtime:** JVM 22+ (currently targeting 22)
- **Kotlin:** 2.2.0+ (prefer latest patch releases)
- **Gradle:** 8.x+ with Kotlin DSL

## Frontend Technology Stack

### Core Frontend Technologies

- **Language:** TypeScript 4.7.3 (strict type checking enabled)
- **Framework:** Vue.js 3.2.37 with Composition API
- **Router:** Vue Router 4.0.16 for single-page application navigation
- **State Management:** Vuex 4.0.2 for centralized application state
- **Server-Side Rendering:** Fastify 4.2.0 with Vue Server Renderer
- **Build Tool:** Vue CLI 5.0.4 with Webpack 5.x
- **HTTP Client:** Axios 0.27.2 for backend API communication

### Frontend Dependencies

#### Core Framework & UI
- **Vue.js Ecosystem:** Vue 3.2.37, Vue Router 4.0.16, Vuex 4.0.2
- **UI Utilities:** @vueuse/core 8.6.0 for Vue composition utilities
- **Design System:** @blume2000/design-system for consistent UI components
- **Date Handling:** date-fns 2.28.0 for date manipulation and formatting

#### SSR Server & Infrastructure
- **Web Server:** Fastify 4.2.0 with static file serving (@fastify/static 6.4.0)
- **Environment Config:** @fastify/env 4.0.0 for configuration management
- **Process Management:** nodemon 2.0.16 for development server automation
- **Serialization:** serialize-javascript 6.0.0 for SSR data transfer

#### Monitoring & Observability
- **Telemetry:** @opentelemetry/api 1.0.4 for distributed tracing
- **Cloud Monitoring:** @google-cloud/opentelemetry-cloud-monitoring-exporter 0.14.0
- **Metrics:** @opentelemetry/sdk-metrics-base 0.27.0 for application metrics
- **Logging:** Pino 8.0.0 for structured JSON logging
- **Performance:** fastify-metrics 9.0.0 for server performance monitoring

#### Development & Build Tools
- **TypeScript:** TypeScript 4.7.3 with strict configuration
- **Linting:** ESLint 8.18.0 with Vue.js and Prettier integration
- **Styling:** Sass 1.52.3 with stylelint 14.9.1 for CSS quality
- **Testing:** Jest 28.1.1 with Vue Test Utils and TypeScript support
- **Build Optimization:** webpack-bundle-analyzer for bundle size analysis

### Frontend Architecture Overview

The frontend follows a well-organized structure with clear separation of concerns:

**Main Application Code** (`app/` directory):
- Main product listing application and administrative tool application as separate Vue.js apps
- HTTP client and API layer for backend communication
- Reusable Vue.js components organized by functionality
- Vue 3 composition functions for business logic reuse
- TypeScript data models matching backend domain objects
- Vuex state management with modular store organization
- Vue Router configuration for single-page application navigation
- Page-level Vue components for major application views

**SSR Server** (`ssrServer/` directory):
- Fastify server implementation for server-side rendering
- Request handlers and controllers for SSR endpoints
- Server utilities including logging and monitoring integration
- Helper functions for server-side operations

**Application Entry Points**:
- Client-side application entry for browser execution
- Server-side rendering entry for SSR execution
- Separate tool application entry for administrative interface

### Multi-Application Architecture

The frontend supports multiple applications within the same codebase:

#### 1. **Produktliste Application**
- **Purpose:** Main product search and listing interface for end users
- **Entry Points:** Separate client-side (SPA) and server-side (SSR) entry points
- **Components:** Product grids, filters, search functionality, mini product displays
- **Build Target:** Environment variable controlled build process for produktliste application

#### 2. **Tool Application**
- **Purpose:** Administrative interface for product management
- **Entry Point:** Dedicated tool application entry point
- **Build Target:** Environment variable controlled build process for tool application

#### 3. **SSR Server**
- **Purpose:** Server-side rendering for SEO and performance optimization
- **Technology:** Fastify server with Vue Server Renderer integration
- **Features:** Health checks, monitoring, static file serving, API proxying
- **Deployment:** Node.js production server with compiled TypeScript

### TypeScript Integration

#### Strict Type Safety Configuration
The frontend enforces strict TypeScript configuration with the following requirements:
- Strict mode enabled for all compilation options
- No implicit any types allowed
- Strict null checks enforced
- No implicit returns required for functions
- Full type safety across the entire frontend codebase

#### Frontend Type Definitions
- **Component Props:** Strict typing required for all Vue component properties
- **API Responses:** Type-safe HTTP client with backend data models
- **State Management:** Typed Vuex modules and actions with proper interfaces
- **Composables:** Type-safe composition functions with proper return types

### Build & Development Configuration

#### Development Mode
**Development server with hot reload:**
- Start development server for produktliste application with live reload
- Watch mode for SSR server with TypeScript compilation
- Nodemon auto-restart for server development

#### Production Build
**Multi-target production build process:**
- Comprehensive build process for all applications (client, server, tool, and SSR server)
- Individual build targets for SPA bundle, SSR bundle, tool application, and SSR server compilation
- Environment-specific optimization and bundling

### Version Requirements

- **Node.js:** 16.x+ (recommended: 18.x+ for optimal performance)
- **Vue.js:** 3.2.37+ (Composition API required)
- **TypeScript:** 4.7.3+ (strict mode enabled)
- **Vue CLI:** 5.0.4+ with Webpack 5.x support

### Frontend Performance Monitoring

#### OpenTelemetry Integration
- **Distributed Tracing:** Full request tracking from frontend through backend
- **Custom Metrics:** Application-specific performance measurements
- **Cloud Monitoring:** Google Cloud integration for production observability
- **Real User Monitoring:** Client-side performance tracking

#### Performance Standards
- **Bundle Size:** < 1MB gzipped for main application bundle
- **First Paint:** < 1.5s on 3G networks
- **Lighthouse Score:** > 90 for Performance, Accessibility, Best Practices
- **SSR Response Time:** < 200ms for server-side rendered pages

## B2K Platform Configuration

### Core Configuration Block (MANDATORY)

The application requires a comprehensive b2k configuration block for platform integration:

**B2K platform configuration requirements:**
- Configure service host endpoint for internal service discovery
- Set deployment zone identifier for service mesh routing
- Define team key for monitoring and access control
- Configure GCP project settings for cloud resource access
- Use environment variables for different deployment environments

### Configuration Properties

#### Host Configuration
- **Purpose**: Defines the service endpoint for internal service discovery
- **Default**: `http://localhost:8081` for local development
- **Production**: Set via `SERVICE_HOST` environment variable

#### Zone & Team Configuration
- **Zone**: Deployment zone identifier for service mesh routing
- **Team Key**: Team identification for monitoring and access control
- **Usage**: Internal platform coordination and resource allocation

#### GCP Project Integration
- **Project Name**: GCP project identifier for cloud resource access
- **Environment**: Deployment environment (dev, staging, prod)
- **Integration**: Used by Stackdriver monitoring and logging

### Stackdriver Integration

The b2k configuration integrates directly with monitoring:

**Stackdriver integration requirements:**
- Enable Stackdriver monitoring with environment variable control
- Reference b2k GCP project configuration for proper integration
- Configure resource type for metrics categorization

### Environment-Specific Overrides

Different environments require specific b2k configurations:

**Development environment configuration:**
- Configure local host settings for development
- Use local zone identifier for development routing
- Set development-specific GCP project environment

**Test environment configuration:**
- Enable custom TestContainer integration for testing
- Configure test-specific GCP project environment
- Support integration testing with proper container management

### Required Environment Variables

For production deployment, ensure these environment variables are set:

**Required production environment variables:**
- SERVICE_HOST: Production service endpoint URL
- DEPLOYMENT_ZONE: Production deployment zone identifier
- TEAM_KEY: Production team identification key
- GCP_PROJECT: Production GCP project identifier
- ENVIRONMENT: Environment stage indicator (prod)
- STACKDRIVER_ENABLED: Monitoring enablement flag

### Validation & Troubleshooting

Common configuration issues:

1. **Missing GCP Project**: Stackdriver metrics fail if `b2k.gcp-project.name` is undefined
2. **Invalid Zone**: Service mesh routing issues if zone doesn't match deployment
3. **Team Key Mismatch**: Access control failures with incorrect team identification

## Environment Profiles Configuration

### Profile-Based Configuration Strategy

The application uses Quarkus profile-based configuration to handle different deployment environments with specific requirements for each stage.

### Development Profile (`%dev`)

Development environment configuration for local development:

Development profile configuration includes environment-specific application naming, disabled DevServices for explicit control, direct local MongoDB connections, and local B2K platform settings.

#### Development Profile Features:
- **Local MongoDB**: Direct connection to local MongoDB instance
- **DevServices Disabled**: Explicit MongoDB configuration instead of auto-provisioned containers
- **Local B2K Config**: Development-specific platform settings
- **Application Naming**: `local-finden-backend` for environment identification

### Test Profile (`%test`)

Test environment configuration for automated testing:

Test profile configuration enables custom TestContainer integration, provides test-specific application identity with distinct naming and versioning, maintains explicit container control, and uses a separate test database for isolation.

#### Test Profile Features:
- **TestContainer Integration**: Custom b2k TestContainer management
- **Isolated Database**: Separate `finden-test` database for test isolation
- **Test Application Identity**: Distinct naming and versioning for tests
- **Explicit Container Control**: Custom TestContainer lifecycle management

### Production Profile (Default)

Production configuration uses base configuration with environment variables:

Production configuration uses environment variables for all sensitive values including service host, deployment zone, team keys, GCP project settings, MongoDB connections, and Stackdriver monitoring integration.

### Profile Activation

#### Local Development
Development profile activates automatically in Quarkus development mode or can be explicitly specified using Gradle properties.

#### Testing
Test profile activates automatically during test execution or can be explicitly specified using test-specific Gradle properties.

#### Production Deployment
**Production deployment:**
- Run Quarkus application JAR with default profile using environment variables
- Optionally specify explicit production profile using system properties

### Configuration Best Practices

#### Environment Variable Strategy
- **Development**: Minimal environment variables, sensible defaults
- **Testing**: Isolated resources, deterministic configuration
- **Production**: All sensitive values via environment variables

#### Application Naming Convention
- **local-finden-backend**: Development environment
- **test-finden-backend**: Test environment
- **finden-backend**: Production environment

#### Database Isolation
- **Development**: Local MongoDB instance
- **Testing**: Separate `finden-test` database
- **Production**: Dedicated production MongoDB cluster

### Troubleshooting Profile Issues

#### Common Profile Problems:
1. **Wrong Profile Active**: Check `quarkus.profile` property
2. **Missing Environment Variables**: Verify production environment setup
3. **TestContainer Issues**: Ensure Docker daemon running for test profile
4. **Database Connection**: Verify MongoDB connectivity per environment

#### Profile Verification:
**Profile verification:**
- Check active profile using application info endpoint
- Verify application name and profile configuration through HTTP requests

## Architecture and Domain Design

### Onion Architecture Implementation

The application follows **Domain-Driven Design (DDD)** with **Onion/Hexagonal Architecture**:

**Onion Architecture package structure:**
- **Domain layer (core):** Contains model entities, value objects, aggregates, domain services, repository interfaces, and domain exceptions
- **Application layer:** Contains application services implementing use cases
- **Adapter layer (outer):** Contains active adapters (REST controllers, message consumers) and passive adapters (database implementations, external service clients)
- **Dependencies:** Domain has zero outward dependencies, application depends only on domain, adapters implement domain interfaces

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

## Frontend Development Guidelines

### Vue.js 3 Development Standards

#### Composition API (MANDATORY)
**All new components MUST use the Composition API with script setup syntax:**
- Import reactive functions (ref, computed, onMounted) from Vue
- Use Vuex store integration with useStore composable
- Define clear TypeScript interfaces for component data structures
- Implement reactive references for component state
- Use computed properties for derived state
- Implement lifecycle hooks for component initialization

**FORBIDDEN practices:**
- Options API for new components (data, methods, computed properties)
- Legacy Vue 2 patterns and syntax
- Mixing Options API and Composition API within same component

#### TypeScript Integration (MANDATORY)
**Strict typing requirements for all components:**
- Define clear Props interfaces with optional properties marked explicitly
- Define Emits interfaces with proper event signatures
- Use withDefaults for component props with default values
- Implement type-safe event emission with proper typing
- Ensure all component interactions are type-checked

**FORBIDDEN practices:**
- Untyped props using array syntax
- Untyped event emissions
- Any type usage without justification
- Bypassing TypeScript type checking

### Component Architecture (MANDATORY)

#### Component Hierarchy Standards
**Organize components in clear functional categories:**
- **Base components:** Reusable UI components (buttons, inputs, form elements)
- **Layout components:** Page structure components (headers, navigation, footers)
- **Business components:** Domain-specific components (Produktkachel for single product display, Produktliste for product grid/list, Produktfilter for filtering interface)
- **Utility components:** Cross-cutting components (loading states, error handling)

#### Props and Events Pattern
**Component interface design requirements:**
- Define clear Props interfaces with readonly data inputs, configuration options, and optional state properties
- Define Emits interfaces with specific event signatures for user actions and state changes
- Separate data input, configuration, and state concerns in prop definitions
- Use specific event types rather than generic handlers

**FORBIDDEN practices:**
- Mixing data and configuration in single prop objects
- Passing functions as props instead of using events
- Generic or unclear prop and event naming

#### Single Responsibility Components
**Each component MUST focus on a single concern:**
- Components should handle only presentation logic for their specific domain
- Avoid combining multiple responsibilities (display, filtering, API calls) in one component
- Use composition to combine multiple single-purpose components
- Maintain clear separation between data, logic, and presentation layers

### Composables & Business Logic (MANDATORY)

#### Composables Pattern
**Implement reusable business logic in composables following these requirements:**
- Create composables in dedicated files with clear naming (useProductFilter, useProductTracking, etc.)
- Define reactive state using ref with proper TypeScript interfaces
- Implement logic functions for state manipulation (applyFilter, clearFilter)
- Return readonly reactive state and manipulation functions
- Use proper component import and destructuring patterns
- Ensure composables are testable and reusable across components

#### State Management Integration
**Vuex integration requirements for typed state management:**
- Define clear state interfaces with all required properties
- Implement namespaced modules for logical separation
- Create typed getters for computed state values
- Define mutations for synchronous state changes
- Implement async actions for API integration and complex operations
- Use proper ActionContext typing for all store actions
- Ensure all store operations are type-safe and follow established patterns

### Template and Styling Standards

#### Template Organization
**Template organization requirements:**
- Use semantic HTML elements (article, header, section, footer) for content structure
- Implement clear CSS class naming with BEM methodology
- Group related template sections with descriptive comments
- Use proper accessibility attributes (alt text, button types)
- Avoid flat, unstructured div-based layouts

#### SCSS/Sass Integration
**SCSS/Sass integration requirements:**
- Use scoped styling to prevent CSS bleeding
- Import design system variables and mixins for consistency
- Implement BEM naming methodology for CSS classes
- Use design system spacing and color variables
- Include responsive design mixins for grid layouts
- Implement state modifiers for component variants (loading, selected, etc.)
- Use smooth transitions for interactive elements

### Error Handling & User Experience

#### Error Boundaries
**Error handling requirements:**
- Implement graceful error boundaries with proper user feedback
- Use reactive error state management with TypeScript typing
- Integrate with Vuex store for centralized error handling
- Use ref() for loading states and error tracking
- Implement try-catch-finally patterns for async operations
- Use computed properties for derived error states
- Use conditional rendering for loading states and product grids
- Implement proper template structure with semantic HTML
- Provide retry buttons for error recovery
- Use semantic loading indicators and messages

### Performance Best Practices

#### Lazy Loading & Code Splitting
**Component lazy loading requirements:**
- Import defineAsyncComponent from Vue for performance optimization
- Lazy load heavy components like ProductDetailsModal and ProductComparison
- Use dynamic imports for code splitting and performance improvement

- Configure route-level code splitting with dynamic imports
- Create routes array with path and lazy-loaded component definitions
- Configure dynamic product detail routes with lazy-loaded components
- Use route parameters for product identification

#### Reactivity Optimization
**Reactivity optimization requirements:**
- Use shallowRef for large arrays that don't need deep reactivity
- Import appropriate Vue reactivity functions (shallowRef, markRaw)

- Mark non-reactive objects using markRaw for performance optimization
- Use computed properties with proper reactive dependencies for expensive calculations
- Implement proper filtering, mapping, and sorting chains for data processing

## Frontend-Backend Integration

### API Communication Patterns

The frontend communicates with the backend through well-defined REST API endpoints using Axios HTTP client with type-safe patterns.

#### HTTP Client Configuration
**Client-side HTTP client configuration requirements:**
- Configure Axios with appropriate base URL, timeout, and default headers
- Implement request interceptors for automatic authentication token injection
- Implement response interceptors for centralized error handling
- Handle 401 authentication errors with proper redirects
- Use environment variables for API configuration

#### SSR HTTP Client Configuration
**SSR HTTP client configuration requirements:**
- Configure separate Axios instance for server-side rendering
- Use appropriate backend API URL with shorter timeout for SSR performance
- Include server identification headers for request tracking
- Separate client-side and server-side HTTP configurations

### API Integration Patterns

#### Product API Integration
**API integration pattern requirements:**
- Define type-safe API functions with proper TypeScript interfaces
- Use const assertions for API endpoint constants
- Separate SSR and client-side API calls with appropriate HTTP clients
- Implement proper parameter typing for filter objects
- Export functions with clear naming conventions for different use cases
- Export API functions with descriptive names and proper parameter typing
- Define comprehensive response interfaces with all required fields
- Implement async functions with proper Promise typing
- Handle response data extraction with type safety

### Data Flow Architecture

#### Client-Side Data Flow
**Client-side data flow:**
User Interaction → Vue Component → Composable → Vuex Store → API Call → Backend → Response → Response Handler → Store Mutation → Component Update → User Interface

#### SSR Data Flow
**SSR data flow:**
Browser Request → Fastify Server → Vue SSR → API Call → Quarkus Backend → MongoDB → JSON Response → Store Hydration → SSR Renderer → HTML Response

### State Management Integration

#### Vuex Store with API Integration
**Vuex store integration requirements:**
- Import Module and ActionContext types from Vuex
- Import API functions for store integration
- Define comprehensive state interfaces with all required properties
- Configure namespaced modules for logical separation
- Implement proper state initialization with all required fields
- Create mutations for SET_LOADING, SET_PRODUCTS, and SET_ERROR
- Implement actions with proper async/await patterns
- Use commit() for state updates and proper error handling
- Implement proper loading state management with commit calls
- Export modules with clear naming conventions

### Error Handling Patterns

#### Centralized Error Handling
**Centralized error handling requirements:**
- Create custom ApiError class extending Error with status codes
- Implement error handling functions that distinguish between server errors, network errors, and unknown errors
- Provide composables for consistent error handling across components
- Integrate with notification systems for user feedback
- Use proper TypeScript typing for error handling patterns

## Frontend Testing Strategy

### Testing Framework Configuration

#### Jest Configuration
**Jest configuration requirements:**
- Use Vue.js preset with TypeScript and Babel support
- Configure JSDOM test environment for DOM testing
- Set up proper transform rules for Vue and TypeScript files
- Configure module name mapping for alias resolution
- Set up test environment with setup files
- Define coverage collection patterns excluding configuration files
- Enforce minimum coverage thresholds (80% for branches, functions, lines, and statements)

### Component Testing Patterns

#### Vue Component Testing
**Vue component testing requirements:**
- Use Vue Test Utils for component mounting and interaction
- Create mock stores for Vuex integration testing
- Define proper TypeScript interfaces for test data
- Implement comprehensive test data structures with all required properties
- Create wrapper functions for component mounting with mock stores
- Configure namespaced Vuex modules with mock mutations for testing
- Mount components with proper props and global plugin configuration
- Test component rendering with proper DOM element selection and assertion
- Verify text content, attributes, and component behavior through expect statements
- Test event emission with wrapper.emitted() for component interactions
- Test loading states with conditional prop testing
- Use proper Jest expect matchers for component state verification

#### Composables Testing
- Test initial filter state with default values
- Test filter application with partial updates
- Test filter clearing functionality
- Use proper Jest expect matchers for equality testing
- Import and test composables in isolation
- Verify reactive state management and function behavior

### Integration Testing

#### API Integration Testing
**API integration testing requirements:**
- Use Mock Service Worker (MSW) for API mocking
- Set up test server with proper request handlers
- Implement proper test lifecycle with beforeAll/afterEach/afterAll
- Test successful API responses with proper data structures
- Test error handling scenarios with different HTTP status codes
- Use async/await patterns for promise-based testing

### E2E Testing Considerations

#### Test Structure for SCS
**E2E testing requirements:**
- Use Playwright for comprehensive user flow testing
- Test complete user journeys from navigation to action completion
- Verify initial page loads and element visibility
- Test filter interactions with data-testid selectors
- Verify result updates after user actions
- Test product selection and navigation flows
- Use page.locator() with data-testid selectors for element verification
- Test SSR functionality by disabling JavaScript
- Verify server-rendered content visibility
- Test product grid and card elements presence
- Complete test suites with proper assertions

## Frontend Build & Deployment

### Build Configuration

#### Multi-App Build System
- Configure code splitting with vendor and common chunk separation
- Set chunk naming conventions and priority levels
- Configure development server with appropriate port and API proxy settings
- Enable change origin for cross-origin API requests
- Use environment variables to determine app name and rendering mode
- Configure dynamic entry points based on app type

### SSR Server Deployment

#### Production SSR Server
**Production SSR server requirements:**
- Import and create Fastify server with proper configuration
- Configure host and port based on environment (localhost for dev, 0.0.0.0 for production)
- Implement proper error handling with server logging and process exit
- Handle graceful shutdown with SIGTERM signal processing
- Implement startup error catching with appropriate error logging

#### Docker Configuration
**Frontend Docker configuration requirements:**
- Use multi-stage builds with Node.js Alpine base images
- Separate builder and runtime stages for optimized image size
- Copy package files and install production dependencies only
- Build application artifacts in builder stage
- Copy only necessary files to runtime stage
- Expose appropriate port and define proper start command

### Environment Configuration

#### Environment Variables
**Environment variable requirements:**
- Configure NODE_ENV for proper production settings
- Set API base URLs for client and server communications
- Configure server ports for SSR server
- Set up monitoring endpoints and service identification

## Frontend Performance & Security

### Performance Optimization

#### Bundle Size Optimization
**Bundle size optimization requirements:**
- Enable tree-shaking with usedExports and sideEffects configuration
- Configure code splitting with appropriate chunk limits
- Set up cache groups for framework-specific chunks (Vue, Vuex, Vue Router)
- Separate vendor libraries into dedicated chunks for better caching
- Prioritize framework chunks over generic library chunks
- Optimize initial and async request limits for performance

#### Performance Monitoring
**Performance monitoring requirements:**
- Implement Web Vitals tracking for Core Web Vitals metrics (CLS, FID, FCP, LCP, TTFB)
- Use dynamic imports for performance measurement libraries
- Implement custom metric tracking with Performance API
- Check for browser API availability before usage
- Track application-specific performance metrics

### Security Standards

#### Content Security Policy
**Content Security Policy requirements:**
- Define restrictive default source directives
- Configure script sources with minimal unsafe-inline usage
- Allow specific trusted domains for analytics and external services

#### XSS Prevention
- Use DOMPurify for HTML sanitization with allowed tags and attributes
- Implement input sanitization for user-provided content
- Configure allowed HTML tags (p, br, strong, em, ul, ol, li)
- Set allowed attributes (class only)
- Remove dangerous characters from input strings

#### Authentication Integration
- Create authentication composables for token management
- Store authentication tokens in localStorage
- Implement computed properties for authentication state
- Create async login functions with proper credential handling
- Integrate with authentication API endpoints
- Set token values and localStorage on successful login
- Clear tokens and localStorage on logout
- Return authentication state and functions from composables

## Performance Standards

### Critical Performance Requirements (MANDATORY)

#### Algorithm Complexity Standards
- **NO O(n²) or higher complexity operations** in production code
- **Database operations MUST use proper indexes**
- **Collection operations MUST be optimized** for expected data sizes
- **Memory usage MUST be bounded** - no unlimited data loading

#### Database Performance (MANDATORY)

1. **Query Optimization:**
   - **Required MongoDB indexes**: Create indexes on klassifikationId, verfuegbarkeiten.bestellschlussUTC, and verfuegbarkeiten.liefertag for optimal query performance

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

**Forbidden performance anti-patterns:**
- Never disable pagination for in-memory sorting (causes memory exhaustion)
- Avoid O(n) operations in comparators (use HashMap lookups instead)
- Never load entire collections without limits (always use pagination)
- Prevent memory exhaustion through proper collection handling

#### Performance Monitoring (MANDATORY)

- Response time P95 < 500ms for all endpoints
- Memory usage < 80% heap under normal load
- Database query time < 200ms average
- No memory leaks in long-running operations

#### JVM 22 Performance Tuning (RECOMMENDED)

**JVM 22 performance tuning requirements:**
- Disable native mode for development, use JVM mode
- Configure ZGC for low-latency garbage collection
- Set maximum GC pause target to 10ms
- Configure heap sizes (512m initial, 2g maximum for development; 1g-4g for production)
- Set metaspace sizes for class loading optimization
- Enable string deduplication and compressed object pointers
- Pre-touch heap pages and configure system proxy usage
- Optimize HTTP I/O threads for CPU cores
- Enable adaptive heap sizing and transparent huge pages
- Use environment variables for development and Docker deployment settings

#### Quarkus 3.24.3 Specific Optimizations

**Quarkus framework optimization requirements:**
- Configure thread pools with appropriate core threads, max threads, and queue size
- Set HTTP I/O threads to match CPU cores and worker threads for blocking operations
- Configure MongoDB with proper database name, timeouts, and authentication settings
- Optimize MongoDB connection pools for production with appropriate pool sizes and timeouts
- Configure Kafka consumer and producer settings for batch processing optimization
- Enable Micrometer bindings for JVM and system metrics monitoring
- Configure logging with JSON formatting control and field selection for different environments

### Logging Configuration Patterns

The application uses sophisticated JSON logging configuration for different environments:

#### Development Logging
**Development logging configuration:**
- Disable JSON logging for human-readable console output during development

#### Production Logging
**Production logging configuration:**
- Enable JSON logging for structured log aggregation
- Include essential fields: timestamp, level, message, logger name
- Enable Mapped Diagnostic Context (MDC) for request correlation

#### Custom Log Field Management
The application allows granular control over JSON log fields:

**Fine-grained logging field control requirements:**
- Configure individual JSON log field enablement through console.json.fields
- Enable level, message, timestamp fields for essential logging information
- Disable logger-name and logger-class-name to reduce log verbosity
- Enable thread-name, hostname, process-name, process-id for system identification
- Disable thread-id to minimize log size while maintaining debugging capability

## Kotlin Development Guidelines

### Language Standards

#### Immutability (MANDATORY)
- **Use val, not var** for all value object properties
- **Implement validation in init blocks** for value objects
- **Make data classes immutable** by using only val properties
- **FORBIDDEN**: Mutable value objects that break immutability principles

#### Null Safety (MANDATORY)
- Use Kotlin's null safety features properly
- Prefer `?.let` over explicit null checks
- Never use `!!` unless absolutely necessary with clear justification
- Use `Optional` patterns for domain modeling

#### Collection Operations
- **PREFERRED**: Use functional collection operations (filter, map, take)
- **Chain operations** for readable data transformations
- **AVOID**: Imperative loops for simple transformations that can be expressed functionally

### Error Handling

#### Domain Exceptions (MANDATORY)
- **Create specific domain exceptions** with meaningful error messages
- **Inherit from base domain exception class** for consistency
- **Include context values** in exception messages for debugging
- **Implement validation in value object constructors** using require() statements

#### Boundary Validation (CRITICAL)
- **MANDATORY**: Validate all boundary conditions to prevent crashes
- **Check for null values** and negative numbers before processing
- **Use when expressions** for comprehensive condition handling
- **FORBIDDEN**: Missing boundary checks that cause runtime crashes with invalid inputs

### MongoDB Integration

#### Repository Pattern (MANDATORY)
- **Domain layer**: Define repository interfaces with domain types only
- **Adapter layer**: Implement repository interfaces using MongoDB-specific classes
- **Use ApplicationScoped annotation** for CDI repository implementations
- **Inherit from PanacheMongoRepository** for MongoDB data access patterns

#### Entity Mapping (MANDATORY)
- MongoDB entities in adapter layer only
- Clean mapping between MongoProdukt ↔ Produkt
- No MongoDB annotations in domain layer

### Kotlin Annotation Processing Configuration

The project uses sophisticated annotation processing for framework integration:

#### AllOpen Configuration
**AllOpen plugin configuration requirements:**
- Configure CDI annotations (ConfigProperties, ApplicationScoped, Singleton, Dependent)
- Configure JAX-RS endpoints with Path annotation
- Configure persistence annotations for JPA and MongoDB entities
- Configure QuarkusTest annotations for test class compatibility
- Enable framework integration through annotation processing

#### NoArg Configuration
**NoArg plugin configuration requirements:**
- Configure custom domain annotation for no-arg constructor generation
- Configure CDI ConfigProperties for framework compatibility
- Configure persistence entity annotations for JPA and MongoDB
- Enable automatic no-arg constructor generation for framework integration

#### Custom NoArg Annotation
**Custom NoArg annotation requirements:**
- Define annotation with CLASS target and RUNTIME retention
- Apply to domain entities for framework compatibility
- Use with data classes to generate no-arg constructors automatically
- Maintain clean domain code while supporting framework requirements

#### Annotation Processing Benefits
- **Framework Integration**: Seamless CDI and JAX-RS integration
- **Persistence Layer**: Both JPA and MongoDB support configured
- **Testing Support**: QuarkusTest classes get proper initialization
- **Domain Model**: Custom NoArg annotation for clean domain code

## Testing Strategy

### Testing Framework Stack

- **Unit Testing:** JUnit 5 Jupiter
- **Mocking:** Mockk 1.14.4 (Kotlin-specific, not Mockito)
- **Assertions:** Strikt 0.35.1 (Kotlin-specific)
- **Integration:** @QuarkusTest with TestContainers 1.21.3 (MongoDB & Kafka)
- **Architecture:** ArchUnit 1.4.1 for layer compliance
- **REST Testing:** REST Assured with Kotlin extensions

### Test Structure & Organization Strategy

The project uses **tag-based test organization** alongside traditional package structure:

#### Physical Test Structure
**Test package structure requirements:**
- Organize tests by architectural layer (adapter, application, domain)
- Separate active adapters (REST endpoints) from passive adapters (database)
- Use integration tags for external dependency tests
- Use unit tags for isolated domain logic tests
- Keep test utilities separate from production code

#### Tag-Based Test Execution
Tests are organized using JUnit5 tags for precise execution control:

**Tag-based test organization requirements:**
- Use unit tags for fast, isolated tests with no external dependencies
- Use integration tags for tests requiring TestContainers, databases, or external services
- Support combined tags for tests that can run in multiple contexts
- Implement precise test execution control through JUnit5 tag filtering
- Follow descriptive test naming conventions with backticks

#### Test Execution Strategy
- **Fast feedback**: Use unitTest gradle task for @Tag("unit") tests only
- **Comprehensive testing**: Use integrationTest gradle task for @Tag("integration") tests
- **Complete test suite**: Use test gradle task to execute all tests

### Testing Patterns (MANDATORY)

#### Unit Tests
- **Use @Test annotation** with descriptive function names in backticks
- **Follow Given-When-Then structure** for clear test organization
- **Use expectThrows** for exception testing with specific exception types

#### Integration Tests
- **Use @QuarkusTest annotation** for full application context testing
- **Add @QuarkusTestResource** with TestContainers for external service integration
- **Apply @Tag("integration")** for proper test categorization
- **Use @Inject** for dependency injection in test classes
- **Test real database integration** with actual external services

#### Architecture Tests (MANDATORY)
- **Use ArchUnit for layer dependency validation**
- **Test domain layer isolation** - no dependencies on adapter packages
- **Validate package structure** with noClasses() and resideInAPackage() rules
- **Check dependency violations** against imported class definitions

### Test Requirements (MANDATORY)

1. **BDD Structure:** ALL tests MUST use Given-When-Then
2. **Test Isolation:** Each test completely independent
3. **Coverage:** 80% minimum, 95% for business logic
4. **No Production Dependencies:** Test utils in test package only
5. **Public Interface Testing:** No reflection or private method testing

## Code Quality Standards

### Critical Bug Prevention (MANDATORY)

#### Boundary Conditions
- **MANDATORY**: Validate all boundary conditions to prevent crashes
- **Use null-safe operators** (?.let) for safe value processing
- **Implement try-catch blocks** for parsing operations that may fail
- **Return null for invalid values** rather than throwing exceptions for negative numbers
- **Throw meaningful exceptions** with context for parsing failures

#### Price Comparison Logic
- **Use proper null handling** with null-checks before value comparisons
- **Avoid contradictory null logic** that returns true for null values in greater-than comparisons
- **Implement consistent comparison logic** that handles nullable parameters correctly

### Architecture Compliance (ENFORCED)

#### Layer Coupling (FORBIDDEN)
- **Domain layer MUST NOT reference adapter DTOs** or external data structures
- **Domain classes should only work with domain types** and interfaces
- **Keep clear separation** between domain model and adapter layer representations
- **Use proper mapping at adapter boundaries** to convert between domain and DTO types

### Quality Tools

- **Static Analysis:** Detekt 1.23.8 for Kotlin
- **Build Quality:** Gradle 8.x build verification with Kotlin DSL
- **Dependency Check:** OWASP Dependency Check 12.1.3 for vulnerability scanning
- **Version Management:** Gradle Versions Plugin 0.52.0 for dependency updates
- **Coverage:** JaCoCo 0.8.7 with 80% minimum threshold
- **Schema Management:** Avro Plugin 1.9.1 for Kafka schema handling

### Build Verification (MANDATORY)

**Build verification requirements:**
- Run clean build to ensure compilation success
- Execute Detekt static analysis for code quality
- Run all tests with coverage reporting before any commit

## Build Tasks & Schema Management

### Custom Gradle Tasks

The project includes several custom Gradle tasks for specialized build operations:

#### Schema Download Task

Automated download of Kafka Avro schemas from schema registry:

**Schema download task requirements:**
- Create custom Gradle task for automated Avro schema downloading
- Use environment variables for schema registry authentication
- Download latest versions of required schemas (produkte-value, verfuegbarkeiten-value)
- Implement proper authentication with Basic Auth header encoding
- Parse JSON responses and extract schema content
- Create target directories and write schema files to appropriate locations

**Usage:**
**Schema download usage requirements:**
- Set required environment variables for schema registry URL, user, and password
- Execute downloadSchemas Gradle task with proper authentication credentials
- Ensure schema registry access and proper network connectivity

#### Separated Test Tasks

Distinct test execution for different test types:

**Unit test task requirements:**
- Create separate Gradle test task for unit tests only
- Use JUnit Platform with tag-based filtering for unit tests
- Organize tasks in verification group for build lifecycle integration
- Finalize unit tests with JaCoCo coverage reporting
- Configure integration test task with proper logging manager
- Use JUnit Platform with integration tag filtering
- Organize in verification group with coverage reporting

**Usage:**
**Test execution usage:**
- Run unit tests only with unitTest task
- Run integration tests (including TestContainers) with integrationTest task
- Run all tests with standard test task

#### JaCoCo Coverage Report

Automated coverage report generation:

**JaCoCo coverage configuration requirements:**
- Configure JaCoCo tool version for compatibility
- Set execution data from build directory with proper file patterns
- Enable XML reports for CI/CD integration
- Enable HTML reports for local development viewing

#### Dependency Update Management

Automated dependency update checking with stability rules:

**Dependency update management requirements:**
- Configure dependency updates task with stability checking
- Define stable version patterns (RELEASE, FINAL, GA keywords)
- Use regex patterns for version validation
- Reject unstable versions when current version is stable
- Configure output formatting with plain text reporter
- Fail build when dependency updates are available for CI/CD integration

**Usage:**
**Dependency update usage:**
- Execute dependencyUpdates task to check for available updates
- Task will fail build if updates are available for CI/CD integration

#### OWASP Dependency Check

Security vulnerability scanning with custom configuration:

- **Disable .NET assembly analyzer** for Java-only projects
- **Fail build on medium+ vulnerabilities** (CVSS 5.0+)
- **Enable build failure on scan errors** for reliable security gates
- **Use custom suppression file** for managing false positives

**Suppression File Configuration:**
- **Create gradle/config/suppressions.xml** for dependency check configuration
- **Use suppress elements** to ignore false positive vulnerabilities
- **Include descriptive notes** explaining why vulnerabilities are suppressed
- **Use regex patterns** for package URL matching when appropriate

### Build Verification Workflow

**Complete build verification process:**
1. **Clean and compile**: Run clean, compileKotlin, and compileTestKotlin tasks
2. **Static analysis**: Execute detekt for code quality scanning
3. **Security scanning**: Run dependencyCheckAnalyze for vulnerability detection
4. **Unit tests**: Run unit tests with coverage reporting
5. **Integration tests**: Execute integration tests with TestContainers
6. **Full build verification**: Perform complete clean build
7. **Dependency updates**: Check for available dependency updates

### CI/CD Integration

Gradle tasks designed for CI/CD pipeline integration:

**CI/CD pipeline integration requirements:**
- Run tests with coverage reporting using Gradle tasks
- Execute security scans with dependency vulnerability checking
- Check for dependency updates with error handling for non-blocking updates
- Upload coverage reports to external services with proper file paths
- Use appropriate CI/CD action versions and configuration

### Schema Management Strategy

#### Avro Schema Versioning
- **Development**: Download latest schemas from registry
- **Production**: Pin schema versions for stability
- **Testing**: Use schema evolution testing patterns

#### Schema Evolution Guidelines
1. **Backward Compatibility**: Always maintain backward compatibility
2. **Version Control**: Store schemas in version control alongside code
3. **Testing**: Validate schema changes with existing data
4. **Documentation**: Document schema changes in release notes

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
**Analysis-driven branch naming examples:**
- Security branches: security/implement-jwt-authentication, security/fix-cors-configuration
- Performance branches: perf/optimize-sorting-algorithm, perf/implement-database-indexes
- Architecture branches: refactor/remove-dto-coupling, refactor/fix-value-object-mutability
- Use descriptive branch names that reference specific analysis findings
- Create branches with descriptive names referencing specific analysis findings

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

- **Run tests**: Execute test and integrationTest tasks for comprehensive testing
- **Check code quality**: Use detekt and spotbugsMain for static analysis
- **Generate coverage report**: Run jacocoTestReport for test coverage analysis
- **Full build verification**: Execute clean build for complete project verification

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

**TaskMaster command reference:**

**Project Setup Commands:**
- Initialize Task Master in current project
- Generate tasks from Product Requirements Documents
- Configure AI models interactively

**Daily Development Workflow Commands:**
- List all tasks with current status
- Get next available task to work on
- View detailed task information with specific IDs
- Mark tasks complete with status updates

**Task Management Commands:**
- Add new tasks with AI assistance and research capabilities
- Break tasks into subtasks with forced expansion
- Update specific tasks or multiple tasks from a starting ID
- Add implementation notes to subtasks

**Analysis & Planning Commands:**
- Analyze task complexity with research mode
- View complexity analysis reports
- Expand all eligible tasks automatically

**Dependencies & Organization Commands:**
- Add task dependencies between related tasks
- Reorganize task hierarchy by moving tasks
- Validate dependency chains for correctness
- Generate task markdown files for documentation

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

**Task structure integration requirements:**
- Use hierarchical ID format (main tasks: 1,2,3; subtasks: 1.1,1.2; sub-subtasks: 1.1.1)
- Include comprehensive task metadata: title, description, status, priority, dependencies
- Reference analysis findings in task details and implementation requirements
- Define clear test strategies for verification and quality assurance
- Support subtask hierarchies for complex feature breakdown
- Complete task structures with comprehensive metadata and hierarchical organization

### Gradle Integration

TaskMaster works seamlessly with existing Gradle workflow:

**TaskMaster-Gradle integration workflow:**
- Get next task from TaskMaster queue
- Run tests for current implementation
- Update subtask with implementation progress notes
- Perform full build verification
- Mark task complete upon successful verification

### Git Integration Patterns

**Git integration pattern requirements:**
- Use conventional commit format with task ID references in commit messages
- Include bullet points for specific changes and test results
- Reference analysis findings and security gaps in commit descriptions
- Create pull requests with comprehensive change descriptions
- Include security review checklists in PR descriptions
- Link tasks to commits and PRs for traceability

## AI Development Workflow

### Standard Development Workflow

#### 1. Project Initialization
**Project initialization workflow:**
- Initialize Task Master in existing project directory
- Create or obtain Product Requirements Documents referencing analysis findings
- Parse PRDs to generate structured task lists
- Analyze task complexity with research mode for informed planning
- Expand all eligible tasks automatically with research assistance

#### 2. AI-Assisted Planning Phase
**AI-assisted planning workflow:**
- Generate additional tasks from PRDs referencing specific analysis findings
- Use research mode for complex technical decision making
- Expand specific tasks with controlled subtask generation
- Validate task dependencies to ensure architectural alignment

#### 3. Daily Development Loop
**Daily development loop workflow:**
- Start sessions by finding next available task
- Review task details and acceptance criteria
- Follow TDD workflow with TaskMaster integration:
  - RED Phase: Set task in-progress, document failing test creation
  - GREEN Phase: Document minimal implementation achieving test pass
  - REFACTOR Phase: Document performance/quality improvements
- Complete tasks with proper status updates

#### 4. Multi-Claude Session Coordination

For complex projects requiring parallel development:

**Multi-Claude session coordination workflow:**
- Terminal 1: Main implementation focusing on security features with JWT authentication
- Terminal 2: Performance optimization on separate branch with database index optimization
- Terminal 3: Testing and validation with integration test development
- Use TaskMaster to coordinate work across multiple Claude sessions
- Maintain separate contexts for different development streams

### Context Engineering Patterns

#### PRD Creation from Analysis Findings

Transform analysis findings into actionable PRDs:

**PRD creation from analysis findings:**
- Create Security Implementation PRDs based on comprehensive analysis
- Identify critical security gaps requiring immediate attention
- Define requirements with priority levels (CRITICAL, HIGH)
- Implement JWT-based authentication for all API endpoints
- Use Quarkus Security with role-based access control
- Replace permissive CORS with restrictive policies
- Configure domain-specific origins for security
- Reference analysis report sections for traceability
- Consider architecture impact on existing Onion Architecture
- Integrate with existing Quarkus CDI patterns
- Maintain performance standards during security implementation

#### Task Generation Workflow

**Task generation workflow from analysis:**
- Create PRDs based on comprehensive analysis findings
- Parse PRDs to generate structured task lists with append mode
- Analyze complexity for specific task ranges with research mode
- Expand critical tasks with forced regeneration for algorithm fixes
- Validate dependencies against established architecture standards

### Complex Workflow Integration

#### Performance Optimization Example

**Performance optimization workflow example:**
- Generate tasks from performance analysis PRDs with append mode
- Expand critical performance tasks with controlled subtask generation
- Work systematically through TDD cycle with TaskMaster integration
- Track implementation progress through all TDD phases:
  - RED: Document failing test creation for performance improvements
  - GREEN: Document algorithm implementation achieving test pass
  - REFACTOR: Document code cleanup and performance validation
- Complete tasks with measurable performance improvements

#### Security Implementation Example

**Security implementation workflow example:**
- Address critical security findings with research-backed task creation
- Expand security tasks into comprehensive implementation subtasks
- Follow security-first development approach with ordered task execution:
  - Dependencies: Add required security frameworks
  - Endpoints: Implement authentication and authorization
  - Configuration: Configure CORS and security policies
  - Access Control: Implement role-based permissions
  - Testing: Security integration and penetration testing
- Track security compliance with specific analysis recommendation references

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

This guide integrates **AI-assisted development workflow** with comprehensive codebase analysis findings to establish a complete development ecosystem for the **Finden Self-Contained System** - a modern product search and listing platform.

### Self-Contained System Architecture
The Finden system exemplifies Self-Contained Systems principles:
- **Complete Business Domain:** End-to-end product search and discovery capability
- **Full Technology Stack:** Frontend + Backend + Database as autonomous unit
- **Team Ownership:** Single team can own entire user experience
- **Independent Deployment:** No coordination required with external systems
- **UI Ownership:** Complete web interface included within system boundaries

### Technology Foundation

#### Frontend Stack
- **Language:** TypeScript 4.7.3 with strict type checking
- **Framework:** Vue.js 3.2.37 with Composition API
- **SSR Server:** Fastify 4.2.0 with Vue Server Renderer
- **State Management:** Vuex 4.0.2 with typed modules
- **Build System:** Vue CLI 5.0.4 with multi-app configuration
- **Testing:** Jest 28.1.1 with Vue Test Utils and E2E coverage

#### Backend Stack
- **Language:** Kotlin 2.2.0 targeting JVM 22
- **Framework:** Quarkus 3.24.3 with reactive patterns
- **Database:** MongoDB with Panache Kotlin
- **Build System:** Gradle 8.x with Kotlin DSL
- **Architecture:** Domain-Driven Design with Onion/Hexagonal pattern
- **Testing:** JUnit 5 with TestContainers and ArchUnit

#### Integration & Operations
- **API Communication:** Type-safe REST APIs with Axios
- **Monitoring:** OpenTelemetry with Stackdriver integration
- **Security:** JWT authentication with role-based access control
- **Performance:** SSR optimization and bundle splitting
- **AI Integration:** TaskMaster with Claude Code for context-driven development

### SCS Implementation Benefits
1. **Reduced Complexity:** No coordination overhead between microservices
2. **Team Autonomy:** Complete feature delivery within single team
3. **Faster Deployment:** Full business capabilities deployed as units
4. **End-to-End Ownership:** Teams responsible for complete user experience
5. **Technology Freedom:** Independent technology choices per system

### Workflow Integration
- **Context Engineering:** PRD-based task generation and management
- **Task Master AI:** Automated complexity analysis and task expansion
- **Claude Code:** Real-time development assistance with project context
- **Analysis-Driven Development:** Quality gates based on comprehensive security and performance analysis
- **Full-Stack Testing:** Frontend and backend integration testing strategies

### Critical Security & Performance Gates
Based on comprehensive analysis findings (System Health Score: 6.2/10):

**PRODUCTION BLOCKERS:**
- ⚠️ **NO O(n²) ALGORITHMS** especially in sorting operations (Performance Score: 5/10)
- ⚠️ **NO MEMORY EXHAUSTION** patterns from full dataset loading
- ⚠️ **NO LAYER COUPLING** violations (enforced by ArchUnit)
- ⚠️ **FRONTEND SECURITY** CSP headers and XSS prevention required

**QUALITY STANDARDS:**
- ⚡ **Database operations optimized** with proper indexing
- 🏗️ **Architecture compliance** maintained through testing
- 🧪 **80%+ test coverage** with BDD structure (frontend and backend)
- 🔍 **Input validation comprehensive** at all boundaries
- ⚡ **Frontend performance** Bundle size < 1MB, Lighthouse score > 90
- 🎯 **SSR optimization** < 200ms response times for server-rendered pages

### Development Workflow Summary
The integrated AI-assisted workflow combines traditional DDD/TDD practices with TaskMaster for **production-ready code quality** and **enhanced developer velocity** through automated planning, context preservation, and analysis-driven quality gates across the complete Self-Contained System stack.

This comprehensive guide enables teams to develop, test, deploy, and maintain the Finden Self-Contained System with confidence, ensuring both frontend and backend components work seamlessly together to deliver exceptional product search experiences.
