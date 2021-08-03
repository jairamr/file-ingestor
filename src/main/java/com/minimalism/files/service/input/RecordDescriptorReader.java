package com.minimalism.files.service.input;

import java.io.IOException;
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

    private RecordDescriptorReader() {}
    /** 
     * @param clientName
     * @param fileName
     * @return RecordDescriptor
     */
    public static RecordDescriptor readDefinition(String clientName, String fileName) {
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
            if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat(".json")))) {
                recordDescriptor = parseJsonDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat(".json")));
            } else if(Files.exists(inputRecordDescriptionPath.resolve(descriptorFileName.concat("yml")))) {
                parseYamlDescriptorFile(inputRecordDescriptionPath.resolve(descriptorFileName.concat("yml")));
            } else {
                
            }

        } catch (NoSuchPathException | IOException e) {
            e.printStackTrace();
        }
        return recordDescriptor;
    }

    
    /** 
     * @param descriptorFile
     * @throws IOException
     */
    private static RecordDescriptor parseJsonDescriptorFile(Path descriptorFile) throws IOException {
        RecordDescriptor recordDescriptor = null;
        try(var input = Files.newInputStream(descriptorFile, StandardOpenOption.READ)) {
            try(JsonParser parser = Json.createParser(input)) {
                while(parser.hasNext()) {
                    Event jsonEvent = parser.next();
                    if(jsonEvent == Event.START_OBJECT) {
                        recordDescriptor = processRecordDefinition(parser);
                    }
                }
            }
            return recordDescriptor;
        }
    }

    
    /** 
     * @param descriptorFile
     */
    private static void parseYamlDescriptorFile(Path descriptorFile) {
        
    }

    
    /** 
     * @param parser
     */
    private static RecordDescriptor processRecordDefinition(JsonParser parser) {
        RecordDescriptor recordDescriptor = new RecordDescriptor();
        while(parser.hasNext()) {
            var event = parser.next();
            switch(event) {
                case KEY_NAME:
                var key = parser.getString();
                switch(key) {
                    case "record-type":
                    parser.next();
                    recordDescriptor.setRecordType(parser.getString());
                    break;
                    case "record-separator":
                    parser.next();
                    var recordSeparator = parser.getString();
                    var recordSeparators = new byte[2];
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
                    recordDescriptor.setRecordSeparator(recordSeparators);
                    break;
                    case "entity-classname":
                    parser.next();
                    recordDescriptor.setEntityClassName(parser.getString());
                    break;
                    case "field-separator":
                    parser.next();
                    recordDescriptor.setFieldSeperator((byte)parser.getString().charAt(0));
                    break;
                    default:
                    break;
                }
                break;
                case START_ARRAY:
                processFieldDefinition(parser, recordDescriptor);
                break;
                default:
                break;
            }
        }
        return recordDescriptor;
    }

    
    /** 
     * @param parser
     */
    private static void processFieldDefinition(JsonParser parser, RecordDescriptor recordDescriptor) {
        FieldDescriptor fd = null;
        while(parser.hasNext()) {
            var event = parser.next();
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
                    fd.setPosition((short)parser.getInt());
                    break;
                    case "data-type":
                    parser.next();
                    fd.setDatatype(Enum.valueOf(DataTypes.class, parser.getString()));
                    break;
                    case "minimum-length":
                    parser.next();
                    fd.setMinimumLength((short)parser.getInt());
                    break;
                    case "maximum-length":
                    parser.next();
                    fd.setMaximumLength((short)parser.getInt());
                    break;
                    case "null-allowed":
                    parser.next();
                    fd.setNullAllowed((parser.getValue() == JsonValue.FALSE));
                    break;
                    default:
                    break;
                }
                break;
                case END_OBJECT:
                recordDescriptor.addFieldDescriptor(fd);
                break;
                default:
                break;
            }
        }
    }
}
