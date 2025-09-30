package com.demo.springboot_restassured_framework.controller;

import com.demo.springboot_restassured_framework.entity.Employee;
import com.demo.springboot_restassured_framework.service.EmployeeNotFoundException;
import com.demo.springboot_restassured_framework.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    private Employee employee;

    @BeforeEach
    void setUp(){

        employee = new Employee();
        employee.setEmpId(1L);
        employee.setEmpName("Rajeev Singh");
        employee.setEmpDesignation("Product Owner");
        employee.setEmpSalary(30000L);
    }

    @Test
    @DisplayName("Should return all employees")
    void testGetAllEmployees() throws Exception {

        // Mock Service Layer
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        // Inject into Controller
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empName").value("Rajeev Singh"))
                .andExpect(jsonPath("$[0].empDesignation").value("Product Owner"))
                .andExpect(jsonPath("$[0].empSalary").value(30000));

        // Assertion and Verify
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("Should return employee by Id")
    void testGetEmployeeById() throws Exception {

        // Mock Service layer
        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        // Inject into Controller layer
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("empName").value("Rajeev Singh"))
                .andExpect(jsonPath("empDesignation").value("Product Owner"))
                .andExpect(jsonPath("empSalary").value(30000));

        // Assertion and Verify
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @DisplayName("Should return exception when employee id is not exist")
    void testGetEmployeeById_NotFound() throws Exception {

        // Mock Service Layer
        when(employeeService.getEmployeeById(99L)).thenReturn(null);

        // Inject into Controller Layer
        mockMvc.perform(get("/api/employees/{id}", 99L))
                .andExpect(status().isNotFound());

        // Assertion and Verification
        verify(employeeService, times(1)).getEmployeeById(99L);
    }

    @Test
    @DisplayName("Should return created employee")
    void testCreateEmployee() throws Exception {

        Employee employee1 = new Employee();
        employee1.setEmpId(2L);
        employee1.setEmpName("Vinay Shan");
        employee1.setEmpDesignation("IT Support");
        employee1.setEmpSalary(35000L);

        // Mock service layer
        when(employeeService.save(any(Employee.class))).thenReturn(employee1);

        // inject into controller layer
        mockMvc.perform(post("/api/employees")
                        .contentType("application/json")
                        .content(
                                """
                                {
                                    "empName": "Vinay Shan",
                                    "empDesignation": "IT Support",
                                    "empSalary": 35000
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empName").value("Vinay Shan"))
                .andExpect(jsonPath("$.empDesignation").value("IT Support"))
                .andExpect(jsonPath("$.empSalary").value(35000L))
                .andExpect(jsonPath("$.empId").value(2L));
        // assertion and verification
        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when required fields are missing")
    void testCreateEmployee_BadRequest() throws Exception {
        mockMvc.perform(post("/api/employees")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update the created employee")
    void testUpdateEmployee() throws Exception {

        Employee updateEmployee = new Employee();
        updateEmployee.setEmpId(2L);
        updateEmployee.setEmpName("Rajesh Rao");
        updateEmployee.setEmpDesignation("IT Admin");
        updateEmployee.setEmpSalary(35000L);

        // Mock Service Layer
        when(employeeService.updateEmployee(eq(2L), any(Employee.class))).thenReturn(updateEmployee);

        // Inject into Controller Layer
        mockMvc.perform(put("/api/employees/{id}", 2L)
                .contentType("application/json")
                .content(
                        """
                               {
                                   "empName": "Rajesh Rao",
                                   "empDesignation": "IT Admin",
                                   "empSalary": 35000
                               }
                        """
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empName").value("Rajesh Rao"))
                .andExpect(jsonPath("$.empDesignation").value("IT Admin"))
                .andExpect(jsonPath("$.empSalary").value(35000L));

        // Assertion and Verification
        verify(employeeService, times(1)).updateEmployee(eq(2L), any(Employee.class));

    }

    @Test
    @DisplayName("Should return bad request on updating the employee")
    void testUpdateEmployee_NotFound() throws Exception {

        // Mock Service Layer
        when(employeeService.updateEmployee(eq(99L), any(Employee.class))).thenReturn(null);

        // Inject into Controller Layer
        mockMvc.perform(put("/api/employees/{id}", 99L)
                        .contentType("application/json")
                        .content(
                                """
                                {}
                                """
                        ))
                .andExpect(status().isNotFound());

        // Assertion and Verification
        verify(employeeService, times(1)).updateEmployee(eq(99L), any(Employee.class));

    }

    @Test
    @DisplayName("Should delete the employee")
    void testDeleteEmployee() throws Exception {

        // Mock Service Layer
        when(employeeService.deleteEmployeeById(1L)).thenReturn(true);

        // First delete → success
        mockMvc.perform(delete("/api/employees/{id}", 1L))
                .andExpect(status().isNoContent());

//        // Second delete → exception → controller should map to 404
//        mockMvc.perform(delete("/api/employees/{id}", 1L))
//                .andExpect(status().isNotFound());

        // Assertion and Verification
        verify(employeeService, times(1)).deleteEmployeeById(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existing employee")
    void testDeleteEmployee_NotFound() throws Exception {

//        // Mock Service Layer
        when(employeeService.deleteEmployeeById(99L)).thenReturn(false);

        // Inject into Controller Layer
        mockMvc.perform(delete("/api/employees/{id}", 99))
                .andExpect(status().isNotFound());

        // Assertion and Verify
        verify(employeeService, times(1)).deleteEmployeeById(99L);
    }
}
