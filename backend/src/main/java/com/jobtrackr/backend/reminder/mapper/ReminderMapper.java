package com.jobtrackr.backend.reminder.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.jobtrackr.backend.reminder.dto.CreateReminderRequest;
import com.jobtrackr.backend.reminder.dto.ReminderResponse;
import com.jobtrackr.backend.reminder.dto.UpdateReminderRequest;
import com.jobtrackr.backend.reminder.model.Reminder;

@Component
public class ReminderMapper {

        public Reminder toDocument(
                        CreateReminderRequest request) {

                Reminder reminder = new Reminder();

                reminder.setApplicationId(
                                request.getApplicationId());
                reminder.setType(request.getType());
                reminder.setScheduledAt(
                                request.getScheduledAt());
                reminder.setChannels(
                                new HashSet<>(request.getChannels()));
                reminder.setMessage(request.getMessage());

                return reminder;
        }

        public void updateDocument(
                        UpdateReminderRequest request,
                        Reminder reminder) {

                reminder.setType(request.getType());
                reminder.setScheduledAt(
                                request.getScheduledAt());
                reminder.setChannels(
                                new HashSet<>(request.getChannels()));
                reminder.setMessage(request.getMessage());
        }

        public ReminderResponse toResponse(
                        Reminder reminder) {

                ReminderResponse response = new ReminderResponse();

                response.setId(reminder.getId());
                response.setApplicationId(
                                reminder.getApplicationId());
                response.setType(reminder.getType());
                response.setScheduledAt(
                                reminder.getScheduledAt());

                if (reminder.getChannels() == null) {
                        response.setChannels(new HashSet<>());
                } else {
                        response.setChannels(
                                        new HashSet<>(
                                                        reminder.getChannels()));
                }

                response.setStatus(reminder.getStatus());
                response.setMessage(reminder.getMessage());
                response.setAttempts(
                                reminder.getAttempts());
                response.setCreatedAt(
                                reminder.getCreatedAt());
                response.setUpdatedAt(
                                reminder.getUpdatedAt());
                response.setProcessingStartedAt(
                                reminder.getProcessingStartedAt());
                return response;
        }
}