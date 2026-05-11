package com.project.TaskManger.Controller;

import com.project.TaskManger.Model.FeedBack;
import com.project.TaskManger.Services.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedbackService;


    // STUDENT


    @GetMapping("/student/feedback") //we use get here Because this page only displays feedbacks
    //No database modification happens
    public String studentFeedbackPage(
            @AuthenticationPrincipal UserDetails userDetails, //inject only auth logged in users
            Model model) {

        List<FeedBack> feedbacks = feedbackService
                .getFeedbacksByStudentEmail(userDetails.getUsername());
        model.addAttribute("feedbacks", feedbacks); // transfers data from BE to FE
        return "student/feedback";
    }

    @PostMapping("/student/feedback/send")
    public String sendFeedback(
            @AuthenticationPrincipal UserDetails userDetails, //get only logged in users
            @RequestParam String message) {  // get form data

        feedbackService.sendFeedback(userDetails.getUsername(), message); //save feedback
        return "redirect:/student/feedback"; //ensure no duplicates in sub.
    }


    //ADMIN


    @GetMapping("/admin/feedbacks")  //we use get here Because this page only displays feedbacks
    //No database modification happens
    public String adminFeedbacksPage(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "admin/feedbacks";
    }

    @PostMapping("/admin/feedbacks/{id}/reply")
    public String replyToFeedback(
            @PathVariable Long id, // dynamic value in url
            @RequestParam String reply) {

        feedbackService.replyToFeedback(id, reply);
        return "redirect:/admin/feedbacks";
    }
}