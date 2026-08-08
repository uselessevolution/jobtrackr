package com.jobtrackr.backend.reminder.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.dto.PagedResponse;
import com.jobtrackr.backend.application.repository.JobApplicationRepository;
import com.jobtrackr.backend.common.exception.ResourceNotFoundException;
import com.jobtrackr.backend.reminder.dto.CreateReminderRequest;
import com.jobtrackr.backend.reminder.dto.ReminderResponse;
import com.jobtrackr.backend.reminder.dto.UpdateReminderRequest;
import com.jobtrackr.backend.reminder.mapper.ReminderMapper;
import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderStatus;
import com.jobtrackr.backend.reminder.repository.ReminderRepository;
import com.jobtrackr.backend.user.service.CurrentUserService;
import com.jobtrackr.backend.common.exception.InvalidReminderStateException;

@Service
public class ReminderService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "scheduledAt",
            "createdAt",
            "updatedAt",
            "type",
            "status");

    private final ReminderRepository reminderRepository;
    private final JobApplicationRepository applicationRepository;
    private final ReminderMapper reminderMapper;
    private final CurrentUserService currentUserService;

    public ReminderService(
            ReminderRepository reminderRepository,
            JobApplicationRepository applicationRepository,
            ReminderMapper reminderMapper,
            CurrentUserService currentUserService) {

        this.reminderRepository = reminderRepository;
        this.applicationRepository = applicationRepository;
        this.reminderMapper = reminderMapper;
        this.currentUserService = currentUserService;
    }

    public ReminderResponse create(
            CreateReminderRequest request) {

        String currentUserId = currentUserService.getCurrentUserId();

        verifyOwnedApplication(
                request.getApplicationId(),
                currentUserId);

        Reminder reminder = reminderMapper.toDocument(request);

        LocalDateTime now = LocalDateTime.now();

        reminder.setId(null);
        reminder.setUserId(currentUserId);
        reminder.setStatus(ReminderStatus.PENDING);
        reminder.setAttempts(0);
        reminder.setCreatedAt(now);
        reminder.setUpdatedAt(now);

        if (reminder.getChannels() == null) {
            reminder.setChannels(new HashSet<>());
        }

        Reminder savedReminder = reminderRepository.save(reminder);

        return reminderMapper.toResponse(
                savedReminder);
    }

    public PagedResponse<ReminderResponse> findAll(
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

        Pageable pageable = PageRequest.of(page, size, sort);

        String currentUserId = currentUserService.getCurrentUserId();

        Page<Reminder> reminderPage = reminderRepository.findAllByUserId(
                currentUserId,
                pageable);

        List<ReminderResponse> content = reminderPage.getContent()
                .stream()
                .map(reminderMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                content,
                reminderPage.getNumber(),
                reminderPage.getSize(),
                reminderPage.getTotalElements(),
                reminderPage.getTotalPages(),
                reminderPage.isFirst(),
                reminderPage.isLast());
    }

    public ReminderResponse findById(
            String reminderId) {

        Reminder reminder = getOwnedReminder(reminderId);

        return reminderMapper.toResponse(reminder);
    }

    public ReminderResponse update(
            String reminderId,
            UpdateReminderRequest request) {

        Reminder reminder = getOwnedReminder(reminderId);

        ensurePendingReminder(
                reminder,
                "updated");

        validateScheduledAt(
                request.getScheduledAt());

        reminderMapper.updateDocument(
                request,
                reminder);

        reminder.setUpdatedAt(
                LocalDateTime.now());

        Reminder savedReminder = reminderRepository.save(reminder);

        return reminderMapper.toResponse(
                savedReminder);
    }

    public void delete(
            String reminderId) {

        Reminder reminder = getOwnedReminder(reminderId);

        reminderRepository.delete(reminder);
    }

    private Reminder getOwnedReminder(
            String reminderId) {

        String currentUserId = currentUserService.getCurrentUserId();

        return reminderRepository
                .findByIdAndUserId(
                        reminderId,
                        currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reminder not found with id: "
                                + reminderId));
    }

    private void verifyOwnedApplication(
            String applicationId,
            String currentUserId) {

        boolean exists = applicationRepository
                .existsByIdAndUserId(
                        applicationId,
                        currentUserId);

        if (!exists) {
            throw new ResourceNotFoundException(
                    "Job application not found with id: "
                            + applicationId);
        }
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

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported reminder sort field: "
                            + sortBy);
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {

            throw new IllegalArgumentException(
                    "Sort direction must be asc or desc");
        }
    }

    public ReminderResponse cancel(
            String reminderId) {

        Reminder reminder = getOwnedReminder(reminderId);

        ensurePendingReminder(
                reminder,
                "cancelled");

        reminder.setStatus(
                ReminderStatus.CANCELLED);

        reminder.setUpdatedAt(
                LocalDateTime.now());

        Reminder savedReminder = reminderRepository.save(reminder);

        return reminderMapper.toResponse(
                savedReminder);
    }

    public ReminderResponse complete(
            String reminderId) {

        Reminder reminder = getOwnedReminder(reminderId);

        ensurePendingReminder(
                reminder,
                "completed");

        reminder.setStatus(
                ReminderStatus.COMPLETED);

        reminder.setUpdatedAt(
                LocalDateTime.now());

        Reminder savedReminder = reminderRepository.save(reminder);

        return reminderMapper.toResponse(
                savedReminder);
    }

    private void ensurePendingReminder(
            Reminder reminder,
            String requestedAction) {

        if (reminder.getStatus() != ReminderStatus.PENDING) {

            throw new InvalidReminderStateException(
                    "Reminder cannot be "
                            + requestedAction
                            + " because its current status is "
                            + reminder.getStatus());
        }
    }

    public List<Reminder> findDuePendingReminders(
            LocalDateTime now,
            int limit) {

        if (now == null) {
            throw new IllegalArgumentException(
                    "Current time is required");
        }

        if (limit < 1 || limit > 500) {
            throw new IllegalArgumentException(
                    "Reminder processing limit must be between 1 and 500");
        }

        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by("scheduledAt").ascending());

        return reminderRepository
                .findByStatusAndScheduledAtLessThanEqualOrderByScheduledAtAsc(
                        ReminderStatus.PENDING,
                        now,
                        pageable);
    }

    private void validateScheduledAt(
            LocalDateTime scheduledAt) {

        if (!scheduledAt.isAfter(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Reminder scheduled time must be in the future");
        }
    }
}