package com.project.TaskManger.Model;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = "student")
@EqualsAndHashCode(exclude = "student")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate deadline;

   
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TaskStatus status = TaskStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private TaskLevel level = TaskLevel.MEDIUM;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    public boolean isOverdue() {
        return !isCompleted()
                && deadline != null
                && deadline.isBefore(LocalDate.now());
    }

    public boolean isPending() {
        return this.status == TaskStatus.PENDING;
    }
}
