package com.demo.springboot_restassured_framework.Entity;

import com.demo.springboot_restassured_framework.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class EmployeeEntityTest {

    @Test
    @DisplayName("Should set and get the Entity methods")
    void testEmployeeEntity(){

        Employee employee = new Employee();
        employee.setEmpId(1L);
        employee.setEmpName("SpringBoot User");
        employee.setEmpDesignation("SDE");
        employee.setEmpSalary(50000L);

        assertThat(1L).isEqualTo(employee.getEmpId());
        assertThat("SpringBoot User").isEqualTo(employee.getEmpName());
        assertThat("SDE").isEqualTo(employee.getEmpDesignation());
        assertThat(50000L).isEqualTo(employee.getEmpSalary());
    }
}

