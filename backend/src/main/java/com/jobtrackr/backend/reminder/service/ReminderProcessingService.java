package com.jobtrackr.backend.reminder.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.notification.service.NotificationService;
import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderStatus;
import com.jobtrackr.backend.reminder.repository.ReminderRepository;
import com.jobtrackr.backend.notification.service.NotificationService;
import com.jobtrackr.backend.reminder.model.ReminderChannel;

@Service
public class ReminderProcessingService {
    private final ReminderEmailService reminderEmailService;
    private final ReminderRepository reminderRepository;
    private final NotificationService notificationService;

    public ReminderProcessingService(
            ReminderRepository reminderRepository,
            NotificationService notificationService,
            ReminderEmailService reminderEmailService) {

        this.reminderRepository = reminderRepository;

        this.notificationService = notificationService;

        this.reminderEmailService = reminderEmailService;
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

            releaseForRetry(reminder);

            throw exception;
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

    private void releaseForRetry(
            Reminder reminder) {

        LocalDateTime now = LocalDateTime.now();

        reminder.setStatus(
                ReminderStatus.PENDING);

        reminder.setProcessingStartedAt(null);

        reminder.setAttempts(
                reminder.getAttempts() + 1);

        reminder.setUpdatedAt(now);

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

            boolean sent = reminderEmailService
                    .sendReminderEmail(reminder);

            if (!sent) {
                throw new IllegalStateException(
                        "Email delivery is disabled");
            }
        }
    }
}