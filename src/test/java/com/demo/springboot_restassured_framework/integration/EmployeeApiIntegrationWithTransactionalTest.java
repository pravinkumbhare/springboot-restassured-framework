package com.demo.springboot_restassured_framework.integration;

import com.demo.springboot_restassured_framework.entity.Employee;
import com.demo.springboot_restassured_framework.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// To make sure each test gets a fresh dataset automatically without Hibernate interfering with IDs. That’s very handy for integration tests.
// For clean and consistent test data in every integration test, we can combine H2 with Spring’s @Transactional test support or explicitly reload data.sql before each test. Here’s the most reliable approach

// Key Improvements
// @Transactional ensures rollback after each test → fresh DB every test.
// Preloaded data.sql guarantees initial employees (Alice, Bob, Charlie).
// Tests do not interfere with each other, even if inserts/updates/deletes happen.
// Assertions use AssertJ for readability.
// No need for manual cleanup of IDs (John Doe or others) anymore.

@SpringBootTest
@Transactional  // Automatically rolls back DB changes after each test, So even if a test inserts, updates, or deletes, the DB resets automatically for the next test.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)       //  reuse same test class instance across all test methods.
public class EmployeeApiIntegrationWithTransactionalTest {

    private static final Logger log = LogManager.getLogger(EmployeeApiIntegrationWithTransactionalTest.class);

    @Autowired      // This is a Spring annotation and it tells please inject the instance of this bean her or in this class.
    EmployeeRepository employeeRepository;

    /*@Autowired
    DataSource dataSource;      // gives you DB connection, This is an alternative to @Transactional, not needed if you already use rollback
    // Optional
    @BeforeEach
    void reloadTestData() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("schema.sql"));
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("data.sql"));
        }
    }*/

    @Test
    public void testGetAllEmployees(){

        List<Employee> allEmployees = employeeRepository.findAll();
        log.info("All Employees: {} ", allEmployees);

        for(Employee employee : allEmployees){
            System.out.println("Employee Id : "+ employee.getEmpId());
            System.out.println("Employee Name : "+ employee.getEmpName());
            System.out.println("Employee Salary : "+ employee.getEmpSalary());
            System.out.println("Employee Designation : "+ employee.getEmpDesignation());
        }

        Assertions.assertEquals(3, allEmployees.size());
        assertThat(allEmployees).hasSize(3);
        assertThat(allEmployees).extracting(Employee::getEmpName).containsExactlyInAnyOrder("Alice", "Bob", "Charlie");
    }

    @Test
    public void testCreateEmployee(){

        Employee employee = new Employee();
        employee.setEmpName("Pravin");
        employee.setEmpDesignation("SDET");
        employee.setEmpSalary(70000L);

        Employee saved = employeeRepository.save(employee);
        assertThat(saved.getEmpId()).isNotNull();   // [OR]
        Assertions.assertNotEquals(null, saved.getEmpId());
        assertThat(employeeRepository.findAll()).hasSize(4);
    }

    @Test
    void testUpdateEmployee(){

         Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase("Alice")).findFirst().orElseThrow();

         employee.setEmpSalary(100000L);

         Employee updated = employeeRepository.save(employee);
         assertThat(updated.getEmpSalary()).isEqualTo(100000L);
    }

    @Test
    void testDeleteEmployee(){

        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase("Alice")).findFirst().orElseThrow();

        employeeRepository.delete(employee);
        assertThat(employeeRepository.findAll()).hasSize(2);
    }

    @Test
    void testGetEmployeeById(){

        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase("Charlie"))
                .findFirst()
                .orElseThrow();

        Employee employeeById = employeeRepository.findById(employee.getEmpId()).orElseThrow();

        assertThat(employeeById.getEmpName()).isEqualTo("Charlie");
    }
}
