package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.UserDTO;
import com.onlinequiz.online_quiz.entity.Role;
import com.onlinequiz.online_quiz.entity.Subject;
import com.onlinequiz.online_quiz.entity.User;
import com.onlinequiz.online_quiz.repository.SubjectRepository;
import com.onlinequiz.online_quiz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import com.onlinequiz.online_quiz.dto.EnrollmentDTO;
import com.onlinequiz.online_quiz.entity.Enrollment;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public List<UserDTO> getAllStudents() {
        return userRepository.findByRole(Role.STUDENT).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getStudentById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getEnrollments() != null) {
            List<EnrollmentDTO> enrollmentDTOs = user.getEnrollments().stream()
                    .map(this::convertEnrollmentToDTO)
                    .collect(Collectors.toList());
            dto.setEnrollments(enrollmentDTOs);
        }

        return dto;
    }

    private EnrollmentDTO convertEnrollmentToDTO(Enrollment enrollment) {
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
