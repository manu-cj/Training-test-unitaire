package com.testTraining.test_training.services;

import com.testTraining.test_training.model.Product;
import com.testTraining.test_training.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        else if (quantity >= 1000) {
            throw new IllegalArgumentException("Quantity must be maximum is 1000");
        }

        Product product = new Product(name, price, quantity);

        return productRepository.save(product);

    }

}
