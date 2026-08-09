package com.jobtrackr.backend.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.application.dto.PagedResponse;
import com.jobtrackr.backend.notification.dto.NotificationResponse;
import com.jobtrackr.backend.notification.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(
        name = "Notifications",
        description = "View and manage in-app notifications"
)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService =
                notificationService;
    }

    @GetMapping
    @Operation(
            summary = "Get the authenticated user's notifications"
    )
    public PagedResponse<NotificationResponse> findAll(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "createdAt")
            String sortBy,

            @RequestParam(defaultValue = "desc")
            String direction) {

        return notificationService.findAll(
                page,
                size,
                sortBy,
                direction);
    }

    @GetMapping("/unread-count")
    @Operation(
            summary = "Get unread notification count"
    )
    public long countUnread() {

        return notificationService.countUnread();
    }

    @GetMapping("/{notificationId}")
    @Operation(
            summary = "Get one notification"
    )
    public NotificationResponse findById(
            @PathVariable String notificationId) {

        return notificationService.findById(
                notificationId);
    }

    @PostMapping("/{notificationId}/read")
    @Operation(
            summary = "Mark a notification as read"
    )
    public NotificationResponse markAsRead(
            @PathVariable String notificationId) {

        return notificationService.markAsRead(
                notificationId);
    }
}