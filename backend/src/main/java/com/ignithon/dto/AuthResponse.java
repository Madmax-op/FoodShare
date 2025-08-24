package com.ignithon.dto;

public class AuthResponse {
    
    private String message;
    private Long userId;
    private String token;
    private String role;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }
    
    public AuthResponse(String message, Long userId, String token, String role) {
        this.message = message;
        this.userId = userId;
        this.token = token;
        this.role = role;
    }
    
    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
} 