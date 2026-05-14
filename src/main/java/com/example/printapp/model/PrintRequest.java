package com.example.printapp.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;

@Entity
public class PrintRequest {

    // =========================
    // Primary Key
    // =========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // User Relationship
    // =========================

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // =========================
    // Request Details
    // =========================

    @NotBlank(message = "Name is required")
@Size(min = 2, max = 100,
       message = "Name must be between 2 and 100 characters")
private String name;

    @NotBlank(message = "Document name is required")
    private String documentName;

    @Min(value = 1,
     message = "Pages must be at least 1")
    private int pages;

    @Min(value = 1,
     message = "Copies must be at least 1")
private int copies = 1;

    private String color;

    private String sided;

    private double amount;

    // =========================
    // Workflow Status
    // =========================

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // =========================
    // Payment Status
    // =========================

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // =========================
// Audit Timestamps
// =========================

private LocalDateTime createdAt;

private LocalDateTime updatedAt;

private LocalDateTime completedAt;

    // =========================
    // File Details
    // =========================

    private String fileName;

    private String filePath;

    @PrePersist
public void prePersist() {

    createdAt = LocalDateTime.now();

    updatedAt = LocalDateTime.now();
}

    @PreUpdate
public void preUpdate() {

    updatedAt = LocalDateTime.now();
}

    // =========================
    // Getters and Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSided() {
        return sided;
    }

    public void setSided(String sided) {
        this.sided = sided;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}

public LocalDateTime getUpdatedAt() {
    return updatedAt;
}

public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
}

public LocalDateTime getCompletedAt() {
    return completedAt;
}

public void setCompletedAt(LocalDateTime completedAt) {
    this.completedAt = completedAt;
}
}