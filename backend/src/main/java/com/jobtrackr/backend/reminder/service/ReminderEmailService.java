package com.jobtrackr.backend.reminder.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.email.model.EmailDelivery;
import com.jobtrackr.backend.email.model.EmailDeliveryStatus;
import com.jobtrackr.backend.email.repository.EmailDeliveryRepository;
import com.jobtrackr.backend.email.service.EmailService;
import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.user.model.User;
import com.jobtrackr.backend.user.repository.UserRepository;

@Service
public class ReminderEmailService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailDeliveryRepository deliveryRepository;

    public ReminderEmailService(
            EmailService emailService,
            UserRepository userRepository,
            EmailDeliveryRepository deliveryRepository) {

        this.emailService = emailService;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public void sendReminderEmail(
            Reminder reminder) {

        boolean alreadySent =
                deliveryRepository
                        .existsByReminderIdAndStatus(
                                reminder.getId(),
                                EmailDeliveryStatus.SENT);

        if (alreadySent) {
            return;
        }

        User user = userRepository
                .findById(reminder.getUserId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "User not found for reminder: "
                                        + reminder.getId()));

        String subject =
                buildSubject(reminder);

        String body =
                buildBody(reminder);

        int attemptNumber =
                reminder.getAttempts() + 1;

        EmailDelivery delivery =
                createDelivery(
                        reminder,
                        user.getEmail(),
                        subject,
                        attemptNumber);

        deliveryRepository.save(delivery);

        try {

            boolean sent =
                    emailService.sendPlainText(
                            user.getEmail(),
                            subject,
                            body);

            if (!sent) {
                throw new IllegalStateException(
                        "Email delivery is disabled");
            }

            delivery.setStatus(
                    EmailDeliveryStatus.SENT);

            delivery.setSentAt(
                    LocalDateTime.now());

            delivery.setErrorMessage(null);

            deliveryRepository.save(delivery);

        } catch (RuntimeException exception) {

            delivery.setStatus(
                    EmailDeliveryStatus.FAILED);

            delivery.setErrorMessage(
                    buildErrorMessage(exception));

            deliveryRepository.save(delivery);

            throw exception;
        }
    }

    private EmailDelivery createDelivery(
            Reminder reminder,
            String recipient,
            String subject,
            int attemptNumber) {

        EmailDelivery delivery =
                new EmailDelivery();

        delivery.setReminderId(
                reminder.getId());

        delivery.setUserId(
                reminder.getUserId());

        delivery.setRecipient(
                recipient);

        delivery.setSubject(
                subject);

        delivery.setStatus(
                EmailDeliveryStatus.PENDING);

        delivery.setAttemptNumber(
                attemptNumber);

        delivery.setErrorMessage(null);

        delivery.setCreatedAt(
                LocalDateTime.now());

        delivery.setSentAt(null);

        return delivery;
    }

    private String buildSubject(
            Reminder reminder) {

        return switch (reminder.getType()) {

            case INTERVIEW ->
                    "JobTrackr: Interview reminder";

            case FOLLOW_UP ->
                    "JobTrackr: Follow-up reminder";

            case APPLICATION_DEADLINE ->
                    "JobTrackr: Application deadline reminder";
        };
    }

    private String buildBody(
            Reminder reminder) {

        if (reminder.getMessage() != null
                && !reminder.getMessage().isBlank()) {

            return reminder.getMessage();
        }

        return switch (reminder.getType()) {

            case INTERVIEW ->
                    "You have an upcoming interview reminder in JobTrackr.";

            case FOLLOW_UP ->
                    "It is time to follow up on your job application.";

            case APPLICATION_DEADLINE ->
                    "A job application deadline is approaching.";
        };
    }

    private String buildErrorMessage(
            RuntimeException exception) {

        String message =
                exception.getMessage();

        if (message == null
                || message.isBlank()) {

            return exception
                    .getClass()
                    .getSimpleName();
        }

        if (message.length() > 1000) {
            return message.substring(0, 1000);
        }

        return message;
    }
}