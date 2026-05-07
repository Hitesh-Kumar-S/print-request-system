package com.example.printapp.controller;

import com.example.printapp.model.Role;
import com.example.printapp.model.User;
import com.example.printapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // =========================
    // Signup API
    // =========================

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {

        // Check existing email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered";
        }

        // Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role
        user.setRole(Role.USER);

        // Save user
        userRepository.save(user);

        return "User registered successfully";
    }
}