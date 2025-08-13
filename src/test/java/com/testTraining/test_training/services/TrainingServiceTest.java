package com.testTraining.test_training.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TrainingServiceTest {
    @Autowired
    TrainingService trainingService;

    @Test
    void addReturnSum() {
        assertEquals(5, trainingService.add(3, 2));
    }

    @Test
    void calculateDiscount() {

        assertAll(
                () -> assertEquals(10, trainingService.calculateDiscount(100)),
                () -> assertEquals(20, trainingService.calculateDiscount(200))
        );
    }

    @Test
    void toUppercase() {
        assertEquals("BONJOUR", trainingService.toUppercase("bonjour"));
    }

    @Test
    void calculateTotal() {
        List<Double> list = new ArrayList<>();
        list.add(5.80);
        list.add(2.20);
        list.add(1.00);

        assertEquals(9, trainingService.calculateTotal(list));
    }

    @Test
    void isValidPassword() {
        assertTrue(trainingService.isValidPassword("motdepasse1"));
    }

    @Test
    void findMax() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(8);
        list.add(2);
        list.add(1000);

        assertEquals(1000, trainingService.findMax(list));
    }

    @Test
    void areEqual() {
        assertTrue(trainingService.areEqual(4,4));
    }
}
