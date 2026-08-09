package com.jobtrackr.backend.notification.dto;

import java.time.LocalDateTime;

import com.jobtrackr.backend.notification.model.NotificationType;

public class NotificationResponse {

    private String id;

    private String applicationId;

    private String reminderId;

    private NotificationType type;

    private String title;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;

    public NotificationResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(
            String applicationId) {

        this.applicationId = applicationId;
    }

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(
            String reminderId) {

        this.reminderId = reminderId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(
            NotificationType type) {

        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(
            LocalDateTime readAt) {

        this.readAt = readAt;
    }
}