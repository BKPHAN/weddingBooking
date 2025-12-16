package com.demo.web.service;

import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final com.demo.web.repository.EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
            com.demo.web.repository.EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User registerUser(String username, String fullName, String email, String rawPassword, UserRole primaryRole) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new IllegalStateException("Email already exists");
        });

        userRepository.findByUsername(username).ifPresent(existing -> {
            throw new IllegalStateException("Username already exists");
        });

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setPrimaryRole(primaryRole);
        user.setStatus(User.UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        com.demo.web.model.Employee employee = new com.demo.web.model.Employee();
        employee.setUser(savedUser);
        employee.setFullName(fullName);
        employee.setEmail(email);
        // Can add more info if available in request but here minimal
        employeeRepository.save(employee);

        return savedUser;
    }

}
