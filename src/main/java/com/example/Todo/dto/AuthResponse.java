package com.example.Todo.dto;

import java.util.Set;

public class AuthResponse {
    private String token;
    private String username;
    private Long userId;
    private Set<String> roles;

    public AuthResponse() {}

    public AuthResponse(String token, String username, Long userId, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
