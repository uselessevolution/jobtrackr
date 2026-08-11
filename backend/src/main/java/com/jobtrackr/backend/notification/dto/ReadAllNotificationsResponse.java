package com.jobtrackr.backend.notification.dto;

public class ReadAllNotificationsResponse {

    private long updatedCount;

    public ReadAllNotificationsResponse() {
    }

    public ReadAllNotificationsResponse(
            long updatedCount) {

        this.updatedCount = updatedCount;
    }

    public long getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(
            long updatedCount) {

        this.updatedCount = updatedCount;
    }
}