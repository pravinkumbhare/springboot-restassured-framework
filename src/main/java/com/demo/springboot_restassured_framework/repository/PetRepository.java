package com.demo.springboot_restassured_framework.repository;

import com.demo.springboot_restassured_framework.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

// PetRepository is a Spring Data JPA repository.
// Spring automatically creates a bean (an implementation of that interface) at runtime.

public interface PetRepository extends JpaRepository<Pet, Long> {
}
