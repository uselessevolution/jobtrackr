package com.jobtrackr.backend.common.exception;

public class InvalidInterviewStateException
        extends RuntimeException {

    public InvalidInterviewStateException(String message) {
        super(message);
    }
}