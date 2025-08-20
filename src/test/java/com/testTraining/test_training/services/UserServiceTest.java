package com.testTraining.test_training.services;

import com.testTraining.test_training.model.User;
import com.testTraining.test_training.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    UserService userService;

    @Captor
    ArgumentCaptor<User> userCaptor;

    private User mockUser;

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
    void shouldIfUserListExist() {
        UUID id = UUID.randomUUID();
        User p1 = new User(id, "Manu", "manu@gmail.com");

        UUID id2 = UUID.randomUUID();
        User p2 = new User(id2, "Julie", "julie@gmail.com");

        when(userRepository.findAll()).thenReturn(List.of(p1, p2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals(p1, users.get(0));
        assertEquals(p2, users.get(1));
        assertEquals("Manu", users.get(0).getName());
        assertEquals("Julie", users.get(1).getName());
        assertNotEquals("Muna", users.get(1).getName());
        verify(userRepository).findAll();
    }


    @Test
    void shouldCreateUser() {
        String name = "Manu";
        String email = "manu@gmail.com";
        User userToSave = new User(name, email);

        when(userRepository.save(userCaptor.capture())).thenReturn(userToSave);

        User result = userService.createUser(name, email);

        assertEquals(userToSave, result);
        assertEquals(name, userCaptor.getValue().getName());
        assertEquals(email, userCaptor.getValue().getEmail());
        verify(userRepository).save(userCaptor.getValue());
        verify(emailService).sendWelcomeEmail(userToSave);
    }

    @Test
    void shouldUpdateUserName() {
        UUID id = UUID.randomUUID();
        User existing = new User(id, "Pedro", "manu@gmail.com");
        String newName = "Manu";

        // verify if user exist
        when(userRepository.findById(id)).thenReturn(Optional.of(existing));

        // Update user
        existing.setName(newName);
        when(userRepository.save(existing)).thenReturn(existing);

        User result = userService.updateUserName(id, newName);

        assertEquals(newName, result.getName());
        assertNotEquals("Pedro", result.getName());
        assertEquals("manu@gmail.com", result.getEmail());
        verify(userRepository).findById(id);
        verify(userRepository).save(existing);
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
