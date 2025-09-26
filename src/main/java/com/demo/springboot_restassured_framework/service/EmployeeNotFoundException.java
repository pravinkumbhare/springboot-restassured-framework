package com.demo.springboot_restassured_framework.service;

public class EmployeeNotFoundException extends RuntimeException{

    public EmployeeNotFoundException(Long id){
        super("Employee not found with Id : "+ id);
    }
}
