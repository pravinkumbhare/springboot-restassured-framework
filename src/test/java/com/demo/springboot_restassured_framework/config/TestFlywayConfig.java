///*
//package com.demo.springboot_restassured_framework.config;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
//public class TestFlywayConfig {
//
//    @Bean(initMethod = "migrate")
//    public Flyway flyway(org.springframework.core.env.Environment env) {
//        return Flyway.configure()
//                .dataSource(
//                        env.getProperty("spring.datasource.url"),
//                        env.getProperty("spring.datasource.username"),
//                        env.getProperty("spring.datasource.password"))
//                .locations(env.getProperty("spring.flyway.locations", "classpath:db/migration"))
//                .load();
//    }
//}
//*/
