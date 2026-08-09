package com.jobtrackr.backend.notification.mapper;

import org.springframework.stereotype.Component;

import com.jobtrackr.backend.notification.dto.NotificationResponse;
import com.jobtrackr.backend.notification.model.Notification;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(
            Notification notification) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(
                notification.getId());

        response.setApplicationId(
                notification.getApplicationId());

        response.setReminderId(
                notification.getReminderId());

        response.setType(
                notification.getType());

        response.setTitle(
                notification.getTitle());

        response.setMessage(
                notification.getMessage());

        response.setRead(
                notification.isRead());

        response.setCreatedAt(
                notification.getCreatedAt());

        response.setReadAt(
                notification.getReadAt());

        return response;
    }
}