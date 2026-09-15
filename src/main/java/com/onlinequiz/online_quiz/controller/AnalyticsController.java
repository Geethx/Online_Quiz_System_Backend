package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.StudentAnalyticsDTO;
import com.onlinequiz.online_quiz.dto.SubjectAnalyticsDTO;
import com.onlinequiz.online_quiz.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/students/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentAnalyticsDTO> getStudentAnalytics(@PathVariable Long studentId) {
        return ResponseEntity.ok(analyticsService.getStudentAnalytics(studentId));
    }

    @GetMapping("/subjects/{subjectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectAnalyticsDTO> getSubjectAnalytics(@PathVariable Long subjectId) {
        return ResponseEntity.ok(analyticsService.getSubjectAnalytics(subjectId));
    }
}
