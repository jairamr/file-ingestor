package com.minimalism.files.domain.input;

import java.util.ArrayList;
import java.util.List;

import com.minimalism.common.AllEnums.RecordTypes;

import org.apache.commons.lang3.tuple.Pair;

public class RecordDescriptor {
    private static char CR = '\r';
    private static char LF = '\n';
    private RecordTypes recordType;
    private byte fieldSeperator;
    private byte[] recordSeparator;
    private List<FieldDescriptor> fieldDescriptors;
    private String entityClassName;
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
    
    /** 
     * @return RecordTypes
     */
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
    /** 
     * @return byte[]
     */
    public byte[] getRecordSeparator() {
        return recordSeparator;
    }
    /** 
     * @param recordSeparators
     */
    public void setRecordSeparator(byte[] recordSeparators) {
        this.recordSeparator = recordSeparators;
    }
    /** 
     * @return Set<FieldDescriptor>
     */
    public List<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }
    /** 
     * @param fieldDescriptors
     */
    public void setFieldDescriptors(List<FieldDescriptor> fieldDescriptors) {
        this.fieldDescriptors = fieldDescriptors;
    }
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
