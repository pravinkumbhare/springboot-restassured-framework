package com.demo.springboot_restassured_framework.service;

import com.demo.springboot_restassured_framework.entity.Employee;
import com.demo.springboot_restassured_framework.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;  // Mock repository

    @InjectMocks
    private EmployeeService employeeService;        // Injects mock into service

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setEmpId(1L);
        employee.setEmpName("John");
        employee.setEmpDesignation("Developer");
        employee.setEmpSalary(50000L);
    }

    @Test
    @DisplayName("Should return all the employees")
    void testGetAllEmployees(){

        // Mock Repository Layer
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));

        // Inject in Service Layer
        List<Employee> employees = employeeService.getAllEmployees();

//        assertEquals(1, employees.size());
        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getEmpName()).isEqualTo("John");
        assertThat(employees.get(0).getEmpDesignation()).isEqualTo("Developer");
        assertThat(employees.get(0).getEmpSalary()).isEqualTo(50000L);

        // Specifies that this method should be called exactly once. and ensures correct repository interaction.
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return employee by Id")
    void testGetEmployeeById(){

        // Mock Repository Layer
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Inject in Service Layer
        Employee employeeById = employeeService.getEmployeeById(1L);

        // Assertion
        assertThat(employeeById).isNotNull();
        assertThat(employeeById.getEmpName()).isEqualTo("John");
        verify(employeeRepository, times(1)).findById(1L);
//        [OR]
        assertThat(employeeById).extracting(Employee::getEmpName).isSameAs("John");
    }

    @Test
    @DisplayName("Should throw exception when employee by Id not found")
    void testGetEmployeeById_NotFound(){

        // Mock Repository Layer
        when(employeeRepository.findById(3L)).thenReturn(Optional.empty());

        // Inject into Service Layer
         EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class ,() -> {

             Employee nonExistingEmployee = employeeService.getEmployeeById(3L);
             assertThat(nonExistingEmployee).isNull();
         });

         assertThat(exception.getMessage()).isEqualTo("Employee not found with Id : 3");
        // Assertion and Verification
        verify(employeeRepository, times(1)).findById(3L);


    }

    @Test
    @DisplayName("Should create new Employee successfully")
    void testCreateEmployee(){

        // Mock Repository Layer
        when(employeeRepository.save(employee)).thenReturn(employee);

//        Employee newEmployee = new Employee();
//        newEmployee.setEmpId(2L);
//        newEmployee.setEmpName("Amit Rathi");
//        newEmployee.setEmpDesignation("AVP");
//        newEmployee.setEmpSalary(75000L);

        // Inject into Service Layer
        Employee newEmp = employeeService.createEmployee(employee);

        // Assertion or Verification
        assertThat(newEmp).isNotNull();
        assertThat(newEmp.getEmpName()).isEqualTo("John");
        verify(employeeRepository, times(1)).save(newEmp);
    }

    @Test
    @DisplayName("Should update the Employee successfully")
    void testUpdateEmployee(){

        Employee updateEmployee = new Employee();
        updateEmployee.setEmpName("Pravin");
        updateEmployee.setEmpDesignation("SDET");
        updateEmployee.setEmpSalary(10L);

        // Mock Repository Layer
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updateEmployee);

        // Inject into Service Layer
        Employee updated = employeeService.updateEmployee(1L, updateEmployee);

        // Assertion and Verification
        assertThat(updated).isNotNull();
        assertThat(updated.getEmpName()).isEqualTo("Pravin");
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    @DisplayName("Should throw exception when update employee details with non-existing Id")
    void testUpdateEmployee_NotFound(){

        Employee updateEmployee = new Employee();
        updateEmployee.setEmpName("Navin");
        updateEmployee.setEmpDesignation("SDE");
        updateEmployee.setEmpSalary(15L);

        // Mock Repository Layer
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Inject into Service Layer
        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(99L, updateEmployee);
        });

        // Assertion and Verification
        assertThat(exception.getMessage()).isEqualTo("Employee not found with Id : 99");
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete employee successfully")
    void testDeleteEmployee(){

        // Mock Repository Layer
        when(employeeRepository.existsById(1L)).thenReturn(true);

        // Inject into Service Layer
        boolean deleted = employeeService.deleteEmployeeById(1L);

        // Assertion and Verification
        assertThat(deleted).isTrue();
        verify(employeeRepository, times(1)).deleteById(1L);
        verify(employeeRepository, times(1)).existsById(1L);
    }

    @Test
    @DisplayName("Should throw exception employee deleted successfully")
    void testDeleteEmployee_NotFound(){

        // Mock Repository Layer
        when(employeeRepository.existsById(99L)).thenReturn(false);

        // Inject into Service Layer
        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            boolean isDeleted = employeeService.deleteEmployeeById(99L);
        });

        // Assertion and Verification
        assertThat(exception.getMessage()).isEqualTo("Employee not found with Id : 99");
        verify(employeeRepository, times(1)).existsById(99L);
        verify(employeeRepository, never()).deleteById(99L);
    }

    @Test
    @DisplayName("Should be saved successfully")
    void testSaved(){

        // Mock Repository Layer
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Inject into Service Layer
        Employee result = employeeService.save(employee);

        // Assertion and Verification
        assertThat(result).isNotNull();
        assertThat(result.getEmpName()).isEqualTo("John");
        verify(employeeRepository, times(1)).save(employee);
    }

}