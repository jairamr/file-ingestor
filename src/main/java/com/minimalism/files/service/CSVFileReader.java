package com.minimalism.files.service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.domain.RecordDescriptor;
import com.minimalism.files.domain.records.Employee;

import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileReader implements IFileReader{
    private static Logger logger = LoggerFactory.getLogger(CSVFileReader.class);

    private int id;
    private InputFileInformation inputFileInformation;
    private long offsetInFile;
    private int bufferSize;
    private int iteration;
    private Map<Integer, ByteBuffer> recordsFromFile;
    private MappedByteBuffer mbb;

    public CSVFileReader(int workerId, InputFileInformation inputFileInformation, int bufferSize) {
        this.id = workerId;
        this.inputFileInformation = inputFileInformation;
        this.bufferSize = bufferSize;
        this.recordsFromFile = new HashMap<>();
    }

    public InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, RecordDescriptor recordDescriptor) {
        
        InputBufferReadStatus returnValue = null;
        List<Employee> records = null;
        byte[] recordSeparators = recordDescriptor.getRecordSeparator();
    
        if(recordSeparators[1] != 0x00) {
            returnValue = processTwoCharRecordSeparator(thisBatchOffsetInFile, iteration);
        } else {
            //recordsFromFile = processOneCharRecordSeparator(fcInputFile);
        }
        if(recordsFromFile.size() > 0) {
            logger.info("Number of records read: {}", recordsFromFile.size());
            InputRecordFormatter formatter = new InputRecordFormatter();
            records = formatter.format(recordsFromFile, recordDescriptor);
        }
        if(records != null) {
            logger.info("Got records from Formatter: {}", records.size());
            logger.info("Record: {}", records.get(records.size() - 1));
        }
        return returnValue;
    }

    public InputBufferReadStatus read(InputFileInformation inputFileInformation, RecordDescriptor recordDescriptor) throws IOException {
        Map<Integer, ByteBuffer> recordsFromFile = new HashMap<>();
        InputBufferReadStatus returnValue = null;
        this.inputFileInformation = inputFileInformation;

        try(RandomAccessFile inputFile = new RandomAccessFile(inputFileInformation.getFilePath().toString(), "rw")) {
            FileChannel fcInputFile = inputFile.getChannel();
        
            byte[] recordSeparators = recordDescriptor.getRecordSeparator();
        
            if(recordSeparators[1] != 0x00) {
                returnValue = processTwoCharRecordSeparator(fcInputFile, recordsFromFile);
            } else {
                //recordsFromFile = processOneCharRecordSeparator(fcInputFile);
            }
            // if(recordsFromFile.size() > 0) {
            //     logger.info("Number of records read: {}", recordsFromFile.size());
            //     InputRecordFormatter formatter = new InputRecordFormatter();
            //     formatter.format(recordsFromFile, recordDescriptor);
            // }
        }
        
        return returnValue;
    }

    private InputBufferReadStatus processTwoCharRecordSeparator(long thisBatchOffsetInFile, int iteration) {

        var currentThread = Thread.currentThread();
        
        InputBufferReadStatus readStatus = new InputBufferReadStatus(currentThread.getId(), 
        iteration, this.offsetInFile, this.bufferSize);
        long thisBuffersOffsetInFile = thisBatchOffsetInFile + this.id * this.bufferSize;
        
        try(RandomAccessFile inputFile = new RandomAccessFile(inputFileInformation.getFilePath().toString(), "rw")) {
            FileChannel fcInputFile = inputFile.getChannel(); 
            this.mbb = fcInputFile.map(MapMode.READ_WRITE, thisBuffersOffsetInFile, this.bufferSize);
            fcInputFile.close();

            processByteBuffer(thisBatchOffsetInFile, readStatus);
        } catch (IOException e) {
            logger.error("An error occurred while reading the file {}. The system returned the message: {}", 
            this.inputFileInformation.getFilePath(), e.getMessage());
            readStatus.setException(e);
        }
        return readStatus;
    }

    private void processByteBuffer(long thisBatchOffsetInFile, InputBufferReadStatus readStatus) {

        long thisBuffersOffsetInFile = thisBatchOffsetInFile + this.id * this.bufferSize;
        var bytesRead = 0;
        ByteBuffer recordBuffer;
        var recordStart = 0;
        var recordEnd = 0;
        var recordNumber = 0;
        byte fromFile = 0;
        var recordLength = 0;

        if(iteration == 0 && thisBuffersOffsetInFile == 0) {
            // file may contain headers
            recordStart = handleHeaders();
        }
        if(iteration != 0 || thisBuffersOffsetInFile != 0) {
            // records could be split due to slicing...
            recordStart = handlePreamble(readStatus);
        }
        var firstRec = true;
        while(this.mbb.remaining() > 0) {
            fromFile = this.mbb.get();
            if(fromFile == '\r') {
                byte lf = this.mbb.get();
                if(lf == '\n') {
                    recordEnd = this.mbb.position() - 2;
                    recordLength = recordEnd - recordStart;
                    bytesRead += recordLength; // these are useful bytes.. without record separators
                
                    var temp = new byte[recordLength];
                    mbb.position(recordStart);
                    mbb.get(temp, 0, recordLength);
                    recordBuffer = ByteBuffer.wrap(temp);
                    if(firstRec) {
                        logger.info("first rec from mbb: {}", new String(recordBuffer.array()));
                        firstRec = false;
                    }
                    this.recordsFromFile.put(recordNumber++, recordBuffer);
                    
                    this.mbb.position(recordStart + recordLength + 2);
                    recordStart = this.mbb.position();
                } // else... nothing. Until end-of-record is found, keep reading bytes... 
            }
        } 
        // if we get to the buffer's end (position == limit) and not finding the end-of-record...
        // handle residual bytes in the ByteBuffer
        recordEnd = this.mbb.position();
        recordLength = recordEnd - recordStart;
        var temp = new byte[recordLength];
        this.mbb.position(recordStart);
        this.mbb.get(temp, 0, recordLength);
        readStatus.setUnprocessedPostamble(temp);
        
        logger.info("File: {}, Thread name: {}, iteration:{}, Offset in File: {}, Bytes read: {}",
                    inputFileInformation.getFilePath(), Thread.currentThread().getName(), iteration, this.offsetInFile, bytesRead);
        readStatus.setRecordsRead(this.recordsFromFile.size());
        readStatus.setBytesRead(bytesRead);
    }
    
    /** 
     * @param fcInputFile
     * @param recordsFromFile
     * @return InputBufferReadStatus
     */
    private InputBufferReadStatus processTwoCharRecordSeparator(FileChannel fcInputFile, 
                                                    Map<Integer, ByteBuffer> recordsFromFile) {

        int bytesRead = 0;
        ByteBuffer recordBuffer;
        byte fromFile = 0;
        int recordStart = 0;
        int recordEnd = 0;
        int recordNumber = 0;

        Thread currentThread = Thread.currentThread();
        int recordLength = 0;
        
        InputBufferReadStatus readStatus = new InputBufferReadStatus(currentThread.getId(), 
        iteration, this.offsetInFile, this.bufferSize);
        
        try {
            this.mbb = fcInputFile.map(MapMode.READ_ONLY, this.offsetInFile, this.bufferSize);
            if(iteration == 0 && offsetInFile == 0) {
                // file may contain headers
                recordStart = handleHeaders();
            }
            if(iteration != 0 || offsetInFile != 0) {
                // records could be split due to slicing...
                handlePreamble(readStatus);
            }
            while(mbb.hasRemaining()) {
                fromFile = this.mbb.get();
                if(fromFile == '\r') {
                    byte lf = this.mbb.get();
                    if(lf == '\n') {
                        recordEnd = this.mbb.position() - 2;
                        recordLength = recordEnd - recordStart;
                        bytesRead += recordLength; // these are useful bytes.. without record separators
                       
                        var temp = new byte[recordLength];
                        this.mbb.position(recordStart);
                        this.mbb.get(temp, 0, recordLength);
                        recordBuffer = ByteBuffer.wrap(temp);
                        recordsFromFile.put(recordNumber++, recordBuffer);
                        this.mbb.position(recordStart + recordLength + 2);
                        recordStart = this.mbb.position();
                    } // else... nothing. Until end-of-record is found, keep reading bytes... 
                }
            }
        } catch(BufferUnderflowException bue) { 
            // if we get to the buffer's end (position == limit) and not finding the end-of-record...
            // handle residual bytes in the ByteBuffer
            recordEnd = this.mbb.position();
            recordLength = recordEnd - recordStart;
            var temp = new byte[recordLength];
            this.mbb.position(recordStart);
            this.mbb.get(temp, 0, recordLength);
            readStatus.setUnprocessedPostamble(temp);
        } catch (IOException e) {
            logger.error("An error occurred while reading the file {}. The system returned the message: {}", 
            this.inputFileInformation.getFilePath(), e.getMessage());
            readStatus.setException(e);
        }
        logger.info("File: {}, Thread name: {}, iteration:{}, Offset in File: {}, Bytes read: {}",
                    inputFileInformation.getFilePath(), currentThread.getName(), iteration, this.offsetInFile, bytesRead);
        readStatus.setRecordsRead(recordsFromFile.size());
        readStatus.setBytesRead(bytesRead);
        return readStatus;
    }

    private int handleHeaders() {
        var startFrom = 0;
        if(inputFileInformation.isHeaderPresent()) {
            while(this.mbb.remaining() > 0) {
                byte fromFile = this.mbb.get();
                if(fromFile == '\r') {
                    byte lf = mbb.get();
                    if(lf == '\n') {
                        startFrom = this.mbb.position();
                        break;
                    }
                }
            }
        }
        return startFrom; 
    }

    private int handlePreamble(InputBufferReadStatus readStatus) {
        // we treat all first records as incomplete since slicing can cause breaks at any point.
        var recordStart = 0;
        while(this.mbb.remaining() > 0) {
            byte fromFile = this.mbb.get();
            if(fromFile == '\r') {
                byte lf = this.mbb.get();
                if(lf == '\n') {
                    var recordEnd = this.mbb.position() - 2;
                    var recordLength = recordEnd - 0;
                    var temp = new byte[recordLength];
                    this.mbb.position(0);
                    this.mbb.get(temp, 0, recordLength);
                    readStatus.setUnprocessedPreamble(temp);
                    this.mbb.position(0 + recordLength + 2);
                    recordStart = this.mbb.position();
                    break;
                } 
            }
        }
        return recordStart;
    }
}
