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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileReader {
    private static Logger logger = LoggerFactory.getLogger(CSVFileReader.class);

    /** 
     * @return Map<Integer, ByteBuffer>
     * @throws IOException
     */
    public Map<Integer, ByteBuffer> readCSVFile() throws IOException {
        Map<Integer, ByteBuffer> recordsFromFile = null;

        try(RandomAccessFile inputFile = new RandomAccessFile(inputFileInformation.getFilePath().toString(), "r")) {
            FileChannel fcInputFile = inputFile.getChannel();
        
            byte[] recordSeparators = this.recordDescriptor.getRecordSeparator();
        
            if(recordSeparators[1] != 0x00) {
                recordsFromFile = processTwoCharRecordSeparator(fcInputFile);
            } else {
            
            }
        }
        
        return recordsFromFile;
    }

    
    /** 
     * @param fcInputFile
     * @param bufferSize
     * @return Map<Integer, ByteBuffer>
     */
    private Map<Integer, ByteBuffer> processTwoCharRecordSeparator(FileChannel fcInputFile) {

        Map<Integer, ByteBuffer> returnValue = new HashMap<>();

        long bytesRead = 0;
        ByteBuffer recordBuffer;
        byte fromFile = 0;
        int recordStart = 0;
        int recordEnd = 0;
        int recordNumber = 0;
        boolean headersHandled = false;
        
        try {
            MappedByteBuffer mbb = fcInputFile.map(MapMode.READ_ONLY, 0, fcInputFile.size());
            
            while(mbb.hasRemaining()) {
                fromFile = mbb.get();
                if(fromFile == '\r') {
                    byte lf = mbb.get();
                    if(lf == '\n') {
                        recordEnd = mbb.position() - 2;
                        int recordLength = recordEnd - recordStart;
                        bytesRead += recordLength; // these are useful bytes.. without record separators
                        // If header row is present, we want to ignore it.
                        if(this.inputFileInformation.isHeaderPresent() && !headersHandled) { 
                            headersHandled = true;
                            recordStart = mbb.position();
                            continue;
                        }
                        byte[] temp = new byte[recordLength];
                        mbb.position(recordStart);
                        mbb.get(temp, 0, recordLength);
                        recordBuffer = ByteBuffer.wrap(temp);
                        returnValue.put(recordNumber++, recordBuffer);
                        mbb.position(recordStart + recordLength + 2);
                        recordStart = mbb.position();
                    } // else... nothing. Until end-of-record is found, keep reading bytes... 
                }
            }
        } catch(BufferUnderflowException bue) { 
            logger.info("The number of bytes read: {}.", bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
}
