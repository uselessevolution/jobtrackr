package com.jobtrackr.backend.dashboard.dto;

import java.time.LocalDateTime;

import com.jobtrackr.backend.reminder.model.ReminderType;

public class UpcomingReminderResponse {

    private String id;
    private String applicationId;
    private ReminderType type;
    private LocalDateTime scheduledAt;
    private String message;

    public UpcomingReminderResponse() {
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

    public ReminderType getType() {
        return type;
    }

    public void setType(
            ReminderType type) {
        this.type = type;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(
            LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message) {
        this.message = message;
    }
}