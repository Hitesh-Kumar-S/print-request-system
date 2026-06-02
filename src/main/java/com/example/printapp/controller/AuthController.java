package com.example.printapp.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import com.example.printapp.model.Role;
import com.example.printapp.model.User;
import com.example.printapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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
public ResponseEntity<String> signup(
        @Valid @RequestBody User user,
        BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {

        return ResponseEntity.badRequest()
                .body(bindingResult
                        .getAllErrors()
                        .get(0)
                        .getDefaultMessage());
    }

    if (userRepository.findByEmail(user.getEmail()).isPresent()) {

        return ResponseEntity.badRequest()
                .body("Email already registered");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    user.setRole(Role.USER);

    userRepository.save(user);

    return ResponseEntity.ok("User registered successfully");
}
}