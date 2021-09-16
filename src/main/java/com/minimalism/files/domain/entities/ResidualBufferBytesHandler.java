package com.minimalism.files.domain.entities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.minimalism.shared.common.AllEnums.FileTypes;
import com.minimalism.files.service.input.InputBufferReadStatus;
/**
 * @author R Jairam Iyer
 * <p>
 * Treating the input file as a sequence of bytes speeds up the reading process,
 * but it will cause records to be split across buffer boundaries. At the end of each 
 * iteration, the last buffer may leave some residual bytes,which we call the postamble.
 * Since the record was sliced mid-way, the first buffer of the current iteration will
 * have some bytes that belong to the record from the previous iterstion. 
 * </p>
 * <p>
 * The <em>ResidualBufferBytesHandler</em> combines the residual bytes from the previous
 * iteration with residual bytes from the current iteration; the following rules apply
 * <ul>
 *  <li>The leftover bytes from the previous iteration is combined with the preamble bytes
 * of the first buffer</li>
 *  <li>The postamble bytes of the first byte is appended with the preamble bytes of the
 * nest (second) buffer
 *  <li><i>this cahining process is followed throughout the buffer list</i></li>
 *  <li>The last iteration is handled as a special case.
 * </ul>
 * </p>
 * <p>
 * The last record will not be followed up with a record separator (the file system will append an EOF).
 * Since parsing for records is based on the record separator, the parser will leave the
 * last record as residual bytes. The natural order of processing for the residual bytes
 * fromthe last buffer is to place it as the premable for the next iteration; for the 
 * last iteration however, this residual is added to the list of records.  
 * </P>
 */
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
    public byte[] 
    processResiduals(List<InputBufferReadStatus> iterationResults, 
                    byte[] leftOversFromPreviousIteration,
                    boolean lastIteration) throws IOException {
        if(this.fileType == FileTypes.CSV){
            return forCSVCRLFFiles(iterationResults, leftOversFromPreviousIteration, lastIteration);
        } else if(fileType == FileTypes.BIN) {
            return new byte[0];
        } else if(fileType == FileTypes.TEXT) {
            return new byte[0];
        } else {
            return new byte[0];
        }
    }

    public byte[] 
    processResiduals(InputBufferReadStatus iterationResult, 
                        byte[] leftOversFromPreviousIteration, 
                        boolean lastIteration) throws IOException {
        if(this.fileType == FileTypes.CSV){
            return forCSVCRLFFiles(iterationResult, leftOversFromPreviousIteration, lastIteration);
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

    private byte[] 
    forCSVCRLFFiles(List<InputBufferReadStatus> iterationResults, 
                    byte[] leftOversFromPreviousIteration,
                    boolean lastIteration) throws IOException {
        var numberOfBuffers = iterationResults.size();
        byte[] leftOversForNextIteration = null;

        // handle leftovers from previous iteration (ByteBuffer 0 can have leftovers)
        // it combines with the preamble bytes from buffer0 in the current iteration
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
            var postambleAndPreambleConcats = new ByteArrayOutputStream();
            var prevBufferPostamble = iterationResults.get(i - 1).getUnprocessedPostamble();
            if(prevBufferPostamble != null) {
                postambleAndPreambleConcats.write(prevBufferPostamble);
            }
            var currBufferPreamble = iterationResults.get(i).getUnprocessedPreamble();
            if(currBufferPreamble != null) {
                postambleAndPreambleConcats.write(currBufferPreamble);
            }
            if(postambleAndPreambleConcats.size() > 0) {
                this.residualRecords.add(removeRecordSeparators(postambleAndPreambleConcats.toByteArray()));
            }
        }
        // save the postamble bytes to be processed in the next iteration
        var lastPostAmble = iterationResults.get(iterationResults.size() - 1).getUnprocessedPostamble();
        if(lastPostAmble != null) {
            if(lastIteration) {
                this.residualRecords.add(removeRecordSeparators(lastPostAmble));
            } else {
                leftOversForNextIteration = lastPostAmble;
            }
        } else {
            leftOversForNextIteration = new byte[0];
        }
        return leftOversForNextIteration;
    }

    private byte[] 
    forCSVCRLFFiles(InputBufferReadStatus iterationResult, 
                    byte[] leftOversFromPreviousIteration,
                    boolean lastIteration) throws IOException {
        
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
            if(lastIteration) {
                this.residualRecords.add(removeRecordSeparators(lastPostAmble));
            } else {
                leftOversForNextIteration = lastPostAmble;
            }
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
