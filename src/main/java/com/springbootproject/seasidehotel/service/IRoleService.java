package com.springbootproject.seasidehotel.service;

import com.springbootproject.seasidehotel.model.Role;
import com.springbootproject.seasidehotel.model.User;

import java.util.List;

/**
 * @author Tedi Kondakçiu
 */

public interface IRoleService {
    Role createRole(Role role);
    void deleteRole(Long id);
    List<Role> getRoles();
    Role findRoleByName(String name);
    User assignRoleToUser(Long userId, Long roleId);
    User removeUserFromRole(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
