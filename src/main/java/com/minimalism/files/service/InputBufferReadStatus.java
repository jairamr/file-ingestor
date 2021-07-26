package com.minimalism.files.service;

public class InputBufferReadStatus { 
    private long offsetInFile;
    private long threadId;
    private int threadNumber;
    private String threadName;
    private int bufferSize;
    private int bytesRead;
    private int recordsRead;
    private int iteration;
    private byte[] unprocessedPreamble;
    private byte[] unprocessedPostamble;
    boolean error;
    Throwable e;

    public InputBufferReadStatus() {}

    public InputBufferReadStatus(long threadId, int iteration, long offsetInFile, int bufferrSize) {
        this.threadId = threadId;
        this.bufferSize = bufferrSize;
        setIteration(iteration);
        setOffsetInFile(offsetInFile);
        setThreadName();
    }

    public long getOffsetInFile() {
        return offsetInFile;
    }
    public void setOffsetInFile(long offsetInFile) {
        this.offsetInFile = offsetInFile;
        setThreadNumber(); 
    }
    public long getThreadId() {
        return threadId;
    }
    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }
    public int getThreadNumber() {
        return threadNumber;
    }
    public void setThreadNumber() {
        this.threadNumber = (int) this.offsetInFile / this.bufferSize;
    }
    public String getThreadName() {
        return threadName;
    }
    public void setThreadName() {
        this.threadName = "Thread - ".concat(String.valueOf(this.getThreadNumber()));
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
        int preBytes = 0;
        int postBytes = 0;
        if(this.unprocessedPostamble != null) {
            postBytes = this.unprocessedPostamble.length;
        }
        if(this.unprocessedPreamble != null) {
            preBytes = this.unprocessedPreamble.length;
        }

        if(!this.error) {
            returnValue = String.format("Thread with id: %d, number: %d, name: %s, completed processing of " +
            "iteration: %d, processed: %d bytes into %d records. The byte buffer had an unprocessed preamble of: %d bytes " +
            "and an unprocessed postamble of: %d bytes", threadId, threadNumber, threadName, iteration, bytesRead, recordsRead, preBytes, postBytes);
        } else {
            returnValue = String.format("Thread with id: %d aborted with an error. The exception: %s occurred " + 
            "with a message: '", threadId, e.getClass().getSimpleName(), e.getMessage());
        }
        return returnValue;
    }
}
