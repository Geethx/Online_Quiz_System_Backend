package com.onlinequiz.online_quiz.dto;

public class AnswerDTO {
    private Long id;
    private Long questionId;
    private Integer selectedAnswer;
    private Boolean markedForReview;
    private Boolean isCorrect;
    
    // Constructors
    public AnswerDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    
    public Integer getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(Integer selectedAnswer) { this.selectedAnswer = selectedAnswer; }
    
    public Boolean getMarkedForReview() { return markedForReview; }
    public void setMarkedForReview(Boolean markedForReview) { this.markedForReview = markedForReview; }
    
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}