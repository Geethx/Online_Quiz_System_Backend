package com.onlinequiz.online_quiz.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "option_a", nullable = false)
    private String optionA;

    @Column(name = "option_b", nullable = false)
    private String optionB;

    @Column(name = "option_c", nullable = false)
    private String optionC;

    @Column(name = "option_d", nullable = false)
    private String optionD;

    @Column(name = "correct_option", nullable = false)
    private Integer correctOption; // 1, 2, 3, or 4

    @Column(nullable = false, length = 50)
    private String difficulty; // EASY, MEDIUM, HARD

    @Column(nullable = false)
    private Integer points;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "questions")
    @JsonIgnore
    private Set<Assignment> assignments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column(name = "question_image_url", columnDefinition = "TEXT")
    private String questionImageUrl;

    @Column(name = "option_a_image_url", columnDefinition = "TEXT")
    private String optionAImageUrl;

    @Column(name = "option_b_image_url", columnDefinition = "TEXT")
    private String optionBImageUrl;

    @Column(name = "option_c_image_url", columnDefinition = "TEXT")
    private String optionCImageUrl;

    @Column(name = "option_d_image_url", columnDefinition = "TEXT")
    private String optionDImageUrl;

    @Column(nullable = false)
    private Integer grade; // 6, 7, 8, 9, 10, 11

    // Constructors
    public Question() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public Integer getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(Integer correctOption) {
        this.correctOption = correctOption;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public void setQuestionImageUrl(String questionImageUrl) {
        this.questionImageUrl = questionImageUrl;
    }

    public String getOptionAImageUrl() {
        return optionAImageUrl;
    }

    public void setOptionAImageUrl(String optionAImageUrl) {
        this.optionAImageUrl = optionAImageUrl;
    }

    public String getOptionBImageUrl() {
        return optionBImageUrl;
    }

    public void setOptionBImageUrl(String optionBImageUrl) {
        this.optionBImageUrl = optionBImageUrl;
    }

    public String getOptionCImageUrl() {
        return optionCImageUrl;
    }

    public void setOptionCImageUrl(String optionCImageUrl) {
        this.optionCImageUrl = optionCImageUrl;
    }

    public String getOptionDImageUrl() {
        return optionDImageUrl;
    }

    public void setOptionDImageUrl(String optionDImageUrl) {
        this.optionDImageUrl = optionDImageUrl;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}