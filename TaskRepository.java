package com.project.TaskManger.Repositories;
import com.project.TaskManger.Model.Task;
import com.project.TaskManger.Model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStudentId(Long studentId);
    List<Task> findByStudentIdAndStatus(Long studentId, TaskStatus status);
    List<Task> findByStatus(TaskStatus status);
    long countByStatus(TaskStatus status);
}
