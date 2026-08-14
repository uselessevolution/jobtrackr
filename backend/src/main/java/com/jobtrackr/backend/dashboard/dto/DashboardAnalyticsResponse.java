package com.jobtrackr.backend.dashboard.dto;

import java.time.LocalDate;
import java.util.List;

public class DashboardAnalyticsResponse {

    private int days;
    private LocalDate startDate;
    private LocalDate endDate;

    private long applicationsInPeriod;
    private long interviewsInPeriod;

    private List<DailyCountResponse> applicationTrend;
    private List<DailyCountResponse> interviewTrend;

    public DashboardAnalyticsResponse() {
    }

    public DashboardAnalyticsResponse(
            int days,
            LocalDate startDate,
            LocalDate endDate,
            long applicationsInPeriod,
            long interviewsInPeriod,
            List<DailyCountResponse> applicationTrend,
            List<DailyCountResponse> interviewTrend) {

        this.days = days;
        this.startDate = startDate;
        this.endDate = endDate;
        this.applicationsInPeriod = applicationsInPeriod;
        this.interviewsInPeriod = interviewsInPeriod;
        this.applicationTrend = applicationTrend;
        this.interviewTrend = interviewTrend;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getApplicationsInPeriod() {
        return applicationsInPeriod;
    }

    public void setApplicationsInPeriod(long applicationsInPeriod) {
        this.applicationsInPeriod = applicationsInPeriod;
    }

    public long getInterviewsInPeriod() {
        return interviewsInPeriod;
    }

    public void setInterviewsInPeriod(long interviewsInPeriod) {
        this.interviewsInPeriod = interviewsInPeriod;
    }

    public List<DailyCountResponse> getApplicationTrend() {
        return applicationTrend;
    }

    public void setApplicationTrend(
            List<DailyCountResponse> applicationTrend) {
        this.applicationTrend = applicationTrend;
    }

    public List<DailyCountResponse> getInterviewTrend() {
        return interviewTrend;
    }

    public void setInterviewTrend(
            List<DailyCountResponse> interviewTrend) {
        this.interviewTrend = interviewTrend;
    }
}