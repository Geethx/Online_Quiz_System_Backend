package com.onlinequiz.online_quiz.dto;

public class AuthResponseDTO {
    private String token;
    private String username;
    private String fullName;
    private String role;
    private Long userId;

    // Constructors
    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String username, String fullName, String role, Long userId) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.userId = userId;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
