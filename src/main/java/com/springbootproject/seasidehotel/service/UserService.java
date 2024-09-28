package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.exception.UserAlreadyExistsException;
import com.springbootproject.seasidehotel.exception.UsernameNotFoundException;
import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;
import com.springbootproject.seasidehotel.repository.RoleRepository;
import com.springbootproject.seasidehotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author Tedi Kondakçiu
 */


@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail()))
            throw new UserAlreadyExistsException(user.getEmail() + " already exists!");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(roleUser));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        if(getUser(email) != null)
            userRepository.deleteByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
