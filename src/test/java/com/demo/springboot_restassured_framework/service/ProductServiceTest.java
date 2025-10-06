package com.demo.springboot_restassured_framework.service;

import com.demo.springboot_restassured_framework.entity.Product;
import com.demo.springboot_restassured_framework.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(11L);
        product.setName("Xiaomi TV");
        product.setDescription("X Pro QLED TV");
        product.setPrice(39000.00);
        product.setQuantity(1);
    }

    @Test
    @DisplayName("")
    void testGetAllProducts(){

        // Mock Repository Layer
        when(productRepository.findAll()).thenReturn(List.of(product));

        // Inject into Service Layer
        List<Product> listOfProduct = productService.getAllProducts();

        // Assertion and Verification Layer
        assertThat(listOfProduct.size()).isNotEqualTo(0);
        assertThat(listOfProduct.get(0).getName()).isEqualTo("Xiaomi TV");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return Product by Id")
    void testGetProductById(){

        // Mock
        when(productRepository.findById(11L)).thenReturn(Optional.of(product));

        // Inject
        Product productById = productService.getProductById(11L);

        // Assertion
        assertThat(productById).isNotNull();
        assertThat(productById.getName()).isEqualTo("Xiaomi TV");
        verify(productRepository, times(1)).findById(11L);
    }

    @Test
    @DisplayName("Should create new Product")
    void testCreateProduct(){

        Product product1 = new Product();
        product1.setId(22L);
        product1.setName("Sansui");
        product1.setDescription("Ultra HD QLED");
        product1.setPrice(35000.00);
        product1.setQuantity(2);

        // Mock
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // inject
        Product createProduct = productService.createProduct(product1);

        // Assertion
        assertThat(createProduct).isNotNull();
        assertThat(createProduct.getName()).isEqualTo("Sansui");
        assertThat(createProduct.getId()).isEqualTo(22L);
        assertThat(createProduct.getQuantity()).isEqualTo(2);
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    @DisplayName("Should update the created product")
    void testUpdateProduct(){

        Product updateProduct = new Product();
        updateProduct.setId(11L);
        updateProduct.setName("Xiaomi TV");
        updateProduct.setDescription("Ultra HD QLED");
        updateProduct.setPrice(40000.00);
        updateProduct.setQuantity(5);

        // Mock
        when(productRepository.findById(11L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updateProduct);

        // Inject
        Product update = productService.updateProduct(11L, updateProduct);

        // Assertion
        assertThat(update.getId()).isEqualTo(11L);
        assertThat(update.getDescription()).isEqualTo("Ultra HD QLED");
        assertThat(update.getPrice()).isEqualTo(40000.00);
        assertThat(update.getQuantity()).isEqualTo(5);
        verify(productRepository, times(1)).findById(11L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product by Id")
    void testDeleteProductById(){

        // Mock
        when(productRepository.existsById(11L)).thenReturn(true);

        // Inject
        boolean isDeleted = productService.deleteProduct(11L);

        // Assertion
        assertThat(isDeleted).isTrue();
        verify(productRepository, times(1)).deleteById(11L);


    }
}
