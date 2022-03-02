package com.minimalism.files.service.input;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.Map;

public class ByteBufferParser {

    private int interationNumber;
    private long thisBuffersOffsetInFile;
    private final MappedByteBuffer mbb;
    final byte highOrderMarker;
    final byte lowOrderMarker;
    final int recordSeparatorSize;

    protected ByteBufferParser(MappedByteBuffer mbb, byte hiMarker, byte loMarker) {
        this.mbb = mbb;
        this.highOrderMarker = hiMarker;
        this.lowOrderMarker = loMarker;
        if(hiMarker != 0x00) {
            recordSeparatorSize = 2;
        } else {
            recordSeparatorSize = 1;
        }
    }

    public int getInterationNumber() {
        return interationNumber;
    }
    public void setInterationNumber(int interationNumber) {
        this.interationNumber = interationNumber;
    }
    public long getThisBuffersOffsetInFile() {
        return thisBuffersOffsetInFile;
    }
    public void setThisBuffersOffsetInFile(long thisBuffersOffsetInFile) {
        this.thisBuffersOffsetInFile = thisBuffersOffsetInFile;
    }

    public void parse(int recordStart, InputBufferReadStatus readStatus, Map<Integer, ByteBuffer> recordsFromFile) {
        var bytesRead = 0;
        ByteBuffer recordBuffer;
        var recordEnd = 0;
        var recordNumber = 0;
        byte fromFile = 0;
        var recordLength = 0;

        mbb.position(recordStart);

        while(this.mbb.remaining() > 0) {
            fromFile = this.mbb.get();
            if(fromFile == this.highOrderMarker) {
                byte lf = this.mbb.get();
                if(lf == this.lowOrderMarker) {
                    recordEnd = this.mbb.position() - this.recordSeparatorSize;
                    recordLength = recordEnd - recordStart;
                    bytesRead += recordLength; // these are useful bytes.. without record separators
                
                    var temp = new byte[recordLength];
                    mbb.position(recordStart);
                    mbb.get(temp, 0, recordLength);
                    recordBuffer = ByteBuffer.wrap(temp);
                    recordsFromFile.put(recordNumber++, recordBuffer);
                    
                    this.mbb.position(recordStart + recordLength + this.recordSeparatorSize);
                    recordStart = this.mbb.position();
                } // else... nothing. Until end-of-record is found, keep reading bytes... 
            }
        } 
        // if we get to the buffer's end (position == limit) and not finding the end-of-record...
        // handle residual bytes in the ByteBuffer
        // sometimes, near the end of the file, there can be a bunch of NULL values, due to variable
        // fields sizes. Since it is the last record, there are no record separators and these NULL
        // characters will remain in the file. WE CANNOT PROCESS NULL STRINGS!
        // So, we better check and drop them!
        if(this.mbb.position() == this.mbb.limit()) {
            recordEnd = this.mbb.position();
            var nullCount = 0;
            for(var x = recordStart; x < recordEnd; x++) {
                if(this.mbb.get(x) == 0x00) {
                    nullCount++;
                }
            }
            if(nullCount > 1) {
                // a sequence of null bytes indicates trailing bytes in the buffer at write time...
                recordEnd -= nullCount;
            }
        }
        recordLength = recordEnd - recordStart;
        var temp = new byte[recordLength];
        this.mbb.position(recordStart);
        this.mbb.get(temp, 0, recordLength);
        readStatus.setUnprocessedPostamble(temp);
        
        readStatus.setRecordsRead(recordsFromFile.size());
        readStatus.setBytesRead(bytesRead);
        readStatus.getIterationStatistics().setParsingEndTime(System.currentTimeMillis());
    }
    
}
