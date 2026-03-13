# Java 21 Migration - Baseline Report

**Date**: March 13, 2026  
**Current Java Version**: 17.0.17-tem  
**Current Spring Boot Version**: 2.5.12  
**Target Java Version**: 21 LTS  
**Target Spring Boot Version**: 3.2.x

## Test Coverage Baseline

### Test Execution Summary
- **Total Test Classes**: 36
- **Total Tests Run**: 98
- **Tests Passed**: 98
- **Tests Failed**: 0
- **Tests Skipped**: 21
- **Build Status**: ✅ SUCCESS

### Test Breakdown by Module
- **sm-core**: 28 tests (13 skipped)
- **sm-shop**: 70 tests (8 skipped)

### Key Test Suites
- Product Management: 37 tests
- Shopping Cart: 7 tests
- Category Management: 7 tests
- Customer Registration: 3 tests
- User Management: 4 tests
- Store Management: 4 tests
- Tax Rate: 2 tests
- Utilities: 9 tests

## Dependency Analysis

### javax.* Usage Statistics
- **Total Files with javax imports**: 353
- **Total javax import statements**: ~1,500+

### Top javax Imports (Migration Required)
| Import | Count | Target (jakarta) |
|--------|-------|------------------|
| javax.inject.Inject | 171 | jakarta.inject.Inject |
| javax.persistence.* | 800+ | jakarta.persistence.* |
| javax.servlet.* | 87+ | jakarta.servlet.* |
| javax.validation.* | 76+ | jakarta.validation.* |

### Most Common javax.persistence Imports
- Table (81), Entity (81), TableGenerator (80)
- Column (78), JoinColumn (76), ManyToOne (75)
- Id (64), GenerationType (63), GeneratedValue (63)
- UniqueConstraint (48), FetchType (42)
- Embedded (32), OneToMany (31), CascadeType (31)
- EntityListeners (29)

### Most Common javax.servlet Imports
- HttpServletRequest (53)
- HttpServletResponse (34)

### Most Common javax.validation Imports
- @Valid (41)
- @NotEmpty (35)

## Deprecated API Usage

### Spring Security
- **WebSecurityConfigurerAdapter**: 4 usages (DEPRECATED)
  - Location: `MultipleEntryPointsSecurityConfig.java`
  - Impact: HIGH - Requires refactor to SecurityFilterChain
  - Affected: CustomerConfigurationAdapter, ServicesApiConfigurationAdapter, AdminConfigurationAdapter, UserApiConfigurationAdapter

### Hibernate
- **MultipleHiLoPerTableGenerator**: Multiple usages (DEPRECATED)
  - Impact: MEDIUM - Will be removed in Hibernate 6
  - Replacement: org.hibernate.id.enhanced.TableGenerator

## Current Dependencies Requiring Updates

### Core Framework
| Dependency | Current | Target | Compatibility |
|------------|---------|--------|---------------|
| Spring Boot | 2.5.12 | 3.2.x | ⚠️ Breaking changes |
| Hibernate | 5.x (via Spring Boot) | 6.x | ⚠️ Breaking changes |
| Java | 17 | 21 | ✅ Compatible |

### Third-Party Libraries
| Dependency | Current | Spring Boot 3 Compatible | Action Required |
|------------|---------|--------------------------|-----------------|
| Elasticsearch | 7.5.2 | ⚠️ Needs update to 8.x | Update required |
| Infinispan | 9.4.18.Final | ⚠️ Needs update to 14.x+ | Update required |
| Swagger | 2.9.2 | ❌ Not compatible | Migrate to SpringDoc OpenAPI |
| MapStruct | 1.3.0.Final | ⚠️ Needs update to 1.5.x+ | Update required |
| Jackson | 2.13.4 | ✅ Compatible (managed) | Managed by Spring Boot |
| Guava | 27.1-jre | ✅ Compatible | Update to latest |
| JWT (jjwt) | 0.8.0 | ⚠️ Needs update to 0.11.x+ | Update required |
| Commons Lang3 | 3.5 | ✅ Compatible | Update to latest |
| Commons IO | 2.7 | ✅ Compatible | Update to latest |
| MySQL Connector | 8.0.21 | ✅ Compatible | Update to latest |
| PostgreSQL | 42.2.18 | ✅ Compatible | Update to latest |
| H2 Database | (managed) | ✅ Compatible | Managed by Spring Boot |

