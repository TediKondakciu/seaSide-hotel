package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.exception.RoleAlreadyExistsException;
import com.springbootproject.seasidehotel.exception.UserAlreadyExistsException;
import com.springbootproject.seasidehotel.exception.UsernameNotFoundException;
import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.repository.RoleRepository;
import com.springbootproject.seasidehotel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private RoleService roleService;
    private Role role;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        roleService = new RoleService(roleRepository, userRepository);
        // Create a Role
        role = new Role();
        role.setId(1L);
        role.setName("user");
    }

    @Test
    void canCreateRole() {
        // Arrange
        when(roleRepository.existsByName("ROLE_USER")).thenReturn(false);

        // Use method to create Role
        roleService.createRole(role);

        // Verify that the save() method of RoleRepository was called
        ArgumentCaptor<Role> argumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(argumentCaptor.capture());

        // Test if Role was created correctly
        Role roleArgumentCaptor = argumentCaptor.getValue();
        assertThat(roleArgumentCaptor.getName()).isEqualTo("ROLE_USER");
    }

    @Test
    void createRole_ShouldThrowRoleAlreadyExistsException_WhenRoleExists(){
        // Arrange
        when(roleRepository.existsByName("ROLE_" + role.getName().toUpperCase())).thenReturn(true);

        // Test if correct exception was thrown
        RoleAlreadyExistsException exception = assertThrows(RoleAlreadyExistsException.class, () -> {
            roleService.createRole(role);
        });
        assertEquals("user already exists!", exception.getMessage());
    }

    @Test
    void canDeleteRole() {
        // Arrange
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
        when(roleService.removeAllUsersFromRole(role.getId())).thenReturn(role);

        // Use method to delete Role
        roleService.deleteRole(role.getId());

        // Test that the Role was successfully deleted
        verify(roleRepository).deleteById(role.getId());
    }

    @Test
    void canGetRoles() {
        roleService.getRoles();
        verify(roleRepository).findAll();
    }

    @Test
    void canFindRoleByName() {
        // Arrange
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));

        // Use method to find Role by its name
        Role foundRole = roleService.findRoleByName(role.getName());

        // Verify that the repository was called
        verify(roleRepository).findByName(role.getName());

        // Ensure the found Role is the same as the one we created
        assertThat(foundRole).isEqualTo(role);
    }

    @Test
    void canAssignRoleToUser() {
        // Create a User
        User user = new User();
        user.setId(1L);
        user.setEmail("example@gmail.com");
        user.setRoles(new ArrayList<>());

        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Use method to assign a Role to a User
        User assignedUser = roleService.assignRoleToUser(user.getId(), role.getId());

        // Test that the Role was added to the User
        assertThat(assignedUser.getRoles()).contains(role);

        // Check that the assigned user is the same
        assertThat(assignedUser).isEqualTo(user);
    }

    @Test
    void assignRoleToUser_ShouldThrowUserAlreadyExistsException_WhenUserAlreadyHasRole() {
        // Create a User
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setEmail("example@gmail.com");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));

        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Test if correct exception was thrown
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> {
            roleService.assignRoleToUser(user.getId(), role.getId());
        });
        assertEquals(user.getFirstName() + "is already assigned to the" + role.getName() + "role!", exception.getMessage());
    }

    @Test
    void canRemoveUserFromRole() {
        // Create a User
        User user = new User();
        user.setId(1L);
        user.setEmail("example@gmail.com");
        user.setRoles(new ArrayList<>(Collections.singletonList(role)));

        role.getUsers().add(user);

        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Use method removeUserFromRole()
        User expectedUser = roleService.removeUserFromRole(user.getId(), role.getId());

        // Test if User was removed from Role
        assertThat(expectedUser.getRoles()).isEmpty();
    }

    @Test
    void removeUserFromRole_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist(){
        // Create a User
        User user = new User();
        user.setId(1L);
        user.setEmail("example@gmail.com");

        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Test if correct exception was thrown
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            roleService.removeUserFromRole(user.getId(), role.getId());
        });
        assertEquals("User not found!", exception.getMessage());
    }
}