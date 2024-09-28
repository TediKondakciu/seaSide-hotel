package com.springbootproject.seasidehotel.repository;

import com.springbootproject.seasidehotel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Tedi Kondakçiu
 */

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.roles r WHERE r.name = 'ROLE_ADMIN'")
    boolean existsByRoleAdmin();
}
