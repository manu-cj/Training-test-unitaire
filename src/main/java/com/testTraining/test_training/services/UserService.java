package com.testTraining.test_training.services;

import com.testTraining.test_training.exception.UserNotFoundException;
import com.testTraining.test_training.model.User;
import com.testTraining.test_training.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // 1️⃣ Récupération d'un user par ID
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // 2️⃣ Création d'un user avec validation
    public User createUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        User user = new User(name, email);
        User savedUser = userRepository.save(user);
        emailService.sendWelcomeEmail(savedUser);
        return savedUser;
    }

    // 3️⃣ Récupération de tous les users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 4️⃣ Suppression d'un user
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    // 5️⃣ Mise à jour du nom d'un user
    public User updateUserName(UUID id, String newName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(newName);
        return userRepository.save(user);
    }
}