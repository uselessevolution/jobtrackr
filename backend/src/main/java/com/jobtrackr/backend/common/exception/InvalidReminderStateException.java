package com.jobtrackr.backend.common.exception;

public class InvalidReminderStateException
        extends RuntimeException {

    public InvalidReminderStateException(String message) {
        super(message);
    }
}