package com.jobtrackr.backend.reminder.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.jobtrackr.backend.reminder.model.Reminder;

public interface ReminderClaimRepository {

    Optional<Reminder> claimNextDueReminder(
            LocalDateTime now);
}