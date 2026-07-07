package com.example.printapp.controller;

import com.example.printapp.model.PrintRequest;
import com.example.printapp.repository.PrintRequestRepository;
import com.example.printapp.model.RequestStatus;
import com.example.printapp.model.User;
import com.example.printapp.repository.UserRepository;
import com.example.printapp.model.PaymentStatus;
import com.example.printapp.service.EmailService;

import java.security.Principal;
import java.util.List;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;

@Controller
@SessionAttributes("printRequest")
public class PrintRequestController {

    private static final double COLOR_RATE = 3.0;
    private static final double BW_RATE = 2.0;

    @Autowired
    private PrintRequestRepository printRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @ModelAttribute("printRequest")
    public PrintRequest getPrintRequest() {
        return new PrintRequest();
    }

    @GetMapping("/user/request")
public String showRequestForm(Model model) {

    if (!model.containsAttribute("printRequest")) {
        model.addAttribute("printRequest", new PrintRequest());
    }

    return "request";
}

    @GetMapping("/user/my-requests")
public String myRequests(Model model,
                         Principal principal) {

    // Logged-in user email
    String email = principal.getName();

    // Find user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // Get user's requests
    List<PrintRequest> requests =
            printRequestRepository.findByUser(user);

    // Send to UI
    model.addAttribute("requests", requests);

    return "my-requests";
}

    @GetMapping("/admin/all-requests")
public String allRequests(

        @RequestParam(defaultValue = "0")
        int page,

        Model model) {

    Pageable pageable =
            PageRequest.of(page, 10);

    Page<PrintRequest> requestPage =
            printRequestRepository.findAll(pageable);

    model.addAttribute(
            "requests",
            requestPage.getContent());

    model.addAttribute(
            "currentPage",
            page);

    model.addAttribute(
            "totalPages",
            requestPage.getTotalPages());

    return "admin-requests";
}

    @GetMapping("/admin/search")
public String searchRequests(

        @RequestParam(required = false)
        String keyword,

        Model model) {

    // Empty search
    if (keyword == null || keyword.isBlank()) {

        Pageable pageable = PageRequest.of(0, 10);

        Page<PrintRequest> requestPage =
                printRequestRepository.findAll(pageable);

        model.addAttribute(
                "requests",
                requestPage.getContent());

        model.addAttribute(
                "currentPage",
                0);

        model.addAttribute(
                "totalPages",
                requestPage.getTotalPages());

        model.addAttribute(
                "filterError",
                "Please enter an email to search.");

        return "admin-requests";
    }

    List<PrintRequest> requests =
            printRequestRepository
                    .findByUser_EmailContainingIgnoreCase(keyword);

    model.addAttribute("requests", requests);

    return "admin-requests";
}

    @GetMapping("/admin/filter")
public String filterRequests(

        @RequestParam(required = false)
        String status,

        Model model) {

    // No status selected
    if (status == null || status.isBlank()) {

        model.addAttribute(
                "filterError",
                "Please select a status filter.");

        List<PrintRequest> requests =
                printRequestRepository.findAll();

        model.addAttribute("requests", requests);

        return "admin-requests";
    }

    RequestStatus requestStatus =
            RequestStatus.valueOf(status);

    List<PrintRequest> requests =
            printRequestRepository
                    .findByStatus(requestStatus);

    model.addAttribute("requests", requests);

    return "admin-requests";
}

    @GetMapping("/admin/payment-filter")
public String filterByPayment(

        @RequestParam(required = false)
        String paymentStatus,

        Model model) {

    // No payment selected
    if (paymentStatus == null ||
        paymentStatus.isBlank()) {

        model.addAttribute(
                "filterError",
                "Please select a payment filter.");

        List<PrintRequest> requests =
                printRequestRepository.findAll();

        model.addAttribute("requests", requests);

        return "admin-requests";
    }

    PaymentStatus status =
            PaymentStatus.valueOf(paymentStatus);

    List<PrintRequest> requests =
            printRequestRepository
                    .findByPaymentStatus(status);

    model.addAttribute("requests", requests);

    return "admin-requests";
}

    @GetMapping("/admin/approve/{id}")
public String approveRequest(@PathVariable Long id) {

    PrintRequest request =
            printRequestRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Request not found"));

    request.setStatus(RequestStatus.APPROVED);

    printRequestRepository.save(request);

    // =========================
// SEND APPROVAL EMAIL
// =========================

try {

    emailService.sendApprovalEmail(

            request.getUser().getEmail(),

            request.getDocumentName()

    );

} catch (Exception e) {

    e.printStackTrace();
}

    return "redirect:/admin/pending-requests";
}

    @GetMapping("/admin/reject/{id}")
public String rejectRequest(@PathVariable Long id) {

    PrintRequest request =
            printRequestRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Request not found"));

    request.setStatus(RequestStatus.REJECTED);

    printRequestRepository.save(request);

    // =========================
// SEND REJECTION EMAIL
// =========================

try {

    emailService.sendRejectionEmail(

            request.getUser().getEmail(),

            request.getDocumentName()

    );

} catch (Exception e) {

    e.printStackTrace();
}
    
    return "redirect:/admin/pending-requests";
}

