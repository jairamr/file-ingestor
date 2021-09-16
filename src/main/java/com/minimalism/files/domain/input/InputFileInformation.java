package com.minimalism.files.domain.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

import com.minimalism.shared.common.AllEnums.FileTypes;

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
        // The path must be a directory!
        if(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS)) {
            this.directory = directory;
        } else {
            this.directory = directory.getParent();
        }
        // the filename must not contain '.'s or extension
        if(fileName.contains(".")) {
            var indexOfLastDot = fileName.lastIndexOf('.');
            var justTheName = fileName.substring(0, indexOfLastDot);
            //just in case there are more dots, replacewith underscore
            this.fileName = justTheName.replace(".","_");
        } else {
            this.fileName = fileName;
        }
        this.fileType = fileType;
        this.fileTimestamp = timestamp;
        this.setFileExtension(fileExtension);
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
        if(fileExtension.contains(".")) {
            fileExtension = fileExtension.replace(".", "");
        }
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
        return this.directory.resolve(fileName.concat(".").concat(this.fileExtension.toLowerCase()));
    }
    
    /** 
     * @return long
     */
    public long getFileSize() {
        return this.fileSize;
    }
}
