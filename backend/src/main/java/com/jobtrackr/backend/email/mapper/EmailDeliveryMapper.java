package com.jobtrackr.backend.email.mapper;

import org.springframework.stereotype.Component;

import com.jobtrackr.backend.email.dto.EmailDeliveryResponse;
import com.jobtrackr.backend.email.model.EmailDelivery;

@Component
public class EmailDeliveryMapper {

    public EmailDeliveryResponse toResponse(
            EmailDelivery delivery) {

        EmailDeliveryResponse response =
                new EmailDeliveryResponse();

        response.setId(
                delivery.getId());

        response.setReminderId(
                delivery.getReminderId());

        response.setRecipient(
                delivery.getRecipient());

        response.setSubject(
                delivery.getSubject());

        response.setStatus(
                delivery.getStatus());

        response.setAttemptNumber(
                delivery.getAttemptNumber());

        response.setErrorMessage(
                delivery.getErrorMessage());

        response.setCreatedAt(
                delivery.getCreatedAt());

        response.setSentAt(
                delivery.getSentAt());

        return response;
    }
}