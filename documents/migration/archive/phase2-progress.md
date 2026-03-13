# Phase 2 Progress - Namespace Migration Started

**Date**: March 13, 2026  
**Phase**: 2 - Namespace Migration (javax → jakarta)  
**Status**: 🔄 IN PROGRESS

## Approach Change

### Initial Plan
Manual migration of 353 files across 5 modules, one module at a time.

### Revised Approach
Given the scale (353 files, 1,512 import statements), a hybrid approach is more efficient:

1. **Upgrade Spring Boot 3.2.x first** (provides jakarta dependencies)
2. **Use OpenRewrite for bulk migration** (automated javax → jakarta)
3. **Manual review and fixes** (critical business logic)
4. **Module-by-module testing** (ensure quality)

## Changes Made So Far

### Root POM Updates (Partial)

#### Spring Boot Upgrade
```xml
<!-- FROM -->
<version>2.7.18</version>

<!-- TO -->
<version>3.2.3</version>
```

#### Dependency Version Updates
- commons-io: 2.7 → 2.15.1
- commons-collections4: 4.1 → 4.4
- commons-validator: 1.5.1 → 1.8.0
- commons-fileupload: 1.3.3 → 1.5
- mapstruct: 1.3.0.Final → 1.5.5.Final
- httpcomponents: 4.5.2 → 4.5.14
- infinispan: 9.4.18.Final → 14.0.21.Final
- mysql-connector: 8.0.21 → 8.0.33
- postgresql: 42.2.18 → 42.7.1
- geoip2: 2.7.0 → 4.2.0
- drools: 7.32.0.Final → 8.44.0.Final
- google-maps-services: 0.1.6 → 2.2.0
- jjwt: 0.8.0 → 0.12.5

#### Namespace Changes in Root POM
- ✅ javax.inject → jakarta.inject (2.0.1)
- ✅ javax.mail → jakarta.mail (2.1.2) + angus-mail (2.0.2)
- ✅ javax.validation → jakarta.validation (managed by Spring Boot)
- ✅ javax.annotation → jakarta.annotation (2.1.1)
- ✅ Swagger (springfox) → SpringDoc OpenAPI (2.3.0)
- ✅ Jackson versions removed (managed by Spring Boot 3)
- ✅ ehcache → cache-api

### Module POMs
**Status**: NOT YET UPDATED

Module POMs still reference:
- javax.validation:validation-api (sm-core-model)
- javax.annotation:javax.annotation-api (sm-shop-model)
- io.springfox:springfox-swagger2 (sm-shop-model, sm-shop)
- io.springfox:springfox-swagger-ui (sm-shop)
- hibernate-ehcache (sm-core)
- httpclient version missing (sm-core)

## OpenRewrite Migration Tool

### Created
`migration-helper-pom.xml` - OpenRewrite configuration for automated migration

### Recipes Configured
1. `org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta`
   - Automatically migrates javax.* → jakarta.*
   - Updates imports across all Java files
   - Handles common migration patterns

2. `org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2`
   - Migrates Spring Boot 2.x → 3.2
   - Updates deprecated APIs
   - Fixes configuration changes

### Usage
```bash
# Run OpenRewrite migration
mvn -f migration-helper-pom.xml org.openrewrite.maven:rewrite-maven-plugin:run \
  -Drewrite.activeRecipes=org.openrewrite.java.migrate.jakarta.JavaxMigrationToJakarta

# Review changes
git diff

# Test build
./mvnw clean install
```

## Recommended Next Steps

### Step 1: Complete Root POM Migration
- [ ] Remove all javax.* property references
- [ ] Ensure all dependencies use jakarta or are Spring Boot managed
- [ ] Verify no version conflicts

### Step 2: Update Module POMs
For each module (sm-core-model, sm-core, sm-core-modules, sm-shop-model, sm-shop):
- [ ] Replace javax dependencies with jakarta
- [ ] Replace Swagger with SpringDoc OpenAPI
- [ ] Remove hibernate-ehcache (deprecated in Hibernate 6)
- [ ] Add missing dependency versions or use dependencyManagement

### Step 3: Run OpenRewrite Migration
```bash
# Dry run first to see what will change
mvn -f migration-helper-pom.xml org.openrewrite.maven:rewrite-maven-plugin:dryRun

# Apply changes
mvn -f migration-helper-pom.xml org.openrewrite.maven:rewrite-maven-plugin:run
```

### Step 4: Manual Fixes
After OpenRewrite:
- [ ] Review security configuration (WebSecurityConfigurerAdapter → SecurityFilterChain)
- [ ] Update Swagger annotations to SpringDoc
- [ ] Fix any OpenRewrite misses
- [ ] Update application.properties for Spring Boot 3

### Step 5: Module-by-Module Testing
- [ ] sm-core-model: Test entity mappings
- [ ] sm-core: Test business logic
- [ ] sm-shop: Test REST APIs and security

### Step 6: Integration Testing
- [ ] Full application startup
- [ ] API endpoint testing
- [ ] Database operations
- [ ] Security/authentication

