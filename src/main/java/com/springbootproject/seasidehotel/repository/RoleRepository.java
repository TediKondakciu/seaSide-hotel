package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Tedi Kondakçiu
 */

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleUser);

    boolean existsByName(String role);
}
