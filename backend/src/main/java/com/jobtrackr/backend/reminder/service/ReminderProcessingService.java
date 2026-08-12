package com.jobtrackr.backend.reminder.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.notification.service.NotificationService;
import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderStatus;
import com.jobtrackr.backend.reminder.repository.ReminderRepository;
import com.jobtrackr.backend.notification.service.NotificationService;
import com.jobtrackr.backend.reminder.model.ReminderChannel;

import org.springframework.beans.factory.annotation.Value;

@Service
public class ReminderProcessingService {
    private final ReminderEmailService reminderEmailService;
    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;
    private final int maxAttempts;

    public ReminderProcessingService(
            ReminderRepository reminderRepository,
            NotificationService notificationService,
            ReminderEmailService reminderEmailService,
            @Value("${jobtrackr.reminder.max-attempts:3}") int maxAttempts) {

        this.reminderRepository = reminderRepository;

        this.notificationService = notificationService;

        this.reminderEmailService = reminderEmailService;

        this.maxAttempts = maxAttempts;
    }

    public int processDueReminders(
            int maxReminders) {

        if (maxReminders < 1
                || maxReminders > 500) {

            throw new IllegalArgumentException(
                    "Reminder processing limit must be between 1 and 500");
        }

        int processedCount = 0;

        while (processedCount < maxReminders) {

            Reminder reminder = reminderRepository
                    .claimNextDueReminder(
                            LocalDateTime.now())
                    .orElse(null);

            if (reminder == null) {
                break;
            }

            processClaimedReminder(reminder);

            processedCount++;
        }

        return processedCount;
    }

    private void processClaimedReminder(
            Reminder reminder) {

        try {

            processChannels(reminder);

            markCompleted(reminder);

        } catch (RuntimeException exception) {

            handleProcessingFailure(reminder);
        }
    }

    private void markCompleted(
            Reminder reminder) {

        LocalDateTime now = LocalDateTime.now();

        reminder.setStatus(
                ReminderStatus.COMPLETED);

        reminder.setProcessingStartedAt(null);

        reminder.setUpdatedAt(now);

        reminderRepository.save(reminder);
    }

    private void handleProcessingFailure(
            Reminder reminder) {

        LocalDateTime now = LocalDateTime.now();

        int newAttempts = reminder.getAttempts() + 1;

        reminder.setAttempts(
                newAttempts);

        reminder.setProcessingStartedAt(null);

        reminder.setUpdatedAt(now);

        if (newAttempts >= maxAttempts) {

            reminder.setStatus(
                    ReminderStatus.FAILED);

        } else {

            reminder.setStatus(
                    ReminderStatus.PENDING);
        }

        reminderRepository.save(reminder);
    }


    private void processChannels(
            Reminder reminder) {

        if (reminder.getChannels() == null
                || reminder.getChannels().isEmpty()) {

            throw new IllegalStateException(
                    "Reminder has no delivery channels: "
                            + reminder.getId());
        }

        if (reminder.getChannels()
                .contains(ReminderChannel.IN_APP)) {

            notificationService
                    .createFromReminder(reminder);
        }

        if (reminder.getChannels()
                .contains(ReminderChannel.EMAIL)) {

            if (reminder.getChannels()
                    .contains(ReminderChannel.EMAIL)) {

                reminderEmailService
                        .sendReminderEmail(reminder);
            }
        }
    }
}