-- V2__Create_pet_table.sql
-- Created new pet table

DROP TABLE IF EXISTS pet;

CREATE TABLE pet (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    age INT
);