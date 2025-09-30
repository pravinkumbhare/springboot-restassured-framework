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

    // Get employee by ID (throws exception if not found)
    public Employee getEmployeeById(Long id){
//        return employeeRepository.findById(id).orElse(null);
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // Create employee (validate name)
    public Employee createEmployee(Employee body){

        /* This we have commented because we have already cover it in Entity with @NotBlank
        if(body.getEmpName()==null || body.getEmpName().isBlank() || body.getEmpName().isEmpty()){
            throw new IllegalArgumentException("Name cannot be Null or Blank");
        }*/
        return employeeRepository.save(body);
    }

    /*
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
    */
    // Update employee (throws exception if not found)
    public Employee updateEmployee(Long id, Employee body){

//        Employee employee = employeeRepository.findById(id).orElse(null);
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        employee.setEmpName(body.getEmpName());
        employee.setEmpDesignation(body.getEmpDesignation());
        employee.setEmpSalary(body.getEmpSalary());
        return employeeRepository.save(employee);
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
             throw new EmployeeNotFoundException(id);
        }
    }
   /* public void deleteEmployeeById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }
        employeeRepository.deleteById(id);
    }*/

    public Employee save(Employee employee){
        return employeeRepository.save(employee);
    }
}
