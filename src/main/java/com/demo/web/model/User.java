package com.demo.web.model;

import com.demo.web.model.common.BaseEntity;
import com.demo.web.model.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank
    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_role", nullable = false, length = 20)
    private UserRole primaryRole = UserRole.USER;

    @jakarta.persistence.OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Employee employee;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserRole getPrimaryRole() {
        return primaryRole;
    }

    public void setPrimaryRole(UserRole primaryRole) {
        this.primaryRole = primaryRole;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        LOCKED
    }
}
