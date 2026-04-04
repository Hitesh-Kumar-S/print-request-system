package com.example.printapp.controller;

import com.example.printapp.model.PrintRequest;
import com.example.printapp.repository.PrintRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

@Controller
@SessionAttributes("printRequest")
public class PrintRequestController {

    private static final double COLOR_RATE = 3.0;
    private static final double BW_RATE = 2.0;

    @Autowired
    private PrintRequestRepository printRequestRepository;

    @ModelAttribute("printRequest")
    public PrintRequest getPrintRequest() {
        return new PrintRequest();
    }

    @GetMapping("/request")
    public String showRequestForm() {
        return "request";
    }

    // 🔹 STEP 1: SUBMIT → TEMP SAVE
    @PostMapping("/submitRequest")
    public String submitRequest(
            @ModelAttribute("printRequest") PrintRequest printRequest,
            @RequestParam("file") MultipartFile file,
            Model model) {

        // ❌ Empty check
        if (file.isEmpty()) {
            model.addAttribute("error", "Please upload a PDF file.");
            return "request";
        }

        String originalFileName = file.getOriginalFilename();

        // ❌ PDF validation
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
            model.addAttribute("error", "Only PDF files are allowed.");
            return "request";
        }

        // ❌ Size validation (5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            model.addAttribute("error", "File size must be less than 5MB.");
            return "request";
        }

        sanitizeInput(printRequest);

        if (!isValidPrintRequest(printRequest)) {
            model.addAttribute("error", "Invalid Print Request.");
            return "request";
        }

        // 💾 TEMP SAVE
        try {
            String tempDir = System.getProperty("user.dir") + "/uploads/temp/";
            File dir = new File(tempDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String cleanName = printRequest.getName().replaceAll("\\s+", "_");
            cleanName = cleanName.replaceAll("[^a-zA-Z0-9_]", "");

            String cleanFileName = originalFileName.replaceAll("\\s+", "_");

            String fileName = cleanName + "_" + System.currentTimeMillis() + "_" + cleanFileName;

            String tempFilePath = tempDir + fileName;

            file.transferTo(new File(tempFilePath));

            printRequest.setFileName(fileName);
            printRequest.setFilePath(tempFilePath);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "File upload failed.");
            return "request";
        }

        double amount = calculateAmount(printRequest);
        printRequest.setAmount(amount);

        model.addAttribute("printRequest", printRequest);

        return "submit";
    }

    // 🔹 STEP 2: CONFIRM → FINAL SAVE
    @PostMapping("/confirmRequest")
    public String confirmRequest(@ModelAttribute("printRequest") PrintRequest printRequest,
                                 Model model,
                                 SessionStatus sessionStatus) {

        try {
            String finalDir = System.getProperty("user.dir") + "/uploads/";
            File dir = new File(finalDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File tempFile = new File(printRequest.getFilePath());

            String finalPath = finalDir + printRequest.getFileName();
            File finalFile = new File(finalPath);

            // 🔥 Move file
            tempFile.renameTo(finalFile);

            printRequest.setFilePath(finalPath);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error saving file.");
            return "request";
        }

        PrintRequest savedRequest = printRequestRepository.save(printRequest);

        model.addAttribute("printRequest", savedRequest);

        sessionStatus.setComplete();

        return "confirmation";
    }

    // 🔹 VIEW FILE
    @GetMapping("/viewFile/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {

        PrintRequest request = printRequestRepository.findById(id).orElse(null);

        if (request == null || request.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(request.getFilePath());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(resource);
    }

    // 🔹 DOWNLOAD FILE
    @GetMapping("/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {

        PrintRequest request = printRequestRepository.findById(id).orElse(null);

        if (request == null || request.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(request.getFilePath());

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + request.getFileName() + "\"")
                .body(resource);
    }

    private double calculateAmount(PrintRequest printRequest) {
        if ("color".equalsIgnoreCase(printRequest.getColor())) {
            return COLOR_RATE * printRequest.getPages();
        } else {
            return BW_RATE * printRequest.getPages();
        }
    }

    private boolean isValidPrintRequest(PrintRequest printRequest) {
        return printRequest.getName() != null && !printRequest.getName().isEmpty()
            && printRequest.getDocumentName() != null && !printRequest.getDocumentName().isEmpty()
            && printRequest.getColor() != null && !printRequest.getColor().isEmpty()
            && printRequest.getSided() != null && !printRequest.getSided().isEmpty()
            && printRequest.getPages() > 0;
    }

    private void sanitizeInput(PrintRequest printRequest) {
        if (printRequest.getName() != null)
            printRequest.setName(printRequest.getName().trim());

        if (printRequest.getDocumentName() != null)
            printRequest.setDocumentName(printRequest.getDocumentName().trim());

        if (printRequest.getColor() != null)
            printRequest.setColor(printRequest.getColor().trim());

        if (printRequest.getSided() != null)
            printRequest.setSided(printRequest.getSided().trim());
    }
}