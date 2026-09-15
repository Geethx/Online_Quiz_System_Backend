package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.*;
import com.onlinequiz.online_quiz.entity.Attempt;
import com.onlinequiz.online_quiz.entity.Subject;
import com.onlinequiz.online_quiz.repository.AttemptRepository;
import com.onlinequiz.online_quiz.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AttemptService attemptService;

    // Student Performance Analytics
    public StudentAnalyticsDTO getStudentAnalytics(Long studentId) {
        UserDTO student = userService.getStudentById(studentId);
        List<Attempt> attempts = attemptRepository.findByUserId(studentId);

        StudentAnalyticsDTO analytics = new StudentAnalyticsDTO();
        analytics.setStudent(student);
        analytics.setTotalAttempts(attempts.size());

        if (!attempts.isEmpty()) {
            double totalPercentage = 0;
            int count = 0;
            for (Attempt attempt : attempts) {
                if ("SUBMITTED".equals(attempt.getStatus()) || "AUTO_SUBMITTED".equals(attempt.getStatus())) {
                    if (attempt.getTotalPoints() != null && attempt.getTotalPoints() > 0) {
                        totalPercentage += ((double) attempt.getScore() / attempt.getTotalPoints()) * 100;
                        count++;
                    }
                }
            }
            if (count > 0) {
                analytics.setAverageScorePercentage(totalPercentage / count);
            } else {
                analytics.setAverageScorePercentage(0.0);
            }
        } else {
            analytics.setAverageScorePercentage(0.0);
        }

        List<ChartDataDTO> performanceHistory = attempts.stream()
                .filter(a -> "SUBMITTED".equals(a.getStatus()) || "AUTO_SUBMITTED".equals(a.getStatus()))
                .map(a -> new ChartDataDTO(
                        a.getAssignment() != null ? a.getAssignment().getName() : "Unknown",
                        a.getScore(),
                        a.getTotalPoints()
                ))
                .collect(Collectors.toList());
        analytics.setPerformanceHistory(performanceHistory);

        List<AttemptDTO> pastAttempts = attempts.stream()
                .map(attempt -> attemptService.getAttemptById(attempt.getId()))
                .collect(Collectors.toList());
        analytics.setPastAttempts(pastAttempts);

        return analytics;
    }

    // Subject Performance Analytics
    public SubjectAnalyticsDTO getSubjectAnalytics(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        List<Attempt> attempts = attemptRepository.findByAssignmentSubjectId(subjectId);

        SubjectAnalyticsDTO analytics = new SubjectAnalyticsDTO();
        
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(subject.getId());
        subjectDTO.setName(subject.getName());
        analytics.setSubject(subjectDTO);

        analytics.setTotalAssignments(subject.getAssignments().size());
        analytics.setTotalStudents(subject.getStudents().size());

        if (!attempts.isEmpty()) {
            double totalPercentage = 0;
            int count = 0;
            for (Attempt attempt : attempts) {
                if ("SUBMITTED".equals(attempt.getStatus()) || "AUTO_SUBMITTED".equals(attempt.getStatus())) {
                    if (attempt.getTotalPoints() != null && attempt.getTotalPoints() > 0) {
                        totalPercentage += ((double) attempt.getScore() / attempt.getTotalPoints()) * 100;
                        count++;
                    }
                }
            }
            if (count > 0) {
                analytics.setAverageScorePercentage(totalPercentage / count);
            } else {
                analytics.setAverageScorePercentage(0.0);
            }
        } else {
            analytics.setAverageScorePercentage(0.0);
        }

        // Group by assignment for charts
        // This is simplified. We take attempts and sum scores per assignment, but for now we just list all attempts as data points
        List<ChartDataDTO> assignmentPerformance = attempts.stream()
                .filter(a -> "SUBMITTED".equals(a.getStatus()) || "AUTO_SUBMITTED".equals(a.getStatus()))
                .map(a -> new ChartDataDTO(
                        a.getAssignment() != null ? a.getAssignment().getName() + " (" + a.getUser().getUsername() + ")" : "Unknown",
                        a.getScore(),
                        a.getTotalPoints()
                ))
                .collect(Collectors.toList());
        
        analytics.setAssignmentPerformance(assignmentPerformance);

        return analytics;
    }
}
