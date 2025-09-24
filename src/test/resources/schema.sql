-- Step 3: Pre-load Employees into H2

DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    emp_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emp_name VARCHAR(100),
    emp_designation VARCHAR(100),
    emp_salary INT
);