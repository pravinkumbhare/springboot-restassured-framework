package com.demo.springboot_restassured_framework.integration;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.flywaydb.core.Flyway;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("Pet Management with TestContainer")
@Feature("Create Employee API using Test Container")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                com.demo.springboot_restassured_framework.SpringbootRestassuredFrameworkApplication.class,
                com.demo.springboot_restassured_framework.config.TestConfig.class // ✅ added here
        }
)
@ActiveProfiles("test")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class PetApiTestContainerIntegrationTest {

    // ✅ Step 1: Define and manage container lifecycle automatically
    @Container
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres")
                    .withReuse(true);  // ✅ allows reuse across test runs

    static {
        // ✅ Run Flyway manually before Spring context initializes
        postgres.start(); // Start container early

        Flyway flyway = Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration")
                .load();

//        flyway.clean();   // optional — ensures fresh DB each run
        flyway.migrate(); // ✅ applies migrations

        // Now Spring can use this DB
    }

    // ✅ Step 2: Dynamically provide DB properties to Spring Boot
    @DynamicPropertySource
    static void registerPostgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");

        // ✅ Hibernate should NOT change schema
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");

        // ✅ Let Flyway manage schema
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");

        // ✅ Ensure Spring doesn’t try to run schema.sql
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @LocalServerPort
    private int port;

    private int petId;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/pets";

        String newPet = """
            {
                "name": "Buddy",
                "age": 2
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .response();

        petId = response.jsonPath().getInt("id");
    }

    @AfterEach
    void cleanup() {
        given()
                .when()
                .delete("/" + petId)
                .then()
                .statusCode(anyOf(is(204), is(404)));
    }

    @Test @Order(1)
    public void testCreatePet() {
        String newPet = """
            {
                "name": "Tommy",
                "age": 3
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(newPet)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("name", equalTo("Tommy"))
                .body("age", equalTo(3))
                .body("id", notNullValue());
    }

    @Test @Order(2)
    public void testGetPets() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test @Order(3)
    public void testGetPetById() {
        given()
                .when()
                .get("/" + petId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Buddy"))
                .body("age", equalTo(2))
                .body("id", equalTo(petId));
    }

    @Test @Order(4)
    public void testUpdatePetById() {
        String updatePet = """
            {
                "name": "Max",
                "age": 5
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updatePet)
                .when()
                .put("/" + petId)
                .then()
                .statusCode(200)
                .body("name", equalTo("Max"))
                .body("age", equalTo(5))
                .body("id", equalTo(petId));
    }

    @Test @Order(5)
    public void testDeletePetById() {
        given()
                .when()
                .delete("/" + petId)
                .then()
                .statusCode(204);
    }

    @Test @Order(6)
    public void testGetNonExistingPet() {
        given()
                .when()
                .get("/9999")
                .then()
                .statusCode(404);
    }

    @Test @Order(7)
    public void testDeleteNonExistingPet() {
        given()
                .when()
                .delete("/8888")
                .then()
                .statusCode(404);
    }
}
