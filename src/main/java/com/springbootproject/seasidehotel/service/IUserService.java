package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.model.User;

import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

public interface IUserService {
    User registerUser(User user);
    void deleteUser(String email);
    User getUser(String email);
    List<User> getUsers();
}
