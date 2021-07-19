package com.minimalism;

import java.io.IOException;

import com.minimalism.files.exceptions.FileTypeNotSupportedException;
import com.minimalism.files.exceptions.InvalidFileException;
import com.minimalism.files.exceptions.NoSuchPathException;
import com.minimalism.files.service.Reader;

public class App 
{
    
    /** 
     * @param args
     */
    public static void main( String[] args )
    {
        long startTime = System.currentTimeMillis();
        Reader reader;
        try {
            reader = new Reader("client_1", "_HrData_Kaggle_Hr5m.csv", true);
            long byteCount = reader.read();
            double duration = (double)(System.currentTimeMillis() - startTime);
            System.out.println(String.format("Read %d bytes in %f seconds", byteCount, duration/1000));
        }catch (InvalidFileException | FileTypeNotSupportedException | IOException | NoSuchPathException e) {
            e.printStackTrace();
        }
    }
}
