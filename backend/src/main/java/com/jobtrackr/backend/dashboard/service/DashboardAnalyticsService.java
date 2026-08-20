package com.jobtrackr.backend.dashboard.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.model.JobApplication;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;
import com.jobtrackr.backend.dashboard.dto.DailyCountResponse;
import com.jobtrackr.backend.user.service.CurrentUserService;
import com.jobtrackr.backend.application.model.Interview;
import com.jobtrackr.backend.dashboard.dto.DashboardAnalyticsResponse;
import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.model.StatusHistory;
import com.jobtrackr.backend.dashboard.dto.DashboardFunnelResponse;

@Service
public class DashboardAnalyticsService {

    private static final int DEFAULT_DAYS = 30;
    private static final int MIN_DAYS = 1;
    private static final int MAX_DAYS = 365;

    private final JobApplicationRepository jobApplicationRepository;
    private final CurrentUserService currentUserService;

    public DashboardAnalyticsService(
            JobApplicationRepository jobApplicationRepository,
            CurrentUserService currentUserService) {

        this.jobApplicationRepository = jobApplicationRepository;
        this.currentUserService = currentUserService;
    }

    public DashboardAnalyticsResponse getAnalytics(Integer days) {

        int resolvedDays = resolveDays(days);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(resolvedDays - 1L);

        List<DailyCountResponse> applicationTrend = getApplicationTrendForRange(
                startDate,
                endDate);

        List<DailyCountResponse> interviewTrend = getInterviewTrendForRange(
                startDate,
                endDate);

        long applicationsInPeriod = applicationTrend.stream()
                .mapToLong(DailyCountResponse::getCount)
                .sum();

        long interviewsInPeriod = interviewTrend.stream()
                .mapToLong(DailyCountResponse::getCount)
                .sum();

        return new DashboardAnalyticsResponse(
                resolvedDays,
                startDate,
                endDate,
                applicationsInPeriod,
                interviewsInPeriod,
                applicationTrend,
                interviewTrend);
    }

    private List<DailyCountResponse> getApplicationTrendForRange(
            LocalDate startDate,
            LocalDate endDate) {

        String currentUserId = currentUserService.getCurrentUserId();

        List<JobApplication> applications = jobApplicationRepository
                .findAllByUserIdAndAppliedDateBetween(
                        currentUserId,
                        startDate,
                        endDate);

        Map<LocalDate, Long> counts = createEmptyDateRange(
                startDate,
                endDate);

        for (JobApplication application : applications) {

            LocalDate appliedDate = application.getAppliedDate();

            if (appliedDate != null) {
                counts.computeIfPresent(
                        appliedDate,
                        (date, count) -> count + 1);
            }
        }

        return toDailyCountResponses(counts);
    }

    private List<DailyCountResponse> getInterviewTrendForRange(
            LocalDate startDate,
            LocalDate endDate) {

        String currentUserId = currentUserService.getCurrentUserId();

        List<JobApplication> applications = jobApplicationRepository
                .findAllByUserId(currentUserId);

        Map<LocalDate, Long> counts = createEmptyDateRange(
                startDate,
                endDate);

        for (JobApplication application : applications) {

            List<Interview> interviews = application.getInterviews();

            if (interviews == null) {
                continue;
            }

            for (Interview interview : interviews) {

                if (interview.getScheduledAt() == null) {
                    continue;
                }

                LocalDate interviewDate = interview.getScheduledAt()
                        .toLocalDate();

                counts.computeIfPresent(
                        interviewDate,
                        (date, count) -> count + 1);
            }
        }

        return toDailyCountResponses(counts);
    }

