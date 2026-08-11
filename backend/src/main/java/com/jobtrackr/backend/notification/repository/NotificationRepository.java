package com.jobtrackr.backend.notification.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobtrackr.backend.notification.model.Notification;

public interface NotificationRepository
                extends MongoRepository<Notification, String>,
                NotificationRepositoryCustom {

        Page<Notification> findAllByUserId(
                        String userId,
                        Pageable pageable);

        Optional<Notification> findByIdAndUserId(
                        String id,
                        String userId);

        boolean existsByReminderId(
                        String reminderId);

        long countByUserIdAndReadFalse(
                        String userId);
}