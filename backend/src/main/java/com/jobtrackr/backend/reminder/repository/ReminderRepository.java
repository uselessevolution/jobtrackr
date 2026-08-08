package com.jobtrackr.backend.reminder.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtrackr.backend.reminder.model.Reminder;

public interface ReminderRepository
        extends MongoRepository<Reminder, String> {

    Page<Reminder> findAllByUserId(
            String userId,
            Pageable pageable);

    Optional<Reminder> findByIdAndUserId(
            String id,
            String userId);
}