---- Step 3: Pre-load Employees into H2
--
--DROP TABLE IF EXISTS employee;
--
--CREATE TABLE employee (
--    emp_id BIGINT AUTO_INCREMENT PRIMARY KEY,
--    emp_name VARCHAR(100),
--    emp_designation VARCHAR(100),
--    emp_salary INT
--);

DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    emp_id BIGSERIAL PRIMARY KEY,
    emp_name VARCHAR(100),
    emp_designation VARCHAR(100),
    emp_salary INT
);

DROP TABLE IF EXISTS pet;

CREATE TABLE pet (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    age INT
);

DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    price NUMERIC,
    quantity INT
);


