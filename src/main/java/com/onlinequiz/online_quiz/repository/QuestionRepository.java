package com.onlinequiz.online_quiz.repository;

import com.onlinequiz.online_quiz.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDifficulty(String difficulty);
    List<Question> findByDifficultyOrderByCreatedAtDesc(String difficulty);
}