    public DashboardFunnelResponse getFunnelAnalytics() {

        String currentUserId = currentUserService.getCurrentUserId();

        List<JobApplication> applications = jobApplicationRepository
                .findAllByUserId(currentUserId);

        long applied = applications.stream()
                .filter(this::hasApplied)
                .count();

        long interviewed = applications.stream()
                .filter(application -> hasReachedStage(
                        application,
                        ApplicationStatus.INTERVIEWING))
                .count();

        long offered = applications.stream()
                .filter(application -> hasReachedStage(
                        application,
                        ApplicationStatus.OFFER))
                .count();

        long accepted = applications.stream()
                .filter(application -> hasReachedStage(
                        application,
                        ApplicationStatus.ACCEPTED))
                .count();

        double interviewRate = percentage(interviewed, applied);

        double offerRate = percentage(offered, applied);

        double acceptedRate = percentage(accepted, applied);

        double interviewToOfferRate = percentage(offered, interviewed);

        double offerToAcceptedRate = percentage(accepted, offered);

        return new DashboardFunnelResponse(
                applied,
                interviewed,
                offered,
                accepted,
                interviewRate,
                offerRate,
                acceptedRate,
                interviewToOfferRate,
                offerToAcceptedRate);
    }

    private int resolveDays(Integer days) {

        if (days == null) {
            return DEFAULT_DAYS;
        }

        if (days < MIN_DAYS || days > MAX_DAYS) {
            throw new IllegalArgumentException(
                    "days must be between 1 and 365");
        }

        return days;
    }

    private Map<LocalDate, Long> createEmptyDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        Map<LocalDate, Long> counts = new LinkedHashMap<>();

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            counts.put(currentDate, 0L);
            currentDate = currentDate.plusDays(1);
        }

        return counts;
    }

    private List<DailyCountResponse> toDailyCountResponses(
            Map<LocalDate, Long> counts) {

        List<DailyCountResponse> responses = new ArrayList<>();

        for (Map.Entry<LocalDate, Long> entry : counts.entrySet()) {

            responses.add(
                    new DailyCountResponse(
                            entry.getKey(),
                            entry.getValue()));
        }

        return responses;
    }

    private boolean hasReachedStage(
            JobApplication application,
            ApplicationStatus targetStatus) {

        if (isStatusAtOrBeyondMilestone(
                application.getStatus(),
                targetStatus)) {

            return true;
        }

        List<StatusHistory> statusHistory = application.getStatusHistory();

        if (statusHistory == null
                || statusHistory.isEmpty()) {

            return false;
        }

        return statusHistory.stream()
                .anyMatch(history -> isStatusAtOrBeyondMilestone(
                        history.getFromStatus(),
                        targetStatus)
                        || isStatusAtOrBeyondMilestone(
                                history.getToStatus(),
                                targetStatus));
    }

    private boolean isStatusAtOrBeyondMilestone(
            ApplicationStatus status,
            ApplicationStatus targetStatus) {

        if (status == null) {
            return false;
        }

        return switch (targetStatus) {

            case APPLIED ->
                status == ApplicationStatus.APPLIED
                        || status == ApplicationStatus.OA_RECEIVED
                        || status == ApplicationStatus.PHONE_SCREEN
                        || status == ApplicationStatus.INTERVIEWING
                        || status == ApplicationStatus.OFFER
                        || status == ApplicationStatus.ACCEPTED;

            case INTERVIEWING ->
                status == ApplicationStatus.INTERVIEWING
                        || status == ApplicationStatus.OFFER
                        || status == ApplicationStatus.ACCEPTED;

            case OFFER ->
                status == ApplicationStatus.OFFER
                        || status == ApplicationStatus.ACCEPTED;

            case ACCEPTED ->
                status == ApplicationStatus.ACCEPTED;

            default -> false;
        };
    }

    private boolean hasApplied(
            JobApplication application) {

        if (application.getAppliedDate() != null) {
            return true;
        }

        return hasReachedStage(
                application,
                ApplicationStatus.APPLIED);
    }

    private double percentage(
            long numerator,
            long denominator) {

        if (denominator == 0) {
            return 0.0;
        }

        return Math.round(
                ((double) numerator / denominator)
                        * 10000.0)
                / 100.0;
    }

}