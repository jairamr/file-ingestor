package com.minimalism.files.domain.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

import com.minimalism.common.AllEnums.FileTypes;
import com.minimalism.common.AllEnums.OutputDestinations;
import com.minimalism.common.AppConfigHelper;
import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.exceptions.FileTypeNotSupportedException;
import com.minimalism.files.exceptions.InvalidFileException;
import com.minimalism.files.exceptions.NoSuchPathException;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.service.input.RecordDescriptorReader;

public class ServiceContext {
    private String clientName;
    private String recordName;
    InputFileInformation inputFileInformation;
    RecordDescriptor recordDescriptor;
    String operatingMode;
    private OutputDestinations destinationType;
    private OutputDestinations failoverDestinationType;
    
    public ServiceContext(String clientName, String fileName) throws IOException, FileTypeNotSupportedException, InvalidFileException, NoSuchPathException, RecordDescriptorException {
        this.clientName = clientName;
        this.operatingMode = AppConfigHelper.getInstance().getServiceOperatingMode();
        buildInputFileInformation(fileName);
        this.recordDescriptor = RecordDescriptorReader.readDefinition(clientName, fileName);
        this.recordName = this.recordDescriptor.getRecordName();
        buildOutputDestinations();
    }

    public ServiceContext(String clientName, String fileName, String recordName) throws IOException, FileTypeNotSupportedException, InvalidFileException, NoSuchPathException, RecordDescriptorException {
        this(clientName, fileName);
        this.recordName = recordName;
        this.recordDescriptor = RecordDescriptorReader.readDefinition(clientName, fileName, recordName);
    }
    public String getClientName() {
        return this.clientName;
    }

    public String getOperatingMode() {
        return this.operatingMode;
    }

    public String getRecordName() {
        return this.recordName;
    }

    public InputFileInformation getInputFileInformation() {
        return this.inputFileInformation;
    }

    public RecordDescriptor getRecordDescriptor() {
        return this.recordDescriptor;
    }

    public OutputDestinations getDestinationType() {
        return this.destinationType;
    }

    public OutputDestinations getFailoverDestinationType() {
        return this.failoverDestinationType;
    }

    private void buildInputFileInformation(String fileName) throws IOException, FileTypeNotSupportedException, InvalidFileException, NoSuchPathException {
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
            BasicFileAttributeView basicView = Files.getFileAttributeView(fullPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        
            this.inputFileInformation = new InputFileInformation(fullPath.getParent(), fileName, 
                    fileType, basicView.readAttributes().creationTime(), fileExtension, true);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidFileException(String.format("The input file named % does not have an extension, indicating the nature of the file (.csv or .dat or.txt).", fileName));
        } catch (NoSuchPathException e) {
            throw e;
        }
    }
    
    private void buildOutputDestinations() throws IOException {
        this.destinationType = Enum.valueOf(OutputDestinations.class, 
        AppConfigHelper.getInstance().getOutputEndpoint().toUpperCase());
        this.failoverDestinationType = Enum.valueOf(OutputDestinations.class,
        AppConfigHelper.getInstance().getOutputFailoverEndpoint().toUpperCase());
    }
}