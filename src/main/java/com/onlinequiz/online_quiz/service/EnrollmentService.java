package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.CreateEnrollmentDTO;
import com.onlinequiz.online_quiz.dto.EnrollmentDTO;
import com.onlinequiz.online_quiz.entity.Enrollment;
import com.onlinequiz.online_quiz.entity.Subject;
import com.onlinequiz.online_quiz.entity.User;
import com.onlinequiz.online_quiz.repository.EnrollmentRepository;
import com.onlinequiz.online_quiz.repository.SubjectRepository;
import com.onlinequiz.online_quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Transactional
    public EnrollmentDTO requestEnrollment(String username, CreateEnrollmentDTO dto) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!student.getRole().name().equals("STUDENT")) {
            throw new RuntimeException("Only students can request enrollment");
        }

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Optional<Enrollment> existing = enrollmentRepository.findByStudentIdAndSubjectIdAndGrade(
                student.getId(), subject.getId(), dto.getGrade());

        if (existing.isPresent()) {
            throw new RuntimeException("Enrollment request already exists for this subject and grade");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setGrade(dto.getGrade());
        enrollment.setStatus("PENDING");

        Enrollment saved = enrollmentRepository.save(enrollment);
        return convertToDTO(saved);
    }

    public List<EnrollmentDTO> getMyEnrollments(String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return enrollmentRepository.findByStudentId(student.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDTO> getPendingRequests() {
        return enrollmentRepository.findByStatus("PENDING").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDTO updateEnrollmentStatus(Long id, String status) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
            throw new RuntimeException("Invalid status");
        }

        enrollment.setStatus(status);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return convertToDTO(saved);
    }

    public List<EnrollmentDTO> getApprovedEnrollmentsBySubjectAndGrade(Long subjectId, Integer grade) {
        List<Enrollment> enrollments;
        if (grade != null) {
            enrollments = enrollmentRepository.findBySubjectIdAndGrade(subjectId, grade);
        } else {
            enrollments = enrollmentRepository.findBySubjectId(subjectId);
        }
        
        return enrollments.stream()
                .filter(e -> "APPROVED".equals(e.getStatus()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getFullName());
        dto.setSubjectId(enrollment.getSubject().getId());
        dto.setSubjectName(enrollment.getSubject().getName());
        dto.setGrade(enrollment.getGrade());
        dto.setStatus(enrollment.getStatus());
        dto.setCreatedAt(enrollment.getCreatedAt());
        dto.setUpdatedAt(enrollment.getUpdatedAt());
        return dto;
    }
}
