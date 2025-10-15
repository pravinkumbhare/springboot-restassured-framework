package com.demo.springboot_restassured_framework.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * ✅ This configuration disables Flyway for test profile only.
 * Prevents circular dependency between Flyway and Hibernate.
 */
@Configuration
//@EnableAutoConfiguration(exclude = {
//        // remove FlywayAutoConfiguration exclusion now!
//})
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class})
public class TestConfig {
}