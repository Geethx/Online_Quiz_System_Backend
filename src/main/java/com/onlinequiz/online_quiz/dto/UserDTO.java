package com.onlinequiz.online_quiz.dto;

import com.onlinequiz.online_quiz.entity.Role;
import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private List<EnrollmentDTO> enrollments;
    
    // Constructors
    public UserDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<EnrollmentDTO> getEnrollments() { return enrollments; }
    public void setEnrollments(List<EnrollmentDTO> enrollments) { this.enrollments = enrollments; }
}
