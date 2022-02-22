package com.minimalism.files.service.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.FileSystemConfigHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimalism.files.domain.input.RecordDescriptor;
import com.minimalism.files.exceptions.RecordDescriptorException;
/**
 * @author R Jairam Iyer
 * The <b>RecordDescriptor>/b> class abstracts the definition of the input records. The description
 * must be input to the system; the class reads the description from a JSON file in the input-definition
 * directory.
 * @implNote The service follows a convention to automatically discover the record descriptor file. For this
 * to work, the input file name must be specified as _<record-name>_<file-name>.<extension> or
 * <fixed-part-of-file-name>_<record-name>_<variable-part-of-file-name>.<extension>. In either of these
 * cases, the service will look for <record-name>.json as the redord descriptor file.
 * @implNote If the concention is not followed, the service will pickup <record-name>.json as the 
 * record rescriptor file.  
 */
public class RecordDescriptorReader {
    private static String JSON_EXTENSION = ".json";
    private static String YAML_EXTENSION = ".yml";
    private RecordDescriptorReader() {}
    /** 
     * @param clientName
     * @param fileName
     * @return RecordDescriptor
     * @throws RecordDescriptorException
     */
    public static RecordDescriptor readDefinition(String clientName, String fileName) throws RecordDescriptorException {
        RecordDescriptor recordDescriptor = null;
        String descriptorFileName = null;
        try {
            var inputRecordDescriptionPath = FileSystemConfigHelper.getInstance().getServiceInputDataDefinitionDirectory(clientName);
            if(fileName.startsWith("_")) {
                descriptorFileName = fileName.substring(1, fileName.indexOf("_", 1));
            } else {
                int indexOfFirstUnderscrore = fileName.indexOf("_");
                int indexOfSecondUnderscrore = fileName.indexOf("_", indexOfFirstUnderscrore + 1);
                descriptorFileName = fileName.substring(indexOfFirstUnderscrore + 1, indexOfSecondUnderscrore);
            }
            if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat(JSON_EXTENSION)))) {
                recordDescriptor = parseJsonDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat(JSON_EXTENSION)));
            } else if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat(YAML_EXTENSION)))) {
                parseYamlDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat(YAML_EXTENSION)));
            } else {
                // not following convention. throw exception and exit.
                throw new RecordDescriptorException(String.format("Unable to find record description file for file: %s, record name: %s", fileName,descriptorFileName));
            }
            if(recordDescriptor != null) {
                recordDescriptor.setRecordName(descriptorFileName);
            }
        } catch (NoSuchPathException | IOException e) {
            throw new RecordDescriptorException(String.format("Exception occurred while locating record description file for file: %s, record name: %s; message: %s", fileName, descriptorFileName, e.getMessage()));
        }
        
        return recordDescriptor;
    }

    public static RecordDescriptor readDefinition(String clientName, String fileName, String recordName) throws RecordDescriptorException {
        RecordDescriptor recordDescriptor = null;
        String descriptorFileName = null;
        if(recordName.contains(".")) {
            var indexOfDot = recordName.indexOf(".");
            recordName = recordName.substring(0, indexOfDot);
        }
        try {
            var sanitizedFileName = sanitizeFileName(fileName);
            var inputRecordDescriptionPath = FileSystemConfigHelper.getInstance().getServiceInputDataDefinitionDirectory(clientName);
            descriptorFileName = recordName.concat("_").concat(sanitizedFileName);
            
            if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat(JSON_EXTENSION)))) {
                recordDescriptor = parseJsonDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat(JSON_EXTENSION)));
            } else if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat(YAML_EXTENSION)))) {
                parseYamlDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat(YAML_EXTENSION)));
            } else if(Files.exists(inputRecordDescriptionPath.resolve(recordName.concat(JSON_EXTENSION)))) {
                recordDescriptor = parseJsonDescriptorFile(inputRecordDescriptionPath.resolve(recordName.concat(JSON_EXTENSION)));
            } else {
                // descriptor file not found...
                throw new RecordDescriptorException(String.format("Unable to find record description file for file: %s, record name: %s", fileName, recordName));
            }
        } catch (NoSuchPathException | IOException e) {
            throw new RecordDescriptorException(String.format("Exception occurred while locating record description file for file: %s, record name: %s; message: %s", fileName, recordName, e.getMessage())); 
        }
        
        return recordDescriptor;
    }

    private static String sanitizeFileName(String fileName) {
        String sanitizedFileName = fileName;
        if(sanitizedFileName.contains("\\") || sanitizedFileName.contains("/")) {
            sanitizedFileName = Paths.get(sanitizedFileName).getFileName().toString();
        }
        
        var indexOfDot = sanitizedFileName.indexOf(".");
        if(indexOfDot > 0) {
            sanitizedFileName = sanitizedFileName.substring(0, indexOfDot);
        }
        
        return sanitizedFileName;
    }

    private static RecordDescriptor parseJsonDescriptorFile(Path descriptorFile) throws IOException {
        return processRecordDescriptor(descriptorFile);
    }

    private static void parseYamlDescriptorFile(Path descriptorFile) {
        
    }

    private static RecordDescriptor processRecordDescriptor(Path recordDescriptionFile) throws IllegalArgumentException, IOException {
        try(var input = Files.newBufferedReader(recordDescriptionFile)) {
            ObjectMapper mapper = new ObjectMapper(); 
            JsonNode jsonNode = mapper.readTree(input);
            return mapper.treeToValue(jsonNode, RecordDescriptor.class);
        }
    }
}
