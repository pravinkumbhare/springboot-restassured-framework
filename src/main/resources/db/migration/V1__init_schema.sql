-- V1__init_schema.sql
-- Initial schema setup for application

DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
    emp_id BIGSERIAL PRIMARY KEY,
    emp_name VARCHAR(100),
    emp_designation VARCHAR(100),
    emp_salary INT
);
