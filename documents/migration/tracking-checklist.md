# Migration Tracking Checklist

**Migration**: Java 17 → Java 21 with Spring Boot 2.5.12 → 3.2.x  
**Strategy**: Blue-Green Deployment with Zero Downtime  
**Start Date**: March 13, 2026

## Phase 1: Foundation & Compatibility Assessment

### Task 1: Create migration baseline and compatibility matrix ✅
- [x] Document current test coverage
- [x] Run dependency analysis
- [x] Create compatibility matrix
- [x] Identify deprecated APIs
- [x] Set up migration tracking document
- [x] Verify all 98 tests pass on Java 17
- **Status**: COMPLETE
- **Completion Date**: March 13, 2026

### Task 2: Set up Java 21 build environment with dual-version support
- [ ] Create Git branch `feature/java21-migration`
- [ ] Add Maven profile for Java 21 build
- [ ] Update .sdkmanrc to support Java 21
- [ ] Create Dockerfile.java21
- [ ] Update CircleCI config for Java 21 build
- [ ] Configure Maven Enforcer plugin
- [ ] Verify builds on both Java 17 and 21
- **Status**: PENDING
- **Estimated Duration**: 2-3 days

### Task 3: Upgrade Spring Boot to 2.7.x
- [ ] Update parent POM to spring-boot-starter-parent 2.7.18
- [ ] Update dependency versions
- [ ] Fix deprecation warnings
- [ ] Update application.properties
- [ ] Run full test suite
- [ ] Add actuator integration test
- **Status**: PENDING
- **Estimated Duration**: 3-5 days

## Phase 2: Namespace Migration (javax → jakarta)

### Task 4: Migrate sm-core-model module (JPA entities)
**Files to migrate**: 94 files

#### Entity Migration Checklist
- [ ] Audit all entity files (94 files)
- [ ] Replace javax.persistence → jakarta.persistence
- [ ] Replace javax.validation → jakarta.validation
- [ ] Review custom JPA converters
- [ ] Review EntityListeners
- [ ] Update module pom.xml
- [ ] Add entity mapping validation tests
- [ ] Verify database schema generation
- [ ] Test cascade operations
- [ ] Test validation constraints
- **Status**: PENDING
- **Estimated Duration**: 1 week

#### Key Entity Classes (Priority)
- [ ] Customer.java
- [ ] MerchantStore.java
- [ ] Product.java
- [ ] User.java
- [ ] ProductAvailability.java
- [ ] ProductVariant.java
- [ ] Content.java
- [ ] TaxRate.java
- [ ] Category.java
- [ ] ProductPrice.java

### Task 5: Migrate sm-core module (business logic and services)
**Files to migrate**: 93 files

#### Service Layer Migration Checklist
- [ ] Audit all service files (93 files)
- [ ] Replace javax.inject → jakarta.inject
- [ ] Replace javax.transaction → jakarta.transaction
- [ ] Replace javax.validation → jakarta.validation
- [ ] Update servlet-related imports
- [ ] Review custom validators
- [ ] Add transaction management tests
- [ ] Test service validation
- **Status**: PENDING
- **Estimated Duration**: 1 week

### Task 6: Migrate sm-shop module (web layer and REST APIs)
**Files to migrate**: 131 files

#### Web Layer Migration Checklist
- [ ] Audit all controller files (131 files)
- [ ] Replace javax.servlet → jakarta.servlet
- [ ] Replace javax.ws.rs → jakarta.ws.rs (if used)
- [ ] Refactor WebSecurityConfigurerAdapter → SecurityFilterChain
- [ ] Update MultipleEntryPointsSecurityConfig.java
  - [ ] CustomerConfigurationAdapter
  - [ ] ServicesApiConfigurationAdapter
  - [ ] AdminConfigurationAdapter
  - [ ] UserApiConfigurationAdapter
- [ ] Update filters and interceptors
- [ ] Migrate Swagger 2.9.2 → SpringDoc OpenAPI 2.x
- [ ] Add API integration tests
- [ ] Test authentication
- [ ] Test authorization
- [ ] Verify JWT authentication
- [ ] Verify Swagger UI accessibility
- **Status**: PENDING
- **Estimated Duration**: 1.5 weeks

### Task 7: Upgrade to Spring Boot 3.2.x
- [ ] Update parent POM to spring-boot-starter-parent 3.2.x
- [ ] Verify Hibernate 6.x (managed by Spring Boot)
- [ ] Update Elasticsearch to 8.x
- [ ] Update Infinispan to 14.x+
- [ ] Update MapStruct to 1.5.x+
- [ ] Update jjwt to 0.11.x+
- [ ] Update Guava to latest
- [ ] Update Commons libraries
- [ ] Resolve compatibility issues
- [ ] Update application.properties for Spring Boot 3
- [ ] Fix runtime issues
- [ ] Run full regression test suite
- [ ] Add Spring Boot 3 feature tests
- **Status**: PENDING
- **Estimated Duration**: 1.5 weeks

## Phase 3: Java 21 Feature Adoption

### Task 8: Implement Virtual Threads for async operations
**Target methods**: 8 async operations

#### Virtual Thread Migration
- [ ] Configure Spring Boot Virtual Thread executor
- [ ] Migrate EmailTemplatesUtils (5 methods)
  - [ ] Method 1: Email template processing
  - [ ] Method 2: Email sending
  - [ ] Method 3: Bulk email operations
  - [ ] Method 4: Email notification
  - [ ] Method 5: Email queue processing
