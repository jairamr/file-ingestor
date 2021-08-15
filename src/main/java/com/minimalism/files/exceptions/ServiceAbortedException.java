package com.minimalism.files.exceptions;

public class ServiceAbortedException extends Exception {
    public ServiceAbortedException(String message) {
        super(message);
    }

    public ServiceAbortedException(String message, Throwable e) {
        super(message, e);
    }
}
