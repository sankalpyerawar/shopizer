# Java 21 Migration Implementation Plan

## Problem Statement
Migrate Shopizer e-commerce platform from Java 17 (Spring Boot 2.5.12) to Java 21 LTS with Spring Boot 3.x, enabling access to security patches, performance improvements (Virtual Threads, enhanced GC), and modern language features while maintaining zero downtime through blue-green deployment.

## Requirements
- **Migration Drivers**: Security/support, performance (Virtual Threads), modern features (Pattern Matching, Records)
- **Testing**: Moderate coverage (111 tests across 36 files), will augment during migration
- **Deployment**: Blue-green with zero downtime
- **Feature Adoption**: Balanced - adopt Virtual Threads and Pattern Matching where valuable
- **Namespace Migration**: Manual review of javax → jakarta changes

## Background

### Current State
- Java 17 (configured in pom.xml: java.version=11, runtime: 17.0.17-tem)
- Spring Boot 2.5.12 (parent POM)
- 354+ files using javax.* imports (JPA, validation, servlet APIs)
- Multi-module Maven project (5 modules)
- Deprecated WebSecurityConfigurerAdapter in security config
- @Async operations in email and facade layers (Virtual Thread candidates)
- Docker deployment with eclipse-temurin:17-jre base image
- CircleCI using java11 image

### Key Migration Challenges
1. Spring Boot 2.5 → 3.x requires javax → jakarta namespace migration
2. WebSecurityConfigurerAdapter deprecated, needs SecurityFilterChain refactor
3. Hibernate 5 → 6 breaking changes
4. Third-party dependencies compatibility (Elasticsearch 7.5.2, Infinispan 9.4.18, Swagger 2.9.2)
5. Manual namespace migration across 354+ Java files

### Java 21 Opportunities
- Virtual Threads for @Async operations (email, order processing)
- Pattern Matching for type checks in business logic
- Sequenced Collections for catalog/cart operations
- Enhanced GC (ZGC, G1GC improvements)

## Proposed Solution

Implement a 4-phase migration strategy with blue-green deployment, where each phase produces a deployable artifact that can run in parallel with the Java 17 version. Manual namespace migration ensures quality control over critical business logic.

## Task Breakdown

### Phase 1: Foundation & Compatibility Assessment

#### Task 1: Create migration baseline and compatibility matrix
- **Objective**: Establish baseline metrics and identify all breaking changes
- **Implementation**:
  - Document current test coverage and create baseline test report
  - Run dependency analysis to identify javax.* usage across all modules
  - Create compatibility matrix for all third-party dependencies (Spring Boot 3.x, Hibernate 6, etc.)
  - Identify deprecated APIs in current codebase (WebSecurityConfigurerAdapter, etc.)
  - Set up migration tracking document with file-by-file checklist
- **Tests**: Verify all existing 111 tests pass on Java 17 baseline
- **Demo**: Compatibility report showing all dependencies requiring updates, deprecated API usage count, and migration checklist

#### Task 2: Set up Java 21 build environment with dual-version support
- **Objective**: Enable building and testing on both Java 17 and Java 21
- **Implementation**:
  - Create new Git branch `feature/java21-migration`
  - Add Maven profile for Java 21 build (keeps Java 17 as default)
  - Update .sdkmanrc to support Java 21
  - Create separate Dockerfile.java21 with eclipse-temurin:21-jre
  - Update CircleCI config to add Java 21 build job (parallel to Java 17)
  - Configure Maven Enforcer plugin to validate Java version
- **Tests**: Build succeeds on both Java 17 and 21 profiles, all tests pass on Java 17
- **Demo**: Successful parallel builds on both Java versions, Docker images for both versions

#### Task 3: Upgrade Spring Boot to 2.7.x (javax compatibility bridge)
- **Objective**: Move to latest Spring Boot 2.x before jumping to 3.x
- **Implementation**:
  - Update parent POM spring-boot-starter-parent to 2.7.18 (latest 2.x)
  - Update dependency versions for compatibility (Jackson, Hibernate, etc.)
  - Fix any deprecation warnings introduced
  - Update application.properties for Spring Boot 2.7 changes
  - Run full test suite and fix any failures
- **Tests**: All existing tests pass, add integration test for actuator endpoints
- **Demo**: Application runs successfully on Spring Boot 2.7.18 with Java 17, all APIs functional

### Phase 2: Namespace Migration (javax → jakarta)

#### Task 4: Migrate sm-core-model module (JPA entities)
- **Objective**: Convert all entity classes from javax.persistence to jakarta.persistence
- **Implementation**:
  - Manually replace javax.persistence imports with jakarta.persistence in all entity files
  - Update javax.validation to jakarta.validation for entity validation
  - Review and update custom JPA converters and listeners
  - Update module's pom.xml dependencies
  - Add module-specific tests for entity mapping validation
- **Tests**: Create tests to verify entity mappings, cascade operations, and validation constraints work correctly
- **Demo**: All JPA entities load correctly, database schema generation works, entity validation functions

#### Task 5: Migrate sm-core module (business logic and services)
- **Objective**: Convert service layer from javax to jakarta namespaces
- **Implementation**:
  - Replace javax.inject with jakarta.inject
  - Update javax.transaction to jakarta.transaction
  - Migrate javax.validation to jakarta.validation in service classes
  - Update any servlet-related javax imports to jakarta
  - Review and update custom validators
- **Tests**: Add service layer tests for transaction management and validation
- **Demo**: Business services execute correctly, transactions commit/rollback properly, validation works

