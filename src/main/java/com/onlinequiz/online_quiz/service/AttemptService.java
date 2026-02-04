package com.onlinequiz.online_quiz.service;

import com.onlinequiz.online_quiz.dto.AnswerDTO;
import com.onlinequiz.online_quiz.dto.AttemptDTO;
import com.onlinequiz.online_quiz.dto.SubmitAnswerDTO;
import com.onlinequiz.online_quiz.entity.Answer;
import com.onlinequiz.online_quiz.entity.Assignment;
import com.onlinequiz.online_quiz.entity.Attempt;
import com.onlinequiz.online_quiz.entity.Question;
import com.onlinequiz.online_quiz.repository.AnswerRepository;
import com.onlinequiz.online_quiz.repository.AssignmentRepository;
import com.onlinequiz.online_quiz.repository.AttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttemptService {

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AssignmentService assignmentService;

    // Start a new attempt
    @Transactional
    public AttemptDTO startAttempt(Long assignmentId) {
        // Check if assignment exists
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found with id: " + assignmentId));

        // Check if assignment is available
        if (!assignmentService.isAssignmentAvailable(assignmentId)) {
            throw new RuntimeException("This assignment is not available at this time.");
        }

        // Create new attempt
        Attempt attempt = new Attempt();
        attempt.setAssignment(assignment);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setStatus("IN_PROGRESS");

        // Calculate total points
        int totalPoints = assignment.getQuestions().stream()
                .mapToInt(Question::getPoints)
                .sum();
        attempt.setTotalPoints(totalPoints);

        Attempt savedAttempt = attemptRepository.save(attempt);

        // Create answer records for all questions
        for (Question question : assignment.getQuestions()) {
            Answer answer = new Answer();
            answer.setAttempt(savedAttempt);
            answer.setQuestion(question);
            answer.setMarkedForReview(false);
            answerRepository.save(answer);
        }

        return convertToDTO(savedAttempt);
    }

    // Get attempt by ID
    public AttemptDTO getAttemptById(Long id) {
        Attempt attempt = attemptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attempt not found with id: " + id));
        return convertToDTO(attempt);
    }

    // Get all attempts for an assignment
    public List<AttemptDTO> getAttemptsByAssignment(Long assignmentId) {
        return attemptRepository.findByAssignmentId(assignmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Submit or update an answer
    @Transactional
    public AnswerDTO submitAnswer(Long attemptId, SubmitAnswerDTO submitDTO) {
        // Verify attempt exists and is in progress
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found with id: " + attemptId));

        if (!"IN_PROGRESS".equals(attempt.getStatus())) {
            throw new RuntimeException("Cannot submit answer. Attempt is already completed.");
        }

        // Check if time is still valid
        LocalDateTime now = LocalDateTime.now();
        long elapsedMinutes = Duration.between(attempt.getStartedAt(), now).toMinutes();
        if (elapsedMinutes > attempt.getAssignment().getDuration()) {
            // Auto-submit if time expired
            submitAttempt(attemptId, true);
            throw new RuntimeException("Time has expired. Assignment has been auto-submitted.");
        }

        // Find or create answer
        Answer answer = answerRepository.findByAttemptIdAndQuestionId(attemptId, submitDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Answer record not found"));

        answer.setSelectedAnswer(submitDTO.getSelectedAnswer());
        answer.setMarkedForReview(submitDTO.getMarkedForReview());

        Answer savedAnswer = answerRepository.save(answer);
        return convertAnswerToDTO(savedAnswer);
    }

    // Submit the entire attempt
    @Transactional
    public AttemptDTO submitAttempt(Long attemptId, boolean isAutoSubmit) {
        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found with id: " + attemptId));

        if (!"IN_PROGRESS".equals(attempt.getStatus())) {
            throw new RuntimeException("Attempt is already submitted.");
        }

        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setStatus(isAutoSubmit ? "AUTO_SUBMITTED" : "SUBMITTED");

        // Calculate score
        int score = calculateScore(attemptId);
        attempt.setScore(score);

        Attempt savedAttempt = attemptRepository.save(attempt);
        return convertToDTO(savedAttempt);
    }

    // Calculate score for an attempt
    private int calculateScore(Long attemptId) {
        List<Answer> answers = answerRepository.findByAttemptId(attemptId);
        int score = 0;

        for (Answer answer : answers) {
            if (answer.getSelectedAnswer() != null) {
                Question question = answer.getQuestion();
                boolean isCorrect = answer.getSelectedAnswer().equals(question.getCorrectOption());
                answer.setIsCorrect(isCorrect);
                answerRepository.save(answer);

                if (isCorrect) {
                    score += question.getPoints();
                }
            } else {
                answer.setIsCorrect(false);
                answerRepository.save(answer);
            }
        }

        return score;
    }

    // Get answers for an attempt
    public List<AnswerDTO> getAnswersByAttempt(Long attemptId) {
        return answerRepository.findByAttemptId(attemptId).stream()
                .map(this::convertAnswerToDTO)
                .collect(Collectors.toList());
    }

    // Convert Attempt Entity to DTO
    private AttemptDTO convertToDTO(Attempt attempt) {
        AttemptDTO dto = new AttemptDTO();
        dto.setId(attempt.getId());
        dto.setAssignmentId(attempt.getAssignment().getId());
        dto.setAssignmentName(attempt.getAssignment().getName());
        dto.setStartedAt(attempt.getStartedAt());
        dto.setSubmittedAt(attempt.getSubmittedAt());
        dto.setStatus(attempt.getStatus());
        dto.setScore(attempt.getScore());
        dto.setTotalPoints(attempt.getTotalPoints());

        // Calculate remaining time in seconds
        if ("IN_PROGRESS".equals(attempt.getStatus())) {
            LocalDateTime now = LocalDateTime.now();
            long elapsedSeconds = Duration.between(attempt.getStartedAt(), now).getSeconds();
            long totalSeconds = attempt.getAssignment().getDuration() * 60L;
            long remainingSeconds = Math.max(0, totalSeconds - elapsedSeconds);
            dto.setRemainingTimeSeconds((int) remainingSeconds);
        }

        // Include answers
        List<AnswerDTO> answerDTOs = answerRepository.findByAttemptId(attempt.getId()).stream()
                .map(this::convertAnswerToDTO)
                .collect(Collectors.toList());
        dto.setAnswers(answerDTOs);

        return dto;
    }

    // Convert Answer Entity to DTO
    private AnswerDTO convertAnswerToDTO(Answer answer) {
        AnswerDTO dto = new AnswerDTO();
        dto.setId(answer.getId());
        dto.setQuestionId(answer.getQuestion().getId());
        dto.setSelectedAnswer(answer.getSelectedAnswer());
        dto.setMarkedForReview(answer.getMarkedForReview());
        dto.setIsCorrect(answer.getIsCorrect());
        return dto;
    }
}