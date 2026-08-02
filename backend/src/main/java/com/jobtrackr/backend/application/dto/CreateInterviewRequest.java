package com.jobtrackr.backend.application.dto;

import java.time.LocalDateTime;

import com.jobtrackr.backend.application.model.InterviewType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateInterviewRequest {

    @NotNull(message = "Interview type is required")
    private InterviewType type;

    @NotNull(message = "Interview scheduled time is required")
    private LocalDateTime scheduledAt;

    @NotNull(message = "Interview duration is required")
    @Min(
            value = 15,
            message = "Interview duration must be at least 15 minutes"
    )
    @Max(
            value = 480,
            message = "Interview duration must not exceed 480 minutes"
    )
    private Integer durationMinutes;

    @Size(
            max = 200,
            message = "Interview location must not exceed 200 characters"
    )
    private String location;

    @Size(
            max = 500,
            message = "Meeting link must not exceed 500 characters"
    )
    private String meetingLink;

    @Size(
            max = 150,
            message = "Interviewer name must not exceed 150 characters"
    )
    private String interviewerName;

    @Size(
            max = 3000,
            message = "Interview notes must not exceed 3000 characters"
    )
    private String notes;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}