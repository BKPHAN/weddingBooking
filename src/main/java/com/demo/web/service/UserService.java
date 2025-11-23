package com.demo.web.service;

import com.demo.web.model.Role;
import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.RoleRepository;
import com.demo.web.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setPrimaryRole(primaryRole);
        user.setPermissions(new HashSet<>());

        Set<Role> roles = new HashSet<>();
        Role baseRole = roleRepository.findByCode(primaryRole.name())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setCode(primaryRole.name());
                    newRole.setDescription(primaryRole.name() + " role");
                    return roleRepository.save(newRole);
                });
        roles.add(baseRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    // Tìm người dùng theo email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);  // Trả về người dùng nếu tồn tại, null nếu không
    }
}
