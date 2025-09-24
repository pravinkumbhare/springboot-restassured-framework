package com.demo.springboot_restassured_framework.service;

import com.demo.springboot_restassured_framework.entity.Pet;
import com.demo.springboot_restassured_framework.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;


    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> getAllPets(){
        return petRepository.findAll();
    }

    public Pet getPetById(Long id){

        return petRepository.findById(id).orElse(null);
    }

    public Pet createPet(Pet pet){

        return petRepository.save(pet);
    }

    public Pet updatePet(Long id, Pet petDetails){

        Pet pet = petRepository.findById(id).orElse(null);
        if(pet!=null){
            pet.setName(petDetails.getName());
            pet.setAge(petDetails.getAge());
            return petRepository.save(pet);
        }
        return null;
    }

//    public void deletePetById(Long id){
//        petRepository.deleteById(id);
//    }
    public boolean deletePetById(Long id){

        if(petRepository.existsById(id)){
            petRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public void deleteAllPets(Pet pet){
        petRepository.delete(pet);
    }

    public Pet save(Pet pet){
        return petRepository.save(pet);
    }
}
