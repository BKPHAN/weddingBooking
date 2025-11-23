package com.demo.web.service;

import com.demo.web.dto.AuthResponse;
import com.demo.web.dto.LoginRequest;
import com.demo.web.dto.RefreshTokenRequest;
import com.demo.web.model.RefreshToken;
import com.demo.web.model.User;
import com.demo.web.model.enums.UserRole;
import com.demo.web.repository.RefreshTokenRepository;
import com.demo.web.repository.UserRepository;
import com.demo.web.security.JwtProperties;
import com.demo.web.security.JwtTokenProvider;
import com.demo.web.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider,
                       RefreshTokenRepository refreshTokenRepository,
                       UserRepository userRepository,
                       JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getUsernameOrEmail();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByEmail(principal.getEmail())
                .or(() -> userRepository.findByUsername(principal.getUsername()))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        RefreshToken refreshToken = issueRefreshToken(user);
        UserRole role = user.getPrimaryRole();
        return new AuthResponse(accessToken,
                refreshToken.getToken(),
                role != null ? role.name() : UserRole.USER.name(),
                resolveRedirect(role));
    }

    @Transactional
    public AuthResponse refresh(TokenRefreshContext context) {
        RefreshToken existingToken = refreshTokenRepository.findByToken(context.refreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (Boolean.TRUE.equals(existingToken.getRevoked()) || existingToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token expired or revoked");
        }

        User user = existingToken.getUser();
        UserPrincipal principal = UserPrincipal.fromUser(user);

        String newAccessToken = jwtTokenProvider.generateAccessToken(principal);
        RefreshToken newRefreshToken = rotateRefreshToken(existingToken);
        UserRole role = user.getPrimaryRole();
        return new AuthResponse(newAccessToken,
                newRefreshToken.getToken(),
                role != null ? role.name() : UserRole.USER.name(),
                resolveRedirect(role));
    }

    private RefreshToken issueRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtTokenProvider.generateRefreshToken(user.getEmail()));
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenValidity()));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken rotateRefreshToken(RefreshToken existingToken) {
        existingToken.setRevoked(true);
        refreshTokenRepository.save(existingToken);

        RefreshToken newToken = new RefreshToken();
        newToken.setUser(existingToken.getUser());
        newToken.setToken(jwtTokenProvider.generateRefreshToken(existingToken.getUser().getEmail()));
        newToken.setExpiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenValidity()));
        newToken.setRevoked(false);
        return refreshTokenRepository.save(newToken);
    }

    private String resolveRedirect(UserRole role) {
        if (role == UserRole.ADMIN) {
            return "/admin/dashboard";
        }
        return "/user/dashboard";
    }

    public record TokenRefreshContext(String refreshToken) {
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        return refresh(new TokenRefreshContext(request.getRefreshToken()));
    }
}
