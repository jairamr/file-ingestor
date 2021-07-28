package com.minimalism.files.service;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.minimalism.files.domain.FieldDescriptor;
import com.minimalism.files.domain.RecordDescriptor;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputRecordFormatter {
    private static Logger logger = LoggerFactory.getLogger(InputRecordFormatter.class);
    
    /** 
     * @param inputFileRecords
     * @param recordDescriptor
     * @return Map<Integer, List<String>>
     */
    public void format(Map<Integer, ByteBuffer> inputFileRecords, RecordDescriptor recordDescriptor) {
        long startTime = System.currentTimeMillis();
        Map<Integer, GenericRecord> records = new HashMap<>(inputFileRecords.size());
        
        byte fieldSeparator = recordDescriptor.getFieldSeperator();

        var recordNumber = 0;

        try{
        for (var i = 0; i < inputFileRecords.size(); i++) {
            var fieldStartIndex = 0;
            var fieldEndIndex = 0;
            var fieldPosition = 0;

            ArrayList<String> fieldsInRecord = new ArrayList<>();
            
            ByteBuffer inputRecord = inputFileRecords.get(i);
            try{
                while(inputRecord.hasRemaining()) {
                    if(inputRecord.get() == fieldSeparator) {
                        fieldEndIndex = inputRecord.position() - 1;
                        int fieldLength = fieldEndIndex - fieldStartIndex;
                        var currentField = new byte[fieldLength];

                        inputRecord.position(fieldStartIndex);
                        inputRecord.get(currentField, 0, fieldLength);
                        
                        fieldsInRecord.add(fieldPosition, new String(currentField));
                        fieldPosition++;
                        
                        inputRecord.position(inputRecord.position() + 1);
                        fieldStartIndex = inputRecord.position();
                    }
                }
            } catch(BufferUnderflowException e) {
                logger.error("Message: (), Cause: {}", e.getMessage(), e.getCause());
                //last field will not have a separator
                fieldEndIndex = inputRecord.position();
                inputRecord.position(fieldStartIndex);
                var currentField = new byte[fieldEndIndex - fieldStartIndex];
                
                inputRecord.get(currentField, 0, fieldEndIndex - fieldStartIndex);
                
                fieldsInRecord.add(fieldPosition, new String(currentField));
            } 
            records.put(recordNumber++, buildDomainObject(recordDescriptor, fieldsInRecord));
        }
        } catch(Exception e) {
            logger.error("Message: {}, Cause: {}", e.getMessage(), e.getStackTrace());
        }
        logger.info("Formatter took {} milliseconds.", System.currentTimeMillis() - startTime);
        //return records;
        if(recordNumber % 10000 == 0) {
            logger.info("Fields: {}", records.get(recordNumber));
        }
    }

    private GenericRecord buildDomainObject(RecordDescriptor recordDescriptor, ArrayList<String> fieldValues) {
        Set<FieldDescriptor> fieldDescriptors = recordDescriptor.getFieldDescriptors();
        Schema schema = new Schema.Parser().parse(OutputRecordSchemaGenerator.createAvroSchema("Client_1", recordDescriptor, "HrData").toString()); 
        GenericRecord inputRecord = new GenericData.Record(schema);
        for(FieldDescriptor fd : fieldDescriptors) {
            if(Thread.currentThread().getName().equals("pool-1-thread-1")) {
                System.out.println(Thread.currentThread().getName() + ": pos: " + fd.getPosition());
            }
            inputRecord.put(fd.getFieldNameForAvro(), fieldValues.get(fd.getPosition()));
        }
        return inputRecord;
    }
}
