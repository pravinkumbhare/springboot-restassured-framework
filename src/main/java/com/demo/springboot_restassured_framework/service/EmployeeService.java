package com.demo.springboot_restassured_framework.service;

import com.demo.springboot_restassured_framework.entity.Employee;
import com.demo.springboot_restassured_framework.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee createEmployee(Employee body){
        return employeeRepository.save(body);
    }

    public Employee updateEmployee(Long id, Employee body){

        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee!=null){
            employee.setEmpName(body.getEmpName());
            employee.setEmpDesignation(body.getEmpDesignation());
            employee.setEmpSalary(body.getEmpSalary());
            return employeeRepository.save(employee);
        }
        return null;
    }

    /*public void deleteEmployeeById(Long id){
        employeeRepository.deleteById(id);
    }*/
    public boolean deleteEmployeeById(Long id){
        boolean flag = employeeRepository.existsById(id);
        if(flag){
            employeeRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }
}
