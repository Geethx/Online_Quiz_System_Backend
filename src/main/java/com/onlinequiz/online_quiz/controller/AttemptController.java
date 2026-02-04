package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.AnswerDTO;
import com.onlinequiz.online_quiz.dto.AttemptDTO;
import com.onlinequiz.online_quiz.dto.SubmitAnswerDTO;
import com.onlinequiz.online_quiz.service.AttemptService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attempts")
@CrossOrigin(origins = "http://localhost:5173")
public class AttemptController {
    
    @Autowired
    private AttemptService attemptService;
    
    // Start a new attempt
    @PostMapping("/start/{assignmentId}")
    public ResponseEntity<AttemptDTO> startAttempt(@PathVariable Long assignmentId) {
        AttemptDTO attempt = attemptService.startAttempt(assignmentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(attempt);
    }
    
    // Get attempt by ID
    @GetMapping("/{id}")
    public ResponseEntity<AttemptDTO> getAttemptById(@PathVariable Long id) {
        AttemptDTO attempt = attemptService.getAttemptById(id);
        return ResponseEntity.ok(attempt);
    }
    
    // Get all attempts for an assignment
    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<AttemptDTO>> getAttemptsByAssignment(@PathVariable Long assignmentId) {
        List<AttemptDTO> attempts = attemptService.getAttemptsByAssignment(assignmentId);
        return ResponseEntity.ok(attempts);
    }
    
    // Submit or update an answer
    @PostMapping("/{attemptId}/answer")
    public ResponseEntity<AnswerDTO> submitAnswer(
            @PathVariable Long attemptId,
            @Valid @RequestBody SubmitAnswerDTO submitDTO) {
        AnswerDTO answer = attemptService.submitAnswer(attemptId, submitDTO);
        return ResponseEntity.ok(answer);
    }
    
    // Submit the entire attempt
    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<AttemptDTO> submitAttempt(@PathVariable Long attemptId) {
        AttemptDTO attempt = attemptService.submitAttempt(attemptId, false);
        return ResponseEntity.ok(attempt);
    }
    
    // Get all answers for an attempt
    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<List<AnswerDTO>> getAnswersByAttempt(@PathVariable Long attemptId) {
        List<AnswerDTO> answers = attemptService.getAnswersByAttempt(attemptId);
        return ResponseEntity.ok(answers);
    }
}