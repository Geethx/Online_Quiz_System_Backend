package com.onlinequiz.online_quiz.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateSubjectDTO {
    
    @NotBlank(message = "Subject name is required")
    private String name;
    
    private String description;

    private java.util.Set<Integer> grades;
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public java.util.Set<Integer> getGrades() { return grades; }
    public void setGrades(java.util.Set<Integer> grades) { this.grades = grades; }
}
