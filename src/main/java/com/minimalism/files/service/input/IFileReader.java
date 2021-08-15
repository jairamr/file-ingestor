package com.minimalism.files.service.input;

import java.io.IOException;

public interface IFileReader {
    InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, int numberOfThreads) throws IOException; 
}
