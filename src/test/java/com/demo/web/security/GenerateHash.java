package com.demo.web.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerateHash {
    @Test
    public void generate() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("12345678");
        System.out.println("GENERATED_HASH_PREFIX " + hash);
    }
}
