package com.example.printapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // =========================
    // Public Pages
    // =========================

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // =========================
    // USER Dashboard
    // =========================

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "user-dashboard";
    }

    // =========================
    // ADMIN Dashboard
    // =========================

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }
}