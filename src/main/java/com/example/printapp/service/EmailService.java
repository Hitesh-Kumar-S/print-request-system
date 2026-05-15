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

    // =========================
// APPROVAL NOTIFICATION
// =========================

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