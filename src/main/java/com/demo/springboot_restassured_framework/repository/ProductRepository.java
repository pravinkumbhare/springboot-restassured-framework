package com.demo.springboot_restassured_framework.repository;

import com.demo.springboot_restassured_framework.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
