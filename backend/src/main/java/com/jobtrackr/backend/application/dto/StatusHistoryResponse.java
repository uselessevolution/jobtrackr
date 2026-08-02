package com.jobtrackr.backend.application.dto;

import java.time.LocalDateTime;

import com.jobtrackr.backend.application.model.ApplicationStatus;

public class StatusHistoryResponse {

    private ApplicationStatus fromStatus;
    private ApplicationStatus toStatus;
    private LocalDateTime changedAt;

    public StatusHistoryResponse() {
    }

    public StatusHistoryResponse(
            ApplicationStatus fromStatus,
            ApplicationStatus toStatus,
            LocalDateTime changedAt) {

        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.changedAt = changedAt;
    }

    public ApplicationStatus getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(
            ApplicationStatus fromStatus) {

        this.fromStatus = fromStatus;
    }

    public ApplicationStatus getToStatus() {
        return toStatus;
    }

    public void setToStatus(
            ApplicationStatus toStatus) {

        this.toStatus = toStatus;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(
            LocalDateTime changedAt) {

        this.changedAt = changedAt;
    }
}