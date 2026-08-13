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
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.jobtrackr.backend.dashboard.dto.UpcomingInterviewResponse;
import com.jobtrackr.backend.dashboard.dto.UpcomingReminderResponse;
import com.jobtrackr.backend.reminder.model.Reminder;

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

        LocalDateTime now = LocalDateTime.now();

        long totalApplications = applicationRepository
                .countByUserId(
                        currentUserId);

        Map<ApplicationStatus, Long> statusCounts = dashboardQueryService
                .getApplicationStatusCounts(
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
                        now);

        List<UpcomingInterviewResponse> nextInterviews = dashboardQueryService
                .findUpcomingInterviews(
                        currentUserId,
                        now,
                        5);

        List<UpcomingReminderResponse> nextReminders = findUpcomingReminders(
                currentUserId,
                now,
                5);

        return new DashboardSummaryResponse(
                totalApplications,
                statusCounts,
                pendingReminders,
                unreadNotifications,
                upcomingInterviews,
                nextInterviews,
                nextReminders);
    }

    private List<UpcomingReminderResponse> findUpcomingReminders(
            String userId,
            LocalDateTime now,
            int limit) {

        PageRequest pageable = PageRequest.of(
                0,
                limit,
                Sort.by(
                        "scheduledAt")
                        .ascending());

        return reminderRepository
                .findByUserIdAndStatusAndScheduledAtGreaterThanEqual(
                        userId,
                        ReminderStatus.PENDING,
                        now,
                        pageable)
                .stream()
                .map(this::toUpcomingReminderResponse)
                .toList();
    }

    private UpcomingReminderResponse toUpcomingReminderResponse(
            Reminder reminder) {

        UpcomingReminderResponse response = new UpcomingReminderResponse();

        response.setId(
                reminder.getId());

        response.setApplicationId(
                reminder.getApplicationId());

        response.setType(
                reminder.getType());

        response.setScheduledAt(
                reminder.getScheduledAt());

        response.setMessage(
                reminder.getMessage());

        return response;
    }

}