package com.demo.springboot_restassured_framework.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * ✅ Ensures Flyway migrations run before Hibernate/JPA starts.
 * Prevents circular dependency between Flyway and EntityManagerFactory.
 */

@Configuration
@Profile("test")
public class FlywayConfig {
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return Flyway::migrate;
    }
}
