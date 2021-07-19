package com.minimalism.files.exceptions;

public class FileTypeNotSupportedException extends Exception{
    public FileTypeNotSupportedException(String message) {
        super(message);
    }

    public FileTypeNotSupportedException(String message, Throwable exception) {
        super(message, exception);
    }
}
