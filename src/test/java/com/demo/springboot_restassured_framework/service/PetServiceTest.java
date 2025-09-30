package com.demo.springboot_restassured_framework.service;

import com.demo.springboot_restassured_framework.entity.Pet;
import com.demo.springboot_restassured_framework.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    private Pet pet;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        pet = new Pet();
        pet.setId(1);
        pet.setName("Vijay Anand");
        pet.setAge(32);
    }

    @Test
    @DisplayName("Should return null for non-existing updated id")
    void testUpdatePet_NotFound(){

        Pet updatePet = new Pet();
        updatePet.setId(1);
        updatePet.setName("Vijay Anand");
        updatePet.setAge(32);

        // Mock Pet Repository Layer
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        // Inject into Service Layer
        petService.updatePet(99L, updatePet);

        // Assertion and Verification
        verify(petRepository, times(1)).findById(99L);
        verify(petRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return 404 for not creating Pet")
    void testCreatePet_NotFound(){

        Pet pet1 = new Pet();
        // Mock Repository Layer
        when(petRepository.save(pet1)).thenReturn(pet1);

        // Inject into Service Layer
        petService.createPet(pet1);

        // Assertion and Verification
        verify(petRepository, times(1)).save(pet1);

    }

    @Test
    @DisplayName("Should delete the Pet")
    void testDeletePet(){

        pet = new Pet();
        pet.setId(2);
        pet.setName("Ajay Rao");
        pet.setAge(30);

        // Mock
        when(petRepository.findAll()).thenReturn(List.of(pet));

        // Inject
        petService.deleteAllPets(pet);

        // Assertion
//        verify(petRepository, times(1)).findAll();
        verify(petRepository, times(1)).delete(pet);
        verify(petRepository, never()).deleteAll();

    }
}
