package com.minimalism.files.domain;

import java.util.HashSet;
import java.util.Set;

import com.minimalism.common.AllEnums.RecordTypes;

import org.apache.commons.lang3.tuple.Pair;

public class RecordDescriptor {
    private static char CR = '\r';
    private static char LF = '\n';
    private RecordTypes recordType;
    private byte fieldSeperator;
    private byte[] recordSeparator;
    private Set<FieldDescriptor> fieldDescriptors;

    public RecordDescriptor() {
        this(RecordTypes.DELIMITED, (byte)',', Pair.of(CR, LF).toString().getBytes());
    }

    public RecordDescriptor(RecordTypes recordType, byte fieldSeperator) {
        this(recordType, fieldSeperator, null);
    }

    public RecordDescriptor(RecordTypes recordType, byte fieldSeperator, byte[] recordSeparator) {
        this.fieldSeperator = fieldSeperator;
        this.recordSeparator = recordSeparator;
        this.recordType = recordType;
        this.fieldDescriptors = new HashSet<FieldDescriptor>();
    }

    public RecordDescriptor(String recordType, byte fieldSeperator, byte[] recordSeparator) {
        this.fieldSeperator = fieldSeperator;
        this.recordSeparator = recordSeparator;
        this.recordType = Enum.valueOf(RecordTypes.class, recordType);
        this.fieldDescriptors = new HashSet<FieldDescriptor>();
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
    public Set<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    
    /** 
     * @param fieldDescriptors
     */
    public void setFieldDescriptors(Set<FieldDescriptor> fieldDescriptors) {
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
    
    public int maxRecordSize() {
        return this.fieldDescriptors.stream().mapToInt(fd -> fd.getMaximumLength()).sum();
    }

    public int getMinRecordSize() {
        return this.fieldDescriptors.stream().mapToInt(fd -> fd.getMinimumLength()).sum();
    }

}
