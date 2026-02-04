package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.AssignmentDTO;
import com.onlinequiz.online_quiz.dto.CreateAssignmentDTO;
import com.onlinequiz.online_quiz.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "http://localhost:5173")
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    // Get all assignments
    @GetMapping
    public ResponseEntity<List<AssignmentDTO>> getAllAssignments() {
        List<AssignmentDTO> assignments = assignmentService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }
    
    // Get available assignments
    @GetMapping("/available")
    public ResponseEntity<List<AssignmentDTO>> getAvailableAssignments() {
        List<AssignmentDTO> assignments = assignmentService.getAvailableAssignments();
        return ResponseEntity.ok(assignments);
    }
    
    // Get assignment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable Long id) {
        AssignmentDTO assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }
    
    // Create new assignment
    @PostMapping
    public ResponseEntity<AssignmentDTO> createAssignment(@Valid @RequestBody CreateAssignmentDTO createDTO) {
        AssignmentDTO created = assignmentService.createAssignment(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // Update assignment
    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody CreateAssignmentDTO updateDTO) {
        AssignmentDTO updated = assignmentService.updateAssignment(id, updateDTO);
        return ResponseEntity.ok(updated);
    }
    
    // Delete assignment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}