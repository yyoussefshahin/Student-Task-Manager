package com.project.TaskManger.Repositories;


import com.project.TaskManger.Model.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> { //extends? free DB methods
    List<FeedBack> findByStudentId(Long studentId);
}