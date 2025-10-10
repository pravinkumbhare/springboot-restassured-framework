package com.demo.springboot_restassured_framework.Repository;

import com.demo.springboot_restassured_framework.entity.Product;
import com.demo.springboot_restassured_framework.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
What Happens Here
@DataJpaTest
Loads only JPA-related beans (no web/server overhead).
Auto-configures H2 in-memory DB.
Rolls back DB changes after each test.
Each test:
Saves test data → retrieves it → asserts correctness.
No mocks or controllers are needed — this is pure DB layer testing.
*/

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save product and return it from DB")
    void testSaveProduct() {
        Product product = new Product();
        product.setName("LG OLED TV");
        product.setDescription("Ultra HD 4K TV");
        product.setPrice(65000.00);
        product.setQuantity(1);

        Product saved = productRepository.save(product);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getName()).isEqualTo("LG OLED TV");
    }

    @Test
    @DisplayName("Should find product by ID")
    void testFindById() {
        Product product = new Product();
        product.setName("Sony Bravia");
        product.setDescription("Smart Google TV");
        product.setPrice(70000.00);
        product.setQuantity(2);

        Product saved = productRepository.save(product);

        Optional<Product> found = productRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Sony Bravia");
    }

    @Test
    @DisplayName("Should return all products")
    void testFindAll() {
        Product p1 = new Product();
        p1.setName("Samsung QLED");
        p1.setDescription("4K HDR TV");
        p1.setPrice(55000.00);
        p1.setQuantity(3);

        Product p2 = new Product();
        p2.setName("TCL Smart TV");
        p2.setDescription("Full HD TV");
        p2.setPrice(30000.00);
        p2.setQuantity(4);

        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> list = productRepository.findAll();
        assertThat(list).hasSize(2);
        assertThat(list.get(0).getId()).isNotNull();
    }

    @Test
    @DisplayName("Should find by product name")
    void testFindByName() {
        Product product = new Product();
        product.setName("Panasonic TV");
        product.setDescription("LED TV");
        product.setPrice(25000.00);
        product.setQuantity(5);

        productRepository.save(product);

        Product found = productRepository.findByName("Panasonic TV");

        assertThat(found).isNotNull();
        assertThat(found.getDescription()).isEqualTo("LED TV");
    }

    @Test
    @DisplayName("Should find by product price")
    void testFindByPrice() {
        Product product = new Product();
        product.setName("Sony TV");
        product.setDescription("LED TV");
        product.setPrice(125000.00);
        product.setQuantity(1);

        productRepository.save(product);

        Product found = productRepository.findByPrice(125000.00);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Sony TV");
    }



}
