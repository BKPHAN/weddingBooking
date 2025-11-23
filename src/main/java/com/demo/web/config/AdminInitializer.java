package com.demo.web.config;

import com.demo.web.model.Role;
import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.RoleRepository;
import com.demo.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@wedding.local";
    private static final String ADMIN_PASSWORD = "12345678";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        ensureRole(UserRole.ADMIN);
        ensureRole(UserRole.USER);

        userRepository.findByUsername(ADMIN_USERNAME)
                .ifPresentOrElse(
                        this::syncAdminUser,
                        this::createAdminUser
                );
    }

    private void ensureRole(UserRole roleCode) {
        roleRepository.findByCode(roleCode.name())
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setCode(roleCode.name());
                    role.setDescription(roleCode.name() + " role");
                    log.info("Seed role {}", roleCode);
                    return roleRepository.save(role);
                });
    }

    private void syncAdminUser(User existing) {
        if (existing.getPrimaryRole() != UserRole.ADMIN || !existing.getRoles().stream().anyMatch(r -> r.getCode().equals("ADMIN"))) {
            assignRoles(existing);
        }
        if (!passwordEncoder.matches(ADMIN_PASSWORD, existing.getPasswordHash())) {
            existing.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
        }
        existing.setFullName("System Administrator");
        existing.setEmail(ADMIN_EMAIL);
        existing.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(existing);
        log.info("Synchronized existing admin user ({})", ADMIN_USERNAME);
    }

    private void createAdminUser() {
        User user = new User();
        user.setUsername(ADMIN_USERNAME);
        user.setFullName("System Administrator");
        user.setEmail(ADMIN_EMAIL);
        user.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
        user.setPrimaryRole(UserRole.ADMIN);
        user.setStatus(User.UserStatus.ACTIVE);
        assignRoles(user);
        userRepository.save(user);
        log.info("Seeded admin user ({}) with default password", ADMIN_USERNAME);
    }

    private void assignRoles(User user) {
        Role adminRole = roleRepository.findByCode("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        user.setRoles(roles);
        user.setPrimaryRole(UserRole.ADMIN);
    }
}
