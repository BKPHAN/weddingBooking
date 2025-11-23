package com.demo.web.repository;

import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findFirstByPrimaryRoleOrderByIdAsc(UserRole role);
}
