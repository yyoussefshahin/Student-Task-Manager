package com.project.TaskManger.Controller;

import com.project.TaskManger.Model.Task;
import com.project.TaskManger.Model.TaskStatus;
import com.project.TaskManger.Model.User;
import com.project.TaskManger.Services.TaskService;
import com.project.TaskManger.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        taskService.markOverdueTasks();  // update before displaying

        List<User> students = userService.getAllStudents();
        List<Task> allTasks = taskService.getAllTasks();

        model.addAttribute("students", students);
        model.addAttribute("tasks", allTasks);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("totalTasks", allTasks.size());
        model.addAttribute("pendingCount", taskService.countByStatus(TaskStatus.PENDING));
        model.addAttribute("inProgressCount", taskService.countByStatus(TaskStatus.IN_PROGRESS));
        model.addAttribute("completedCount", taskService.countByStatus(TaskStatus.COMPLETED));
        model.addAttribute("overdueCount", taskService.countByStatus(TaskStatus.OVERDUE));
        model.addAttribute("cancelledCount", taskService.countByStatus(TaskStatus.CANCELLED));
        return "admin/dashboard";
    }
}


