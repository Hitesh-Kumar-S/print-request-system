package com.example.printapp.model;

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

    private String name;

    private String documentName;

    private int pages;

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
    // File Details
    // =========================

    private String fileName;

    private String filePath;

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
}