    @GetMapping("/admin/complete/{id}")
public String completeRequest(@PathVariable Long id) {

    PrintRequest request =
            printRequestRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Request not found"));

    request.setStatus(RequestStatus.COMPLETED);
    request.setCompletedAt(LocalDateTime.now());

    printRequestRepository.save(request);

    // =========================
// SEND COMPLETION EMAIL
// =========================

try {

    emailService.sendCompletionEmail(

            request.getUser().getEmail(),

            request.getDocumentName()

    );

} catch (Exception e) {

    e.printStackTrace();
}

    return "redirect:/admin/approved-requests";
}

    @GetMapping("/admin/pending-requests")
public String pendingRequests(Model model) {

    List<PrintRequest> requests =
            printRequestRepository.findByStatus(
                    RequestStatus.PENDING
            );

    model.addAttribute("requests", requests);

    // =========================
    // DASHBOARD ANALYTICS
    // =========================

    long totalRequests =
            printRequestRepository.count();

    long pendingRequests =
            printRequestRepository.countByStatus(
                    RequestStatus.PENDING);

    long approvedRequests =
            printRequestRepository.countByStatus(
                    RequestStatus.APPROVED);

    List<PrintRequest> allRequests =
            printRequestRepository.findAll();

    double totalRevenue = allRequests.stream()
            .filter(r ->
                    r.getPaymentStatus() ==
                            PaymentStatus.PAID)
            .mapToDouble(PrintRequest::getAmount)
            .sum();

    // Send analytics to UI
    model.addAttribute(
            "totalRequests",
            totalRequests);

    model.addAttribute(
            "pendingRequests",
            pendingRequests);

    model.addAttribute(
            "approvedRequests",
            approvedRequests);

    model.addAttribute(
            "totalRevenue",
            totalRevenue);

    return "pending-requests";
}

    @GetMapping("/admin/pay/{id}")
public String markAsPaid(@PathVariable Long id) {

    PrintRequest request =
            printRequestRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Request not found"));

    request.setPaymentStatus(PaymentStatus.PAID);

    printRequestRepository.save(request);

    // =========================
// SEND PAYMENT EMAIL
// =========================

try {

    emailService.sendPaymentEmail(

            request.getUser().getEmail(),

            request.getDocumentName(),

            request.getAmount()

    );

} catch (Exception e) {

    e.printStackTrace();
}

    return "redirect:/admin/payment-requests";
}

    @GetMapping("/admin/approved-requests")
public String approvedRequests(Model model) {

    List<PrintRequest> requests =
            printRequestRepository.findByStatus(
                    RequestStatus.APPROVED
            );

    model.addAttribute("requests", requests);

    return "approved-requests";
}

    @GetMapping("/admin/payment-requests")
public String paymentRequests(Model model) {

    List<PrintRequest> requests =
            printRequestRepository
                    .findByStatusAndPaymentStatus(
                            RequestStatus.COMPLETED,
                            PaymentStatus.UNPAID
                    );

    model.addAttribute("requests", requests);

    return "payment-requests";
}

    // 🔹 STEP 1: SUBMIT → TEMP SAVE
    @PostMapping("/user/submitRequest")
    public String submitRequest(
        @Valid @ModelAttribute("printRequest") PrintRequest printRequest,
        BindingResult bindingResult,
        @RequestParam("file") MultipartFile file,
        Model model,
        HttpSession session) {

        if (bindingResult.hasErrors()) {

    model.addAttribute(
            "error",
            bindingResult.getAllErrors()
                    .get(0)
                    .getDefaultMessage()
    );

    return "request";
}

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
        if (file.getSize() > 20 * 1024 * 1024) {
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

            String tempFilePath = tempDir + System.currentTimeMillis() + ".pdf";

            file.transferTo(new File(tempFilePath));

            // printRequest.setFileName(fileName);

            session.setAttribute("tempFilePath", tempFilePath);

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
    @PostMapping("/user/confirmRequest")
public String confirmRequest(
        @ModelAttribute("printRequest") PrintRequest printRequest,
        Model model,
        SessionStatus sessionStatus,
        Principal principal,
        HttpSession session) {

    // =========================
    // Attach Logged-in User
    // =========================

    String email = principal.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    printRequest.setUser(user);

    // =========================
    // Default Status
    // =========================

    printRequest.setStatus(RequestStatus.PENDING);

    printRequest.setPaymentStatus(PaymentStatus.UNPAID);

    // Save request
    PrintRequest savedRequest =
            printRequestRepository.save(printRequest);

    String tempFilePath =
        (String) session.getAttribute("tempFilePath");

    // System.out.println("Temp file path in confirmRequest: " + tempFilePath);

    // =========================
    // SEND EMAIL TO ADMIN
    // =========================

    try {

        emailService.sendPrintRequestEmail(

                "printapp.service@gmail.com",

                savedRequest.getUser().getEmail(),

                savedRequest.getDocumentName(),

                savedRequest.getSided(),

                savedRequest.getPages(),

                savedRequest.getCopies(),

                savedRequest.getAmount(),

                tempFilePath

        );

    } catch (Exception e) {

        e.printStackTrace();
    }

    session.removeAttribute("tempFilePath");

    model.addAttribute("printRequest", savedRequest);

    // Clear session
    sessionStatus.setComplete();

    return "confirmation";
}

    private double calculateAmount(PrintRequest printRequest) {

    if ("color".equalsIgnoreCase(printRequest.getColor())) {

        return COLOR_RATE
                * printRequest.getPages()
                * printRequest.getCopies();

    } else {

        return BW_RATE
                * printRequest.getPages()
                * printRequest.getCopies();
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