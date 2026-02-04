package com.onlinequiz.online_quiz.dto;

import jakarta.validation.constraints.*;

public class CreateQuestionDTO {
    
    @NotBlank(message = "Question text is required")
    private String text;
    
    @NotBlank(message = "Option A is required")
    private String optionA;
    
    @NotBlank(message = "Option B is required")
    private String optionB;
    
    @NotBlank(message = "Option C is required")
    private String optionC;
    
    @NotBlank(message = "Option D is required")
    private String optionD;
    
    @NotNull(message = "Correct option is required")
    @Min(value = 1, message = "Correct option must be between 1 and 4")
    @Max(value = 4, message = "Correct option must be between 1 and 4")
    private Integer correctOption;
    
    @NotBlank(message = "Difficulty is required")
    @Pattern(regexp = "EASY|MEDIUM|HARD", message = "Difficulty must be EASY, MEDIUM, or HARD")
    private String difficulty;
    
    @NotNull(message = "Points are required")
    @Min(value = 1, message = "Points must be at least 1")
    private Integer points;
    
    // Getters and Setters
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    
    public Integer getCorrectOption() { return correctOption; }
    public void setCorrectOption(Integer correctOption) { this.correctOption = correctOption; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}