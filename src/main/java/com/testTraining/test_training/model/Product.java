package com.testTraining.test_training.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    private boolean inStock = true;

    public Product (String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }


    public void rechargeStock(int amount) {
        final int MAX_STOCK = 9000;
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.quantity + amount > MAX_STOCK) {
            throw new IllegalArgumentException("Quantity it's over 9000");
        } else {
            this.quantity += amount;
        }
        this.inStock = this.quantity != 0;
    }

    public void decreaseStock(int amount) {
        if (this.quantity < amount) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        int newQuantity = this.quantity - amount;
        setQuantity(newQuantity);
    }



}
