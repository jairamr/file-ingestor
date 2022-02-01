package com.minimalism.files.exceptions;

public class RecordDescriptorException extends Exception{
    public RecordDescriptorException(String message) {
        super(message);
    }
    public RecordDescriptorException(String message, Throwable t) {
        super(message, t);
    } 
}
