# Task 2 Complete: Java 21 Build Environment Setup

**Date**: March 13, 2026  
**Status**: ✅ COMPLETE

## Changes Implemented

### 1. Git Branch Created
- **Branch**: `feature/java21-migration`
- **Purpose**: Isolate Java 21 migration work from main branch
- **Status**: ✅ Created and checked out

### 2. Maven Profile for Java 21
**File**: `pom.xml` (root)

Added Java 21 profile that can be activated with `-Pjava21`:

```xml
<profiles>
    <profile>
        <id>java21</id>
        <properties>
            <java.version>21</java.version>
            <maven.compiler.source>21</maven.compiler.source>
            <maven.compiler.target>21</maven.compiler.target>
        </properties>
    </profile>
</profiles>
```

**Usage**:
- Default build (Java 17): `./mvnw clean install`
- Java 21 build: `./mvnw clean install -Pjava21`

### 3. Maven Enforcer Plugin
**File**: `pom.xml` (root)

Added Maven Enforcer Plugin to validate Java version:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-enforcer-plugin</artifactId>
    <version>3.4.1</version>
    <executions>
        <execution>
            <id>enforce-java</id>
            <goals>
                <goal>enforce</goal>
            </goals>
            <configuration>
                <rules>
                    <requireJavaVersion>
                        <version>[17,)</version>
                        <message>Java 17 or higher is required for this build</message>
                    </requireJavaVersion>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**Purpose**: Ensures builds fail fast if Java version is below 17

### 4. SDKMAN Configuration
**File**: `.sdkmanrc`

Updated to document both Java versions:

```bash
# Java 17 (current production version)
java=17.0.17-tem
# Java 21 (migration target - use: sdk use java 21.0.2-tem)
# java=21.0.2-tem
```

**Switching Java versions**:
```bash
# Use Java 17 (default)
sdk use java 17.0.17-tem

# Use Java 21 for testing
sdk use java 21.0.2-tem
```

### 5. Docker Configuration for Java 21
**File**: `sm-shop/Dockerfile.java21`

Created separate Dockerfile for Java 21 builds:

```dockerfile
FROM eclipse-temurin:21-jre
RUN mkdir /opt/app
RUN mkdir /files
COPY target/shopizer.jar /opt/app
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/app/shopizer.jar"]
```

**Building Docker images**:
```bash
# Java 17 image (existing)
docker build -f sm-shop/Dockerfile -t shopizer:java17 .

# Java 21 image (new)
docker build -f sm-shop/Dockerfile.java21 -t shopizer:java21 .
```

### 6. CircleCI Configuration
**File**: `.circleci/config.yml`

Added Java 21 executor and build job:

**New Executor**:
```yaml
shopizer-ci-java21:
  docker:
    - image: eclipse-temurin:21-jdk
      auth:
        username: shopizerecomm
        password: $DOCKERHUB_PASSWORD
```

**New Job**:
```yaml
build-java21:
  executor: shopizer-ci-java21
  steps:
    - checkout
    - run: echo "shopizer build and test with Java 21"
    - run:
        name: Build with Java 21 profile
        command: |
          set -x
          ./mvnw clean install -Pjava21
    - persist_to_workspace:
        root: .
        paths:
          - ./sm-shop
```

**Updated Workflow**:
```yaml
workflows:
  build_and_deploy:
    jobs:
      - build          # Java 17 (existing)
      - build-java21   # Java 21 (new, runs in parallel)
      - deploy:
          requires:
            - build
```

## Build Verification

### Java 17 Build (Default) ✅
```bash
$ ./mvnw clean compile
[INFO] Compiling 186 source files (sm-core-model)
[INFO] Compiling 14 source files (sm-core-modules)
[INFO] Compiling 335 source files (sm-core)
[INFO] Compiling 327 source files (sm-shop-model)
[INFO] Compiling 313 source files (sm-shop)
[INFO] BUILD SUCCESS
```

**Total source files**: 1,175 files compiled successfully

