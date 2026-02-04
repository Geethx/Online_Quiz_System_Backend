package com.onlinequiz.online_quiz.repository;

import com.onlinequiz.online_quiz.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    
    @Query("SELECT a FROM Assignment a WHERE a.startTime <= :currentTime AND a.endTime >= :currentTime")
    List<Assignment> findAvailableAssignments(@Param("currentTime") LocalDateTime currentTime);
    
    List<Assignment> findAllByOrderByCreatedAtDesc();
}