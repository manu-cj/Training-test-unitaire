package com.testTraining.test_training.services.impl;

import com.testTraining.test_training.model.User;
import com.testTraining.test_training.services.EmailService;
import org.springframework.stereotype.Service;

@Service
public class ConsoleEmailService implements EmailService {
    @Override
    public void sendWelcomeEmail(User user) {
        // Implémentation minimale (no-op ou log)
        System.out.println("[EmailService] Welcome email sent to: " + user.getName());
    }
}
