package com.demo.springboot_restassured_framework.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "emp_name", nullable = false)
    private String empName;

    @Column(name = "emp_designation")
    private String empDesignation;

    @Column(name = "emp_salary")
    private Long empSalary;

    public Employee() {}        // Default Constructor

    public Long getEmpId(){
        return empId;
    }

    public void setEmpId(Long empId){
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpDesignation() {
        return empDesignation;
    }

    public void setEmpDesignation(String empDesignation) {
        this.empDesignation = empDesignation;
    }

    public Long getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(Long empSalary) {
        this.empSalary = empSalary;
    }
}
