package com.minimalism.files.domain.input;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimalism.shared.common.AllEnums.RecordTypes;

import org.apache.commons.lang3.tuple.Pair;

public class RecordDescriptor {
    private static char CR = '\r';
    private static char LF = '\n';
    private String recordName;
    @JsonProperty("record-type")
    private RecordTypes recordType;
    @JsonProperty("field-separator")
    private byte fieldSeperator;
    @JsonProperty("record-separator")
    private byte[] recordSeparator;
    @JsonProperty("date-format")
    private String dateFormat;
    @JsonProperty("field-descriptors")
    private List<FieldDescriptor> fieldDescriptors;
    @JsonProperty("entity-classname")
    private String entityClassName;
    @JsonProperty("target-domain-classname")
    private String targetDomainClassName;

    public RecordDescriptor() {
        this(RecordTypes.DELIMITED, (byte)',', Pair.of(CR, LF).toString().getBytes());
    }

    public RecordDescriptor(RecordTypes recordType, byte fieldSeperator) {
        this(recordType, fieldSeperator, Pair.of(CR, LF).toString().getBytes());
    }

    public RecordDescriptor(RecordTypes recordType, byte fieldSeperator, byte[] recordSeparator) {
        this.fieldSeperator = fieldSeperator;
        this.recordSeparator = recordSeparator;
        this.recordType = recordType;
        this.fieldDescriptors = new ArrayList<>();
    }

    public RecordDescriptor(String recordType, byte fieldSeperator, byte[] recordSeparator) {
        this.fieldSeperator = fieldSeperator;
        this.recordSeparator = recordSeparator;
        this.recordType = Enum.valueOf(RecordTypes.class, recordType);
        this.fieldDescriptors = new ArrayList<>();
    }
    
    public String getRecordName() {
        return this.recordName;
    }
    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
    /** 
     * @return RecordTypes
     */
    @JsonGetter("record-type")
    public RecordTypes getRecordType() {
        return recordType;
    }
    /** 
     * @param recordType
     */
    public void setRecordType(RecordTypes recordType) {
        this.recordType = recordType;
    }
    /** 
     * @param recordType
     */
    @JsonSetter("record-type")
     public void setRecordType(String recordType) {
        this.recordType = Enum.valueOf(RecordTypes.class, recordType);
    }
    /** 
     * @return byte
     */
    public byte getFieldSeperator() {
        return fieldSeperator;
    }
    
    /** 
     * @return String
     */
    @JsonGetter("field-separator")
    public String getFieldSeparatorAsString() {
        var fsAsArray = new byte[1];
        fsAsArray[0] = this.fieldSeperator;
        return new String(fsAsArray);
    }
    /** 
     * @param fieldSeperator
     */
    public void setFieldSeperator(byte fieldSeperator) {
        this.fieldSeperator = fieldSeperator;
    }
    @JsonSetter("field-separator")
    public void setFieldSeperator(String fieldSeperator) {
        this.fieldSeperator = fieldSeperator.getBytes()[0];
    }
    /** 
     * @return byte[]
     */
    @JsonGetter("record-separator")
    public byte[] getRecordSeparator() {
        return recordSeparator;
    }
    /** 
     * @param recordSeparators
     */
    public void setRecordSeparator(byte[] recordSeparators) {
        this.recordSeparator = recordSeparators;
    }
    @JsonSetter("record-separator")
    public void setRecordSeparator(String recordSeparators) {
        if(recordSeparators.equalsIgnoreCase("CRLF")) {
            this.recordSeparator = new byte[2];
            this.recordSeparator[0] = '\r';
            this.recordSeparator[1] = '\n';
        } else if(recordSeparators.equalsIgnoreCase("LF")) {
            this.recordSeparator = new byte[1];
            this.recordSeparator[0] = '\n';
        } else {
            this.recordSeparator = new byte[recordSeparators.length()];
            this.recordSeparator = recordSeparators.getBytes();
        }
    }
    @JsonGetter("date-format")
    public String getDateFormat() {
        return this.dateFormat;
    }
    @JsonSetter("date-format")
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
    /** 
     * @return Set<FieldDescriptor>
     */
    @JsonGetter("field-descriptors")
    public List<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }
    /** 
     * @param fieldDescriptors
     */
    @JsonSetter("field-descriptors")
    public void setFieldDescriptors(List<FieldDescriptor> fieldDescriptors) {
        this.fieldDescriptors = fieldDescriptors;
    }
    //@JsonSetter("field-descriptors")
    // public void setFieldDescriptors(String fieldDescriptors) throws JsonMappingException, JsonProcessingException {
    //     ObjectMapper mapper = new ObjectMapper();
    //     this.fieldDescriptors = mapper.readValue(fieldDescriptors, new TypeReference<List<FieldDescriptor>>(){});
    // }
    /** 
     * @param descriptor
     */
    public void addFieldDescriptor(FieldDescriptor descriptor) {
        if(descriptor == null)
            return;
        this.fieldDescriptors.add(descriptor);
    }
    
    /** 
     * @return int
     */
    public int maxRecordSize() {
        return this.fieldDescriptors.stream().mapToInt(FieldDescriptor::getMaximumLength).sum();
    }
    
    /** 
     * @return int
     */
    public int getMinRecordSize() {
        return this.fieldDescriptors.stream().mapToInt(FieldDescriptor::getMinimumLength).sum();
    }
    
    /** 
     * @return String
     */
    public String getEntityClassName() {
        return entityClassName;
    }
    
    /** 
     * @param className
     */
    public void setEntityClassName(String className) {
        this.entityClassName = className;
    }
    /** 
     * @return String
     */
    public String getTargetDomainClassName() {
        return targetDomainClassName;
    }
    
    /** 
     * @param className
     */
    public void setTargetDomainClassName(String className) {
        this.targetDomainClassName = className;
    }

}
