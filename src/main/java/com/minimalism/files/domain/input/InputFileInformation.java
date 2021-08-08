package com.minimalism.files.domain.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import com.minimalism.common.AllEnums.FileTypes;

public class InputFileInformation {
    private Path directory;
    private String fileName;
    private FileTypes fileType;
    private FileTime fileTimestamp;
    private String fileExtension;
    private boolean headerPresent;
    private long fileSize;

    public InputFileInformation() {}

    public InputFileInformation(Path directory, String fileName, FileTypes fileType, 
    FileTime timestamp, String fileExtension, boolean headerPresent) {
        this.directory = directory;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileTimestamp = timestamp;
        this.fileExtension = fileExtension;
        this.headerPresent = headerPresent;
        try {
            this.fileSize = Files.size(this.getFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /** 
     * @return Path
     */
    public Path getDirectory() {
        return directory;
    }
    
    /** 
     * @param directory
     */
    public void setDirectory(Path directory) {
        this.directory = directory;
    }
    
    /** 
     * @return String
     */
    public String getFileName() {
        return fileName;
    }
    
    /** 
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /** 
     * @return FileTypes
     */
    public FileTypes getFileType() {
        return fileType;
    }
    
    /** 
     * @param fileType
     */
    public void setFileType(FileTypes fileType) {
        this.fileType = fileType;
    }
    
    /** 
     * @return FileTime
     */
    public FileTime getFileTimestamp() {
        return fileTimestamp;
    }
    
    /** 
     * @param fileTimestamp
     */
    public void setFileTimestamp(FileTime fileTimestamp) {
        this.fileTimestamp = fileTimestamp;
    }
    
    /** 
     * @return String
     */
    public String getFileExtension() {
        return fileExtension;
    }
    
    /** 
     * @param fileExtension
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
    
    /** 
     * @return boolean
     */
    public boolean isHeaderPresent() {
        return headerPresent;
    }
    
    /** 
     * @param headerPresent
     */
    public void setHeaderPresent(boolean headerPresent) {
        this.headerPresent = headerPresent;
    }
    
    /** 
     * @return Path
     */
    public Path getFilePath() {
        return this.directory.resolve(fileName);
    }
    
    /** 
     * @return long
     */
    public long getFileSize() {
        return this.fileSize;
    }
}
