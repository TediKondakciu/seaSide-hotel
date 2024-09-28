package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp(){
        role = new Role("ROLE_TEST");
        roleRepository.save(role);
    }

    @AfterEach
    void tearDown(){
        roleRepository.deleteAll();
    }

    @Test
    void itShouldFindARoleByName(){
        Role expected = roleRepository.findByName("ROLE_TEST").get();

        assertThat(expected).isEqualTo(role);
    }

    @Test
    void itShouldCheckIfRoleNameExists(){
        boolean expected = roleRepository.existsByName("ROLE_TEST");

        assertThat(expected).isTrue();
    }

}
