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

    @Transactional
    public UserDTO updateStudentSubjects(Long studentId, List<Long> subjectIds) {
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + studentId));

        if (user.getRole() != Role.STUDENT) {
            throw new RuntimeException("User is not a student");
        }

        user.getSubjects().clear();
        for (Long subjectId : subjectIds) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + subjectId));
            user.getSubjects().add(subject);
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getSubjects() != null) {
            List<Long> subjectIds = user.getSubjects().stream()
                    .map(Subject::getId)
                    .collect(Collectors.toList());
            dto.setSubjectIds(subjectIds);
        }

        return dto;
    }
}
