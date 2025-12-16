package com.demo.web.security;

import com.demo.web.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String fullName;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean active;

    public UserPrincipal(Long id,
            String username,
            String fullName,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean active) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.active = active;
    }

    public static UserPrincipal fromUser(User user) {
        Set<GrantedAuthority> authorities = new java.util.HashSet<>();

        if (user.getPrimaryRole() != null) {
            String primaryRoleCode = "ROLE_" + user.getPrimaryRole().name();
            authorities.add(new SimpleGrantedAuthority(primaryRoleCode));
        }

        String fullName = user.getEmployee() != null ? user.getEmployee().getFullName() : user.getUsername();

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                fullName,
                user.getEmail(),
                user.getPasswordHash(),
                authorities,
                user.getStatus() == User.UserStatus.ACTIVE);
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