- [ ] Migrate UserFacadeImpl (1 method)
- [ ] Migrate CustomerFacadeImpl (1 method)
- [ ] Migrate OrderFacadeImpl (1 method)
- [ ] Add performance monitoring
- [ ] Add logging for Virtual Threads
- [ ] Update thread pool configurations
- [ ] Create load tests
- [ ] Test email sending concurrency
- [ ] Measure throughput improvements
- [ ] Document performance gains
- **Status**: PENDING
- **Estimated Duration**: 3-5 days

### Task 9: Apply Pattern Matching and modern Java features
- [ ] Identify instanceof checks for Pattern Matching
- [ ] Refactor type checks in business logic
- [ ] Identify DTOs suitable for Records
- [ ] Convert immutable DTOs to Records
- [ ] Identify Sequenced Collections opportunities
- [ ] Apply Sequenced Collections in catalog operations
- [ ] Apply Sequenced Collections in cart operations
- [ ] Identify switch expression opportunities
- [ ] Apply switch expressions
- [ ] Add tests for refactored components
- [ ] Verify behavior unchanged
- [ ] Document code improvements
- **Status**: PENDING
- **Estimated Duration**: 1 week

### Task 10: Optimize performance and garbage collection
- [ ] Research Java 21 GC improvements
- [ ] Configure ZGC settings
- [ ] Configure G1GC settings
- [ ] Update Dockerfile JVM arguments
- [ ] Update startup scripts
- [ ] Add GC logging
- [ ] Add GC monitoring
- [ ] Create performance benchmarks
- [ ] Run Java 17 baseline benchmarks
- [ ] Run Java 21 benchmarks
- [ ] Measure GC pause times
- [ ] Measure throughput improvements
- [ ] Measure memory efficiency
- [ ] Document recommended JVM settings
- [ ] Create performance report
- **Status**: PENDING
- **Estimated Duration**: 5-7 days

## Phase 4: Blue-Green Deployment & Validation

### Task 11: Set up blue-green deployment infrastructure
- [ ] Create deployment scripts for blue environment (Java 17)
- [ ] Create deployment scripts for green environment (Java 21)
- [ ] Configure load balancer
- [ ] Set up traffic routing
- [ ] Configure health checks (blue)
- [ ] Configure health checks (green)
- [ ] Set up monitoring (blue)
- [ ] Set up monitoring (green)
- [ ] Create rollback procedures
- [ ] Document deployment runbook
- [ ] Test traffic routing
- [ ] Test health checks
- [ ] Test rollback procedures
- [ ] Verify both environments running simultaneously
- **Status**: PENDING
- **Estimated Duration**: 5-7 days

### Task 12: Execute production migration with validation
- [ ] Deploy Java 21 to green environment
- [ ] Run smoke tests on green
- [ ] Run integration tests on green
- [ ] Route 10% traffic to green
- [ ] Monitor error rates (10%)
- [ ] Monitor performance metrics (10%)
- [ ] Monitor business metrics (10%)
- [ ] Increase to 50% traffic
- [ ] Monitor error rates (50%)
- [ ] Monitor performance metrics (50%)
- [ ] Monitor business metrics (50%)
- [ ] Increase to 100% traffic
- [ ] Monitor error rates (100%)
- [ ] Monitor performance metrics (100%)
- [ ] Monitor business metrics (100%)
- [ ] Keep blue environment running (24-48 hours)
- [ ] Verify rollback capability
- [ ] Decommission blue environment
- [ ] Create migration success report
- **Status**: PENDING
- **Estimated Duration**: 3-5 days (+ 48 hour monitoring)

## File Migration Tracking by Module

### sm-core-model (94 files) - Phase 2, Task 4
**Status**: Not Started  
**Priority**: HIGH (Foundation for all other modules)

### sm-core (93 files) - Phase 2, Task 5
**Status**: Not Started  
**Priority**: HIGH (Business logic layer)

### sm-shop (131 files) - Phase 2, Task 6
**Status**: Not Started  
**Priority**: HIGH (Web layer and APIs)

### sm-core-modules - Phase 2, Task 5
**Status**: Not Started  
**Priority**: MEDIUM

### sm-shop-model - Phase 2, Task 6
**Status**: Not Started  
**Priority**: MEDIUM

## Overall Progress

- **Phase 1**: 1/3 tasks complete (33%)
- **Phase 2**: 0/4 tasks complete (0%)
- **Phase 3**: 0/3 tasks complete (0%)
- **Phase 4**: 0/2 tasks complete (0%)

**Total Progress**: 1/12 tasks complete (8%)

## Risk Register

| Risk | Severity | Mitigation | Status |
|------|----------|------------|--------|
| Namespace migration errors | HIGH | Manual review, peer reviews | Active |
| Security config breaking | HIGH | Comprehensive auth tests | Active |
| Third-party incompatibility | MEDIUM | Early testing in Phase 1 | Active |
| Performance regression | MEDIUM | Benchmarking each phase | Active |
| Production downtime | HIGH | Blue-green deployment | Mitigated |
| Test coverage gaps | MEDIUM | Add tests during migration | Active |

## Notes

- All 98 baseline tests passing on Java 17 ✅
- 353 files require javax → jakarta migration
- 8 async operations identified for Virtual Thread migration
- 4 security adapters require SecurityFilterChain refactor
- Zero downtime requirement drives blue-green strategy

---

**Last Updated**: March 13, 2026  
**Current Phase**: Phase 1, Task 1 Complete  
**Next Task**: Task 2 - Set up Java 21 build environment
