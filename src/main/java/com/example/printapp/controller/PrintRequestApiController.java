package com.example.printapp.controller;

import com.example.printapp.model.PrintRequest;
import com.example.printapp.repository.PrintRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class PrintRequestApiController {

    @Autowired
    private PrintRequestRepository repository;

    // ✅ GET all requests (JWT protected)
    @GetMapping
    public List<PrintRequest> getAllRequests() {
        return repository.findAll();
    }

    // ✅ POST request
    @PostMapping
    public PrintRequest createRequest(@RequestBody PrintRequest request) {
        return repository.save(request);
    }
}
