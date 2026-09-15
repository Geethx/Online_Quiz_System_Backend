package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.CreateQuestionDTO;
import com.onlinequiz.online_quiz.dto.QuestionDTO;
import com.onlinequiz.online_quiz.entity.Question;
import com.onlinequiz.online_quiz.entity.Subject;
import com.onlinequiz.online_quiz.repository.QuestionRepository;
import com.onlinequiz.online_quiz.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
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
        Subject subject = subjectRepository.findById(createDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Question question = new Question();
        question.setSubject(subject);
        question.setText(createDTO.getText());
        question.setOptionA(createDTO.getOptionA());
        question.setOptionB(createDTO.getOptionB());
        question.setOptionC(createDTO.getOptionC());
        question.setOptionD(createDTO.getOptionD());
        question.setCorrectOption(createDTO.getCorrectOption());
        question.setDifficulty(createDTO.getDifficulty());
        question.setPoints(createDTO.getPoints());
        question.setQuestionImageUrl(createDTO.getQuestionImageUrl());
        question.setOptionAImageUrl(createDTO.getOptionAImageUrl());
        question.setOptionBImageUrl(createDTO.getOptionBImageUrl());
        question.setOptionCImageUrl(createDTO.getOptionCImageUrl());
        question.setOptionDImageUrl(createDTO.getOptionDImageUrl());
        question.setGrade(createDTO.getGrade());
        
        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }
    
    // Update question
    @Transactional
    public QuestionDTO updateQuestion(Long id, CreateQuestionDTO updateDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
        
        Subject subject = subjectRepository.findById(updateDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        
        question.setSubject(subject);
        question.setText(updateDTO.getText());
        question.setOptionA(updateDTO.getOptionA());
        question.setOptionB(updateDTO.getOptionB());
        question.setOptionC(updateDTO.getOptionC());
        question.setOptionD(updateDTO.getOptionD());
        question.setCorrectOption(updateDTO.getCorrectOption());
        question.setDifficulty(updateDTO.getDifficulty());
        question.setPoints(updateDTO.getPoints());
        question.setQuestionImageUrl(updateDTO.getQuestionImageUrl());
        question.setOptionAImageUrl(updateDTO.getOptionAImageUrl());
        question.setOptionBImageUrl(updateDTO.getOptionBImageUrl());
        question.setOptionCImageUrl(updateDTO.getOptionCImageUrl());
        question.setOptionDImageUrl(updateDTO.getOptionDImageUrl());
        question.setGrade(updateDTO.getGrade());
        
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
        if (question.getSubject() != null) {
            dto.setSubjectId(question.getSubject().getId());
        }
        dto.setGrade(question.getGrade());
        dto.setText(question.getText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setCorrectOption(question.getCorrectOption());
        dto.setDifficulty(question.getDifficulty());
        dto.setPoints(question.getPoints());
        dto.setQuestionImageUrl(question.getQuestionImageUrl());
        dto.setOptionAImageUrl(question.getOptionAImageUrl());
        dto.setOptionBImageUrl(question.getOptionBImageUrl());
        dto.setOptionCImageUrl(question.getOptionCImageUrl());
        dto.setOptionDImageUrl(question.getOptionDImageUrl());
        return dto;
    }
    
    // Convert Entity to DTO without correct answer (for student view during attempt)
    public QuestionDTO convertToDTOWithoutAnswer(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        if (question.getSubject() != null) {
            dto.setSubjectId(question.getSubject().getId());
        }
        dto.setGrade(question.getGrade());
        dto.setText(question.getText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setDifficulty(question.getDifficulty());
        dto.setPoints(question.getPoints());
        dto.setQuestionImageUrl(question.getQuestionImageUrl());
        dto.setOptionAImageUrl(question.getOptionAImageUrl());
        dto.setOptionBImageUrl(question.getOptionBImageUrl());
        dto.setOptionCImageUrl(question.getOptionCImageUrl());
        dto.setOptionDImageUrl(question.getOptionDImageUrl());
        // DO NOT set correctOption - keep it hidden during attempt
        return dto;
    }
}