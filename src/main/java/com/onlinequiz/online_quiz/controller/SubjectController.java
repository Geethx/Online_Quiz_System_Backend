package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.CreateSubjectDTO;
import com.onlinequiz.online_quiz.dto.SubjectDTO;
import com.onlinequiz.online_quiz.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // Get all subjects (accessible to all authenticated users)
    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    // Get subject by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    // Create new subject (Admin only)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody CreateSubjectDTO createDTO) {
        return new ResponseEntity<>(subjectService.createSubject(createDTO), HttpStatus.CREATED);
    }

    // Update subject (Admin only)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody CreateSubjectDTO updateDTO) {
        return ResponseEntity.ok(subjectService.updateSubject(id, updateDTO));
    }

    // Delete subject (Admin only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
