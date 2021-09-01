package com.minimalism.files.domain.entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.minimalism.shared.common.AllEnums.FileTypes;
import com.minimalism.files.service.input.InputBufferReadStatus;

public class ResidualBufferBytesHandler {
    
    private FileTypes fileType;
    private byte[] recordSeparators;
    private List<ByteArrayOutputStream> residualRecords;
    
    public ResidualBufferBytesHandler(byte[] recordSeparators, FileTypes fileType) {
        this.fileType = fileType;
        this.recordSeparators = recordSeparators;
        this.residualRecords = new ArrayList<>();
    }
    /** 
     * @param iterationResults
     * @throws IOException
     */
    public byte[] processResiduals(List<InputBufferReadStatus> iterationResults, byte[] leftOversFromPreviousIteration) throws IOException {
        if(this.fileType == FileTypes.CSV){
            return forCSVCRLFFiles(iterationResults, leftOversFromPreviousIteration);
        } else if(fileType == FileTypes.BIN) {
            return new byte[0];
        } else if(fileType == FileTypes.TEXT) {
            return new byte[0];
        } else {
            return new byte[0];
        }
    }

    public byte[] processResiduals(InputBufferReadStatus iterationResult, byte[] leftOversFromPreviousIteration) throws IOException {
        if(this.fileType == FileTypes.CSV){
            return forCSVCRLFFiles(iterationResult, leftOversFromPreviousIteration);
        } else if(fileType == FileTypes.BIN) {
            return new byte[0];
        } else if(fileType == FileTypes.TEXT) {
            return new byte[0];
        } else {
            return new byte[0];
        }
    }
    
    public List<ByteArrayOutputStream> getResidualRecords() {
        return this.residualRecords;
    }

    private byte[] forCSVCRLFFiles(List<InputBufferReadStatus> iterationResults, byte[] leftOversFromPreviousIteration) throws IOException {
        var numberOfBuffers = iterationResults.size();
        byte[] leftOversForNextIteration = null;

        // handle leftovers from previous iteration (ByteBuffer 0 can have leftovers)
        // it combines with the preamble bytes frombuffer0 in the current iteration
        // to make a valid record.
        if(leftOversFromPreviousIteration != null) {
            var leftOvers = new ByteArrayOutputStream();
            leftOvers.write(leftOversFromPreviousIteration);
            if(iterationResults.get(0).getUnprocessedPreamble() != null) {
                leftOvers.write(iterationResults.get(0).getUnprocessedPreamble());
            }
            if(leftOvers.size() > 0) {
                this.residualRecords.add(removeRecordSeparators(leftOvers.toByteArray()));
            }
        }
        for(var i = 1; i < numberOfBuffers; i++) {
            var postPreConcats = new ByteArrayOutputStream();
            var prevBufferPostamble = iterationResults.get(i - 1).getUnprocessedPostamble();
            if(prevBufferPostamble != null) {
                postPreConcats.write(prevBufferPostamble);
            }
            var currBufferPreamble = iterationResults.get(i).getUnprocessedPreamble();
            if(currBufferPreamble != null) {
                postPreConcats.write(currBufferPreamble);
            }
            if(postPreConcats.size() > 0) {
                this.residualRecords.add(removeRecordSeparators(postPreConcats.toByteArray()));
            }
        }
        // save the postamble bytes to be processed in the next iteration
        var lastPostAmble = iterationResults.get(iterationResults.size() - 1).getUnprocessedPostamble();
        if(lastPostAmble != null) {
            leftOversForNextIteration = lastPostAmble;
        } else {
            leftOversForNextIteration = new byte[0];
        }
        return leftOversForNextIteration;
    }

    private byte[] forCSVCRLFFiles(InputBufferReadStatus iterationResult, byte[] leftOversFromPreviousIteration) throws IOException {
        
        byte[] leftOversForNextIteration = null;

        // handle leftovers from previous iteration (ByteBuffer 0 can have leftovers)
        // it combines with the preamble bytes frombuffer0 in the current iteration
        // to make a valid record.
        if(leftOversFromPreviousIteration != null) {
            var leftOvers = new ByteArrayOutputStream();
            leftOvers.write(leftOversFromPreviousIteration);
            if(iterationResult.getUnprocessedPreamble() != null) {
                leftOvers.write(iterationResult.getUnprocessedPreamble());
            }
            if(leftOvers.size() > 0) {
                this.residualRecords.add(removeRecordSeparators(leftOvers.toByteArray()));
            }
        }
        
        // save the postamble bytes to be processed in the next iteration
        var lastPostAmble = iterationResult.getUnprocessedPostamble();
        if(lastPostAmble != null) {
            leftOversForNextIteration = lastPostAmble;
        } else {
            leftOversForNextIteration = new byte[0];
        }
        return leftOversForNextIteration;
    }

    private ByteArrayOutputStream removeRecordSeparators(byte[] recordData) throws IOException {
        var scrubbedRecord = new ByteArrayOutputStream();
        if(this.recordSeparators[1] != 0x00) {
            var indexOfCR = Arrays.binarySearch(recordData, this.recordSeparators[0]);
            if(indexOfCR >= 0) {
                if(recordData[indexOfCR + 1] == this.recordSeparators[1]) {
                    scrubbedRecord.write(Arrays.copyOfRange(recordData, 0, indexOfCR));
                } else {
                    // error...
                }
            } else {
                scrubbedRecord.write(recordData);
            }
        } else {
            var indexOfCRorLF = Arrays.binarySearch(recordData, this.recordSeparators[0]);
            if(indexOfCRorLF != -1) {
                scrubbedRecord.write(Arrays.copyOfRange(recordData, 0, indexOfCRorLF));
            } else {
                scrubbedRecord.write(recordData);
            }
        }
        
        return scrubbedRecord;
    }
}
