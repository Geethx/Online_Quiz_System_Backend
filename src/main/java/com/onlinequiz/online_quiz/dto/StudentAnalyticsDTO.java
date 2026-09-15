package com.onlinequiz.online_quiz.dto;

import java.util.List;

public class StudentAnalyticsDTO {
    private UserDTO student;
    private int totalAttempts;
    private double averageScorePercentage;
    private List<ChartDataDTO> performanceHistory;
    private List<AttemptDTO> pastAttempts;

    public UserDTO getStudent() { return student; }
    public void setStudent(UserDTO student) { this.student = student; }

    public int getTotalAttempts() { return totalAttempts; }
    public void setTotalAttempts(int totalAttempts) { this.totalAttempts = totalAttempts; }

    public double getAverageScorePercentage() { return averageScorePercentage; }
    public void setAverageScorePercentage(double averageScorePercentage) { this.averageScorePercentage = averageScorePercentage; }

    public List<ChartDataDTO> getPerformanceHistory() { return performanceHistory; }
    public void setPerformanceHistory(List<ChartDataDTO> performanceHistory) { this.performanceHistory = performanceHistory; }

    public List<AttemptDTO> getPastAttempts() { return pastAttempts; }
    public void setPastAttempts(List<AttemptDTO> pastAttempts) { this.pastAttempts = pastAttempts; }
}
