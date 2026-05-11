package com.project.TaskManger.Controller;

import com.project.TaskManger.Model.User;
import com.project.TaskManger.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/landing";
    }

    @GetMapping("/landing")
    public String landing() {
        return "landing";
    }


    @GetMapping("/redirect")
    public String loginRedirect(@AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        if (role.equals("ROLE_ADMIN")) return "redirect:/admin/dashboard";
        return "redirect:/student/dashboard";
    }



    @GetMapping("/student/login")
    public String studentLogin(@RequestParam(required = false) String error, Model model) {
        if ("true".equals(error)) model.addAttribute("errorMsg", "Invalid email or password");
        if ("role".equals(error)) model.addAttribute("errorMsg", "⚠️ You are an Admin! Please use the Admin login page.");
        model.addAttribute("role", "STUDENT");
        return "login";
    }

    @GetMapping("/student/register")
    public String studentRegister(Model model) {
        model.addAttribute("role", "STUDENT");
        return "register";
    }


    @GetMapping("/admin/login")
    public String adminLogin(@RequestParam(required = false) String error, Model model) {
        if ("true".equals(error)) model.addAttribute("errorMsg", "Invalid email or password");
        if ("role".equals(error)) model.addAttribute("errorMsg", "⚠️ You are a Student! Please use the Student login page.");
        model.addAttribute("role", "ADMIN");
        return "login";
    }

    @GetMapping("/admin/register")
    public String adminRegister(Model model) {
        model.addAttribute("role", "ADMIN");
        return "register";
    }


    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role,
                           Model model) {
        try {
            userService.registerUser(name, email, password, User.Role.valueOf(role));
        } catch (RuntimeException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("role", role);
            return "register";
        }


        if ("ADMIN".equals(role)) {
            return "redirect:/admin/login";
        }
        return "redirect:/student/login";
    }
}
