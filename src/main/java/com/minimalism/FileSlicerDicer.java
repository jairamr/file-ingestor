package com.minimalism;

import java.io.IOException;

import com.minimalism.files.domain.input.IngestorContext;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.exceptions.ResourceAllocationException;
import com.minimalism.files.exceptions.ServiceAbortedException;
import com.minimalism.files.service.input.Reader;
import com.minimalism.files.service.output.IntakeStatsPublisher;
import com.minimalism.shared.domain.IngestServiceSummary;
import com.minimalism.shared.exceptions.FileTypeNotSupportedException;
import com.minimalism.shared.exceptions.InvalidFileException;
import com.minimalism.shared.exceptions.NoSuchPathException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSlicerDicer 
{
    private static Logger logger = LoggerFactory.getLogger(FileSlicerDicer.class);
    /** 
     * @param args
     */
    public static void main( String[] args )
    {
        String clientName = null;
        String inputFileName = null;
        String recordDescriptorFileName = null;
        boolean headersPresent = true;
        
        if(args.length < 3) {
            System.console().printf("usage: java -jar <app_name>.jar <client_name> <input_data_file_name>.csv <record_dscription_file_name>.json");
            clientName = System.console().readLine("Client Name: ");
            inputFileName = System.console().readLine("Input file Name (with file type as .csv, .bin or .txt): "); 
            recordDescriptorFileName = System.console().readLine("Record descriptor file name (.json): ");
            headersPresent = Boolean.parseBoolean(System.console().readLine("Input file contains a header row (true/false): "));
            
        } else if(args.length == 4) {
            clientName = args[0];
            inputFileName = args[1];
            recordDescriptorFileName = args[2];
            headersPresent = Boolean.parseBoolean(args[3]);
        } else if(args.length == 3) {
            clientName = args[0];
            inputFileName = args[1];
            headersPresent = Boolean.parseBoolean(args[2]);
        }
        Reader reader = null;
        IngestorContext context = null;
        long start = System.currentTimeMillis();
        try {
            if(args.length < 3) {
                context = new IngestorContext(clientName, inputFileName, recordDescriptorFileName);
                reader = new Reader(context, headersPresent);
            }
            if(args.length == 3) {
                context = new IngestorContext(clientName, inputFileName);
                reader = new Reader(context, headersPresent);
            } else if(args.length == 4) {
                context = new IngestorContext(clientName, inputFileName, recordDescriptorFileName);
                reader = new Reader(context, headersPresent);
            }
            
            if(reader != null) {
                reader.read();
                IngestServiceSummary runSummary = reader.getIngestionSummary();
                IntakeStatsPublisher statsPublisher = new IntakeStatsPublisher();
                statsPublisher.saveStats(clientName, inputFileName, context.getRecordName(), runSummary);
                logger.info("Run completed with following statistics: {}", runSummary);
                logger.info("TOTAL TIME = {}", System.currentTimeMillis() - start);
            } else {
                System.console().printf("Unable to create an instance of com.minimalism.FileSlicerDicer");
                System.exit(1);
            }
        } catch (InvalidFileException | FileTypeNotSupportedException | IOException | NoSuchPathException | InterruptedException | RecordDescriptorException | ServiceAbortedException | ResourceAllocationException e) {
            Thread.currentThread().interrupt();
            logger.error("Service terminated with error due to: {}", e.getMessage());
            if(logger.isDebugEnabled()) {
                logger.debug("Service terminated with error due to: {} and a stack trace of {}", e.getMessage(), e.getStackTrace());
            }
            System.exit(1);
        }
    }
}
