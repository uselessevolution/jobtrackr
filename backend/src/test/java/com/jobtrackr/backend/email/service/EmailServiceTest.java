package com.jobtrackr.backend.email.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

class EmailServiceTest {

    @Test
    void shouldNotSendWhenEmailIsDisabled() {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        EmailService emailService =
                new EmailService(
                        mailSender,
                        false,
                        "no-reply@jobtrackr.local");

        boolean sent =
                emailService.sendPlainText(
                        "user@example.com",
                        "Test subject",
                        "Test body");

        assertFalse(sent);

        verify(mailSender, never())
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldSendWhenEmailIsEnabled() {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        EmailService emailService =
                new EmailService(
                        mailSender,
                        true,
                        "no-reply@jobtrackr.local");

        boolean sent =
                emailService.sendPlainText(
                        "user@example.com",
                        "Test subject",
                        "Test body");

        assertTrue(sent);

        verify(mailSender)
                .send(any(SimpleMailMessage.class));
    }

    @Test
    void shouldRejectBlankRecipient() {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        EmailService emailService =
                new EmailService(
                        mailSender,
                        true,
                        "no-reply@jobtrackr.local");

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        emailService.sendPlainText(
                                " ",
                                "Test subject",
                                "Test body"));
    }

    @Test
    void shouldRejectBlankSubject() {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        EmailService emailService =
                new EmailService(
                        mailSender,
                        true,
                        "no-reply@jobtrackr.local");

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        emailService.sendPlainText(
                                "user@example.com",
                                " ",
                                "Test body"));
    }

    @Test
    void shouldRejectBlankBody() {

        JavaMailSender mailSender =
                mock(JavaMailSender.class);

        EmailService emailService =
                new EmailService(
                        mailSender,
                        true,
                        "no-reply@jobtrackr.local");

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        emailService.sendPlainText(
                                "user@example.com",
                                "Test subject",
                                " "));
    }
}