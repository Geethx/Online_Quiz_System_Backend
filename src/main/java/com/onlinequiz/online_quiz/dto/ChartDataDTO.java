package com.onlinequiz.online_quiz.dto;

public class ChartDataDTO {
    private String name;
    private Integer score;
    private Integer totalPoints;

    public ChartDataDTO() {}

    public ChartDataDTO(String name, Integer score, Integer totalPoints) {
        this.name = name;
        this.score = score;
        this.totalPoints = totalPoints;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
}
