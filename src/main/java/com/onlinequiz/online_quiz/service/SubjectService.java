package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.CreateSubjectDTO;
import com.onlinequiz.online_quiz.dto.SubjectDTO;
import com.onlinequiz.online_quiz.entity.Subject;
import com.onlinequiz.online_quiz.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // Get all subjects
    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get subject by ID
    public SubjectDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        return convertToDTO(subject);
    }

    // Create new subject
    @Transactional
    public SubjectDTO createSubject(CreateSubjectDTO createDTO) {
        if (subjectRepository.existsByName(createDTO.getName())) {
            throw new RuntimeException("Subject with this name already exists");
        }
        
        Subject subject = new Subject();
        subject.setName(createDTO.getName());
        subject.setDescription(createDTO.getDescription());
        if (createDTO.getGrades() != null) {
            subject.setGrades(createDTO.getGrades());
        }
        
        Subject savedSubject = subjectRepository.save(subject);
        return convertToDTO(savedSubject);
    }

    // Update subject
    @Transactional
    public SubjectDTO updateSubject(Long id, CreateSubjectDTO updateDTO) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        
        if (!subject.getName().equals(updateDTO.getName()) && subjectRepository.existsByName(updateDTO.getName())) {
            throw new RuntimeException("Subject with this name already exists");
        }
        
        subject.setName(updateDTO.getName());
        subject.setDescription(updateDTO.getDescription());
        if (updateDTO.getGrades() != null) {
            subject.getGrades().clear();
            subject.getGrades().addAll(updateDTO.getGrades());
        }
        
        Subject updatedSubject = subjectRepository.save(subject);
        return convertToDTO(updatedSubject);
    }

    // Delete subject
    @Transactional
    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found with id: " + id);
        }
        subjectRepository.deleteById(id);
    }

    // Convert Entity to DTO
    private SubjectDTO convertToDTO(Subject subject) {
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        dto.setGrades(subject.getGrades());
        dto.setCreatedAt(subject.getCreatedAt());
        return dto;
    }
}
