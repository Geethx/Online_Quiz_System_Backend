package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.CreateQuestionDTO;
import com.onlinequiz.online_quiz.dto.QuestionDTO;
import com.onlinequiz.online_quiz.entity.Question;
import com.onlinequiz.online_quiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    // Get all questions
    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Get question by ID
    public QuestionDTO getQuestionById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        return convertToDTO(question);
    }
    
    // Get questions by difficulty
    public List<QuestionDTO> getQuestionsByDifficulty(String difficulty) {
        return questionRepository.findByDifficultyOrderByCreatedAtDesc(difficulty).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Create new question
    @Transactional
    public QuestionDTO createQuestion(CreateQuestionDTO createDTO) {
        Question question = new Question();
        question.setText(createDTO.getText());
        question.setOptionA(createDTO.getOptionA());
        question.setOptionB(createDTO.getOptionB());
        question.setOptionC(createDTO.getOptionC());
        question.setOptionD(createDTO.getOptionD());
        question.setCorrectOption(createDTO.getCorrectOption());
        question.setDifficulty(createDTO.getDifficulty());
        question.setPoints(createDTO.getPoints());
        
        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }
    
    // Update question
    @Transactional
    public QuestionDTO updateQuestion(Long id, CreateQuestionDTO updateDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        question.setText(updateDTO.getText());
        question.setOptionA(updateDTO.getOptionA());
        question.setOptionB(updateDTO.getOptionB());
        question.setOptionC(updateDTO.getOptionC());
        question.setOptionD(updateDTO.getOptionD());
        question.setCorrectOption(updateDTO.getCorrectOption());
        question.setDifficulty(updateDTO.getDifficulty());
        question.setPoints(updateDTO.getPoints());
        
        Question updatedQuestion = questionRepository.save(question);
        return convertToDTO(updatedQuestion);
    }
    
    // Delete question
    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }
    
    // Convert Entity to DTO
    private QuestionDTO convertToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setCorrectOption(question.getCorrectOption());
        dto.setDifficulty(question.getDifficulty());
        dto.setPoints(question.getPoints());
        return dto;
    }
    
    // Convert Entity to DTO without correct answer (for student view during attempt)
    public QuestionDTO convertToDTOWithoutAnswer(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setDifficulty(question.getDifficulty());
        dto.setPoints(question.getPoints());
        // DO NOT set correctOption - keep it hidden during attempt
        return dto;
    }
}