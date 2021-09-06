package com.minimalism.files.service.input;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.minimalism.shared.common.AllEnums.DataSources;
import com.minimalism.shared.common.AllEnums.FileTypes;
import com.minimalism.shared.exceptions.FileTypeNotSupportedException;
import com.minimalism.shared.exceptions.InvalidFileException;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.BrokerConfiguration;
import com.minimalism.shared.service.BrokerConfigurationReader;
import com.minimalism.files.domain.entities.InputEntity;
import com.minimalism.files.domain.entities.ResidualBufferBytesHandler;
import com.minimalism.files.domain.input.InputFileInformation;
import com.minimalism.files.domain.input.IngestorContext;
import com.minimalism.files.domain.input.SlicerConfigurationInformation;
import com.minimalism.files.domain.output.IngestServiceSummary;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.exceptions.ServiceAbortedException;
import com.minimalism.files.service.output.kafka.Publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The <em>Reader</em> class in the primary service class for the slice-and-dice utility. This Service Class 
 * supports three types of input files:
 * <ol>
 *  <li> CSV or Comma Separated Values
 *  <li> DAT or Binary file
 *  <li> TXT or Text file
 * </ol>
 * The <em>Reader</em> class uses defined configuration to split the input file into sections. 
 * Each section is mapped to a <em>MappedByteBuffer</em> and each of these buffers is processed in a 
 * separate thread. 
 * 
 */
public class Reader {
    private static Logger logger = LoggerFactory.getLogger(Reader.class);
    
    IngestorContext serviceContext;
    SlicerConfigurationInformation slicerConfguration;
    private byte[] leftOversFromPreviousIteration;
    BrokerConfiguration brokerConfiguration;
    IngestServiceSummary ingestionSummary;

    public Reader(IngestorContext context, boolean headerPresent) throws NoSuchPathException, IOException {
        serviceContext = context;
        this.serviceContext.getInputFileInformation().setHeaderPresent(headerPresent);
        var slicer = new Slicer(this.serviceContext.getInputFileInformation());
        this.slicerConfguration = slicer.sliceFile();
        ingestionSummary = new IngestServiceSummary();

        setupOutput();
    }

    public Reader(String clientName, String fileName, boolean headerPresent) throws InvalidFileException, FileTypeNotSupportedException, IOException, NoSuchPathException, RecordDescriptorException {
        
        // We expect the filename is the input file that must be processed. It must have a file name and
        // an extension
        this.serviceContext = new IngestorContext(clientName, fileName);
        this.serviceContext.getInputFileInformation().setHeaderPresent(headerPresent);

        var slicer = new Slicer(this.serviceContext.getInputFileInformation());
        this.slicerConfguration = slicer.sliceFile();

        setupOutput();
    }

    public Reader(String clientName, String path, String fileName, boolean headerPresent) throws FileTypeNotSupportedException, NoSuchPathException, InvalidFileException, IOException, RecordDescriptorException {
        this(clientName, Paths.get(path).resolve(fileName).toString(), headerPresent);
    }
    
