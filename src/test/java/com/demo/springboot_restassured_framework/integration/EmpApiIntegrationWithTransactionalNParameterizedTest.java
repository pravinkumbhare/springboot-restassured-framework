package com.demo.springboot_restassured_framework.integration;

import com.demo.springboot_restassured_framework.entity.Employee;
import com.demo.springboot_restassured_framework.repository.EmployeeRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest  <= spins up the whole Spring context, which is heavier.
@DataJpaTest    // <= Since these tests are repository-level test, we can make them lighter/faster with @DataJpaTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmpApiIntegrationWithTransactionalNParameterizedTest {

    @Autowired
    EmployeeRepository employeeRepository;

    // -------------------------------
    // Create Employee
    // -------------------------------
    @DisplayName("Should create employees successfully [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "Pravin, SDET, 70000",
            "Raj, Developer, 80000",
            "Sneha, QA, 60000"
    })
    void testCreateEmployeeParameterized(String name, String designation, Long salary){

        Employee employee = new Employee();
        employee.setEmpName(name);
        employee.setEmpDesignation(designation);
        employee.setEmpSalary(salary);

        Employee saved = employeeRepository.save(employee);
        assertThat(saved.getEmpId()).isNotNull();   // [OR]
        assertThat(saved.getEmpName()).isEqualToIgnoringCase(name);
        assertThat(saved.getEmpDesignation()).isEqualToIgnoringCase(designation);
        assertThat(saved.getEmpSalary()).isEqualTo(salary);

//        [OR]

        // Assert (more expressive + clearer failure messages)
        assertThat(saved)
                .isNotNull()
                .extracting(Employee::getEmpName, Employee::getEmpDesignation, Employee::getEmpSalary)
                .containsExactlyInAnyOrder(name, designation, salary);

        assertThat(saved.getEmpId()).isNotNull();
    }

    // -------------------------------
    // Update Employee
    // -------------------------------
    @DisplayName("Should update employee salary [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "Alice, 100000",
            "Bob, 90000",
            "Charlie, 95000"
    })
    void testUpdateEmployeeParameterized(String name, Long newSalary){

        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase(name)).findFirst().orElseThrow();

        employee.setEmpSalary(newSalary);
        Employee updated = employeeRepository.save(employee);

        assertThat(updated.getEmpSalary()).isEqualTo(newSalary);
    }

    // -------------------------------
    // Get Employee by Name
    // -------------------------------
    @DisplayName("Should fetch employee by name [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "Alice",
            "Bob",
            "Charlie"
    })
    void testGetEmployeeByNameParameterized(String name){

        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();

        assertThat(employee.getEmpName().equalsIgnoreCase(name));
    }


    // -------------------------------
    // Get Employee by Id
    // -------------------------------
    @DisplayName("Should fetch employee by Id [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "Alice",
            "Bob",
            "Charlie"
    })
    void testGetEmployeeByIdParameterized(String name){

        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();

        Employee employeeById = employeeRepository.findById(employee.getEmpId()).orElseThrow();
        assertThat(employeeById.getEmpId().equals(employee.getEmpId()));
    }


    // -------------------------------
    // Delete Employee
    // -------------------------------
    @DisplayName("Should delete employee by name [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "Alice",
            "Bob",
            "Charlie"
    })
    void testDeleteEmployeeParameterized(String name){

        Employee employee = employeeRepository.findAll().stream()
                .filter(e -> e.getEmpName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();

        employeeRepository.delete(employee);

        assertThat(employeeRepository.findAll().stream()
                .allMatch(e -> e.getEmpName().equalsIgnoreCase(name))).isFalse();
    }

    // ========================================================================
    // Added more test cases for edge scenarios (like invalid updates/deletes).  (In real project it is for Test Coverage)
    // =========================================================================

    // -------------------------------
    // Parameterized test to Fetch/GET non-existing employees
    // -------------------------------
    @DisplayName("Should return empty when fetching non-existing employees by Id [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "1111111",
            "2222222",
            "3333333"
    })
    void testGetNonExistingEmployeeByIdParameterized(Long id){

        var nonExistingEmployee = employeeRepository.findById(id);
        assertThat(nonExistingEmployee).isEmpty();
    }


    // -------------------------------
    // Parameterized test for updating non-existing employees
    // -------------------------------
    @DisplayName("Should throw exception when updating non-existing employee [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "1111111, 10000",
            "2222222, 20000",
            "3333333, 30000"
    })
    void testUpdateNonExistingEmployeeByIdParameterized(Long id, Long newSalary){

        Assertions.assertThrows(RuntimeException.class, () -> {

            Employee employeeById = employeeRepository.findById(id).orElseThrow();

            Employee employee = new Employee();
            employee.setEmpSalary(newSalary);

            employeeRepository.save(employee);
        });
    }


    // -------------------------------
    // Parameterized test for deleting non-existing employees
    // -------------------------------
    @DisplayName("Should throw exception when deleting non-existing employee [Parameterized]")
    @ParameterizedTest
    @CsvSource({
            "1111111",
            "2222222",
            "3333333"
    })
    void testDeleteInvalidEmployeeParameterized(Long id){

        Assertions.assertThrows(RuntimeException.class, ()-> {

            Employee employee = employeeRepository.findById(id).orElseThrow();
            employeeRepository.delete(employee);
        });

//        [OR]

        employeeRepository.deleteById(id);
        assertThat(employeeRepository.findAll()).hasSize(3);
    }


    // -------------------------------
    // Invalid Employee Names
    // -------------------------------
    @DisplayName("Should throw ConstraintViolationException for invalid names [Parameterized]")
    @ParameterizedTest
    @CsvSource(value = {
            "null",   // becomes real null
            "' '",    // space
            "''"      // empty string
    }, nullValues = "null")
    void testCreateEmployeeWithInvalidName(String invalidName) {

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Employee employee = new Employee();
            employee.setEmpDesignation("Test");
            employee.setEmpSalary(1000L);
            employee.setEmpName(invalidName); // now handles null, "", " "
            employeeRepository.save(employee);
        });
    }

}
