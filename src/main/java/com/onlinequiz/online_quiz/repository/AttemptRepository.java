package com.onlinequiz.online_quiz.repository;

import com.onlinequiz.online_quiz.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findByAssignmentId(Long assignmentId);
    Optional<Attempt> findByIdAndStatus(Long id, String status);
    List<Attempt> findByStatus(String status);
}