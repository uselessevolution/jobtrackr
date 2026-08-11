package com.jobtrackr.backend.reminder.service;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.email.service.EmailService;
import com.jobtrackr.backend.reminder.model.Reminder;
import com.jobtrackr.backend.reminder.model.ReminderType;
import com.jobtrackr.backend.user.model.User;
import com.jobtrackr.backend.user.repository.UserRepository;

@Service
public class ReminderEmailService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public ReminderEmailService(
            EmailService emailService,
            UserRepository userRepository) {

        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public boolean sendReminderEmail(
            Reminder reminder) {

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

        return emailService.sendPlainText(
                user.getEmail(),
                subject,
                body);
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

        ReminderType type = reminder.getType();

        return switch (type) {

            case INTERVIEW ->
                    "You have an upcoming interview reminder in JobTrackr.";

            case FOLLOW_UP ->
                    "It is time to follow up on your job application.";

            case APPLICATION_DEADLINE ->
                    "A job application deadline is approaching.";
        };
    }
}