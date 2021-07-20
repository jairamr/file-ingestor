package com.minimalism.files.service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minimalism.files.domain.RecordDescriptor;

public class InputRecordFormatter {

    
    /** 
     * @param inputFileRecords
     * @param recordDescriptor
     * @return Map<Integer, List<String>>
     */
    public Map<Integer, List<String>> format(Map<Integer, ByteBuffer> inputFileRecords, RecordDescriptor recordDescriptor) {
        Map<Integer, List<String>> records = new HashMap<>();
        
        byte fieldSeparator = recordDescriptor.getFieldSeperator();

        int recordNumber = 0;
        
        for (int i = 0; i < inputFileRecords.size(); i++) {
            if(records.size() % 101 == 0) records.clear();
            int fieldStartIndex = 0;
            int fieldEndIndex = 0;
            int fieldPosition = 0;
        
            ArrayList<String> fieldsInRecord = new ArrayList<>();
            
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
            records.put(recordNumber++, fieldsInRecord);
        }
        return records;
    }
}
