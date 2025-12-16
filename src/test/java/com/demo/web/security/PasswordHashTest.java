package com.demo.web.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordHashTest {

    @Test
    public void testPasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "12345678";
        String dbHash = "$2a$10$zVr31i8t0teuN0N7bYIZVuV6d3cYE3cLBYktnA4Ancc7P6BAIQFse";

        assertTrue(encoder.matches(rawPassword, dbHash), "Password hash does not match!");
        System.out.println("Password valid: " + encoder.matches(rawPassword, dbHash));
    }
}
