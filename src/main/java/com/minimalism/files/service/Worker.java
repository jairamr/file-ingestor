package com.minimalism.files.service;

import java.util.concurrent.Callable;
import com.minimalism.files.domain.RecordDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker implements Callable<InputBufferReadStatus> {
    private static Logger logger = LoggerFactory.getLogger(Worker.class);

    private int numberOfThreads;
    private long thisBatchOffsetInFile;
    private int iteration;
    private IFileReader fileReader;
    private RecordDescriptor recordDescriptor;

    public Worker(long thisBatchOffsetInFile, int iteration, IFileReader fileReader, int numberOfThreads, RecordDescriptor recordDescriptor) {
        this.thisBatchOffsetInFile = thisBatchOffsetInFile;
        this.iteration = iteration;
        this.fileReader = fileReader;
        this.numberOfThreads = numberOfThreads;
        this.recordDescriptor = recordDescriptor;
    }

    @Override
    public InputBufferReadStatus call() throws Exception {
        logger.info("Thread: {} started...", Thread.currentThread().getName());
        return this.fileReader.read(this.thisBatchOffsetInFile, this.iteration, this.numberOfThreads, this.recordDescriptor);
    }

    public void reset(long offsetInFile, int iteration) {
        this.thisBatchOffsetInFile = offsetInFile;
        this.iteration = iteration;
    }
    
}
