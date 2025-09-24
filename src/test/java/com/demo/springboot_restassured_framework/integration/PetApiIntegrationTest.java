package com.demo.springboot_restassured_framework.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Optional: ensure test order
public class PetApiIntegrationTest {

    @LocalServerPort
    private int port;

    private int petId; // dynamically created pet ID

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/pets";

        // Create a pet dynamically before each test
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
        // Delete the pet after each test
        given()
                .when()
                .delete("/" + petId)
                .then()
                .statusCode(anyOf(is(204), is(404))); // 404 if already deleted
    }

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    public void testGetPets() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(3)
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

    @Test
    @Order(4)
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

    @Test
    @Order(5)
    public void testDeletePetById() {
        given()
                .when()
                .delete("/" + petId)
                .then()
                .statusCode(204);

//        // Verify deletion
//        given()
//                .when()
//                .get("/" + petId)
//                .then()
//                .statusCode(404);
    }

    // Negative test example
    @Test
    @Order(6)
    public void testGetNonExistingPet() {
        given()
                .when()
                .get("/9999")
                .then()
                .statusCode(404);
    }

    @Test
    @Order(7)
    public void testDeletePetByIds() {
        given()
                .when()
                .delete("/" + 8888)
                .then()
                .statusCode(404);
    }
}
