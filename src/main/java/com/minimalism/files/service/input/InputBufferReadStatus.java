package com.minimalism.files.service.input;

import com.minimalism.files.domain.output.IterationStatistics;

public class InputBufferReadStatus {
    private IterationStatistics iterationStatistics; 
    private long iterationOffsetInFile;
    private long bufferOffsetInFile;
    private int numberOfBuffers;
    private int bufferNumber;
    private long iterationStartTime;
    private long parsingEndTime;
    private long publishingEndTime;
    private long iterationEndTime;
    private byte[] unprocessedPreamble;
    private byte[] unprocessedPostamble;
    boolean error;
    Throwable e;

    public InputBufferReadStatus() {
        this.iterationStatistics = new IterationStatistics();
    }

    public InputBufferReadStatus(long threadId, String threadName, int numberOfBuffers,
                    int iteration, long iterationOffsetInFile, long bufferOffsetInFile, int bufferSize) {
        this();
        this.iterationStatistics.setWorkerId(threadId);
        this.iterationStatistics.setThreadName(threadName);
        this.numberOfBuffers = numberOfBuffers;
        this.iterationStatistics.setBufferSize(bufferSize);
        
        this.iterationStatistics.setIterationNumber(iteration);
        this.iterationOffsetInFile = iterationOffsetInFile;
        this.bufferOffsetInFile = bufferOffsetInFile;
        setBufferNumber();
    }
    public IterationStatistics getIterationStatistics() {
        return this.iterationStatistics;
    }

    /** 
     * @return long
     */
    public long getIterationOffsetInFile() {
        return iterationOffsetInFile;
    }
    /** 
     * @param iterationOffsetInFile
     */
    public void setIterationOffsetInFile(long iterationOffsetInFile) {
        this.iterationOffsetInFile = iterationOffsetInFile;
    }
    /** 
     * @return long
     */
    public long getBufferOffsetInFile() {
        return bufferOffsetInFile;
    }
    /** 
     * @param bufferOffsetInFile
     */
    public void setBufferOffsetInFile(long bufferOffsetInFile) {
        this.bufferOffsetInFile = bufferOffsetInFile;
    }
    /** 
     * @return long
     */
    public long getThreadId() {
        return this.iterationStatistics.getWorkerId();
    }
    /** 
     * @param threadId
     */
    public void setThreadId(long threadId) {
        this.iterationStatistics.setWorkerId(threadId);
    }
    /** 
     * @return int
     */
    public int getBufferNumber() {
        return this.bufferNumber;
    }
    public void setBufferNumber() {
        this.bufferNumber = ((int)(this.bufferOffsetInFile / this.iterationStatistics.getBufferSize())) 
                            - (this.iterationStatistics.getIterationNumber() * this.numberOfBuffers);
    }
    /** 
     * @return int
     */
    public int getBufferSize() {
        return this.iterationStatistics.getBufferSize();
    }
    /** 
     * @param bufferSize
     */
    public void setBufferSize(int bufferSize) {
        this.iterationStatistics.setBufferSize(bufferSize);
    }
    /** 
     * @return String
     */
    public String getThreadName() {
        return this.iterationStatistics.getThreadName();
    }
    /** 
     * @param threadName
     */
    public void setThreadName(String threadName) {
        this.iterationStatistics.setThreadName(threadName);
    }
    /** 
     * @return long
     */
    public long getBytesRead() {
        return this.iterationStatistics.getProcessedBytes();
    }
    /** 
     * @param bytesRead
     */
    public void setBytesRead(long bytesRead) {
        this.iterationStatistics.setProcessedBytes(bytesRead);
    }
    /** 
     * @return int
     */
    public int getRecordsRead() {
        return this.iterationStatistics.getProcessedRecords();
    }
    /** 
     * @param recordsRead
     */
    public void setRecordsRead(int recordsRead) {
        this.iterationStatistics.setProcessedRecords(recordsRead);
    }
    /** 
     * @return int
     */
    public int getIteration() {
        return this.iterationStatistics.getIterationNumber();
    }
    /** 
     * @param iteration
     */
    public void setIteration(int iteration) {
        this.iterationStatistics.setIterationNumber(iteration);
    }
    /** 
     * @return byte[]
     */
    public byte[] getUnprocessedPreamble() {
        return unprocessedPreamble;
    }
    /** 
     * @param unprocessedPreamble
     */
    public void setUnprocessedPreamble(byte[] unprocessedPreamble) {
        this.unprocessedPreamble = unprocessedPreamble;
    }
    /** 
     * @return byte[]
     */
    public byte[] getUnprocessedPostamble() {
        return unprocessedPostamble;
    }
    /** 
     * @param unprocessedPostamble
     */
    public void setUnprocessedPostamble(byte[] unprocessedPostamble) {
        this.unprocessedPostamble = unprocessedPostamble;
    }
    /** 
     * @return boolean
     */
    public boolean hasError() {
        return error;
    }
    /** 
     * @param error
     */
    public void setError(boolean error) {
        this.error = error;
    }
    /** 
     * @return Throwable
     */
    public Throwable getE() {
        return e;
    }
    /** 
     * @param e
     */
    public void setException(Throwable e) {
        this.e = e;
        this.error = true;
    }
    public long getIterationStartTime() {
        return this.iterationStartTime;
    }
    public void setIterationStartTime(long startTime) {
        this.iterationStartTime = startTime;
    }
    public void setParsingEndTime(long parsingEndTime) {
        this.parsingEndTime = parsingEndTime;
    }
    public void setPublishingEndTime(long publishingEndTime) {
        this.publishingEndTime = publishingEndTime;
    }
    public long getIterationEndTime() {
        return this.iterationEndTime;
    }
    public void setIterationEndTime(long endTime) {
        this.iterationEndTime = endTime;
        this.iterationStatistics.setParsingDuration((int)(this.parsingEndTime - this.iterationStartTime));
        this.iterationStatistics.setPublishingDuration((int)(this.publishingEndTime - this.parsingEndTime));
    }
    public long getIterationTime() {
        return this.iterationStatistics.getProcessingDuration();
    }
    /** 
     * @return String
     */
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
            "iteration: %d and processed: %d bytes into %d records, in %d ms. The byte buffer had an unprocessed preamble of: %d bytes " +
            "and an unprocessed postamble of: %d bytes", this.iterationStatistics.getThreadName(), 
            bufferNumber, this.iterationStatistics.getIterationNumber(), 
            this.iterationStatistics.getProcessedBytes(), this.iterationStatistics.getProcessedRecords(), 
            this.getIterationTime(), preBytes, postBytes);
        } else {
            returnValue = String.format("Thread with id: %d aborted with an error. The exception: %s occurred " + 
            "with a message: '", this.iterationStatistics.getWorkerId(), 
            e.getClass().getSimpleName(), e.getMessage());
        }
        return returnValue;
    }
}
