package com.demo.springboot_restassured_framework.repository;

import com.demo.springboot_restassured_framework.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

// EmployeeRepository is a Spring Data JPA repository.
// Spring automatically creates a bean (an implementation of that interface) at runtime.

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
