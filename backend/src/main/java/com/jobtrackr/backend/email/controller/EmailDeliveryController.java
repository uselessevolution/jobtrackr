package com.jobtrackr.backend.email.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.email.dto.EmailDeliveryResponse;
import com.jobtrackr.backend.email.service.EmailDeliveryQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reminders/{reminderId}/email-deliveries")
@Tag(name = "Email Deliveries", description = "View email delivery attempts for reminders")
public class EmailDeliveryController {

    private final EmailDeliveryQueryService queryService;

    public EmailDeliveryController(
            EmailDeliveryQueryService queryService) {

        this.queryService = queryService;
    }

    @GetMapping
    @Operation(summary = "Get email delivery attempts for a reminder")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email delivery attempts returned"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Reminder not found")
    })
    public List<EmailDeliveryResponse> findAll(
            @PathVariable String reminderId) {

        return queryService
                .findAllByReminder(
                        reminderId);
    }
}