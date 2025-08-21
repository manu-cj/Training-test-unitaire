package com.testTraining.test_training.services;

import com.testTraining.test_training.model.Product;
import com.testTraining.test_training.model.User;
import com.testTraining.test_training.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldCreateProduct() {
        String name = "Carotte";
        double price =  3.19;
        int quantity = 300;
        Product productToSave = new Product(name, price, quantity);

        when(productRepository.save(any(Product.class))).thenReturn(productToSave);

        Product result = productService.createProduct(name, price, quantity);

        assertEquals(productToSave, result);
        assertEquals("Carotte", result.getName());
        assertEquals(3.19, result.getPrice());
        assertEquals(300, result.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(null, 10.0, 300);
        });
        assertEquals("Name cannot be blank", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    // test for price
    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct("Carotte", 0, 300);
        });
        assertEquals("Price must be positive", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    // test for minimal quantity
    @Test
    void shouldThrowExceptionWhenQuantityIsZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct("Carotte", 3.19, 0);
        });
        assertEquals("Quantity must be positive", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
    // Test for maximum quantity
    @Test
    void shouldThrowExceptionWhenQuantityIsOverNineThousand() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct("Carotte", 3.19, 9001);
        });
        assertEquals("Quantity it's over 9000", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldReturnIfProductExist() {
        UUID id = UUID.randomUUID();
        Product expected = new Product("Carotte", 3.19, 300);
        expected.setId(id);

        when(productRepository.findById(id)).thenReturn(Optional.of(expected));

        Product actual = productService.getProductById(id);

        assertEquals(expected, actual);
        assertEquals("Carotte", actual.getName());
        assertEquals(3.19, actual.getPrice());
        assertEquals(300, actual.getQuantity());
        assertEquals(id, actual.getId());

        verify(productRepository).findById(id);
    }

    @Test
    void shouldReturnIfUserListExist() {
        Product product1 = new Product("Carotte", 3.19, 300);
        Product product2 = new Product("Lasagne", 5.89, 450);
        List<Product> productList = new ArrayList<>(List.of(
                product1,
                product2
        ));

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> expected = productService.getAllProducts();

        assertEquals(2, expected.size());
        assertEquals(productList.get(0), expected.get(0));
        assertEquals(productList.get(1), expected.get(1));
        assertEquals("Carotte", expected.get(0).getName());
        assertEquals("Lasagne", expected.get(1).getName());
        assertEquals(3.19, expected.get(0).getPrice());
        assertEquals(5.89, expected.get(1).getPrice());
        assertEquals(300, expected.get(0).getQuantity());
        assertEquals(450, expected.get(1).getQuantity());
        verify(productRepository).findAll();
    }

    @Test
    void shouldUpdateProductName() {
        UUID id = UUID.randomUUID();
        Product product = new Product("crotte", 3.19, 300);
        product.setId(id);
        String newProductName = "Carotte";

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.updateProductName(id, newProductName);

        assertEquals(newProductName, result.getName());
        assertNotEquals("Crotte", result.getName());
        assertEquals(3.19, result.getPrice());
        assertEquals(300, result.getQuantity());

        verify(productRepository).findById(id);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowException_whenProductNotFoundOnUpdate() {
        UUID id = UUID.randomUUID();
        String newProductName = "Carotte";

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProductName(id, newProductName);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).findById(id);
        verify(productRepository, never()).save(any());
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product("carotte", 3.19, 300);

        when(productRepository.existsById(id)).thenReturn(true);

        productService.deleteProduct(id);
        verify(productRepository).deleteById(id);
    }

    @Test
    void shouldThrowException_whenDeletingNonExistingProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(id);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository).existsById(id);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void shouldDecreaseProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product("carotte", 3.19, 300);
        product.setId(id);
        int amount = 100;

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.decreaseStock(id, amount);

        assertEquals(200, result.getQuantity());
        verify(productRepository).findById(id);
        verify(productRepository).save(product);
    }

}