package com.minimalism.files.domain.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;

import javax.json.JsonObject;

import com.minimalism.shared.common.AllEnums.DataSources;
import com.minimalism.shared.common.AllEnums.FileTypes;
import com.minimalism.shared.domain.ServiceConfiguration;
import com.minimalism.shared.exceptions.FileTypeNotSupportedException;
import com.minimalism.shared.exceptions.InvalidFileException;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.ClientConfigHelper;
import com.minimalism.shared.service.FileSystemConfigHelper;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.service.input.RecordDescriptorReader;
import com.minimalism.files.service.output.avro.OutputRecordSchemaGenerator;

import org.apache.avro.Schema;

public class IngestorContext {
    private String clientName;
    private String recordName;
    InputFileInformation inputFileInformation;
    RecordDescriptor recordDescriptor;
    ServiceConfiguration serviceConfiguration;
    private Schema avroSchema;
    
    public IngestorContext(String clientName, String fileName) throws IOException, FileTypeNotSupportedException, InvalidFileException, NoSuchPathException, RecordDescriptorException {
        this.clientName = clientName;
        ClientConfigHelper configHelper = new ClientConfigHelper(clientName);
        this.serviceConfiguration = configHelper.getServiceConfiguration(clientName);

        buildInputFileInformation(fileName);
        
        this.recordDescriptor = RecordDescriptorReader.readDefinition(clientName, fileName);
        this.recordName = this.recordDescriptor.getRecordName();
        
        JsonObject avroSchemaJson = OutputRecordSchemaGenerator.createAvroSchema(clientName, this.recordDescriptor, this.recordName);
        this.avroSchema = new Schema.Parser().parse(avroSchemaJson.toString());
        
    }

    public IngestorContext(String clientName, String fileName, String recordName) throws IOException, FileTypeNotSupportedException, InvalidFileException, NoSuchPathException, RecordDescriptorException {
        this(clientName, fileName);
        this.recordName = recordName;
        this.recordDescriptor = RecordDescriptorReader.readDefinition(clientName, fileName, recordName);
    }
    
    public String getClientName() {
        return this.clientName;
    }

    public String getOperatingMode() {
        return this.serviceConfiguration.getOperatingMode();
    }

    public String getRecordName() {
        return this.recordName;
    }

    public ServiceConfiguration getServiceConfiguration() {
        return this.serviceConfiguration;
    }

    public InputFileInformation getInputFileInformation() {
        return this.inputFileInformation;
    }

    public RecordDescriptor getRecordDescriptor() {
        return this.recordDescriptor;
    }

    public DataSources getDestinationType() {
        return this.serviceConfiguration.getOutputEndpoint();
    }

    public DataSources getFailoverDestinationType() {
        return this.serviceConfiguration.getOutputFailEndpoint();
    }

    public Schema getAvroSchema() {
        return this.avroSchema;
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
}
