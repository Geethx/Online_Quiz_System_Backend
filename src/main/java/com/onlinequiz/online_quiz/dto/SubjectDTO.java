package com.onlinequiz.online_quiz.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class SubjectDTO {
    private Long id;
    private String name;
    private String description;
    private Set<Integer> grades;
    private LocalDateTime createdAt;
    
    // Constructors
    public SubjectDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<Integer> getGrades() { return grades; }
    public void setGrades(Set<Integer> grades) { this.grades = grades; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
