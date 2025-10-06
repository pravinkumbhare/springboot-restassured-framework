package com.demo.springboot_restassured_framework.controller;

import com.demo.springboot_restassured_framework.entity.Pet;
import com.demo.springboot_restassured_framework.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PetController.class)
public class PetControllerTest {

    @MockitoBean
    private PetService petService;

    @Autowired
    private MockMvc mockMvc;

    private Pet pet;

    @BeforeEach
    void setUp(){

        pet = new Pet();
        pet.setId(1);
        pet.setName("Pet Name");
        pet.setAge(4);
    }

    @Test
    @DisplayName("Should delete all Pet")
    void testDeletePet() throws Exception {

        Pet pet1 = new Pet();
        pet1.setId(2);
        pet1.setName("Pet2 Name");
        pet1.setAge(6);

        // Mock Service Layer
        doNothing().when(petService).deleteAllPets(pet1);

        // Inject into Controller Layer
        mockMvc.perform(delete("/api/pets")
                        .contentType("application/json")
                        .content(
                                """
                                {
                                    "id": 2,
                                    "name": "Pet2 Name",
                                    "age": 6
                                }
                                """))
                .andExpect(status().isNoContent());

        // Assertion and Verification
        verify(petService, times(1)).deleteAllPets(any(Pet.class));

    }

}
