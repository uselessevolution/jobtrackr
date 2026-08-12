package com.jobtrackr.backend.email.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "email_deliveries")
public class EmailDelivery {

    @Id
    private String id;

    private String reminderId;

    private String userId;

    private String recipient;

    private String subject;

    private EmailDeliveryStatus status;

    private int attemptNumber;

    private String errorMessage;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    public EmailDelivery() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public EmailDeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(
            EmailDeliveryStatus status) {

        this.status = status;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(
            int attemptNumber) {

        this.attemptNumber = attemptNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(
            String errorMessage) {

        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(
            LocalDateTime sentAt) {

        this.sentAt = sentAt;
    }
}