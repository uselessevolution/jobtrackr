package com.jobtrackr.backend.dashboard.service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.model.ApplicationStatus;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;
import com.jobtrackr.backend.dashboard.dto.DashboardSummaryResponse;
import com.jobtrackr.backend.notification.repository.NotificationRepository;
import com.jobtrackr.backend.reminder.model.ReminderStatus;
import com.jobtrackr.backend.reminder.repository.ReminderRepository;
import com.jobtrackr.backend.user.service.CurrentUserService;

@Service
public class DashboardService {

    private final JobApplicationRepository applicationRepository;
    private final ReminderRepository reminderRepository;
    private final NotificationRepository notificationRepository;
    private final DashboardQueryService dashboardQueryService;
    private final CurrentUserService currentUserService;

    public DashboardService(
            JobApplicationRepository applicationRepository,
            ReminderRepository reminderRepository,
            NotificationRepository notificationRepository,
            DashboardQueryService dashboardQueryService,
            CurrentUserService currentUserService) {

        this.applicationRepository = applicationRepository;

        this.reminderRepository = reminderRepository;

        this.notificationRepository = notificationRepository;

        this.dashboardQueryService = dashboardQueryService;

        this.currentUserService = currentUserService;
    }

    public DashboardSummaryResponse getSummary() {

        String currentUserId = currentUserService
                .getCurrentUserId();

        long totalApplications = applicationRepository
                .countByUserId(
                        currentUserId);

        Map<ApplicationStatus, Long> statusCounts = buildStatusCounts(
                currentUserId);

        long pendingReminders = reminderRepository
                .countByUserIdAndStatus(
                        currentUserId,
                        ReminderStatus.PENDING);

        long unreadNotifications = notificationRepository
                .countByUserIdAndReadFalse(
                        currentUserId);

        long upcomingInterviews = dashboardQueryService
                .countUpcomingInterviews(
                        currentUserId,
                        LocalDateTime.now());

        return new DashboardSummaryResponse(
                totalApplications,
                statusCounts,
                pendingReminders,
                unreadNotifications,
                upcomingInterviews);
    }

    private Map<ApplicationStatus, Long> buildStatusCounts(
            String userId) {

        Map<ApplicationStatus, Long> counts = new EnumMap<>(
                ApplicationStatus.class);

        for (ApplicationStatus status : ApplicationStatus.values()) {

            long count = applicationRepository
                    .countByUserIdAndStatus(
                            userId,
                            status);

            counts.put(
                    status,
                    count);
        }

        return counts;
    }
}