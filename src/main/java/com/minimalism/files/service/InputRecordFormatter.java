package com.minimalism.files.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minimalism.files.domain.RecordDescriptor;

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
        Map<Integer, List<String>> records = new HashMap<>(30000);
        
        byte fieldSeparator = recordDescriptor.getFieldSeperator();

        Integer recordNumber = Integer.valueOf(0);
        
        for (var i = 0; i < inputFileRecords.size(); i++) {
            var fieldStartIndex = 0;
            var fieldEndIndex = 0;
            var fieldPosition = 0;

            ArrayList<String> fieldsInRecord = new ArrayList<>(recordDescriptor.getFieldDescriptors().size());
            
            ByteBuffer inputRecord = inputFileRecords.get(i);
            
            while(inputRecord.hasRemaining()) {
                if(inputRecord.get() == fieldSeparator) {
                    fieldEndIndex = inputRecord.position() - 1;
                    int fieldLength = fieldEndIndex - fieldStartIndex;
                    byte[] currentField = new byte[fieldLength];

                    inputRecord.position(fieldStartIndex);
                    inputRecord.get(currentField, 0, fieldLength);
                    fieldsInRecord.add(fieldPosition++, new String(currentField));
                    inputRecord.position(inputRecord.position() + 1);
                    fieldStartIndex = inputRecord.position();
                }
            }
            { //last field will not have a separator
                fieldEndIndex = inputRecord.position();
                inputRecord.position(fieldStartIndex);
                byte[] currentField = new byte[fieldEndIndex - fieldStartIndex];
                inputRecord.get(currentField, 0, fieldEndIndex - fieldStartIndex);
                fieldsInRecord.add(fieldPosition, new String(currentField));
            }
            records.put(recordNumber, fieldsInRecord);
            recordNumber = Integer.valueOf(recordNumber.intValue() + 1);
        }
        logger.info("Formatter took {} milliseconds.", System.currentTimeMillis() - startTime);
        //return records;
    }
}
