package com.jobtrackr.backend.application.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jobtrackr.backend.application.model.ApplicationStatus;

class JobApplicationStatusServiceTest {

    private JobApplicationStatusService statusService;

    @BeforeEach
    void setUp() {
        statusService = new JobApplicationStatusService();
    }

    @Test
    void shouldAllowNormalForwardTransition() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.APPLIED,
                ApplicationStatus.PHONE_SCREEN
        );

        assertTrue(allowed);
    }

    @Test
    void shouldAllowSkippingOptionalRecruitmentStages() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.APPLIED,
                ApplicationStatus.INTERVIEWING
        );

        assertTrue(allowed);
    }

    @Test
    void shouldAllowRejectionFromActiveStatus() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.INTERVIEWING,
                ApplicationStatus.REJECTED
        );

        assertTrue(allowed);
    }

    @Test
    void shouldAllowWithdrawalFromSavedStatus() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.SAVED,
                ApplicationStatus.WITHDRAWN
        );

        assertTrue(allowed);
    }

    @Test
    void shouldAllowKeepingTheSameStatus() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.APPLIED,
                ApplicationStatus.APPLIED
        );

        assertTrue(allowed);
    }

    @Test
    void shouldRejectSkippingFromSavedDirectlyToOffer() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.SAVED,
                ApplicationStatus.OFFER
        );

        assertFalse(allowed);
    }

    @Test
    void shouldRejectBackwardTransition() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.INTERVIEWING,
                ApplicationStatus.APPLIED
        );

        assertFalse(allowed);
    }

    @Test
    void shouldRejectTransitionFromTerminalStatus() {

        boolean allowed = statusService.isTransitionAllowed(
                ApplicationStatus.REJECTED,
                ApplicationStatus.INTERVIEWING
        );

        assertFalse(allowed);
    }

    @Test
    void shouldRecognizeTerminalStatuses() {

        assertTrue(
                statusService.isTerminalStatus(
                        ApplicationStatus.ACCEPTED
                )
        );

        assertTrue(
                statusService.isTerminalStatus(
                        ApplicationStatus.REJECTED
                )
        );

        assertTrue(
                statusService.isTerminalStatus(
                        ApplicationStatus.WITHDRAWN
                )
        );
    }

    @Test
    void shouldRejectNullStatusTransition() {

        assertFalse(
                statusService.isTransitionAllowed(
                        null,
                        ApplicationStatus.APPLIED
                )
        );

        assertFalse(
                statusService.isTransitionAllowed(
                        ApplicationStatus.APPLIED,
                        null
                )
        );
    }
}