#### Task 6: Migrate sm-shop module (web layer and REST APIs)
- **Objective**: Convert controllers and web configuration from javax to jakarta
- **Implementation**:
  - Replace javax.servlet with jakarta.servlet
  - Update javax.ws.rs to jakarta.ws.rs (if used)
  - Migrate security configuration from WebSecurityConfigurerAdapter to SecurityFilterChain
  - Update filter and interceptor implementations
  - Migrate Swagger 2.9.2 to SpringDoc OpenAPI 2.x (Spring Boot 3 compatible)
- **Tests**: Add API integration tests for authentication, authorization, and key endpoints
- **Demo**: REST APIs respond correctly, Swagger UI accessible, security filters work, JWT authentication functional

#### Task 7: Upgrade to Spring Boot 3.2.x
- **Objective**: Complete Spring Boot 3 migration with all namespace changes in place
- **Implementation**:
  - Update parent POM to spring-boot-starter-parent 3.2.x
  - Update Hibernate to 6.x (managed by Spring Boot)
  - Resolve any remaining compatibility issues
  - Update application.properties for Spring Boot 3 changes
  - Fix any runtime issues discovered during testing
- **Tests**: Full regression test suite, add tests for new Spring Boot 3 features
- **Demo**: Complete application runs on Spring Boot 3 + Java 21, all modules integrated

### Phase 3: Java 21 Feature Adoption

#### Task 8: Implement Virtual Threads for async operations
- **Objective**: Replace traditional @Async with Virtual Threads for I/O-bound operations
- **Implementation**:
  - Configure Spring Boot to use Virtual Thread executor
  - Identify @Async methods in EmailTemplatesUtils, UserFacadeImpl, CustomerFacadeImpl, OrderFacadeImpl
  - Refactor async operations to leverage Virtual Threads
  - Add monitoring/logging to measure performance improvements
  - Update thread pool configurations
- **Tests**: Add load tests to verify Virtual Thread performance under concurrent requests, test email sending concurrency
- **Demo**: Demonstrate improved throughput for concurrent email operations and order processing, show reduced thread count

#### Task 9: Apply Pattern Matching and modern Java features
- **Objective**: Refactor code to use Pattern Matching, Records, and Sequenced Collections
- **Implementation**:
  - Identify instanceof checks suitable for Pattern Matching (type checks in business logic)
  - Convert DTOs to Records where immutability is appropriate
  - Use Sequenced Collections for catalog browsing and cart operations
  - Apply switch expressions where applicable
  - Keep changes minimal and focused on clear value-add
- **Tests**: Add tests for refactored components, ensure behavior unchanged
- **Demo**: Show cleaner code examples, demonstrate Sequenced Collections in catalog API

#### Task 10: Optimize performance and garbage collection
- **Objective**: Configure and tune Java 21 GC for optimal performance
- **Implementation**:
  - Configure ZGC or G1GC with Java 21 improvements
  - Update JVM arguments in Dockerfile and startup scripts
  - Add GC logging and monitoring
  - Conduct performance testing and tuning
  - Document recommended JVM settings
- **Tests**: Run performance benchmarks comparing Java 17 vs Java 21, measure GC pause times
- **Demo**: Performance report showing throughput improvements, reduced GC pauses, memory efficiency gains

### Phase 4: Blue-Green Deployment & Validation

#### Task 11: Set up blue-green deployment infrastructure
- **Objective**: Configure parallel environments for zero-downtime migration
- **Implementation**:
  - Create deployment scripts for blue (Java 17) and green (Java 21) environments
  - Configure load balancer for traffic switching
  - Set up health checks and monitoring for both environments
  - Create rollback procedures
  - Document deployment runbook
- **Tests**: Test traffic routing, health checks, and rollback procedures
- **Demo**: Both environments running simultaneously, traffic routing works, health monitoring active

#### Task 12: Execute production migration with validation
- **Objective**: Migrate production traffic to Java 21 with validation gates
- **Implementation**:
  - Deploy Java 21 version to green environment
  - Run smoke tests and integration tests on green
  - Route 10% traffic to green, monitor for errors
  - Gradually increase to 50%, then 100% if metrics are healthy
  - Monitor performance, error rates, and business metrics
  - Keep blue environment running for 24-48 hours before decommission
- **Tests**: Production smoke tests, synthetic transaction monitoring, error rate validation
- **Demo**: Successful production migration with zero downtime, performance metrics showing improvements, rollback capability verified

## Timeline & Milestones

- **Phase 1**: 1-2 weeks (Foundation & Assessment)
- **Phase 2**: 3-4 weeks (Namespace Migration - most labor intensive)
- **Phase 3**: 2-3 weeks (Java 21 Feature Adoption)
- **Phase 4**: 1 week (Blue-Green Deployment & Validation)

**Total Estimated Duration**: 7-10 weeks

## Risk Mitigation

1. **Namespace Migration Errors**: Manual review process with peer reviews for each module
2. **Third-party Dependency Issues**: Early compatibility testing in Phase 1
3. **Performance Regression**: Comprehensive benchmarking before and after each phase
4. **Production Issues**: Blue-green deployment with gradual traffic shift and instant rollback capability
5. **Test Coverage Gaps**: Augment tests during migration, especially for critical paths

## Success Criteria

- Zero production downtime during migration
- All 111+ tests passing on Java 21
- Performance improvements: 10-20% throughput increase, reduced GC pauses
- Virtual Threads successfully handling async operations
- Successful rollback capability demonstrated
- Complete documentation of migration process and new features

## Rollback Strategy

Each phase produces a deployable artifact. If issues arise:
1. Immediate traffic switch back to Java 17 (blue) environment
2. Investigate and fix issues in green environment
3. Re-test and re-deploy when ready
4. Blue environment maintained for 48 hours post-migration

---

**Plan Created**: March 13, 2026
**Target Java Version**: Java 21 LTS
**Target Spring Boot Version**: 3.2.x
**Deployment Strategy**: Blue-Green with Zero Downtime
