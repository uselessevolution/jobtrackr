package com.jobtrackr.backend.application.model;

import java.time.LocalDateTime;

public class Interview {

    private String id;

    private InterviewType type;

    private LocalDateTime scheduledAt;

    private Integer durationMinutes;

    private String location;

    private String meetingLink;

    private String interviewerName;

    private InterviewStatus status;

    private InterviewResult result;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Interview() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InterviewType getType() {
        return type;
    }

    public void setType(InterviewType type) {
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

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getInterviewerName() {
        return interviewerName;
    }

    public void setInterviewerName(
            String interviewerName) {

        this.interviewerName = interviewerName;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(
            InterviewStatus status) {

        this.status = status;
    }

    public InterviewResult getResult() {
        return result;
    }

    public void setResult(
            InterviewResult result) {

        this.result = result;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}