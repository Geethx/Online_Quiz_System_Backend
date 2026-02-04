package com.onlinequiz.online_quiz.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AttemptDTO {
    private Long id;
    private Long assignmentId;
    private String assignmentName;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private String status;
    private Integer score;
    private Integer totalPoints;
    private Integer remainingTimeSeconds;
    private List<AnswerDTO> answers;
    
    // Constructors
    public AttemptDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    
    public String getAssignmentName() { return assignmentName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    
    public Integer getRemainingTimeSeconds() { return remainingTimeSeconds; }
    public void setRemainingTimeSeconds(Integer remainingTimeSeconds) { this.remainingTimeSeconds = remainingTimeSeconds; }
    
    public List<AnswerDTO> getAnswers() { return answers; }
    public void setAnswers(List<AnswerDTO> answers) { this.answers = answers; }
}