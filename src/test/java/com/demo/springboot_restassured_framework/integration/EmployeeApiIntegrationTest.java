package com.demo.springboot_restassured_framework.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Step 4: Tell Spring Boot Tests to Use H2
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeApiIntegrationTest {

    @LocalServerPort
    private int port;

    private int empId;          // Employee created for each test
    private String empName;     // Unique name per test

    @BeforeEach
    void setup(TestInfo testInfo) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api/employees";

        System.out.println("Port no. is : "+ port);
        // Generate unique name with test method name + random UUID
        empName = testInfo.getDisplayName() + "-" + UUID.randomUUID().toString().substring(0, 6);

        String newEmployee = String.format("""
            {
                "empName": "%s",
                "empDesignation": "QA Engineer",
                "empSalary": 50000
            }
            """, empName);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(newEmployee)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("empName", equalTo(empName))
                .body("empDesignation", equalTo("QA Engineer"))
                .body("empSalary", equalTo(50000))
                .body("empId", notNullValue())
                .extract()
                .response();

        empId = response.jsonPath().getInt("empId");
        System.out.println("Created employee for test: " + empName + " with ID " + empId);
    }

    @AfterEach
    void cleanup() {
        given()
                .when()
                .delete("/" + empId)
                .then()
                .statusCode(anyOf(is(204), is(404)));
        System.out.println("Cleaned up employee with ID " + empId);
    }

    @Test
    @Order(1)
    void testGetEmployeeById() {
        given()
                .when()
                .get("/" + empId)
                .then()
                .statusCode(200)
                .body("empId", equalTo(empId))
                .body("empName", equalTo(empName));
    }

    @Test
    @Order(2)
    void testUpdateEmployee() {
        String updatedEmployee = """
            {
                "empName": "UpdatedUser",
                "empDesignation": "Senior QA",
                "empSalary": 75000
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(updatedEmployee)
                .when()
                .put("/" + empId)
                .then()
                .statusCode(200)
                .body("empId", equalTo(empId))
                .body("empName", equalTo("UpdatedUser"))
                .body("empSalary", equalTo(75000));
    }

    @Test
    @Order(3)
    void testGetAllEmployees() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(4)
    void testDeleteEmployee() {
        // Delete employee explicitly
        given()
                .when()
                .delete("/" + empId)
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .when()
                .get("/" + empId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(5)
    void testGetNonExistingEmployee() {
        given()
                .when()
                .get("/99999")
                .then()
                .statusCode(404);
    }


   /* @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void verifyPreloadedEmployees() {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM employee", Integer.class);
        System.out.println("Employees in DB at startup: " + count);
    }*/
}
