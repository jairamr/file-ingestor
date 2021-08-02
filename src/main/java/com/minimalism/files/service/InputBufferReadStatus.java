package com.minimalism.files.service;

public class InputBufferReadStatus { 
    private long iterationOffsetInFile;
    private long bufferOffsetInFile;
    private long threadId;
    private String threadName;
    private int numberOfBuffers;
    private int bufferNumber;
    private int bufferSize;
    private int bytesRead;
    private int recordsRead;
    private int iteration;
    private byte[] unprocessedPreamble;
    private byte[] unprocessedPostamble;
    boolean error;
    Throwable e;

    public InputBufferReadStatus() {}

    public InputBufferReadStatus(long threadId, String threadName, int numberOfBuffers,
                    int iteration, long iterationOffsetInFile, long bufferOffsetInFile, int bufferSize) {
        this.threadId = threadId;
        this.threadName = threadName;
        this.numberOfBuffers = numberOfBuffers;
        this.bufferSize = bufferSize;
        this.iteration = iteration;
        this.iterationOffsetInFile = iterationOffsetInFile;
        this.bufferOffsetInFile = bufferOffsetInFile;
        setBufferNumber();
    }

    public long getIterationOffsetInFile() {
        return iterationOffsetInFile;
    }
    public void setIterationOffsetInFile(long iterationOffsetInFile) {
        this.iterationOffsetInFile = iterationOffsetInFile;
    }
    public long getBufferOffsetInFile() {
        return bufferOffsetInFile;
    }
    public void setBufferOffsetInFile(long bufferOffsetInFile) {
        this.bufferOffsetInFile = bufferOffsetInFile;
    }
    public long getThreadId() {
        return threadId;
    }
    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }
    public int getBufferNumber() {
        return this.bufferNumber;
    }
    public void setBufferNumber() {
        this.bufferNumber = ((int)(this.bufferOffsetInFile / this.bufferSize)) - (this.iteration * this.numberOfBuffers);
    }
    public int getBufferSize() {
        return this.bufferSize;
    }
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    public String getThreadName() {
        return threadName;
    }
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
    public int getBytesRead() {
        return bytesRead;
    }
    public void setBytesRead(int bytesRead) {
        this.bytesRead = bytesRead;
    }
    public int getRecordsRead() {
        return recordsRead;
    }
    public void setRecordsRead(int recordsRead) {
        this.recordsRead = recordsRead;
    }
    public int getIteration() {
        return iteration;
    }
    public void setIteration(int iteration) {
        this.iteration = iteration;
    }
    public byte[] getUnprocessedPreamble() {
        return unprocessedPreamble;
    }
    public void setUnprocessedPreamble(byte[] unprocessedPreamble) {
        this.unprocessedPreamble = unprocessedPreamble;
    }
    public byte[] getUnprocessedPostamble() {
        return unprocessedPostamble;
    }
    public void setUnprocessedPostamble(byte[] unprocessedPostamble) {
        this.unprocessedPostamble = unprocessedPostamble;
    }
    public boolean hasError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }
    public Throwable getE() {
        return e;
    }
    public void setException(Throwable e) {
        this.e = e;
        this.error = true;
    }
    @Override
    public String toString() {
        String returnValue = null;
        var preBytes = 0;
        var postBytes = 0;
        if(this.unprocessedPostamble != null) {
            postBytes = this.unprocessedPostamble.length;
        }
        if(this.unprocessedPreamble != null) {
            preBytes = this.unprocessedPreamble.length;
        }

        if(!this.error) {
            returnValue = String.format("Thread with name: %s, processed buffer number: %d, completed processing of " +
            "iteration: %d and processed: %d bytes into %d records. The byte buffer had an unprocessed preamble of: %d bytes " +
            "and an unprocessed postamble of: %d bytes", threadName, bufferNumber, iteration, bytesRead, recordsRead, preBytes, postBytes);
        } else {
            returnValue = String.format("Thread with id: %d aborted with an error. The exception: %s occurred " + 
            "with a message: '", threadId, e.getClass().getSimpleName(), e.getMessage());
        }
        return returnValue;
    }
}
