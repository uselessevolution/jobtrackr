package com.jobtrackr.backend.reminder.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.jobtrackr.backend.reminder.model.ReminderChannel;
import com.jobtrackr.backend.reminder.model.ReminderType;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReminderRequest {

    @NotBlank(
            message = "Application id is required"
    )
    private String applicationId;

    @NotNull(
            message = "Reminder type is required"
    )
    private ReminderType type;

    @NotNull(
            message = "Reminder scheduled time is required"
    )
    @Future(
            message = "Reminder scheduled time must be in the future"
    )
    private LocalDateTime scheduledAt;

    @NotEmpty(
            message = "At least one reminder channel is required"
    )
    private Set<ReminderChannel> channels;

    @Size(
            max = 1000,
            message = "Reminder message must not exceed 1000 characters"
    )
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}