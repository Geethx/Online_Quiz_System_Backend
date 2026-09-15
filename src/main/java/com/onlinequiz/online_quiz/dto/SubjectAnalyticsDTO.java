package com.onlinequiz.online_quiz.dto;

import java.util.List;

public class SubjectAnalyticsDTO {
    private SubjectDTO subject;
    private int totalAssignments;
    private int totalStudents;
    private double averageScorePercentage;
    private List<ChartDataDTO> assignmentPerformance;

    public SubjectDTO getSubject() { return subject; }
    public void setSubject(SubjectDTO subject) { this.subject = subject; }

    public int getTotalAssignments() { return totalAssignments; }
    public void setTotalAssignments(int totalAssignments) { this.totalAssignments = totalAssignments; }

    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }

    public double getAverageScorePercentage() { return averageScorePercentage; }
    public void setAverageScorePercentage(double averageScorePercentage) { this.averageScorePercentage = averageScorePercentage; }

    public List<ChartDataDTO> getAssignmentPerformance() { return assignmentPerformance; }
    public void setAssignmentPerformance(List<ChartDataDTO> assignmentPerformance) { this.assignmentPerformance = assignmentPerformance; }
}
