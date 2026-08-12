package com.jobtrackr.backend.email.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.common.exception.ResourceNotFoundException;
import com.jobtrackr.backend.email.dto.EmailDeliveryResponse;
import com.jobtrackr.backend.email.mapper.EmailDeliveryMapper;
import com.jobtrackr.backend.email.repository.EmailDeliveryRepository;
import com.jobtrackr.backend.reminder.repository.ReminderRepository;
import com.jobtrackr.backend.user.service.CurrentUserService;

@Service
public class EmailDeliveryQueryService {

    private final EmailDeliveryRepository deliveryRepository;
    private final ReminderRepository reminderRepository;
    private final EmailDeliveryMapper deliveryMapper;
    private final CurrentUserService currentUserService;

    public EmailDeliveryQueryService(
            EmailDeliveryRepository deliveryRepository,
            ReminderRepository reminderRepository,
            EmailDeliveryMapper deliveryMapper,
            CurrentUserService currentUserService) {

        this.deliveryRepository =
                deliveryRepository;

        this.reminderRepository =
                reminderRepository;

        this.deliveryMapper =
                deliveryMapper;

        this.currentUserService =
                currentUserService;
    }

    public List<EmailDeliveryResponse> findAllByReminder(
            String reminderId) {

        verifyOwnedReminder(reminderId);

        return deliveryRepository
                .findAllByReminderIdOrderByAttemptNumberAsc(
                        reminderId)
                .stream()
                .map(deliveryMapper::toResponse)
                .toList();
    }

    private void verifyOwnedReminder(
            String reminderId) {

        String currentUserId =
                currentUserService
                        .getCurrentUserId();

        boolean exists =
                reminderRepository
                        .findByIdAndUserId(
                                reminderId,
                                currentUserId)
                        .isPresent();

        if (!exists) {
            throw new ResourceNotFoundException(
                    "Reminder not found with id: "
                            + reminderId);
        }
    }
}