package com.jobtrackr.backend.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final boolean enabled;

    private final String fromAddress;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${jobtrackr.email.enabled:false}")
            boolean enabled,
            @Value("${jobtrackr.email.from:no-reply@jobtrackr.local}")
            String fromAddress) {

        this.mailSender = mailSender;
        this.enabled = enabled;
        this.fromAddress = fromAddress;
    }

    public boolean sendPlainText(
            String recipient,
            String subject,
            String body) {

        validateEmailContent(
                recipient,
                subject,
                body);

        if (!enabled) {
            return false;
        }

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromAddress);
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        return true;
    }

    private void validateEmailContent(
            String recipient,
            String subject,
            String body) {

        if (!StringUtils.hasText(recipient)) {
            throw new IllegalArgumentException(
                    "Email recipient is required");
        }

        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException(
                    "Email subject is required");
        }

        if (!StringUtils.hasText(body)) {
            throw new IllegalArgumentException(
                    "Email body is required");
        }
    }
}