package com.demo.springboot_restassured_framework.controller;

import com.demo.springboot_restassured_framework.entity.Pet;
import com.demo.springboot_restassured_framework.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public List<Pet> getAllPets(){
        return petService.getAllPets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id){

        Pet pet = petService.getPetById(id);
        if(pet != null){
            return ResponseEntity.ok(pet);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet body){
        Pet savedPet = petService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPet);
    }

    @PutMapping("/{id}")
    public Pet updatePetById(@PathVariable Long id, @RequestBody Pet body){

        return petService.updatePet(id, body);
    }

    /* @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable Long id){
        petService.deletePetById(id);
        return ResponseEntity.noContent().build();
    }*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetById(@PathVariable Long id){

        boolean flag = petService.deletePetById(id);
        if (flag){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPets(Pet pet){
        petService.deleteAllPets(pet);
        return ResponseEntity.noContent().build();
    }
}
