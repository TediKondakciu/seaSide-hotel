package com.springbootproject.seasidehotel;

import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.repository.RoleRepository;
import com.springbootproject.seasidehotel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SeaSideHotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeaSideHotelApplication.class, args);
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    protected PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(SeaSideHotelApplication.class);

    CommandLineRunner runner(){
        return args -> {
            // Create an admin user if no admins initially exist in database
            if (!userRepository.existsByRoleAdmin()) {
                User newAdmin = new User();
                newAdmin.setEmail("admin@gmail.com");
                newAdmin.setPassword(passwordEncoder.encode("admin123"));
                Role role = new Role("ROLE_ADMIN");
                newAdmin.setRoles(Collections.singletonList(role));
                userRepository.save(newAdmin);

                log.info("Admin user created");
            }

            // Create a role for simple users if it does not initially exist
            if (!roleRepository.existsByName("ROLE_USER")){
                Role userRole = new Role("ROLE_USER");
                userRole.setUsers(List.of());
                roleRepository.save(userRole);

                log.info("ROLE_USER created");
            }
        };
    }
}
