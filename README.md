# springboot-restassured-framework
Spring Boot with Rest Assured Framework for API and Unit Testing, with Addon Integrated with Gatling for Performance Testing

## Overview

    This repository demonstrates a small Spring Boot + Rest Assured test framework and examples for:
    
    Unit tests (service) using Mockito + JUnit 5
    
    Slice tests for controllers (@WebMvcTest) and repositories (@DataJpaTest)
    
    Full integration tests (@SpringBootTest + H2 in-memory DB)
    
    Test reporting using ExtentReports (HTML created by framework) and Allure (JSON results → HTML report)

## Prerequisites

    Java 17 (project is configured to compile with source/target 17)
    
    Maven 3.6+ (Maven installed and available in PATH)
    
    Optional: Allure CLI (for using allure generate locally)
    
    Install from https://docs.qameta.io/allure/
    
    IDE: IntelliJ IDEA / VS Code (recommended)


## Project structure
    
    restassured-demo/
    ├── src/
    │ ├── main/
    │ │ └── java/com/demo/
    │ │ ├── controller/ # REST controllers
    │ │ ├── service/ # Business services
    │ │ ├── repository/ # Spring Data JPA repositories
    │ │ └── model/ # Entities/DTOs
    │ └── test/
    │ └── java/com/demo/
    │ ├── controller/ # @WebMvcTest
    │ ├── service/ # Mockito unit tests
    │ └── repository/ # @DataJpaTest
    └── pom.xml

Key files you will find in this repo (examples used in docs): User, UserRepository, UserService, UserController and tests for each layer.

## How to build & run tests
    
    From the project root:
    
    Clean & run tests
    
    mvn clean test
    
    Run a single test class (example)
    
    mvn -Dtest=com.demo.service.UserServiceTest test
    
    Notes:
    
    mvn clean test runs unit and slice tests. Integration tests annotated with @SpringBootTest will also run unless you mark them with a profile or a tag and exclude them.
    
    Service unit tests (Mockito) do not load Spring context and therefore run fastest.


    Report generation (Allure & Extent)
    Extent Report
    
    The framework produces an extent-report.html in target/ (depending on your test setup). Open that file directly in your browser.
    
    Allure Report - Manual flow (CLI)
    
    If your tests produce allure-results (JSON + attachments), you can create the HTML report with the Allure CLI:

# From project root (adjust path to results if different)
allure generate target/allure-results --clean -o target/allure-report
# open report folder or use serve
allure open target/allure-report
# OR
allure serve target/allure-results


Allure via Maven plugin

If you prefer generating Allure report using Maven, the project contains the allure-maven plugin. You can run:

mvn allure:report
# or serve
mvn allure:serve

If you want the Allure HTML report generated automatically when you run mvn test, consider binding the plugin to the test phase in the pom.xml. Example (optional):

    <plugin>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-maven</artifactId>
    <version>2.12.0</version>
    <executions>
    <execution>
    <phase>test</phase>
    <goals>
    <goal>report</goal>
    </goals>
    </execution>
    </executions>
    </plugin>


Where to look for results

target/allure-results/ (raw results created by Allure listener)

target/allure-report/ or target/site/allure-maven-plugin/ (generated HTML)

Testing strategy & examples

Short summary of the test types used in the project and example location references.

1. Unit tests (Service layer) — Fastest

Use Mockito and JUnit 5.

No Spring context is loaded.

Example: src/test/java/com/demo/service/UserServiceTest.java

2. Repository tests (@DataJpaTest) — Fast

Tests JPA repositories with in-memory H2 DB.

Example: src/test/java/com/demo/repository/UserRepositoryTest.java

Example annotation:
    
    @DataJpaTest
    class UserRepositoryTest { ... }

3. Controller tests (@WebMvcTest) — Medium

Loads only web layer (controllers) and mocks services with @MockBean.

Example: src/test/java/com/demo/controller/UserControllerTest.java

4. Full integration tests (@SpringBootTest) — Slowest

Loads entire Spring context, embedded server and H2 DB.

Example: src/test/java/com/demo/integration/UserIntegrationTest.java

Example annotation:
    
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class UserIntegrationTest { ... }

application-test.properties (recommended)

Create src/test/resources/application-test.properties or use application.properties under test resources to ensure integration tests use H2 and do not run migrations against real DB.

# Use in-memory H2 for tests
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true


# Disable DB migrations during tests (if using Flyway/Liquibase)
spring.flyway.enabled=false
spring.liquibase.enabled=false

Troubleshooting & common issues

Allure results not generated after mvn test

Ensure you have io.qameta.allure:allure-junit5 (and/or allure-junit-platform) as a test dependency.

Avoid overriding JUnit platform properties incorrectly in the Surefire configuration. If you previously set a systemPropertyVariables entry like junit.platform.listeners=io.qameta.allure.junitplatform.AllureJunitPlatform, try removing it — allure-junit5 normally registers listeners automatically.

If target/allure-results is still missing

Run tests with debug logging: mvn clean test -X and inspect test execution logs for JUnit platform / Allure listener mentions.

Confirm tests are executed by JUnit 5 (no fallback to JUnit 4).

Try launching with an explicit system property temporarily to validate:

mvn clean test -Djunit.platform.listeners=io.qameta.allure.junitplatform.AllureJunitPlatform

## CI / Jenkins notes

A typical Jenkins pipeline stage for tests + reports:
    
    stage('Run Tests') {
    steps {
    sh 'mvn clean test'
    }
    }
    
    
    stage('Generate Allure') {
    steps {
    // Option A: use Allure Maven plugin
    sh 'mvn allure:report'
    
    
    // Option B: use Allure CLI
    // sh 'allure generate target/allure-results --clean -o target/allure-report'
    }
    }
    
    
    stage('Archive Reports') {
    steps {
    archiveArtifacts artifacts: 'target/allure-report/**', fingerprint: true
    archiveArtifacts artifacts: 'target/extent-report.html', fingerprint: true
    }
    }

Note: Adjust sh/bat commands for Windows nodes.