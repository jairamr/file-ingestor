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

import com.minimalism.common.AllEnums.OutputDestinations;
import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.domain.input.ServiceContext;
import com.minimalism.files.exceptions.NoSuchPathException;
import com.minimalism.files.service.output.kafka.BrokerConfiguration;
import com.minimalism.files.service.output.kafka.BrokerConfigurationReader;
import com.minimalism.files.service.output.kafka.Publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileReader implements IFileReader{
    private static Logger logger = LoggerFactory.getLogger(CSVFileReader.class);

    private int id;
    private ServiceContext serviceContext;
    private BrokerConfiguration brokerConfiguration;
    private int bufferSize;
    private int iteration;
    private Map<Integer, ByteBuffer> recordsFromFile;
    private MappedByteBuffer mbb;

    public CSVFileReader(int workerId, ServiceContext context, int bufferSize) throws NoSuchPathException {
        this.id = workerId;
        this.serviceContext = context;
        this.bufferSize = bufferSize;
        this.recordsFromFile = new HashMap<>();
        setupOutput();
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
    public InputBufferReadStatus read(long thisBatchOffsetInFile, int iteration, int numberOfThreads) {
        
        InputBufferReadStatus returnValue = null;
        List<Entity> records = null;
        byte[] recordSeparators = this.serviceContext.getRecordDescriptor().getRecordSeparator();
    
        if(recordSeparators[1] != 0x00) {
            returnValue = processTwoCharRecordSeparator(thisBatchOffsetInFile, iteration, numberOfThreads);
        } else {
            //recordsFromFile = processOneCharRecordSeparator(fcInputFile);
        }
        if(recordsFromFile.size() > 0) {
            logger.info("Number of records read from file: {}", recordsFromFile.size());
            var formatter = new InputRecordFormatter(this.serviceContext.getRecordDescriptor());
            records = formatter.format(recordsFromFile);
        }
        if(records != null) {
            // publish the parsed records.
            long start = System.currentTimeMillis();
            publishRecords(records);
            logger.info("For iteration:{}, CSVFileReader took: {} ms", iteration, System.currentTimeMillis() - start);
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
        
        try(var inputFile = new RandomAccessFile(this.serviceContext.getInputFileInformation().getFilePath().toString(), "rw")) {
            FileChannel fcInputFile = inputFile.getChannel(); 
            this.mbb = fcInputFile.map(MapMode.READ_WRITE, thisBuffersOffsetInFile, this.bufferSize);
            fcInputFile.close();

            processByteBuffer(thisBatchOffsetInFile, readStatus);
        } catch (Exception e) {
            logger.error("An error occurred while reading the file {} during iteration {}, at  offset: {} and thread offset: {}. The system returned the message: {}", 
            this.serviceContext.getInputFileInformation().getFilePath(), iteration, thisBatchOffsetInFile, thisBuffersOffsetInFile, e.getMessage());
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
                    this.recordsFromFile.put(recordNumber++, recordBuffer);
                    
                    this.mbb.position(recordStart + recordLength + 2);
                    recordStart = this.mbb.position();
                } // else... nothing. Until end-of-record is found, keep reading bytes... 
            }
        } 
        // if we get to the buffer's end (position == limit) and not finding the end-of-record...
        // handle residual bytes in the ByteBuffer
        // sometimes, near the end of the file, there can be a bunch of NULL values, due to variable
        // fields sizes. Since it is the last record, there are record separators and these NULL
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
        
        readStatus.setRecordsRead(this.recordsFromFile.size());
        readStatus.setBytesRead(bytesRead);
    }
    
    /** 
     * @return int
     */
    private int handleHeaders() {
        var startFrom = 0;
        if(this.serviceContext.getInputFileInformation().isHeaderPresent()) {
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

    private void setupOutput() throws NoSuchPathException{
        try {
            switch(this.serviceContext.getDestinationType()) {
                case FILE_SYSTEM:
                break;
                case JMS:
                break;
                case KAFKA:
                    BrokerConfigurationReader brokerConfigurationReader = 
                    new BrokerConfigurationReader(this.serviceContext.getClientName(), 
                    this.serviceContext.getRecordName());
                    this.brokerConfiguration = brokerConfigurationReader.getBrokerConfiguration(); 
                break;
                case RABBIT_MQ:
                break;
                case RESTFUL:
                break;
                case SQL_SERVER:
                break;
                case WEB_SOCKET:
                break;
                default:
                break;
            }
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Error while accessing Definition directory: {}, for service output. Defaulting to filesystem based output", e.getMessage());
        }
    }

    private void publishRecords(List<Entity> records) {
        if(this.serviceContext.getDestinationType() == OutputDestinations.KAFKA) {
            var kafkaPublisher = new Publisher(this.brokerConfiguration, this.serviceContext);
            try {
                kafkaPublisher.publish(records);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
        }
    }
}
