package com.minimalism.files.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import com.minimalism.common.AllEnums.DataTypes;
import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.domain.FieldDescriptor;
import com.minimalism.files.domain.RecordDescriptor;
import com.minimalism.files.exceptions.NoSuchPathException;

public class RecordDescriptorReader {
    private RecordDescriptor recordDescriptor;

    
    /** 
     * @param clientName
     * @param fileName
     * @return RecordDescriptor
     */
    public RecordDescriptor readDefinition(String clientName, String fileName) {
        String descriptorFileName = null;
        try {
            Path inputRecordDescriptionPath = FileSystemConfigHelper.getInstance().getServiceInputDataDefinitionDirectory(clientName);
            if(fileName.startsWith("_")) {
                descriptorFileName = fileName.substring(1, fileName.indexOf("_", 1));
            } else {
                int indexOfFirstUnderscrore = fileName.indexOf("_");
                int indexOfSecondUnderscrore = fileName.indexOf("_", indexOfFirstUnderscrore + 1);
                descriptorFileName = fileName.substring(indexOfFirstUnderscrore + 1, indexOfSecondUnderscrore);
            }
            if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat(".json")))) {
                parseJsonDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat(".json")));
            } else if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat("yml")))) {
                parseYamlDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat("yml")));
            } else {
                
            }

        } catch (NoSuchPathException | IOException e) {
            e.printStackTrace();
        }
        return this.recordDescriptor;
    }

    
    /** 
     * @param descriptorFile
     * @throws IOException
     */
    private void parseJsonDescriptorFile(Path descriptorFile) throws IOException {
        try(InputStream input = Files.newInputStream(descriptorFile, StandardOpenOption.READ)) {
            JsonParser parser = Json.createParser(input);
            
            while(parser.hasNext()) {
                Event jsonEvent = parser.next();
                switch(jsonEvent) {
                    case START_OBJECT:
                    processRecordDefinition(parser);
                    break;
                    default:
                    break;
                }
            } 
        }
    }

    
    /** 
     * @param descriptorFile
     */
    private void parseYamlDescriptorFile(Path descriptorFile) {
        
    }

    
    /** 
     * @param parser
     */
    private void processRecordDefinition(JsonParser parser) {
        this.recordDescriptor = new RecordDescriptor();
        while(parser.hasNext()) {
            Event event = parser.next();
            switch(event) {
                case KEY_NAME:
                String key = parser.getString();
                switch(key) {
                    case "record-type":
                    parser.next();
                    this.recordDescriptor.setRecordType(parser.getString());
                    break;
                    case "record-separator":
                    parser.next();
                    String recordSeparator = parser.getString();
                    byte[] recordSeparators = new byte[2];
                    if(recordSeparator.equalsIgnoreCase("CRLF")) {
                        recordSeparators[0] = '\r';
                        recordSeparators[1] = '\n';
                    } else if(recordSeparator.equalsIgnoreCase("CR")) {
                        recordSeparators[0] = '\r';
                    } else if(recordSeparator.equalsIgnoreCase("LF")) {
                        recordSeparators[0] = '\n';
                    } else {
                        byte[] inputBytes = recordSeparator.getBytes();
                        for(int i = 0; i < 2; i++) {
                            recordSeparators[i] = inputBytes[i];
                        }
                    }
                    this.recordDescriptor.setRecordSeparator(recordSeparators);
                    break;
                    case "field-separator":
                    parser.next();
                    this.recordDescriptor.setFieldSeperator((byte)parser.getString().charAt(0));
                    break;
                    default:
                    break;
                }
                break;
                case START_ARRAY:
                processFieldDefinition(parser);
                break;
                default:
                break;
            }
        }
        
    }

    
    /** 
     * @param parser
     */
    private void processFieldDefinition(JsonParser parser) {
        FieldDescriptor fd = null;
        while(parser.hasNext()) {
            Event event = parser.next();
            switch(event){
                case START_OBJECT:
                fd = new FieldDescriptor();
                break;
                case KEY_NAME:
                String key = parser.getString();
                switch(key){
                    case "field-name":
                    parser.next();
                    fd.setFieldName(parser.getString());
                    break;
                    case "position":
                    parser.next();
                    fd.setPosition(parser.getInt());
                    break;
                    case "data-type":
                    parser.next();
                    fd.setDatatype(Enum.valueOf(DataTypes.class, parser.getString()));
                    break;
                    case "minimum-length":
                    parser.next();
                    fd.setMinimumLength(parser.getInt());
                    break;
                    case "maximum-length":
                    parser.next();
                    fd.setMaximumLength(parser.getInt());
                    break;
                    case "null-allowed":
                    parser.next();
                    fd.setNullAllowed((parser.getValue() == JsonValue.FALSE) ? false : true);
                    break;
                }
                break;
                case END_OBJECT:
                this.recordDescriptor.addFieldDescriptor(fd);
                break;
                default:
                break;
            }
        }
    }
}
