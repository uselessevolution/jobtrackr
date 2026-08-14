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

}