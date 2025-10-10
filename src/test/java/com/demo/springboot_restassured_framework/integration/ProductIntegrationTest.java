package com.demo.springboot_restassured_framework.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

//    If you haven’t yet run a real integration test using an H2 in-memory database, that’s one small piece left.
//✅ That test spins up the full Spring Boot context (Controller + Service + Repository + H2).
//
//   ➡ Status: ⚠️ Just do 1-2 integration tests like below to fully complete Phase 2.

 /*   String json = """
        {
            "name": "Samsung TV",
            "description": "QLED 4K",
            "price": 45000,
            "quantity": 2
        }
        """;

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Samsung TV"));

  */
}


/*
*⚡ Summary
Topic	Status	Notes
Unit Tests (Service layer)	✅	Excellent coverage
Controller Slice Tests (@WebMvcTest)	✅	Done
Repository Slice Tests (@DataJpaTest)	⚠️	Add 1 simple example
Integration Tests (@SpringBootTest + H2)	⚠️	Add 1–2 examples
Assertions (assertThat, JSONPath)	✅	Done
 */