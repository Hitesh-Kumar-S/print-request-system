package com.example.printapp.repository;

import com.example.printapp.model.PrintRequest;
import com.example.printapp.model.User;
import com.example.printapp.model.RequestStatus;
import com.example.printapp.model.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrintRequestRepository
        extends JpaRepository<PrintRequest, Long> {

    // Get all requests of a user
    List<PrintRequest> findByUser(User user);

    List<PrintRequest> findByStatus(RequestStatus status);

    List<PrintRequest> findByStatusAndPaymentStatus(
            RequestStatus status,
            PaymentStatus paymentStatus
    );

    List<PrintRequest> findByUser_EmailContainingIgnoreCase(
            String email
    );

    List<PrintRequest> findByPaymentStatus(
        PaymentStatus paymentStatus
);
}