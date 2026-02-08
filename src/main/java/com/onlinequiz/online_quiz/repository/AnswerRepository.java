package com.onlinequiz.online_quiz.repository;

import com.onlinequiz.online_quiz.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("SELECT a FROM Answer a WHERE a.attempt.id = :attemptId ORDER BY a.question.id ASC")
    List<Answer> findByAttemptId(@Param("attemptId") Long attemptId);

    Optional<Answer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);

    List<Answer> findByAttemptIdAndMarkedForReview(Long attemptId, Boolean markedForReview);
}