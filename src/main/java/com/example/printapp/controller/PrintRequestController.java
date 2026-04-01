package com.example.printapp.controller;

import com.example.printapp.model.PrintRequest;
import com.example.printapp.repository.PrintRequestRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes("printRequest")
public class PrintRequestController {

    private static final double COLOR_RATE = 3.0;
    private static final double BW_RATE = 2.0;

    @Autowired
    private PrintRequestRepository printRequestRepository;

    // 🔄 Initialize session object
    @ModelAttribute("printRequest")
    public PrintRequest getPrintRequest() {
        return new PrintRequest();
    }

    // 🧾 Show request form
    @GetMapping("/request")
    public String showRequestForm() {
        return "request";
    }

    // 📄 Handle form submission → show summary
    @PostMapping("/submitRequest")
    public String submitRequest(@ModelAttribute("printRequest") PrintRequest printRequest, Model model) {

        sanitizeInput(printRequest);

        if (!isValidPrintRequest(printRequest)) {
            model.addAttribute("error", "Invalid Print Request. Please fill all fields correctly.");
            return "request";
        }

        double amount = calculateAmount(printRequest);
        printRequest.setAmount(amount);

        model.addAttribute("printRequest", printRequest);

        return "submit";
    }

    // ✅ Confirm & save request
    @PostMapping("/confirmRequest")
    public String confirmRequest(@ModelAttribute("printRequest") PrintRequest printRequest,
                                 Model model,
                                 SessionStatus sessionStatus) {

        printRequestRepository.save(printRequest);

        // 🧹 Clear session
        sessionStatus.setComplete();

        model.addAttribute("message",
                "Your print request has been successfully submitted! Please pay at collection.");

        return "confirmation";
    }

    // 💰 Calculate amount
    private double calculateAmount(PrintRequest printRequest) {
        if ("color".equalsIgnoreCase(printRequest.getColor())) {
            return COLOR_RATE * printRequest.getPages();
        } else {
            return BW_RATE * printRequest.getPages();
        }
    }

    // ✅ Validation
    private boolean isValidPrintRequest(PrintRequest printRequest) {
        return printRequest.getName() != null && !printRequest.getName().isEmpty()
            && printRequest.getDocumentName() != null && !printRequest.getDocumentName().isEmpty()
            && printRequest.getColor() != null && !printRequest.getColor().isEmpty()
            && printRequest.getSided() != null && !printRequest.getSided().isEmpty()
            && printRequest.getPages() > 0;
    }

    // ✂️ Sanitize input
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