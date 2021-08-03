package com.minimalism.files.service.input;

import java.io.IOException;

import com.minimalism.files.domain.RecordDescriptor;

public interface IFileReader {
    InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, int numberOfThreads, RecordDescriptor recordDescriptor) throws IOException; 
}
