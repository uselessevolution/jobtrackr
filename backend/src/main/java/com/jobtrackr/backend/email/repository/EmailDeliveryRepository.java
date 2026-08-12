package com.jobtrackr.backend.email.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtrackr.backend.email.model.EmailDelivery;
import com.jobtrackr.backend.email.model.EmailDeliveryStatus;

public interface EmailDeliveryRepository
        extends MongoRepository<EmailDelivery, String> {

    boolean existsByReminderIdAndStatus(
            String reminderId,
            EmailDeliveryStatus status);

    List<EmailDelivery> findAllByReminderIdOrderByAttemptNumberAsc(
            String reminderId);
}