### Java 21 Build (Profile)
**Note**: Requires Java 21 to be installed locally
```bash
# Install Java 21 via SDKMAN
sdk install java 21.0.2-tem

# Switch to Java 21
sdk use java 21.0.2-tem

# Build with Java 21 profile
./mvnw clean install -Pjava21
```

## Dual-Version Support Strategy

### Development Workflow
1. **Default**: All developers continue using Java 17
2. **Testing**: CI/CD runs both Java 17 and Java 21 builds in parallel
3. **Migration**: Gradual transition as we complete namespace migration

### CI/CD Pipeline
- **Java 17 build**: Continues as primary build, deploys to production
- **Java 21 build**: Runs in parallel, validates compatibility, no deployment yet
- **Failure handling**: Java 21 build failures don't block Java 17 deployment

### Docker Strategy
- **Blue environment**: Uses `Dockerfile` (Java 17)
- **Green environment**: Uses `Dockerfile.java21` (Java 21)
- **Load balancer**: Routes traffic between environments

## Next Steps

### Immediate (Task 3)
- [ ] Upgrade Spring Boot to 2.7.18
- [ ] Test both Java 17 and Java 21 builds with Spring Boot 2.7.18
- [ ] Verify all tests pass

### Future (Phase 2)
- [ ] Begin namespace migration (javax → jakarta)
- [ ] Requires Spring Boot 3.x (incompatible with current setup)
- [ ] Java 21 profile will be essential for testing

## Testing Instructions

### Local Testing
```bash
# Test Java 17 build (default)
./mvnw clean test

# Test Java 21 build (requires Java 21 installed)
sdk use java 21.0.2-tem
./mvnw clean test -Pjava21

# Switch back to Java 17
sdk use java 17.0.17-tem
```

### Docker Testing
```bash
# Build and run Java 17 version
./mvnw clean package
docker build -f sm-shop/Dockerfile -t shopizer:java17 sm-shop
docker run -p 8080:8080 shopizer:java17

# Build and run Java 21 version
./mvnw clean package -Pjava21
docker build -f sm-shop/Dockerfile.java21 -t shopizer:java21 sm-shop
docker run -p 8081:8080 shopizer:java21
```

### CI/CD Testing
- Push to `feature/java21-migration` branch
- CircleCI will run both `build` (Java 17) and `build-java21` jobs
- Monitor both builds for success

## Files Modified

1. ✅ `pom.xml` - Added Java 21 profile and Maven Enforcer plugin
2. ✅ `.sdkmanrc` - Documented Java 21 option
3. ✅ `sm-shop/Dockerfile.java21` - Created Java 21 Dockerfile
4. ✅ `.circleci/config.yml` - Added Java 21 executor, job, and workflow

## Success Criteria

- [x] Git branch `feature/java21-migration` created
- [x] Maven profile for Java 21 added
- [x] Maven Enforcer plugin configured
- [x] .sdkmanrc updated with Java 21 option
- [x] Dockerfile.java21 created
- [x] CircleCI config updated with Java 21 build
- [x] Java 17 build verified successful
- [ ] Java 21 build verified successful (requires Java 21 installation)

## Known Limitations

1. **Java 21 not installed locally**: Cannot test Java 21 build locally yet
2. **Spring Boot 2.5.12**: Not optimized for Java 21, will improve with 2.7.x upgrade
3. **javax namespace**: Still using javax, will migrate in Phase 2

## Recommendations

1. **Install Java 21**: Team members should install Java 21 for local testing
   ```bash
   sdk install java 21.0.2-tem
   ```

2. **Test both versions**: Regularly test with both Java 17 and 21 profiles

3. **Monitor CI/CD**: Watch for Java 21 build failures in CircleCI

4. **Document issues**: Track any Java 21-specific issues in migration tracker

---

**Task 2 Status**: ✅ COMPLETE  
**Next Task**: Task 3 - Upgrade Spring Boot to 2.7.x  
**Estimated Duration for Task 3**: 3-5 days
