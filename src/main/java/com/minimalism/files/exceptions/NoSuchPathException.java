package com.minimalism.files.exceptions;

public class NoSuchPathException extends Exception{
    public NoSuchPathException(String message) {
        super(message);
    }

    public NoSuchPathException(String message, Throwable exception) {
        super(message, exception);
    }
}
