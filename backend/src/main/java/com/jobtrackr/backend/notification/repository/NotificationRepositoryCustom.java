package com.jobtrackr.backend.notification.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jobtrackr.backend.notification.model.Notification;
import com.jobtrackr.backend.notification.model.NotificationType;

public interface NotificationRepositoryCustom {

    Page<Notification> search(
            String userId,
            Boolean read,
            NotificationType type,
            Pageable pageable);

    long markAllAsRead(
            String userId,
            LocalDateTime readAt);
}