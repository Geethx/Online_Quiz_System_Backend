package com.onlinequiz.online_quiz.repository;

import com.onlinequiz.online_quiz.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findBySubjectIdAndGrade(Long subjectId, Integer grade);
    List<Enrollment> findBySubjectId(Long subjectId);
    List<Enrollment> findByStatus(String status);
    Optional<Enrollment> findByStudentIdAndSubjectIdAndGrade(Long studentId, Long subjectId, Integer grade);
}
