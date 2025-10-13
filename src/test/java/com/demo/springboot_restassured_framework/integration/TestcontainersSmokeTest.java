package com.demo.springboot_restassured_framework.integration;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestcontainersSmokeTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void testContainerStarts() {
        postgres.start();
        System.out.println("✅ PostgreSQL container started at: " + postgres.getJdbcUrl());
        postgres.stop();
    }
}
