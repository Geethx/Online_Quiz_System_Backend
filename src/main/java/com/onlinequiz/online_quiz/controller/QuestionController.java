package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.CreateQuestionDTO;
import com.onlinequiz.online_quiz.dto.QuestionDTO;
import com.onlinequiz.online_quiz.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:5173")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    // Get all questions
    @GetMapping
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        List<QuestionDTO> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
    
    // Get question by ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return ResponseEntity.ok(question);
    }
    
    // Get questions by difficulty
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByDifficulty(@PathVariable String difficulty) {
        List<QuestionDTO> questions = questionService.getQuestionsByDifficulty(difficulty.toUpperCase());
        return ResponseEntity.ok(questions);
    }
    
    // Create new question
    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(@Valid @RequestBody CreateQuestionDTO createDTO) {
        QuestionDTO created = questionService.createQuestion(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    // Update question
    @PutMapping("/{id}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody CreateQuestionDTO updateDTO) {
        QuestionDTO updated = questionService.updateQuestion(id, updateDTO);
        return ResponseEntity.ok(updated);
    }
    
    // Delete question
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }
}