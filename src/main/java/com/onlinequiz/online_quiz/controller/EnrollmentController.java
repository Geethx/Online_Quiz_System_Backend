package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.CreateEnrollmentDTO;
import com.onlinequiz.online_quiz.dto.EnrollmentDTO;
import com.onlinequiz.online_quiz.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = {"http://localhost:5173", "https://studysprintonline.vercel.app"})
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/request")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentDTO> requestEnrollment(
            Authentication authentication,
            @Valid @RequestBody CreateEnrollmentDTO dto) {
        EnrollmentDTO created = enrollmentService.requestEnrollment(authentication.getName(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentDTO>> getMyEnrollments(Authentication authentication) {
        return ResponseEntity.ok(enrollmentService.getMyEnrollments(authentication.getName()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EnrollmentDTO>> getPendingRequests() {
        return ResponseEntity.ok(enrollmentService.getPendingRequests());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null) {
            throw new RuntimeException("Status is required");
        }
        return ResponseEntity.ok(enrollmentService.updateEnrollmentStatus(id, status));
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EnrollmentDTO>> getStudentsBySubjectAndGrade(
            @PathVariable Long subjectId,
            @RequestParam(required = false) Integer grade) {
        return ResponseEntity.ok(enrollmentService.getApprovedEnrollmentsBySubjectAndGrade(subjectId, grade));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
