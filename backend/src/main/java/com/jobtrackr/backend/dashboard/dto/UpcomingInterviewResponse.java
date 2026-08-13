package com.jobtrackr.backend.dashboard.dto;

import java.time.LocalDateTime;

import com.jobtrackr.backend.application.model.InterviewType;

public class UpcomingInterviewResponse {

    private String applicationId;
    private String companyName;
    private String jobTitle;
    private String interviewId;
    private InterviewType type;
    private LocalDateTime scheduledAt;
    private Integer durationMinutes;
    private String location;
    private String meetingLink;

    public UpcomingInterviewResponse() {
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(
            String applicationId) {
        this.applicationId = applicationId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(
            String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(
            String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(
            String interviewId) {
        this.interviewId = interviewId;
    }

    public InterviewType getType() {
        return type;
    }

    public void setType(
            InterviewType type) {
        this.type = type;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(
            LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(
            Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(
            String location) {
        this.location = location;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(
            String meetingLink) {
        this.meetingLink = meetingLink;
    }
}