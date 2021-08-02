package com.minimalism.files.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.minimalism.common.AppConfigHelper;
import com.minimalism.common.AllEnums.FileTypes;
import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.domain.RecordDescriptor;
import com.minimalism.files.domain.SlicerConfigurationInformation;
import com.minimalism.files.exceptions.FileTypeNotSupportedException;
import com.minimalism.files.exceptions.InvalidFileException;
import com.minimalism.files.exceptions.NoSuchPathException;

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
    
    InputFileInformation inputFileInformation;
    SlicerConfigurationInformation slicerConfguration;
    RecordDescriptor recordDescriptor;
    String operatingMode;

    public Reader(String clientName, String fileName, boolean headerPresent) throws InvalidFileException, FileTypeNotSupportedException, IOException, NoSuchPathException {
        
        // We expect the filename is the input file that must be processed. It must have a file name and
        // an extension
        
        FileTypes fileType = FileTypes.CSV;
        Path fullPath = null;
        String fileExtension = null;
        try{
            fileExtension = fileName.substring(fileName.lastIndexOf('.'));
            if(fileExtension.equalsIgnoreCase(".csv")){
                fileType = FileTypes.CSV;
                fullPath = FileSystemConfigHelper.getInstance().getServiceInputDataCSVDirectory(clientName).resolve(fileName);
            } else if(fileExtension.equalsIgnoreCase(".dat")) {
                fileType = FileTypes.BIN;
                fullPath = FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataBinDirectory(clientName).resolve(fileName);
            } else if(fileExtension.equalsIgnoreCase(".txt")) {
                fileType = FileTypes.TEXT;
                fullPath = FileSystemConfigHelper.getInstance().getServiceInputDataCSVDirectory(clientName).resolve(fileName);
            } else {
                throw new FileTypeNotSupportedException(String.format("The input file: %s is not supported. Only files with types CSV, BIN or TXT will be processed", fileName));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileException(String.format("The input file named % does not have an extension, indicating the nature of the file (.csv or .dat or.txt).", fileName));
        } catch (NoSuchPathException e) {
            throw e;
        }
        
        this.operatingMode = AppConfigHelper.getInstance().getServiceOperatingMode();
        BasicFileAttributeView basicView = Files.getFileAttributeView(fullPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            
        this.inputFileInformation = new InputFileInformation(fullPath.getParent(), fileName, 
        fileType, basicView.readAttributes().creationTime(), fileExtension, headerPresent);

        Slicer slicer = new Slicer(this.inputFileInformation);
        this.slicerConfguration = slicer.sliceFile();

        this.recordDescriptor = RecordDescriptorReader.readDefinition(clientName, fileName);
    }

    public Reader(String clientName, String path, String fileName, boolean headerPresent) throws FileTypeNotSupportedException, NoSuchPathException, InvalidFileException, IOException {
        this(clientName, Paths.get(path).resolve(fileName).toString(), headerPresent);
    }

    
    /** 
     * @return InputFileInformation
     */
    public InputFileInformation getFileInformation() {
        return inputFileInformation;
    }

    
    /** 
     * @return long
     * @throws IOException
     * @throws FileTypeNotSupportedException
     * @throws InterruptedException
     */
    public long read() throws IOException, FileTypeNotSupportedException, InterruptedException {
        logger.info("Reading input file: {}", this.inputFileInformation.getFilePath());

        long numberofRecordsProcessed = 0;

        if(inputFileInformation.getFileType() == FileTypes.CSV) {
            if(this.operatingMode.equalsIgnoreCase("single")) {
                processFile(FileTypes.CSV);
            } else {
                sliceAndProcessFile(FileTypes.CSV);
            }
        } else if(inputFileInformation.getFileType() == FileTypes.BIN) {
            // process binary data file
        } else if(inputFileInformation.getFileType() ==FileTypes.TEXT) {
            // process text file
        } else {
            throw new FileTypeNotSupportedException(String.format("The input file type: %s is not supported. This utility works with comma-separated, binary and text files only.", inputFileInformation.getFileType().name()));
        }
        
        return numberofRecordsProcessed;
    }

    private void processFile(FileTypes fileType) throws IOException {
        // if(fileType == FileTypes.CSV) {
        //     IFileReader reader = new CSVFileReader(0, inputFileInformation, slicerConfguration.getThreadReadBufferSize());
        //     //InputBufferReadStatus readStatus = reader.read(inputFileInformation, recordDescriptor);
        //     if(readStatus != null) {
        //         logger.info("{}", readStatus);
        //     }
        // }
    }

    private void sliceAndProcessFile(FileTypes fileType) throws IOException, InterruptedException {
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
                iterationResults.stream().forEach(i -> {
                    try {
                        logger.info((i.get().toString()));
                    } catch (InterruptedException | ExecutionException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                
            }
        } finally {
            if(inputBufferReaderService != null)
                shutdownAndAwaitTermination(inputBufferReaderService);
        }
    }

    private void processByteBufferResiduals(List<InputBufferReadStatus> iterationResults) {
        var numberOfBuffers = iterationResults.size();
        List<ByteArrayOutputStream> residualBytes = new ArrayList<>();
       
    }

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

    private List<Callable<InputBufferReadStatus>> prepareWorkers(FileTypes fileType, 
                                                    long thisBatchOffsetInFile, int iteration) {
        List<Callable<InputBufferReadStatus>> returnValue = new ArrayList<>();

        int bufferSize = slicerConfguration.getThreadReadBufferSize();
        int numberOfBuffers = slicerConfguration.getNumberOfThreads();

        for(int workerId = 0; workerId < numberOfBuffers; workerId++) {
            if(fileType == FileTypes.CSV) {
                IFileReader reader = new CSVFileReader(workerId, inputFileInformation, bufferSize);
                Worker worker = new Worker(thisBatchOffsetInFile, iteration, reader, numberOfBuffers, recordDescriptor);
                returnValue.add(worker);
            }
        }
        return returnValue;
    }

    private void resetWorkers(List<Callable<InputBufferReadStatus>> workers, long offsetInFile, int iteration) {
        workers.stream().forEach(w -> ((Worker)w).reset(offsetInFile, iteration));
    }
}