    /** 
     * @return InputFileInformation
     */
    public InputFileInformation getFileInformation() {
        return this.serviceContext.getInputFileInformation();
    }
    public IngestServiceSummary getIngestionSummary() {
        return this.ingestionSummary;
    }
    /** 
     * @return long
     * @throws IOException
     * @throws FileTypeNotSupportedException
     * @throws InterruptedException
     * @throws NoSuchPathException
     * @throws ServiceAbortedException
     */
    public void read() throws IOException, FileTypeNotSupportedException, InterruptedException, NoSuchPathException, ServiceAbortedException {
        logger.info("Reading input file: {}", this.serviceContext.getInputFileInformation().getFilePath());

        if(this.serviceContext.getInputFileInformation().getFileType() == FileTypes.CSV) {
            if(this.serviceContext.getOperatingMode().equalsIgnoreCase("single")) {
                processFile(FileTypes.CSV);
            } else {
                sliceAndProcessFile(FileTypes.CSV);
            }
        } else if(this.serviceContext.getInputFileInformation().getFileType() == FileTypes.BIN) {
            // process binary data file
        } else if(this.serviceContext.getInputFileInformation().getFileType() ==FileTypes.TEXT) {
            // process text file
        } else {
            throw new FileTypeNotSupportedException(String.format("The input file type: %s is not supported. This utility works with comma-separated, binary and text files only.", 
            this.serviceContext.getInputFileInformation().getFileType().name()));
        }
    }

    
    /** 
     * @param fileType
     * @throws IOException
     * @throws NoSuchPathException
     * @throws ServiceAbortedException
     */
    private void processFile(FileTypes fileType) throws IOException, NoSuchPathException, ServiceAbortedException {
        int bufferSize = slicerConfguration.getThreadReadBufferSize();
        int numberOfBuffers = slicerConfguration.getNumberOfThreads();
        long thisBatchOffsetInFile = 0;

        ExecutorService inputBufferReaderService = null;

        try { 
            inputBufferReaderService = Executors.newSingleThreadExecutor(); // newFixedThreadPool(numberOfBuffers);
            Callable<InputBufferReadStatus> worker = null;
                
            for(var iteration = 0; iteration < slicerConfguration.getIterationCount(); iteration++) {
                thisBatchOffsetInFile = iteration * (long)bufferSize * numberOfBuffers;
                if(iteration == 0) {
                    worker = prepareWorker(fileType, thisBatchOffsetInFile, iteration);
                } else {
                    resetWorker(worker, thisBatchOffsetInFile, iteration);
                }
                Future<InputBufferReadStatus> iterationResult = inputBufferReaderService.submit(worker);
                //process the outcome of each iteration
                // actions are -> 
                // 1. process residual bytes in each buffer
                // 2. Split valid Records from invalid records
                // 3. Stream records to destination (as per configuration)
                // 4. update update iteration statistics
                try {
                    InputBufferReadStatus ibrs = iterationResult.get();
                    if(iterationResult.isDone()) {
                        logger.info((ibrs.toString()));
                        long start = System.currentTimeMillis();
                        if(iterationResult != null) {
                            var anyThreadAborted = iterationResult.get().hasError();
                            if(!anyThreadAborted) {
                                handleResidualBytesInBuffer(ibrs);
                            } else {
                                // if thread has aborted, many records are lost! Abort this batch!
                                throw new ServiceAbortedException("One or more threads aborted with an exception.");
                            }
                        }
                        logger.info("Residual bytes handler took {} ms", System.currentTimeMillis() - start);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Thread.currentThread().interrupt();
                } 
            }
        } finally {
            if(inputBufferReaderService != null)
                shutdownAndAwaitTermination(inputBufferReaderService);
        }
    }

    /** 
     * @param fileType
     * @throws IOException
     * @throws InterruptedException
     * @throws NoSuchPathException
     * @throws ServiceAbortedException
     */
    private void sliceAndProcessFile(FileTypes fileType) throws IOException, InterruptedException, NoSuchPathException, ServiceAbortedException {
        int bufferSize = slicerConfguration.getThreadReadBufferSize();
        int numberOfBuffers = slicerConfguration.getNumberOfThreads();
        long thisBatchOffsetInFile = 0;

        ExecutorService inputBufferReaderService = null;

        try { 
            inputBufferReaderService = Executors.newFixedThreadPool(numberOfBuffers);
            List<Callable<InputBufferReadStatus>> workers = null;
                
            for(var iteration = 0; iteration < slicerConfguration.getIterationCount(); iteration++) {
                thisBatchOffsetInFile = iteration * (long)bufferSize * numberOfBuffers;
                if(iteration == 0) {
                    workers = prepareWorkers(fileType, thisBatchOffsetInFile, iteration);
                } else {
                    resetWorkers(workers, thisBatchOffsetInFile, iteration);
                }
                List<Future<InputBufferReadStatus>> iterationResults = inputBufferReaderService.invokeAll(workers);
                //process the outcome of each iteration
                // actions are -> 
                // 1. process residual bytes in each buffer
                // 2. Split valid Records from invalid records
                // 3. Stream records to destination (as per configuration)
                // 4. update update iteration statistics
                List<InputBufferReadStatus> resultsFromWorkers = new ArrayList<>();
                iterationResults.stream().forEach(i -> {
                    try {
                        if(i.isDone()) {
                            InputBufferReadStatus ibrs = i.get();
                            logger.info((ibrs.toString()));
                            resultsFromWorkers.add(ibrs);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                
                // only when all threads have completed processing, we should be processing the 
                // residual bytes from the buffers
                long start = System.currentTimeMillis();
                if(resultsFromWorkers.size() == workers.size()) {
                    var anyThreadAborted = resultsFromWorkers.stream().anyMatch(InputBufferReadStatus::hasError);
                    if(!anyThreadAborted) {
                        handleResidualBytesInBuffers(resultsFromWorkers);
                        resultsFromWorkers.stream().forEach(rfws -> this.ingestionSummary.addStat(rfws.getIterationStatistics()));
                    } else {
                        // if thread has aborted, many records are lost! Abort this batch!
                        throw new ServiceAbortedException("One or more threads aborted with an exception.");
                    }
                } else {
                    //something seriously wrong!
                    throw new ServiceAbortedException(String.format("Expected %d results but received %d. Please check logfile and correct the situation.", 
                                                        workers.size(), resultsFromWorkers.size()));
                }
                logger.info("Residual bytes handler completed in {} ms", System.currentTimeMillis() - start);
            }
        } finally {
            if(inputBufferReaderService != null)
                shutdownAndAwaitTermination(inputBufferReaderService);
        }
    }

    private void handleResidualBytesInBuffers(List<InputBufferReadStatus> resultsFromWorkers) throws IOException {
        var residualsHandler = new ResidualBufferBytesHandler
        (this.serviceContext.getRecordDescriptor().getRecordSeparator(), 
            this.serviceContext.getInputFileInformation().getFileType());

        this.leftOversFromPreviousIteration = 
            residualsHandler.processResiduals
            (resultsFromWorkers, leftOversFromPreviousIteration);
    
        List<ByteArrayOutputStream> residualRecords = residualsHandler.getResidualRecords();
    
        // format and publish residual records
        if(!residualRecords.isEmpty()) {
            var formatter = new InputRecordFormatter(this.serviceContext.getRecordDescriptor());
            List<InputEntity> records = formatter.format(residualRecords);
            if(records != null) {
                logger.info("Got {} residual records from Formatter.", records.size());
                // publish the parsed records.
                publishRecords(records);
                resultsFromWorkers.stream().forEach(ibrs -> ibrs.setIterationEndTime(System.currentTimeMillis())); 
            }
        }
    }

    private void handleResidualBytesInBuffer(InputBufferReadStatus resultsFromWorker) throws IOException {
        var residualsHandler = new ResidualBufferBytesHandler
        (this.serviceContext.getRecordDescriptor().getRecordSeparator(), 
            this.serviceContext.getInputFileInformation().getFileType());

        this.leftOversFromPreviousIteration = 
            residualsHandler.processResiduals
            (resultsFromWorker, this.leftOversFromPreviousIteration);
    
        List<ByteArrayOutputStream> residualRecords = residualsHandler.getResidualRecords();
    
        // format and publish residual records
        if(!residualRecords.isEmpty()) {
            var formatter = new InputRecordFormatter(this.serviceContext.getRecordDescriptor());
            List<InputEntity> records = formatter.format(residualRecords);
            if(records != null) {
                logger.info("Got {} residual records from Formatter.", records.size());
                // publish the parsed records.
                publishRecords(records); 
                resultsFromWorker.setIterationEndTime(System.currentTimeMillis());
            }
        }
    }

    /** 
     * @param pool
     */
    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
          // Wait a while for existing tasks to terminate
          if (!pool.awaitTermination(60, TimeUnit.MILLISECONDS)) {
            pool.shutdownNow(); // Cancel currently executing tasks
            // Wait a while for tasks to respond to being cancelled
            if (!pool.awaitTermination(60, TimeUnit.MILLISECONDS))
                logger.error("Pool did not terminate");
          }
        } catch (InterruptedException ie) {
          // (Re-)Cancel if current thread also interrupted
          pool.shutdownNow();
          // Preserve interrupt status
          Thread.currentThread().interrupt();
        }
      }
    
    /** 
     * @param fileType
     * @param thisBatchOffsetInFile
     * @param iteration
     * @return List<Callable<InputBufferReadStatus>>
     * @throws NoSuchPathException
     */
    private List<Callable<InputBufferReadStatus>> prepareWorkers(FileTypes fileType, 
                                                    long thisBatchOffsetInFile, int iteration) throws NoSuchPathException {
        List<Callable<InputBufferReadStatus>> returnValue = new ArrayList<>();

        int bufferSize = slicerConfguration.getThreadReadBufferSize();
        int numberOfBuffers = slicerConfguration.getNumberOfThreads();

        for(int workerId = 0; workerId < numberOfBuffers; workerId++) {
            if(fileType == FileTypes.CSV) {
                IFileReader reader = new CSVFileReader(workerId, this.serviceContext, bufferSize);
                var worker = new Worker(thisBatchOffsetInFile, iteration, reader, numberOfBuffers);
                returnValue.add(worker);
            }
        }
        return returnValue;
    }

    private Callable<InputBufferReadStatus> prepareWorker(FileTypes fileType, long thisBatchOffsetInFile, int iteration) throws NoSuchPathException {
        Callable<InputBufferReadStatus> returnValue = null;

        int bufferSize = slicerConfguration.getThreadReadBufferSize();
        int numberOfBuffers = slicerConfguration.getNumberOfThreads();

        if(fileType == FileTypes.CSV) {
            IFileReader reader = new CSVFileReader(0, this.serviceContext, bufferSize);
            var worker = new Worker(thisBatchOffsetInFile, iteration, reader, numberOfBuffers);
            returnValue = worker;
        }
        return returnValue;
    }
    
    /** 
     * @param workers
     * @param offsetInFile
     * @param iteration
     */
    private void resetWorkers(List<Callable<InputBufferReadStatus>> workers, long offsetInFile, int iteration) {
        workers.stream().forEach(w -> ((Worker)w).reset(offsetInFile, iteration));
    }

    private void resetWorker(Callable<InputBufferReadStatus> worker, long offsetInFile, int iteration) {
        ((Worker)worker).reset(offsetInFile, iteration);
    }

    private void publishRecords(List<InputEntity> records) {
        if(this.serviceContext.getDestinationType() == DataSources.KAFKA) {
            var kafkaPublisher = new Publisher(this.brokerConfiguration, this.serviceContext);
            try {
                //kafkaPublisher.publish(records, true);
                kafkaPublisher.publishGenericRecord(records);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void setupOutput() throws NoSuchPathException{
        try {
            switch(this.serviceContext.getDestinationType()) {
                case ACTIVE_MQ:
                break;
                case BROKER_J:
                break;
                case RABBIT_MQ:
                break;
                case KAFKA:
                    BrokerConfigurationReader brokerConfigurationReader = 
                    new BrokerConfigurationReader(this.serviceContext.getClientName(), 
                    this.serviceContext.getRecordName());
                    this.brokerConfiguration = brokerConfigurationReader.getBrokerConfiguration(); 
                break;
                case NTFS:
                break;
                case UNIX_FS:
                break;
                case GENERIC_REST:
                break;
                case SPRING_BOOT:
                break;
                case GENERIC_WEBSOCKET:
                break;
                case SPRING_MVC:
                break;
                case ASP:
                break;
                default:
                break;
            }
        } catch (IOException | IllegalArgumentException e) {
            logger.error("Error while accessing Definition directory: {}, for service output. Defaulting to filesystem based output", e.getMessage());
        }
    }
}
