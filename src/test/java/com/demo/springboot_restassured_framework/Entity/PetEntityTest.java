package com.demo.springboot_restassured_framework.Entity;

import com.demo.springboot_restassured_framework.entity.Pet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PetEntityTest {

    @Test
    @DisplayName("Should assign value through constructor")
    void testEntityConstructor(){

        Pet pet = new Pet("Prabhat", 10);
        assertThat(pet.getName()).isEqualTo("Prabhat");
        assertThat(pet.getAge()).isEqualTo(10);
        assertThat(pet.getId()).isNotEqualTo("");
    }

}
