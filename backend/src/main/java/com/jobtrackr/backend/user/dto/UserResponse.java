package com.jobtrackr.backend.user.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.jobtrackr.backend.user.model.UserRole;

public class UserResponse {

    private String id;
    private String email;
    private String displayName;
    private Set<UserRole> roles;
    private boolean enabled;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(
            String id,
            String email,
            String displayName,
            Set<UserRole> roles,
            boolean enabled,
            LocalDateTime createdAt) {

        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.roles = roles;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}