package com.minimalism.files.domain.output;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class IterationStatistics {
    private int iterationNumber;
    private long workerId;
    private String threadName;
    private int bufferSize;
    private int parsingDuration;
    private int publishingDuration;
    private long processedBytes;
    private int processedRecords;
    private int validRecords;
    private int invalidRecords;
    private int missingInformationRecords;

    public long getWorkerId() {
        return workerId;
    }
    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }
    public String getThreadName() {
        return threadName;
    }
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
    public int getIterationNumber() {
        return this.iterationNumber;
    }
    public void setIterationNumber(int iterationNumber) {
        this.iterationNumber = iterationNumber;
    }
    public int getBufferSize() {
        return bufferSize;
    }
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
    public int getParsingDuration() {
        return parsingDuration;
    }
    public void setParsingDuration(int parsingDuration) {
        this.parsingDuration = parsingDuration;
    }
    public int getPublishingDuration() {
        return publishingDuration;
    }
    public void setPublishingDuration(int publishingDuration) {
        this.publishingDuration = publishingDuration;
    }
    public int getProcessingDuration() {
        return this.parsingDuration + this.publishingDuration;
    }
    public long getProcessedBytes() {
        return processedBytes;
    }
    public void setProcessedBytes(long processedBytes) {
        this.processedBytes = processedBytes;
    }
    public int getProcessedRecords() {
        return processedRecords;
    }
    public void setProcessedRecords(int processedRecords) {
        this.processedRecords = processedRecords;
    }
    public int getValidRecords() {
        return validRecords;
    }
    public void setValidRecords(int validRecords) {
        this.validRecords = validRecords;
    }
    public int getInvalidRecords() {
        return invalidRecords;
    }
    public void setInvalidRecords(int invalidRecords) {
        this.invalidRecords = invalidRecords;
    }
    public int getMissingInformationRecords() {
        return missingInformationRecords;
    }
    public void setMissingInformationRecords(int missingInformationRecords) {
        this.missingInformationRecords = missingInformationRecords;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.threadName, this.workerId, this.iterationNumber);
    }
    @Override
    public boolean equals(Object other) {
        if(other == null)
            return false;
        if(!(other instanceof IterationStatistics))
            return false;
        return other.hashCode() == this.hashCode();
    }
    @Override
    public String toString() {
        return this.asJson().toString();
    }
    public JsonObject asJson() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("iterationNumber", this.getIterationNumber())
        .add("workerId", this.getWorkerId())
        .add("threadName", this.getThreadName())
        .add("bufferSize", this.getThreadName())
        .add("parsingDuration", this.getParsingDuration())
        .add("publishingDuration", this.getParsingDuration())
        .add("processedBytes", this.getProcessedBytes())
        .add("processedRecords", this.getProcessedRecords())
        .add("validRecords",this.getValidRecords())
        .add("invalidRecords", this.getInvalidRecords())
        .add("missingInformationRecords", this.getMissingInformationRecords());

        return jsonBuilder.build();
    }
    
}
