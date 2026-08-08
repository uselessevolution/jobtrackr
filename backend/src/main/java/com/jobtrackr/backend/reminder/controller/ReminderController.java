package com.jobtrackr.backend.reminder.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jobtrackr.backend.application.dto.PagedResponse;
import com.jobtrackr.backend.reminder.dto.CreateReminderRequest;
import com.jobtrackr.backend.reminder.dto.ReminderResponse;
import com.jobtrackr.backend.reminder.dto.UpdateReminderRequest;
import com.jobtrackr.backend.reminder.service.ReminderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reminders")
@Tag(name = "Reminders", description = "Manage reminders for job applications")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(
            ReminderService reminderService) {

        this.reminderService = reminderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a reminder")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reminder created"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Job application not found")
    })
    public ReminderResponse create(
            @Valid @RequestBody CreateReminderRequest request) {

        return reminderService.create(request);
    }

    @GetMapping
    @Operation(summary = "Get the authenticated user's reminders")
    public PagedResponse<ReminderResponse> findAll(
            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "scheduledAt") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return reminderService.findAll(
                page,
                size,
                sortBy,
                direction);
    }

    @GetMapping("/{reminderId}")
    @Operation(summary = "Get one reminder")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reminder returned"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Reminder not found")
    })
    public ReminderResponse findById(
            @PathVariable String reminderId) {

        return reminderService.findById(
                reminderId);
    }

    @PutMapping("/{reminderId}")
    @Operation(summary = "Update a pending reminder")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reminder updated"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Reminder not found")
    })
    public ReminderResponse update(
            @PathVariable String reminderId,
            @Valid @RequestBody UpdateReminderRequest request) {

        return reminderService.update(
                reminderId,
                request);
    }

    @DeleteMapping("/{reminderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a reminder")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reminder deleted"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Reminder not found")
    })
    public void delete(
            @PathVariable String reminderId) {

        reminderService.delete(reminderId);
    }

    @PostMapping("/{reminderId}/cancel")
    @Operation(summary = "Cancel a pending reminder")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reminder cancelled"),
            @ApiResponse(responseCode = "401", description = "Authentication is required"),
            @ApiResponse(responseCode = "404", description = "Reminder not found"),
            @ApiResponse(responseCode = "409", description = "Reminder cannot be cancelled in its current state")
    })
    public ReminderResponse cancel(
            @PathVariable String reminderId) {

        return reminderService.cancel(
                reminderId);
    }

}