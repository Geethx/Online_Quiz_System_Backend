package com.onlinequiz.online_quiz.repository;

import com.onlinequiz.online_quiz.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByAttemptId(Long attemptId);
    Optional<Answer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);
    List<Answer> findByAttemptIdAndMarkedForReview(Long attemptId, Boolean markedForReview);
}