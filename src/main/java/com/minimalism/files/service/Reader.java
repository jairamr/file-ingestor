package com.minimalism.files.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.HashMap;
import java.util.Map;

import com.minimalism.common.AllEnums.FileTypes;
import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.domain.RecordDescriptor;
import com.minimalism.files.domain.SlicerConfigurationInformation;
import com.minimalism.files.exceptions.FileTypeNotSupportedException;
import com.minimalism.files.exceptions.InvalidFileException;
import com.minimalism.files.exceptions.NoSuchPathException;
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
    InputFileInformation inputFileInformation;
    SlicerConfigurationInformation slicerConfguration;
    RecordDescriptor recordDescriptor;

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
                throw new FileTypeNotSupportedException(String.format("The input file: %s is not supported. Only files with types CSV, BIN or TXT will be processed", fileName.toString()));
            }
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileException(String.format("The input file named % does not have an extension, indicating the nature of the file (.csv or .dat or.txt).", fileName));
        } catch (NoSuchPathException e) {
            throw e;
        }
                
        BasicFileAttributeView basicView = Files.getFileAttributeView(fullPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            
        this.inputFileInformation = new InputFileInformation(fullPath.getParent(), fileName, 
        fileType, basicView.readAttributes().creationTime(), fileExtension, headerPresent);

        Slicer slicer = new Slicer(this.inputFileInformation);
        this.slicerConfguration = slicer.sliceFile();

        RecordDescriptorReader recordDescriptionReader = new RecordDescriptorReader();
        this.recordDescriptor = recordDescriptionReader.readDefinition(clientName, fileName);
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
     */
    public long read() throws IOException, FileTypeNotSupportedException {
        System.out.println(this.inputFileInformation.getFilePath());
        Map<Integer, ByteBuffer> recordsFromFile = null;
        long bytesRead = 0;
        if(inputFileInformation.getFileType() == FileTypes.CSV) {
            recordsFromFile = readCSVFile();
        } else if(inputFileInformation.getFileType() == FileTypes.BIN) {
            readBinaryFile();
        } else if(inputFileInformation.getFileType() ==FileTypes.TEXT) {
            readTextFile();
        } else {
            throw new FileTypeNotSupportedException(String.format("The input file type: %s is not supported. This utility works with comma-separated, binary and text files only.", inputFileInformation.getFileType().name()));
        }
        if(recordsFromFile != null) {
            bytesRead = recordsFromFile.size();
        } else {
            bytesRead = 0;
        }
        InputRecordFormatter formatter = new InputRecordFormatter();
        formatter.format(recordsFromFile, recordDescriptor);
        return bytesRead;
    }

    
    /** 
     * @return Map<Integer, ByteBuffer>
     * @throws IOException
     */
    private Map<Integer, ByteBuffer> readCSVFile() throws IOException {
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
            System.out.println(bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }
    
    private void readBinaryFile() {

    }

    private void readTextFile() {

    }
}
