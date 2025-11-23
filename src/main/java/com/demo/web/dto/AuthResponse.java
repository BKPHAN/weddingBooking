package com.demo.web.dto;

public class AuthResponse {
    private final String accessToken;
    private final String refreshToken;
    private final String tokenType = "Bearer";
    private final String primaryRole;
    private final String redirectUrl;

    public AuthResponse(String accessToken, String refreshToken, String primaryRole, String redirectUrl) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.primaryRole = primaryRole;
        this.redirectUrl = redirectUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getPrimaryRole() {
        return primaryRole;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
