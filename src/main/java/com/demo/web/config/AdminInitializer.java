package com.demo.web.config;

import com.demo.web.model.Employee;
import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.EmployeeRepository;
import com.demo.web.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL = "admin@wedding.local";
    private static final String ADMIN_PASSWORD = "12345678";

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository,
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        userRepository.findByUsername(ADMIN_USERNAME)
                .ifPresentOrElse(
                        this::syncAdminUser,
                        this::createAdminUser);
    }

    private void syncAdminUser(User existing) {
        if (existing.getPrimaryRole() != UserRole.ADMIN) {
            existing.setPrimaryRole(UserRole.ADMIN);
        }
        if (!passwordEncoder.matches(ADMIN_PASSWORD, existing.getPasswordHash())) {
            existing.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
        }

        existing.setEmail(ADMIN_EMAIL);
        existing.setStatus(User.UserStatus.ACTIVE);
        User savedUser = userRepository.save(existing);

        ensureEmployeeProfile(savedUser, "System Administrator", ADMIN_EMAIL);

        log.info("Synchronized existing admin user ({})", ADMIN_USERNAME);
    }

    private void createAdminUser() {
        User user = new User();
        user.setUsername(ADMIN_USERNAME);
        user.setEmail(ADMIN_EMAIL);
        user.setPasswordHash(passwordEncoder.encode(ADMIN_PASSWORD));
        user.setPrimaryRole(UserRole.ADMIN);
        user.setStatus(User.UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);

        ensureEmployeeProfile(savedUser, "System Administrator", ADMIN_EMAIL);

        log.info("Seeded admin user ({}) with default password", ADMIN_USERNAME);
    }

    private void ensureEmployeeProfile(User user, String fullName, String email) {
        Employee employee = employeeRepository.findByUserId(user.getId())
                .orElse(new Employee());
        if (employee.getUser() == null)
            employee.setUser(user);

        employee.setFullName(fullName);
        employee.setEmail(email);
        employeeRepository.save(employee);
    }
}
