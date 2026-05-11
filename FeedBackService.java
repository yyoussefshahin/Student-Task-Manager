package com.project.TaskManger.Services;

import com.project.TaskManger.Model.FeedBack;
import com.project.TaskManger.Model.User;
import com.project.TaskManger.Repositories.FeedBackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackService {

    private final FeedBackRepository feedbackRepository; // talks to DB
    private final UserService userService; // gets user data

    public FeedBack sendFeedback(String studentEmail, String message) {
        User student = userService.findByEmail(studentEmail); //Find the student from database using email
        FeedBack feedback = FeedBack.builder() //build feedback obj.
                .message(message)
                .student(student)
                .build();
        return feedbackRepository.save(feedback);
    }

    public List<FeedBack> getFeedbacksByStudentEmail(String email) { //feedbacks for one student
        User student = userService.findByEmail(email); //find just like line 21
        return feedbackRepository.findByStudentId(student.getId()); //returns feedbacks by ID
    }

    public List<FeedBack> getAllFeedbacks() { //feedbacks for all student
        return feedbackRepository.findAll(); //returns all feedbacks
    }
    @Transactional
    public FeedBack replyToFeedback(Long feedbackId, String reply) {
        FeedBack feedback = feedbackRepository.findById(feedbackId)
        throw new RuntimeException("Feedback not found")
        feedback.setAdminReply(reply); //set reply
        feedback.setRepliedAt(LocalDateTime.now()); //set time
        return feedbackRepository.save(feedback); //save update(reply)
    }
}