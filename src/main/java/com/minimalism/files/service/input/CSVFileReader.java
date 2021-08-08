package com.minimalism.files.service.input;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.domain.input.InputFileInformation;
import com.minimalism.files.domain.input.RecordDescriptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileReader implements IFileReader{
    private static Logger logger = LoggerFactory.getLogger(CSVFileReader.class);

    private int id;
    private InputFileInformation inputFileInformation;
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
    
    /** 
     * <p>
     * The read method runs in the context of a worker thread in an java.util.concurrent.<em>ExecutorService</em>
     * and return a Future instance containing an <em>InputBufferReadStatus</em> instance. The number of threads
     * to be used is configurable; it defaults to the number of cores availabletothe JVM. Each thread manages
     * a single MappedByteBuffer instance, which maps a section of the input file.
     * </p>
     * <p>
     * It uses a <em>MappedByteBuffer</em> to map the input file to an off-heap memory segment. The size
     * of the buffer defaults to 1 MB, which can be changed through a configuration parameter. This causes
     * each thread in the ExecutorService to iterate through the input file; after completing one iteration,
     * the MappedByteBuffer is mapped to a new region in the input file.
     * </p>
     * @param thisBatchOffsetInFile
     * @param iteration
     * @param numberOfThreads
     * @param recordDescriptor
     * @return InputBufferReadStatus
     */
    public InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, int numberOfThreads, RecordDescriptor recordDescriptor) {
        
        InputBufferReadStatus returnValue = null;
        List<Entity> records = null;
        byte[] recordSeparators = recordDescriptor.getRecordSeparator();
    
        if(recordSeparators[1] != 0x00) {
            logger.info("Record separator byte 0: {}, byte 1: {}", recordSeparators[0], recordSeparators[1]);
            returnValue = processTwoCharRecordSeparator(thisBatchOffsetInFile, iteration, numberOfThreads);
        } else {
            //recordsFromFile = processOneCharRecordSeparator(fcInputFile);
        }
        if(recordsFromFile.size() > 0) {
            logger.info("Number of records read: {}", recordsFromFile.size());
            var formatter = new InputRecordFormatter(recordDescriptor);
            records = formatter.format(recordsFromFile);
        }
        if(records != null) {
            logger.info("Got records from Formatter: {}", records.size());
            // publish the parsed records.
        }
        return returnValue;
    }
    /** 
     * <p>
     * Windows systems create CSV files with <CR><LF> combination to demarcate records, while Linix
     * system use either <CR> or <LF>. We handle both cases. The <em>processTwoCharRecordSeparator</em>
     * method handles CSV files created in Windows Systems.
     * </p>
     * <p>
     * While parsing the input records, the record separaters are filtered out fromthe input byte array.
     * The resulting byte array from eacg input record has a stream of bytes containing the fields and
     * the field separators. The <em>InputRecordFormatter</em> class handles the fields and field separators.
     * </p>
     * @param thisBatchOffsetInFile - The marker the position in the input file where the current iteration starts.
     * @param iteration - the number of the current iteration
     * @param numberOfThreads - the number of threads (and therefore, the number of buffers) being used.
     * @return InputBufferReadStatus - the out come of processing a single byte buffer.
     */
    private InputBufferReadStatus processTwoCharRecordSeparator(long thisBatchOffsetInFile, int iteration, int numberOfThreads) {

        var currentThread = Thread.currentThread();
        
        long thisBuffersOffsetInFile = thisBatchOffsetInFile + this.id * this.bufferSize;
        
        var readStatus = new InputBufferReadStatus(currentThread.getId(),
                        currentThread.getName(), numberOfThreads, iteration, thisBatchOffsetInFile, 
                        thisBuffersOffsetInFile, this.bufferSize);
        
        try(var inputFile = new RandomAccessFile(inputFileInformation.getFilePath().toString(), "rw")) {
            FileChannel fcInputFile = inputFile.getChannel(); 
            this.mbb = fcInputFile.map(MapMode.READ_WRITE, thisBuffersOffsetInFile, this.bufferSize);
            fcInputFile.close();

            processByteBuffer(thisBatchOffsetInFile, readStatus);
        } catch (IOException e) {
            logger.error("An error occurred while reading the file {} at iteration offset: {} and thread offset: {}. The system returned the message: {}", 
            this.inputFileInformation.getFilePath(), thisBatchOffsetInFile, thisBuffersOffsetInFile, e.getMessage());
            readStatus.setException(e);
        }
        return readStatus;
    }
    
    /** 
     * @param thisBatchOffsetInFile
     * @param readStatus
     */
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
        
        logger.info("Thread name: {}, iteration:{}, Offset in File: {}, Bytes read: {}",
                    Thread.currentThread().getName(), 
                    iteration, thisBuffersOffsetInFile, bytesRead);
        
        readStatus.setRecordsRead(this.recordsFromFile.size());
        readStatus.setBytesRead(bytesRead);
    }
    
    
    /** 
     * @return int
     */
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

    
    /** 
     * @param readStatus
     * @return int
     */
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

    private void setupOutput(){
        try {
            FileSystemConfigHelper.getInstance().getServiceOutputDataDefinitionDirectory();

        } catch (IOException e) {
            logger.error("Error while accessing Definition directory: {}, for service output. Defaulting to filesystem based output", e.getMessage());
        }
    }
}
