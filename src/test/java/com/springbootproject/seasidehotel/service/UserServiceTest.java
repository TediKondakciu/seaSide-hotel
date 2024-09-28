package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.exception.UserAlreadyExistsException;
import com.springbootproject.seasidehotel.exception.UsernameNotFoundException;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.repository.RoleRepository;
import com.springbootproject.seasidehotel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    private User user;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
        // Create a User
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("example@gmail.com");
        user.setPassword("example123");
    }

    @Test
    void canRegisterUser() {
        // Create a Role
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");

        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));

        // Use method to register User
        userService.registerUser(user);

        // Verify that the save() method of UserRepository was called
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        // Test if User was registered correctly
        User userArgumentCaptor = argumentCaptor.getValue();
        assertThat(userArgumentCaptor).isEqualTo(user);
    }

    @Test
    void registerUser_ShouldThrowUserAlreadyExistsException_WhenUserExists() {
        // Arrange
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // Test if correct exception was thrown
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("example@gmail.com already exists!", exception.getMessage());
    }


    @Test
    void canDeleteUser() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        // Use method to delete User
        userService.deleteUser(user.getEmail());

        // Test that User was successfully deleted
        verify(userRepository).deleteByEmail(user.getEmail());
    }

    @Test
    void canGetUsers() {
        userService.getUsers();
        verify(userRepository).findAll();
    }

    @Test
    void canGetUser() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Use method to find User
        userService.getUser(user.getEmail());

        // Test that User was found
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void testGetUser_UserNotFound(){
        //Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Test if correct exception was thrown
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
           userService.getUser(user.getEmail());
        });
        assertEquals("User not found!", exception.getMessage());
    }
}