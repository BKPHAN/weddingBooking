package com.demo.web.controller;

import com.demo.web.dto.ApiResponse;
import com.demo.web.dto.AuthResponse;
import com.demo.web.dto.LoginRequest;
import com.demo.web.dto.RefreshTokenRequest;
import com.demo.web.dto.RegisterRequest;
import com.demo.web.model.enums.UserRole;
import com.demo.web.security.JwtProperties;
import com.demo.web.service.AuthService;
import com.demo.web.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtProperties jwtProperties;

    public AuthController(AuthService authService, UserService userService, JwtProperties jwtProperties) {
        this.authService = authService;
        this.userService = userService;
        this.jwtProperties = jwtProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return buildAuthResponse(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return buildAuthResponse(authService.refresh(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        userService.registerUser(request.getUsername(),
                request.getFullName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole() != null ? request.getRole() : UserRole.USER);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful", null));
    }

    private ResponseEntity<AuthResponse> buildAuthResponse(AuthResponse authResponse) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();

        if (authResponse.getAccessToken() != null) {
            ResponseCookie accessCookie = ResponseCookie.from("access_token", authResponse.getAccessToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofSeconds(jwtProperties.getAccessTokenValidity()))
                    .sameSite("Strict")
                    .build();
            builder.header(HttpHeaders.SET_COOKIE, accessCookie.toString());
        }

        if (authResponse.getRefreshToken() != null) {
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", authResponse.getRefreshToken())
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofSeconds(jwtProperties.getRefreshTokenValidity()))
                    .sameSite("Strict")
                    .build();
            builder.header(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        return builder.body(authResponse);
    }
}
