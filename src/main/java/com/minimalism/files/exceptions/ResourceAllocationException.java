package com.minimalism.files.exceptions;

public class ResourceAllocationException extends Exception {
    public ResourceAllocationException(String message) {
        super(message);
    }

    public ResourceAllocationException(String message, Throwable e) {
        super(message, e);
    }
}
