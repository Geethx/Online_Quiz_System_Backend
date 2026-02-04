package com.onlinequiz.online_quiz.dto;

import jakarta.validation.constraints.NotNull;

public class SubmitAnswerDTO {
    
    @NotNull(message = "Question ID is required")
    private Long questionId;
    
    private Integer selectedAnswer; // Can be null if user wants to clear answer
    
    private Boolean markedForReview = false;
    
    // Constructors
    public SubmitAnswerDTO() {}
    
    // Getters and Setters
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    
    public Integer getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(Integer selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public Boolean getMarkedForReview() { return markedForReview; }
    public void setMarkedForReview(Boolean markedForReview) { this.markedForReview = markedForReview; }
}