package com.example.printapp.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

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
    public String signup(@Valid @RequestBody User user, 
                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

    return bindingResult
            .getAllErrors()
            .get(0)
            .getDefaultMessage();
}

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