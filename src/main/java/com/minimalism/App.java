package com.minimalism;

import java.io.IOException;

import com.minimalism.files.exceptions.FileTypeNotSupportedException;
import com.minimalism.files.exceptions.InvalidFileException;
import com.minimalism.files.exceptions.NoSuchPathException;
import com.minimalism.files.service.input.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);
    /** 
     * @param args
     */
    public static void main( String[] args )
    {
        long startTime = System.currentTimeMillis();
        Reader reader;
        try {
            reader = new Reader("client_1", "_HrData_Kaggle_Hr1m.csv", true);
            long byteCount = reader.read();
            double duration = (double)(System.currentTimeMillis() - startTime);
            logger.info("Read {} bytes in {} seconds", byteCount, duration/1000);
        }catch (InvalidFileException | FileTypeNotSupportedException | IOException | NoSuchPathException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}
