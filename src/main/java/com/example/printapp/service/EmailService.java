package com.example.printapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // =========================
    // SEND PRINT REQUEST EMAIL
    // =========================

@Async("emailTaskExecutor")
public void sendPrintRequestEmail(
        String toEmail,
        String userEmail,
        String documentName,
        String color,
        String sided,
        int pages,
        int copies,
        double amount,
        String filePath
) {
    File tempFile = null;

    if (filePath != null && !filePath.isBlank()) {
        tempFile = new File(filePath);
    }

    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("New Print Request Submitted");

        String colorDisplay =
                "bw".equalsIgnoreCase(color) ? "B&W" : "Color";

        String sidedDisplay =
                "double".equalsIgnoreCase(sided) ? "Front & Back" : "One Side";

        helper.setText(
                "A new print request has been submitted.\n\n"
                        + "User Email: " + userEmail + "\n"
                        + "Document: " + documentName + "\n"
                        + "Color: " + colorDisplay + "\n"
                        + "Print Type: " + sidedDisplay + "\n"
                        + "Pages: " + pages + "\n"
                        + "Copies: " + copies + "\n"
                        + "Amount: ₹" + amount + "\n"
        );

        if (tempFile != null && tempFile.exists()) {
            FileSystemResource file = new FileSystemResource(tempFile);
            helper.addAttachment(file.getFilename(), file);
        }

        mailSender.send(message);

    } catch (Exception e) {
        System.err.println("Failed to send print request email:");
        e.printStackTrace();

    } finally {
        if (tempFile != null && tempFile.exists()) {
            if (tempFile.delete()) {
                System.out.println("Temporary file deleted successfully.");
            } else {
                System.err.println("Failed to delete temporary file: "
                        + tempFile.getAbsolutePath());
            }
        } else {
            System.out.println("No temporary file found for deletion.");
        }
    }
}

// =========================
// APPROVAL NOTIFICATION
// =========================
@Async("emailTaskExecutor")
public void sendApprovalEmail(

        String toEmail,
        String documentName

) throws MessagingException {

    MimeMessage message =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(message, true);

    helper.setTo(toEmail);

    helper.setSubject(
            "Print Request Approved");

    helper.setText(

            "Your print request has been approved.\n\n"

            + "Document: " + documentName + "\n\n"

            + "You will be notified once printing is completed."

    );

    mailSender.send(message);
}

        // =========================
// REJECTION NOTIFICATION
// =========================
@Async("emailTaskExecutor")
public void sendRejectionEmail(

        String toEmail,
        String documentName

) throws MessagingException {

    MimeMessage message =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(message, true);

    helper.setTo(toEmail);

    helper.setSubject(
            "Print Request Rejected");

    helper.setText(

            "We’re sorry, but your print request could not be processed at this time.\n\n"

            + "Document: " + documentName + "\n\n"

            + "Please contact admin for more details."

    );

    mailSender.send(message);
}

        // =========================
// COMPLETION NOTIFICATION
// =========================
@Async("emailTaskExecutor")
public void sendCompletionEmail(

        String toEmail,
        String documentName

) throws MessagingException {

    MimeMessage message =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(message, true);

    helper.setTo(toEmail);

    helper.setSubject(
            "Print Request Completed");

    helper.setText(

            "Your print request has been completed and is ready for collection.\n\n"

            + "Document: " + documentName + "\n\n"

            + "Please make sure to collect your printed document."

    );

    mailSender.send(message);
}

// =========================
// PAYMENT NOTIFICATION
// =========================
@Async("emailTaskExecutor")
public void sendPaymentEmail(

        String toEmail,
        String documentName,
        double amount

) throws MessagingException {

    MimeMessage message =
            mailSender.createMimeMessage();

    MimeMessageHelper helper =
            new MimeMessageHelper(message, true);

    helper.setTo(toEmail);

    helper.setSubject(
            "Payment Confirmed");

    helper.setText(

            "Your payment has been confirmed.\n\n"

            + "Document: " + documentName + "\n"

            + "Amount Paid: ₹" + amount + "\n\n"

            + "Thank you for using PrintApp."

    );

    mailSender.send(message);
}
}