package com.demo.web.repository;

import com.demo.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA tự động tạo phương thức findByEmail từ tên phương thức
    User findByEmail(String email);
}