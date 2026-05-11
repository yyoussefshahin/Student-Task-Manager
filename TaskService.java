package com.project.TaskManger.Services;

import com.project.TaskManger.Model.Task;
import com.project.TaskManger.Model.TaskLevel;
import com.project.TaskManger.Model.TaskStatus;
import com.project.TaskManger.Model.User;
import com.project.TaskManger.Repositories.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

   

    public Task createTask(String email, String title,
                           String description, LocalDate deadline , TaskLevel level) {
        User student = userService.findByEmail(email);

        Task task = Task.builder()
                .title(title)
                .description(description)
                .deadline(deadline)
                .level(level)
                .status(TaskStatus.PENDING)    // ← default status
                .student(student)
                .build();

        return taskRepository.save(task);
    }

    

    public List<Task> getTasksByStudentEmail(String email) {
        User student = userService.findByEmail(email);
        return taskRepository.findByStudentId(student.getId());
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    public long countByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

    @Transactional
    public Task changeStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    
    public Task completeTask(Long taskId) {
        return changeStatus(taskId, TaskStatus.COMPLETED);
    }

    public Task startTask(Long taskId) {
        return changeStatus(taskId, TaskStatus.IN_PROGRESS);
    }

    public Task cancelTask(Long taskId) {
        return changeStatus(taskId, TaskStatus.CANCELLED);
    }

    
    @Transactional
    public Task updateTask(Long taskId, String title,
                           String description, LocalDate deadline , TaskLevel level) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));
        task.setTitle(title);
        task.setDescription(description);
        task.setLevel(level);
        task.setDeadline(deadline);
        return taskRepository.save(task);
    }

    
    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public void markOverdueTasks() {
        List<Task> activeTasks = taskRepository.findByStatus(TaskStatus.PENDING);

        LocalDate today = LocalDate.now();
        for (Task task : activeTasks) {
            if (task.getDeadline() != null && task.getDeadline().isBefore(today)) {
                task.setStatus(TaskStatus.OVERDUE);
                taskRepository.save(task);
            }
        }
    }
}