## Module Structure

### Maven Modules (5 total)
1. **sm-core-model** - JPA entities (HIGH impact)
2. **sm-core-modules** - Core modules (MEDIUM impact)
3. **sm-core** - Business logic (HIGH impact)
4. **sm-shop-model** - Shop models (MEDIUM impact)
5. **sm-shop** - Web layer & REST APIs (HIGH impact)

## Async Operations (Virtual Thread Candidates)

### @Async Method Locations
- **EmailTemplatesUtils**: 5 async methods
- **UserFacadeImpl**: 1 async method
- **CustomerFacadeImpl**: 1 async method
- **OrderFacadeImpl**: 1 async method

**Total**: 8 async operations identified for Virtual Thread migration

## Build & Deployment Configuration

### Build Tools
- Maven Wrapper (mvnw)
- Java version configured: 11 (pom.xml)
- Runtime version: 17.0.17-tem (.sdkmanrc)

### Docker
- Base image: eclipse-temurin:17-jre
- Exposed port: 8080
- Health check: Actuator endpoint

### CI/CD
- CircleCI configuration present
- Current CI image: shopizerecomm/ci:java11

## Migration Complexity Assessment

### High Complexity Areas
1. **Namespace Migration** (354 files)
   - Effort: HIGH
   - Risk: MEDIUM (manual review mitigates)
   - Estimated: 3-4 weeks

2. **Security Configuration Refactor**
   - Effort: MEDIUM
   - Risk: HIGH (authentication/authorization critical)
   - Estimated: 1 week

3. **Third-Party Dependency Updates**
   - Effort: MEDIUM
   - Risk: MEDIUM
   - Estimated: 1-2 weeks

### Medium Complexity Areas
1. **Hibernate 6 Migration**
   - Effort: MEDIUM
   - Risk: MEDIUM
   - Estimated: 1 week

2. **Swagger to SpringDoc Migration**
   - Effort: MEDIUM
   - Risk: LOW
   - Estimated: 3-5 days

### Low Complexity Areas
1. **Virtual Threads Implementation**
   - Effort: LOW
   - Risk: LOW
   - Estimated: 3-5 days

2. **Pattern Matching & Modern Features**
   - Effort: LOW
   - Risk: LOW
   - Estimated: 1 week

## Warnings & Issues in Current Build

### Hibernate Warnings
- HHH90000015: Deprecated MultipleHiLoPerTableGenerator usage
- Action: Will be addressed in Hibernate 6 migration

### Security Warnings
- BCryptPasswordEncoder warnings in tests
- Action: Review during security configuration refactor

## Recommendations

### Phase 1 Priorities
1. ✅ Establish baseline (COMPLETE)
2. Set up dual Java 17/21 build environment
3. Upgrade to Spring Boot 2.7.18 (compatibility bridge)

### Risk Mitigation
1. **Manual namespace migration** - Ensures quality control
2. **Blue-green deployment** - Zero downtime guarantee
3. **Comprehensive testing** - Add tests during migration
4. **Gradual rollout** - 10% → 50% → 100% traffic shift

### Success Metrics
- All 98+ tests passing on Java 21
- Zero production downtime
- 10-20% performance improvement
- Successful Virtual Thread implementation
- Complete API compatibility maintained

## Next Steps

1. ✅ **Task 1 Complete**: Baseline established
2. **Task 2**: Set up Java 21 build environment with dual-version support
3. **Task 3**: Upgrade Spring Boot to 2.7.x

---

**Report Generated**: March 13, 2026  
**Status**: Phase 1, Task 1 - COMPLETE ✅
