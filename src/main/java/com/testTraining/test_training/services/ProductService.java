package com.testTraining.test_training.services;

import com.testTraining.test_training.model.Product;
import com.testTraining.test_training.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(String name, double price, int quantity) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        else if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        else if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        else if (quantity >= 9000) {
            throw new IllegalArgumentException("Quantity it's over 9000");
        }

        Product product = new Product(name, price, quantity);

        return productRepository.save(product);
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProductName(UUID id, String newProductName) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setName(newProductName);
        return productRepository.save(product);

    }

    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public Product decreaseStock(UUID id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.decreaseStock(amount);

        if (product.getQuantity() == 0) {
            product.setInStock(false);
        }

        return productRepository.save(product);
    }

    public Product increaseStock(UUID id, int amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.rechargeStock(amount);
        return productRepository.save(product);
    }

}
