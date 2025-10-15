-- V3__Create_product_table.sql
-- Created new product table

DROP TABLE IF EXISTS product;

CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    price NUMERIC,
    quantity INT
);