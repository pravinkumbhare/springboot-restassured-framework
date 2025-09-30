package com.demo.springboot_restassured_framework.controller;

import com.demo.springboot_restassured_framework.entity.Employee;
import com.demo.springboot_restassured_framework.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeService.getEmployeeById(id);
        if(employee!=null){
            return ResponseEntity.ok(employee);
//            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee body){
        Employee employee = employeeService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    /*@PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee body){
        return employeeService.updateEmployee(id, body);
    }*/
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee body){
        Employee employee = employeeService.getEmployeeById(id);
        Employee updated = employeeService.updateEmployee(id, body);
        if(updated!=null){
            return ResponseEntity.ok(updated);

        }else {
            return ResponseEntity.notFound().build();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
//        // Above code is commented because it handle via custom exception and never give null value
//        Employee updated = employeeService.updateEmployee(id, body);
//        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id){
        boolean flag = employeeService.deleteEmployeeById(id);
        if (flag){
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }

//        employeeService.deleteEmployeeById(id);
//        return ResponseEntity.noContent().build();
    }

}
