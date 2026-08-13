package com.jobtrackr.backend.dashboard.dto;

import java.util.Map;

import com.jobtrackr.backend.application.model.ApplicationStatus;

public class DashboardSummaryResponse {

    private long totalApplications;

    private Map<ApplicationStatus, Long> applicationStatusCounts;

    private long pendingReminders;

    private long unreadNotifications;

    private long upcomingInterviews;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(
            long totalApplications,
            Map<ApplicationStatus, Long> applicationStatusCounts,
            long pendingReminders,
            long unreadNotifications,
            long upcomingInterviews) {

        this.totalApplications = totalApplications;

        this.applicationStatusCounts = applicationStatusCounts;

        this.pendingReminders = pendingReminders;

        this.unreadNotifications = unreadNotifications;

        this.upcomingInterviews = upcomingInterviews;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(
            long totalApplications) {

        this.totalApplications = totalApplications;
    }

    public Map<ApplicationStatus, Long> getApplicationStatusCounts() {

        return applicationStatusCounts;
    }

    public void setApplicationStatusCounts(
            Map<ApplicationStatus, Long> applicationStatusCounts) {

        this.applicationStatusCounts = applicationStatusCounts;
    }

    public long getPendingReminders() {
        return pendingReminders;
    }

    public void setPendingReminders(
            long pendingReminders) {

        this.pendingReminders = pendingReminders;
    }

    public long getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(
            long unreadNotifications) {

        this.unreadNotifications = unreadNotifications;
    }

    public long getUpcomingInterviews() {
        return upcomingInterviews;
    }

    public void setUpcomingInterviews(
            long upcomingInterviews) {

        this.upcomingInterviews = upcomingInterviews;
    }
}