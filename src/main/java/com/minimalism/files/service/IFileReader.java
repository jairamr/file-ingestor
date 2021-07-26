package com.minimalism.files.service;

import java.io.IOException;

import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.domain.RecordDescriptor;

public interface IFileReader {
    InputBufferReadStatus read(InputFileInformation inputFileInformation, RecordDescriptor recordDescriptor) throws IOException;  
    InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, RecordDescriptor recordDescriptor) throws IOException; 
}
