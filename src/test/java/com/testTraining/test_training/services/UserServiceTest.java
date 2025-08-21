package com.testTraining.test_training.services;

import com.testTraining.test_training.exception.UserNotFoundException;
import com.testTraining.test_training.model.User;
import com.testTraining.test_training.repository.UserRepository;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private UserRepository userRepository;
    private EmailService emailService;
    private UserService userService;


    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);
        userService = new UserService(userRepository, emailService);
    }

    @Test
    void shouldReturnIfUserExist() {
        // Crée un utilisateur attendu avec l'UUID, un nom et un email
        UUID id = UUID.randomUUID();
        User expected = new User(id, "Manu", "manu@gmail.com");

        // Configure le mock du repository pour retourner l'utilisateur attendu lors de la recherche par id
        when(userRepository.findById(id)).thenReturn(Optional.of(expected));

        // Appelle la méthode du service à tester
        User actual = userService.getUserById(id);

        // Vérifie que l'utilisateur retourné correspond à l'utilisateur attendu
        assertEquals(expected, actual);

        // Vérifie que la méthode findById a bien été appelée avec le bon id
        verify(userRepository).findById(id);
    }

    @Test
    void shouldReturnIfUserListExist() {
        UUID id = UUID.randomUUID();
        User p1 = new User(id, "Manu", "manu@gmail.com");

        UUID id2 = UUID.randomUUID();
        User p2 = new User(id2, "Julie", "julie@gmail.com");

        List<User> userList = new ArrayList<>(List.of(p1, p2));

        when(userRepository.findAll()).thenReturn(userList);

        List<User> expected = userService.getAllUsers();

        assertEquals(2, expected.size());
        assertEquals(p1, expected.get(0));
        assertEquals(p2, expected.get(1));
        assertEquals("Manu", expected.get(0).getName());
        assertEquals("Julie", expected.get(1).getName());
        assertNotEquals("Muna", expected.get(1).getName());
        verify(userRepository).findAll();
    }


    @Test
    void shouldCreateUser() {
        String name = "Manu";
        String email = "manu@gmail.com";
        User userToSave = new User(name, email);

        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        User result = userService.createUser(name, email);

        assertEquals(userToSave, result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        verify(userRepository).save(result);
        verify(emailService).sendWelcomeEmail(userToSave);
    }

    @Test
    void shouldUpdateUserName() {
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Pedro", "manu@gmail.com");
        String newName = "Manu";

        // verify if user exist
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        // Update user
        User result = userService.updateUserName(id, newName);

        assertEquals(newName, result.getName());
        assertNotEquals("Pedro", result.getName());
        assertEquals("manu@gmail.com", result.getEmail());
        verify(userRepository).findById(id);
        verify(userRepository).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserName(id, "NewName");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(id);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUser() {
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Manu", "manu@gmail.com");

        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }


}
