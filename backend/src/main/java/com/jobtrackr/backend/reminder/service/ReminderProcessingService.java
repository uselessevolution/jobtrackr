package com.jobtrackr.backend.reminder.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderStatus;
import com.jobtrackr.backend.reminder.repository.ReminderRepository;

@Service
public class ReminderProcessingService {

    private final ReminderRepository reminderRepository;

    public ReminderProcessingService(
            ReminderRepository reminderRepository) {

        this.reminderRepository = reminderRepository;
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

            Reminder reminder =
                    reminderRepository
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

            /*
             * Phase 5C will perform the real work here:
             *
             * notificationService.createNotification(...)
             * emailService.send(...)
             *
             * For this phase we only verify that claiming,
             * scheduling and lifecycle handling work.
             */

            markCompleted(reminder);

        } catch (RuntimeException exception) {

            releaseForRetry(reminder);

            throw exception;
        }
    }

    private void markCompleted(
            Reminder reminder) {

        LocalDateTime now =
                LocalDateTime.now();

        reminder.setStatus(
                ReminderStatus.COMPLETED);

        reminder.setProcessingStartedAt(null);

        reminder.setUpdatedAt(now);

        reminderRepository.save(reminder);
    }

    private void releaseForRetry(
            Reminder reminder) {

        LocalDateTime now =
                LocalDateTime.now();

        reminder.setStatus(
                ReminderStatus.PENDING);

        reminder.setProcessingStartedAt(null);

        reminder.setAttempts(
                reminder.getAttempts() + 1);

        reminder.setUpdatedAt(now);

        reminderRepository.save(reminder);
    }
}