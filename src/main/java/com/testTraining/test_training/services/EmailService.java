package com.testTraining.test_training.services;


import com.testTraining.test_training.model.User;

public interface EmailService {
    void sendWelcomeEmail(User user);
}