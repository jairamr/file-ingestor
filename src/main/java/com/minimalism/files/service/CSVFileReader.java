package com.minimalism.files.service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.domain.RecordDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileReader implements IFileReader{
    private static Logger logger = LoggerFactory.getLogger(CSVFileReader.class);

    private int id;
    private InputFileInformation inputFileInformation;
    private long offsetInFile;
    private int bufferSize;
    private int iteration;

    public CSVFileReader(int workerId, InputFileInformation inputFileInformation, int bufferSize) {
        this.id = workerId;
        this.inputFileInformation = inputFileInformation;
        this.bufferSize = bufferSize;
    }

    public InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, RecordDescriptor recordDescriptor) {
        Map<Integer, ByteBuffer> recordsFromFile = new HashMap<>();
        InputBufferReadStatus returnValue = null;

        byte[] recordSeparators = recordDescriptor.getRecordSeparator();
    
        if(recordSeparators[1] != 0x00) {
            returnValue = processTwoCharRecordSeparator(thisBatchOffsetInFile, iteration, recordsFromFile);
        } else {
            //recordsFromFile = processOneCharRecordSeparator(fcInputFile);
        }
        if(recordsFromFile.size() > 0) {
            logger.info("Number of records read: {}", recordsFromFile.size());
            InputRecordFormatter formatter = new InputRecordFormatter();
            formatter.format(recordsFromFile, recordDescriptor);
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

    private InputBufferReadStatus processTwoCharRecordSeparator(long thisBatchOffsetInFile, int iteration, 
                                                    Map<Integer, ByteBuffer> recordsFromFile) {

        int bytesRead = 0;
        ByteBuffer recordBuffer;
        byte fromFile = 0;
        int recordStart = 0;
        int recordEnd = 0;
        int recordNumber = 0;
        boolean printedRawRecord = false;

        MappedByteBuffer mbb = null;
        Thread currentThread = Thread.currentThread();
        int recordLength = 0;
        
        InputBufferReadStatus readStatus = new InputBufferReadStatus(currentThread.getId(), 
        iteration, this.offsetInFile, this.bufferSize);
        long thisBuffersOffsetInFile = thisBatchOffsetInFile + this.id * this.bufferSize;
        
        try(RandomAccessFile inputFile = new RandomAccessFile(inputFileInformation.getFilePath().toString(), "rw")) {
            FileChannel fcInputFile = inputFile.getChannel(); 
            mbb = fcInputFile.map(MapMode.READ_WRITE, thisBuffersOffsetInFile, this.bufferSize);
            fcInputFile.close();
            if(iteration == 0 && thisBuffersOffsetInFile == 0) {
                // file may contain headers
                recordStart = handleHeaders(mbb);
            }
            if(iteration != 0 || thisBuffersOffsetInFile != 0) {
                // records could be split due to slicing...
                handlePreamble(mbb, readStatus);
            }
            while(mbb.hasRemaining()) {
                fromFile = mbb.get();
                if(fromFile == '\r') {
                    byte lf = mbb.get();
                    if(lf == '\n') {
                        recordEnd = mbb.position() - 2;
                        recordLength = recordEnd - recordStart;
                        bytesRead += recordLength; // these are useful bytes.. without record separators
                       
                        byte[] temp = new byte[recordLength];
                        mbb.position(recordStart);
                        mbb.get(temp, 0, recordLength);
                        recordBuffer = ByteBuffer.wrap(temp);
                        recordsFromFile.put(recordNumber++, recordBuffer);
                        // if(!printedRawRecord) {
                        //     printRawRecord(recordBuffer);
                        //     printedRawRecord = true;
                        // }
                        mbb.position(recordStart + recordLength + 2);
                        recordStart = mbb.position();
                    } // else... nothing. Until end-of-record is found, keep reading bytes... 
                }
            }
        } catch(BufferUnderflowException bue) { 
            // if we get to the buffer's end (position == limit) and not finding the end-of-record...
            // handle residual bytes in the ByteBuffer
            recordEnd = mbb.position();
            recordLength = recordEnd - recordStart;
            byte[] temp = new byte[recordLength];
            mbb.position(recordStart);
            mbb.get(temp, 0, recordLength);
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

        MappedByteBuffer mbb = null;
        Thread currentThread = Thread.currentThread();
        int recordLength = 0;
        
        InputBufferReadStatus readStatus = new InputBufferReadStatus(currentThread.getId(), 
        iteration, this.offsetInFile, this.bufferSize);
        
        try {
            mbb = fcInputFile.map(MapMode.READ_ONLY, this.offsetInFile, this.bufferSize);
            if(iteration == 0 && offsetInFile == 0) {
                // file may contain headers
                recordStart = handleHeaders(mbb);
            }
            if(iteration != 0 || offsetInFile != 0) {
                // records could be split due to slicing...
                handlePreamble(mbb, readStatus);
            }
            while(mbb.hasRemaining()) {
                fromFile = mbb.get();
                if(fromFile == '\r') {
                    byte lf = mbb.get();
                    if(lf == '\n') {
                        recordEnd = mbb.position() - 2;
                        recordLength = recordEnd - recordStart;
                        bytesRead += recordLength; // these are useful bytes.. without record separators
                       
                        byte[] temp = new byte[recordLength];
                        mbb.position(recordStart);
                        mbb.get(temp, 0, recordLength);
                        recordBuffer = ByteBuffer.wrap(temp);
                        recordsFromFile.put(recordNumber++, recordBuffer);
                        mbb.position(recordStart + recordLength + 2);
                        recordStart = mbb.position();
                    } // else... nothing. Until end-of-record is found, keep reading bytes... 
                }
            }
        } catch(BufferUnderflowException bue) { 
            // if we get to the buffer's end (position == limit) and not finding the end-of-record...
            // handle residual bytes in the ByteBuffer
            recordEnd = mbb.position();
            recordLength = recordEnd - recordStart;
            byte[] temp = new byte[recordLength];
            mbb.position(recordStart);
            mbb.get(temp, 0, recordLength);
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

    private int handleHeaders(MappedByteBuffer mbb) {
        int startFrom = 0;
        if(inputFileInformation.isHeaderPresent()) {
            while(mbb.hasRemaining()) {
                byte fromFile = mbb.get();
                if(fromFile == '\r') {
                    byte lf = mbb.get();
                    if(lf == '\n') {
                        startFrom = mbb.position();
                        break;
                    }
                }
            }
        }
        return startFrom; 
    }

    private void handlePreamble(MappedByteBuffer mbb, InputBufferReadStatus readStatus) {
        // we treat all first records as incomplete since slicing can cause breaks at any point.

        while(mbb.hasRemaining()) {
            byte fromFile = mbb.get();
            if(fromFile == '\r') {
                byte lf = mbb.get();
                if(lf == '\n') {
                    int recordEnd = mbb.position() - 2;
                    int recordLength = recordEnd - 0;
                    
                    byte[] temp = new byte[recordLength];
                    mbb.position(0);
                    mbb.get(temp, 0, recordLength);
                    readStatus.setUnprocessedPreamble(temp);
                    mbb.position(0 + recordLength + 2);
                    break;
                } 
            }
        }
    }

    private void printRawRecord(ByteBuffer buffer) {
        logger.info("{}", buffer.array());
    }
}
