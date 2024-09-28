package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("example@gmail.com");
        user.setPassword("example123");
        userRepository.save(user);
    }

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    @Test
    void itShouldCheckIfUserEmailExists(){
        boolean expected = userRepository.existsByEmail("example@gmail.com");

        assertThat(expected).isTrue();
    }

    @Test
    void itShouldFindUserByEmail(){
        User expectedUser = userRepository.findByEmail("example@gmail.com").get();

        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    void itShouldDeleteUserByEmail(){
        userRepository.deleteByEmail(user.getEmail());

        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(0);
    }

    @Test
    void itShouldNotDeleteUserIfEmailDoesNotExist() {
        userRepository.deleteByEmail("nonexistent@example.com");

        List<User> users = userRepository.findAll();
        assertThat(users).containsExactly(user);
    }
}
