package com.onlinequiz.online_quiz.dto;

public class QuestionDTO {
    private Long id;
    private String text;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String difficulty;
    private Integer points;
    private Long subjectId;
    private Integer grade;

    // For admin view only - don't send to students during attempt
    private Integer correctOption;
    private String questionImageUrl;
    private String optionAImageUrl;
    private String optionBImageUrl;
    private String optionCImageUrl;
    private String optionDImageUrl;

    // Constructors
    public QuestionDTO() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(Integer correctOption) {
        this.correctOption = correctOption;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public void setQuestionImageUrl(String questionImageUrl) {
        this.questionImageUrl = questionImageUrl;
    }

    public String getOptionAImageUrl() {
        return optionAImageUrl;
    }

    public void setOptionAImageUrl(String optionAImageUrl) {
        this.optionAImageUrl = optionAImageUrl;
    }

    public String getOptionBImageUrl() {
        return optionBImageUrl;
    }

    public void setOptionBImageUrl(String optionBImageUrl) {
        this.optionBImageUrl = optionBImageUrl;
    }

    public String getOptionCImageUrl() {
        return optionCImageUrl;
    }

    public void setOptionCImageUrl(String optionCImageUrl) {
        this.optionCImageUrl = optionCImageUrl;
    }

    public String getOptionDImageUrl() {
        return optionDImageUrl;
    }

    public void setOptionDImageUrl(String optionDImageUrl) {
        this.optionDImageUrl = optionDImageUrl;
    }

}