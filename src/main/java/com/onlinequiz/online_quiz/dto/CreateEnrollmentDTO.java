package com.onlinequiz.online_quiz.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateEnrollmentDTO {
    
    @NotNull(message = "Subject is required")
    private Long subjectId;
    
    @NotNull(message = "Grade is required")
    @Min(value = 6, message = "Grade must be between 6 and 11")
    @Max(value = 11, message = "Grade must be between 6 and 11")
    private Integer grade;

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }
}
