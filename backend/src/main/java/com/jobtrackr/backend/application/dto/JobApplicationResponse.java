package com.jobtrackr.backend.application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jobtrackr.backend.application.model.ApplicationPriority;
import com.jobtrackr.backend.application.model.ApplicationStatus;

public class JobApplicationResponse {

    private String id;
    private String companyName;
    private String jobTitle;
    private String location;
    private String jobUrl;
    private ApplicationStatus status;
    private ApplicationPriority priority;
    private List<String> skills;
    private List<StatusHistoryResponse> statusHistory;
    private LocalDate appliedDate;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobApplicationResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public ApplicationPriority getPriority() {
        return priority;
    }

    public void setPriority(ApplicationPriority priority) {
        this.priority = priority;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<StatusHistoryResponse> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(
            List<StatusHistoryResponse> statusHistory) {

        this.statusHistory = statusHistory;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}