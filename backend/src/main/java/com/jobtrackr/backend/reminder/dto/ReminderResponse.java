package com.jobtrackr.backend.reminder.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.jobtrackr.backend.reminder.model.ReminderChannel;
import com.jobtrackr.backend.reminder.model.ReminderStatus;
import com.jobtrackr.backend.reminder.model.ReminderType;

public class ReminderResponse {

    private String id;

    private String applicationId;

    private ReminderType type;

    private LocalDateTime scheduledAt;

    private Set<ReminderChannel> channels;

    private ReminderStatus status;

    private String message;

    private int attempts;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime processingStartedAt;

    public ReminderResponse() {
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

    public void setType(ReminderType type) {
        this.type = type;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(
            LocalDateTime scheduledAt) {

        this.scheduledAt = scheduledAt;
    }

    public Set<ReminderChannel> getChannels() {
        return channels;
    }

    public void setChannels(
            Set<ReminderChannel> channels) {

        this.channels = channels;
    }

    public ReminderStatus getStatus() {
        return status;
    }

    public void setStatus(
            ReminderStatus status) {

        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt) {

        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt) {

        this.updatedAt = updatedAt;
    }

    public LocalDateTime getProcessingStartedAt() {
        return processingStartedAt;
    }

    public void setProcessingStartedAt(
            LocalDateTime processingStartedAt) {

        this.processingStartedAt = processingStartedAt;
    }
}