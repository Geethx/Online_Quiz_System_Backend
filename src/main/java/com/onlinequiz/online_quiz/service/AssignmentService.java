package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.AssignmentDTO;
import com.onlinequiz.online_quiz.dto.CreateAssignmentDTO;
import com.onlinequiz.online_quiz.dto.QuestionDTO;
import com.onlinequiz.online_quiz.entity.Assignment;
import com.onlinequiz.online_quiz.entity.Question;
import com.onlinequiz.online_quiz.entity.Subject;
import com.onlinequiz.online_quiz.repository.AssignmentRepository;
import com.onlinequiz.online_quiz.repository.QuestionRepository;
import com.onlinequiz.online_quiz.repository.SubjectRepository;
import com.onlinequiz.online_quiz.repository.UserRepository;
import com.onlinequiz.online_quiz.entity.User;
import com.onlinequiz.online_quiz.entity.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all assignments
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get available assignments (based on current time)
    public List<AssignmentDTO> getAvailableAssignments(String username) {
        LocalDateTime now = LocalDateTime.now();
        List<Assignment> activeAssignments = assignmentRepository.findAvailableAssignments(now);

        if (username != null) {
            User user = userRepository.findByUsername(username)
                    .orElse(null);
            if (user != null && user.getRole().name().equals("STUDENT")) {
                List<Enrollment> approvedEnrollments = user.getEnrollments().stream()
                        .filter(e -> "APPROVED".equals(e.getStatus()))
                        .collect(Collectors.toList());
                
                activeAssignments = activeAssignments.stream()
                        .filter(a -> {
                            if (a.getSubject() == null) return false;
                            return approvedEnrollments.stream().anyMatch(e -> 
                                e.getSubject().getId().equals(a.getSubject().getId()) && 
                                e.getGrade().equals(a.getGrade())
                            );
                        })
                        .collect(Collectors.toList());
            }
        }

        return activeAssignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get assignment by ID
    public AssignmentDTO getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findByIdWithQuestions(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));
        return convertToDTO(assignment);
    }

    // Check if assignment is available
    public boolean isAssignmentAvailable(Long id) {
        Assignment assignment = assignmentRepository.findByIdWithQuestions(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(assignment.getStartTime()) && !now.isAfter(assignment.getEndTime());
    }

    // Create new assignment
    @Transactional
    public AssignmentDTO createAssignment(CreateAssignmentDTO createDTO) {
        // Validate time logic
        if (createDTO.getEndTime().isBefore(createDTO.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        if (createDTO.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Start time cannot be in the past");
        }

        // Validate duration doesn't exceed assignment window
        long minutesBetween = java.time.Duration.between(createDTO.getStartTime(), createDTO.getEndTime()).toMinutes();
        if (createDTO.getDuration() > minutesBetween) {
            throw new RuntimeException("Duration (" + createDTO.getDuration()
                    + " minutes) cannot exceed the time window (" + minutesBetween + " minutes)");
        }

        Assignment assignment = new Assignment();
        
        Subject subject = subjectRepository.findById(createDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        assignment.setSubject(subject);
        
        assignment.setName(createDTO.getName());
        assignment.setDescription(createDTO.getDescription());
        assignment.setStartTime(createDTO.getStartTime());
        assignment.setEndTime(createDTO.getEndTime());
        assignment.setDuration(createDTO.getDuration());
        assignment.setGrade(createDTO.getGrade());

        // Add selected questions
        Set<Question> questions = new HashSet<>();
        for (Long questionId : createDTO.getQuestionIds()) {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
            questions.add(question);
        }
        assignment.setQuestions(questions);

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(savedAssignment);
    }

    // Update assignment
    @Transactional
    public AssignmentDTO updateAssignment(Long id, CreateAssignmentDTO updateDTO) {
        Assignment assignment = assignmentRepository.findByIdWithQuestions(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + id));

        // Validate time logic
        if (updateDTO.getEndTime().isBefore(updateDTO.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        // Validate duration doesn't exceed assignment window
        long minutesBetween = java.time.Duration.between(updateDTO.getStartTime(), updateDTO.getEndTime()).toMinutes();
        if (updateDTO.getDuration() > minutesBetween) {
            throw new RuntimeException("Duration (" + updateDTO.getDuration()
                    + " minutes) cannot exceed the time window (" + minutesBetween + " minutes)");
        }

        Subject subject = subjectRepository.findById(updateDTO.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        assignment.setSubject(subject);

        assignment.setName(updateDTO.getName());
        assignment.setDescription(updateDTO.getDescription());
        assignment.setStartTime(updateDTO.getStartTime());
        assignment.setEndTime(updateDTO.getEndTime());
        assignment.setDuration(updateDTO.getDuration());
        assignment.setGrade(updateDTO.getGrade());

        // Update questions
        Set<Question> questions = new HashSet<>();
        for (Long questionId : updateDTO.getQuestionIds()) {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
            questions.add(question);
        }
        assignment.setQuestions(questions);

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return convertToDTO(updatedAssignment);
    }

    // Delete assignment
    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new RuntimeException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    // Convert Entity to DTO
    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        if (assignment.getSubject() != null) {
            dto.setSubjectId(assignment.getSubject().getId());
        }
        dto.setName(assignment.getName());
        dto.setDescription(assignment.getDescription());
        dto.setStartTime(assignment.getStartTime());
        dto.setEndTime(assignment.getEndTime());
        dto.setDuration(assignment.getDuration());
        dto.setGrade(assignment.getGrade());

        // Convert questions (with answers for admin view)
        List<QuestionDTO> questionDTOs = assignment.getQuestions().stream()
                .sorted((q1, q2) -> q1.getId().compareTo(q2.getId())) // Sort by question ID
                .map(questionService::convertToDTOWithoutAnswer) // Hide correct answers
                .collect(Collectors.toList());
        dto.setQuestions(questionDTOs);

        // Calculate total points
        int totalPoints = assignment.getQuestions().stream()
                .mapToInt(Question::getPoints)
                .sum();
        dto.setTotalPoints(totalPoints);

        // Check if available
        LocalDateTime now = LocalDateTime.now();
        boolean isAvailable = !now.isBefore(assignment.getStartTime()) && !now.isAfter(assignment.getEndTime());
        dto.setIsAvailable(isAvailable);

        return dto;
    }
}