package com.project.TaskManger.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity //rep. database table
@Table(name = "feedbacks") //table name
@Data @NoArgsConstructor @AllArgsConstructor @Builder //1-empty constructor automatically 2-constructor with all fields
@ToString(exclude = "student") //avoids recursion
@EqualsAndHashCode(exclude = "student") //avoids recursion
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String adminReply;

    @ManyToOne(fetch = FetchType.EAGER) // load feedback , load student immediately
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime repliedAt;

    @PrePersist //before saving to DB
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}