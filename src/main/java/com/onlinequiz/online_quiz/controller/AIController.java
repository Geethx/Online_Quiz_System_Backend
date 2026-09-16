package com.onlinequiz.online_quiz.controller;

import com.onlinequiz.online_quiz.dto.CreateQuestionDTO;
import com.onlinequiz.online_quiz.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = {"http://localhost:5173", "https://studysprintonline.vercel.app"})
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/extract-questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CreateQuestionDTO>> extractQuestions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam("grade") Integer grade) {
        
        try {
            List<CreateQuestionDTO> extractedQuestions = aiService.extractQuestionsFromFile(file, subjectId, grade);
            return ResponseEntity.ok(extractedQuestions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
