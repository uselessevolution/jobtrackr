package com.jobtrackr.backend.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.dto.PagedResponse;
import com.jobtrackr.backend.common.exception.ResourceNotFoundException;
import com.jobtrackr.backend.notification.dto.NotificationResponse;
import com.jobtrackr.backend.notification.mapper.NotificationMapper;
import com.jobtrackr.backend.notification.model.Notification;
import com.jobtrackr.backend.notification.model.NotificationType;
import com.jobtrackr.backend.notification.repository.NotificationRepository;
import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.user.service.CurrentUserService;

@Service
public class NotificationService {

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of(
                    "createdAt",
                    "read"
            );

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CurrentUserService currentUserService;

    public NotificationService(
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper,
            CurrentUserService currentUserService) {

        this.notificationRepository =
                notificationRepository;

        this.notificationMapper =
                notificationMapper;

        this.currentUserService =
                currentUserService;
    }

    public void createFromReminder(
            Reminder reminder) {

        if (notificationRepository
                .existsByReminderId(
                        reminder.getId())) {

            return;
        }

        Notification notification =
                new Notification();

        notification.setUserId(
                reminder.getUserId());

        notification.setApplicationId(
                reminder.getApplicationId());

        notification.setReminderId(
                reminder.getId());

        notification.setType(
                NotificationType.REMINDER);

        notification.setTitle(
                buildReminderTitle(reminder));

        notification.setMessage(
                buildReminderMessage(reminder));

        notification.setRead(false);

        notification.setCreatedAt(
                LocalDateTime.now());

        notification.setReadAt(null);

        notificationRepository.save(
                notification);
    }

    public PagedResponse<NotificationResponse> findAll(
            int page,
            int size,
            String sortBy,
            String direction) {

        validatePaginationAndSorting(
                page,
                size,
                sortBy,
                direction);

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort);

        String currentUserId =
                currentUserService
                        .getCurrentUserId();

        Page<Notification> notificationPage =
                notificationRepository
                        .findAllByUserId(
                                currentUserId,
                                pageable);

        List<NotificationResponse> content =
                notificationPage
                        .getContent()
                        .stream()
                        .map(notificationMapper::toResponse)
                        .toList();

        return new PagedResponse<>(
                content,
                notificationPage.getNumber(),
                notificationPage.getSize(),
                notificationPage.getTotalElements(),
                notificationPage.getTotalPages(),
                notificationPage.isFirst(),
                notificationPage.isLast());
    }

    public NotificationResponse findById(
            String notificationId) {

        Notification notification =
                getOwnedNotification(
                        notificationId);

        return notificationMapper
                .toResponse(notification);
    }

    public NotificationResponse markAsRead(
            String notificationId) {

        Notification notification =
                getOwnedNotification(
                        notificationId);

        if (!notification.isRead()) {

            notification.setRead(true);

            notification.setReadAt(
                    LocalDateTime.now());

            notificationRepository.save(
                    notification);
        }

        return notificationMapper
                .toResponse(notification);
    }

    public long countUnread() {

        String currentUserId =
                currentUserService
                        .getCurrentUserId();

        return notificationRepository
                .countByUserIdAndReadFalse(
                        currentUserId);
    }

    private Notification getOwnedNotification(
            String notificationId) {

        String currentUserId =
                currentUserService
                        .getCurrentUserId();

        return notificationRepository
                .findByIdAndUserId(
                        notificationId,
                        currentUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with id: "
                                        + notificationId));
    }

    private String buildReminderTitle(
            Reminder reminder) {

        return switch (reminder.getType()) {

            case INTERVIEW ->
                    "Interview reminder";

            case FOLLOW_UP ->
                    "Follow-up reminder";

            case APPLICATION_DEADLINE ->
                    "Application deadline reminder";
        };
    }

    private String buildReminderMessage(
            Reminder reminder) {

        if (reminder.getMessage() != null
                && !reminder.getMessage()
                        .isBlank()) {

            return reminder.getMessage();
        }

        return switch (reminder.getType()) {

            case INTERVIEW ->
                    "You have an upcoming interview reminder.";

            case FOLLOW_UP ->
                    "It is time to follow up on your job application.";

            case APPLICATION_DEADLINE ->
                    "A job application deadline is approaching.";
        };
    }

    private void validatePaginationAndSorting(
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new IllegalArgumentException(
                    "Page number must not be negative");
        }

        if (size < 1 || size > 100) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        if (!ALLOWED_SORT_FIELDS
                .contains(sortBy)) {

            throw new IllegalArgumentException(
                    "Unsupported notification sort field: "
                            + sortBy);
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Sort direction must be asc or desc");
        }
    }
}