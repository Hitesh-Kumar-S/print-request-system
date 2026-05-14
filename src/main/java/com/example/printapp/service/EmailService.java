package com.example.printapp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // =========================
    // SEND PRINT REQUEST EMAIL
    // =========================

    public void sendPrintRequestEmail(

            String toEmail,
            String userEmail,
            String documentName,
            int pages,
            int copies,
            double amount,
            String filePath

    ) throws MessagingException {

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(toEmail);

        helper.setSubject(
                "New Print Request Submitted");

        helper.setText(

                "A new print request has been submitted.\n\n"

                + "User Email: " + userEmail + "\n"

                + "Document: " + documentName + "\n"

                + "Pages: " + pages + "\n"

                + "Copies: " + copies + "\n"

                + "Amount: ₹" + amount + "\n"

        );

        // =========================
        // ATTACH PDF
        // =========================

        FileSystemResource file =
                new FileSystemResource(
                        new File(filePath));

        helper.addAttachment(
                file.getFilename(),
                file);

        // SEND MAIL
        mailSender.send(message);
    }
}