## Known Migration Challenges

### 1. Security Configuration
**Issue**: WebSecurityConfigurerAdapter deprecated  
**Files**: `MultipleEntryPointsSecurityConfig.java`  
**Solution**: Refactor to SecurityFilterChain (4 adapters)

### 2. Swagger → SpringDoc
**Issue**: Different annotation model  
**Files**: All controller classes with @Api, @ApiOperation  
**Solution**: Replace with @Tag, @Operation

### 3. Hibernate 6 Changes
**Issue**: hibernate-ehcache removed, ID generator changes  
**Files**: Entity classes using MultipleHiLoPerTableGenerator  
**Solution**: Use TableGenerator, configure cache differently

### 4. javax.servlet → jakarta.servlet
**Issue**: Filter and servlet API changes  
**Files**: All filters, servlets, interceptors  
**Solution**: OpenRewrite handles most, manual review needed

### 5. javax.persistence → jakarta.persistence
**Issue**: JPA namespace change  
**Files**: All 94 entity files in sm-core-model  
**Solution**: OpenRewrite handles automatically

## Estimated Effort

### With OpenRewrite (Recommended)
- **Root POM completion**: 2-3 hours
- **Module POM updates**: 2-3 hours
- **OpenRewrite execution**: 30 minutes
- **Manual review/fixes**: 1-2 days
- **Security refactor**: 1 day
- **Swagger migration**: 1 day
- **Testing & fixes**: 2-3 days

**Total**: 5-7 days

### Manual Migration (Original Plan)
- **sm-core-model**: 1 week
- **sm-core**: 1 week
- **sm-shop**: 1.5 weeks
- **Testing**: 1 week

**Total**: 4-5 weeks

## Decision Point

### Option A: OpenRewrite (Recommended)
**Pros**:
- 80% faster
- Consistent changes
- Less human error
- Industry standard tool

**Cons**:
- Requires review of automated changes
- May miss edge cases
- Learning curve for tool

### Option B: Manual Migration (Original Plan)
**Pros**:
- Complete control
- Deep understanding of changes
- No tool dependencies

**Cons**:
- Very time-consuming
- Higher error risk
- Tedious for 353 files

### Recommendation
**Use OpenRewrite** for bulk migration, then manual review and fixes. This balances speed with quality control.

## Current Build Status

### Root POM
**Status**: ⚠️ Partially migrated, won't build yet

**Issues**:
- Module POMs not updated
- Some dependency versions missing
- Swagger references still present in modules

### Modules
**Status**: ❌ Not migrated

**Blockers**:
- Need root POM completion first
- Need dependency version alignment
- Need Swagger → SpringDoc migration

## Files Modified

1. ✅ `pom.xml` (root) - Partially migrated to Spring Boot 3 + jakarta
2. ✅ `migration-helper-pom.xml` - OpenRewrite configuration created

## Files Pending

1. ⏳ `pom.xml` (root) - Complete migration
2. ⏳ `sm-core-model/pom.xml`
3. ⏳ `sm-core/pom.xml`
4. ⏳ `sm-core-modules/pom.xml`
5. ⏳ `sm-shop-model/pom.xml`
6. ⏳ `sm-shop/pom.xml`
7. ⏳ All 353 Java files (via OpenRewrite)
8. ⏳ `MultipleEntryPointsSecurityConfig.java` (manual refactor)
9. ⏳ All controller files (Swagger → SpringDoc)
10. ⏳ `application.properties` (Spring Boot 3 updates)

## Rollback Plan

Changes not yet committed. To rollback:
```bash
git checkout pom.xml
git clean -f migration-helper-pom.xml
```

## Next Session Plan

1. **Complete root POM** (30 min)
2. **Update all module POMs** (1 hour)
3. **Run OpenRewrite migration** (30 min)
4. **Review and commit** (30 min)
5. **Test build** (30 min)
6. **Fix compilation errors** (1-2 hours)

**Total**: 4-5 hours for working build

## Lessons Learned

### What Worked
- Identifying OpenRewrite as better approach
- Partial POM migration shows path forward
- Clear documentation of changes

### Challenges
- Scale of manual migration underestimated
- Module POM dependencies complex
- Spring Boot 3 has many breaking changes

### Adjustments
- Switched to OpenRewrite approach
- Will do bulk migration then manual review
- Focus on getting build working first, then fix runtime issues

## References

- [OpenRewrite Jakarta Migration](https://docs.openrewrite.org/recipes/java/migrate/jakarta)
- [Spring Boot 3.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)
- [Spring Security 6.0 Migration](https://docs.spring.io/spring-security/reference/6.0/migration/index.html)
- [Hibernate 6.0 Migration Guide](https://github.com/hibernate/hibernate-orm/blob/6.0/migration-guide.adoc)

---

**Phase 2 Status**: 🔄 IN PROGRESS (10% complete)  
**Approach**: Hybrid (OpenRewrite + Manual)  
**Next**: Complete POM migrations, run OpenRewrite  
**Blocker**: None (clear path forward)
