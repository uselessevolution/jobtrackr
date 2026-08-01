package com.jobtrackr.backend.application.service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.jobtrackr.backend.application.model.ApplicationStatus;

@Service
public class JobApplicationStatusService {

    private static final Set<ApplicationStatus> TERMINAL_STATUSES =
            Set.of(
                    ApplicationStatus.ACCEPTED,
                    ApplicationStatus.REJECTED,
                    ApplicationStatus.WITHDRAWN
            );

    private static final Map<
            ApplicationStatus,
            Set<ApplicationStatus>> ALLOWED_TRANSITIONS =
            createAllowedTransitions();

    public boolean isTransitionAllowed(
            ApplicationStatus currentStatus,
            ApplicationStatus newStatus) {

        if (currentStatus == null || newStatus == null) {
            return false;
        }

        if (currentStatus == newStatus) {
            return true;
        }

        return ALLOWED_TRANSITIONS
                .getOrDefault(currentStatus, Set.of())
                .contains(newStatus);
    }

    public boolean isTerminalStatus(
            ApplicationStatus status) {

        return status != null
                && TERMINAL_STATUSES.contains(status);
    }

    public Set<ApplicationStatus> getAllowedTransitions(
            ApplicationStatus currentStatus) {

        if (currentStatus == null) {
            return Set.of();
        }

        return ALLOWED_TRANSITIONS.getOrDefault(
                currentStatus,
                Set.of()
        );
    }

    private static Map<
            ApplicationStatus,
            Set<ApplicationStatus>> createAllowedTransitions() {

        Map<ApplicationStatus, Set<ApplicationStatus>> transitions =
                new EnumMap<>(ApplicationStatus.class);

        transitions.put(
                ApplicationStatus.SAVED,
                Set.of(
                        ApplicationStatus.APPLIED,
                        ApplicationStatus.WITHDRAWN
                )
        );

        transitions.put(
                ApplicationStatus.APPLIED,
                Set.of(
                        ApplicationStatus.OA_RECEIVED,
                        ApplicationStatus.PHONE_SCREEN,
                        ApplicationStatus.INTERVIEWING,
                        ApplicationStatus.REJECTED,
                        ApplicationStatus.WITHDRAWN
                )
        );

        transitions.put(
                ApplicationStatus.OA_RECEIVED,
                Set.of(
                        ApplicationStatus.PHONE_SCREEN,
                        ApplicationStatus.INTERVIEWING,
                        ApplicationStatus.REJECTED,
                        ApplicationStatus.WITHDRAWN
                )
        );

        transitions.put(
                ApplicationStatus.PHONE_SCREEN,
                Set.of(
                        ApplicationStatus.INTERVIEWING,
                        ApplicationStatus.REJECTED,
                        ApplicationStatus.WITHDRAWN
                )
        );

        transitions.put(
                ApplicationStatus.INTERVIEWING,
                Set.of(
                        ApplicationStatus.OFFER,
                        ApplicationStatus.REJECTED,
                        ApplicationStatus.WITHDRAWN
                )
        );

        transitions.put(
                ApplicationStatus.OFFER,
                Set.of(
                        ApplicationStatus.ACCEPTED,
                        ApplicationStatus.REJECTED,
                        ApplicationStatus.WITHDRAWN
                )
        );

        transitions.put(
                ApplicationStatus.ACCEPTED,
                Set.of()
        );

        transitions.put(
                ApplicationStatus.REJECTED,
                Set.of()
        );

        transitions.put(
                ApplicationStatus.WITHDRAWN,
                Set.of()
        );

        return Map.copyOf(transitions);
    }
}