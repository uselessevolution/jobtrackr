package com.jobtrackr.backend.reminder.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderStatus;

public interface ReminderRepository
                extends MongoRepository<Reminder, String>,
                ReminderClaimRepository {

        Page<Reminder> findAllByUserId(
                        String userId,
                        Pageable pageable);

        Optional<Reminder> findByIdAndUserId(
                        String id,
                        String userId);

        List<Reminder> findByStatusAndScheduledAtLessThanEqualOrderByScheduledAtAsc(
                        ReminderStatus status,
                        LocalDateTime scheduledAt,
                        Pageable pageable);

        long countByUserIdAndStatus(
                        String userId,
                        ReminderStatus status);

        List<Reminder> findByUserIdAndStatusAndScheduledAtGreaterThanEqual(
                        String userId,
                        ReminderStatus status,
                        LocalDateTime scheduledAt,
                        Pageable pageable);
}