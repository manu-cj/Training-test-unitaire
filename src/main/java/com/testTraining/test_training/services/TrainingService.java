package com.testTraining.test_training.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    public int add(int a, int b) {
        return a + b;
    }

    public double calculateDiscount(double amount) {
        if (amount >= 100) return amount * 0.10;
        return 0.0;
    }

    public String toUppercase(String input) {
        if (input == null) throw new IllegalArgumentException("Input cannot be null");
        return input.toUpperCase();
    }

    public double calculateTotal(List<Double> prices) {
        return prices.stream().mapToDouble(Double::doubleValue).sum();
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        return password.chars().anyMatch(Character::isDigit);
    }

    public int findMax(List<Integer> numbers) {
        if (numbers.isEmpty()) throw new IllegalStateException("List is empty");
        return numbers.stream().max(Integer::compare).get();
    }

    public boolean areEqual(Object a, Object b) {
        return Objects.equals(a, b);
    }
}

