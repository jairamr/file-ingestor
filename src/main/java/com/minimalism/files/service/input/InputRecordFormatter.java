package com.minimalism.files.service.input;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.minimalism.files.domain.FieldDescriptor;
import com.minimalism.files.domain.RecordDescriptor;
import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.service.output.avro.OutputRecordSchemaGenerator;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputRecordFormatter {
    private static Logger logger = LoggerFactory.getLogger(InputRecordFormatter.class);
    private RecordDescriptor recordDescriptor;

    public InputRecordFormatter(RecordDescriptor recordDescriptor) {
        this.recordDescriptor = recordDescriptor;
    }
    /** 
     * @param inputFileRecords
     * @param recordDescriptor
     * @return Map<Integer, List<String>>
     */
    public List<Entity> format(Map<Integer, ByteBuffer> inputFileRecords, RecordDescriptor recordDescriptor) {
        long startTime = System.currentTimeMillis();
        List<Entity> records = new ArrayList<>();
        //byte fieldSeparator = recordDescriptor.getFieldSeperator();

        var recordNumber = 0;

        for (var i = 0; i < inputFileRecords.size(); i++) {
            // var fieldStartIndex = 0;
            // var fieldEndIndex = 0;
            // var fieldPosition = 0;

            // ArrayList<String> fieldsInRecord = new ArrayList<>(recordDescriptor.getFieldDescriptors().size());
            
            ByteBuffer inputRecord = inputFileRecords.get(i);
            try{
                records.add(recordNumber++, 
                EntityBuilder.build(new String(inputRecord.array()), recordDescriptor));
                //records.add(recordNumber++, new Employee(recordDescriptor.getFieldSeparatorAsString(), new String(inputRecord.array())));
            } catch (Exception e) {
                logger.error("Exception while adding record Entity: message: {}, stack:{}", e.getMessage(), e.getStackTrace());
            }

        //     while(inputRecord.hasRemaining()) {
        //         if(inputRecord.get() == fieldSeparator) {
        //             fieldEndIndex = inputRecord.position() - 1;
        //             int fieldLength = fieldEndIndex - fieldStartIndex;
        //             var currentField = new byte[fieldLength];

        //             inputRecord.position(fieldStartIndex);
        //             inputRecord.get(currentField, 0, fieldLength);
                    
        //             fieldsInRecord.add(fieldPosition, new String(currentField));
        //             fieldPosition++;
                    
        //             inputRecord.position(inputRecord.position() + 1);
        //             fieldStartIndex = inputRecord.position();
        //         }
        //     }
        //     //last field will not have a separator
        //     fieldEndIndex = inputRecord.position();
        //     inputRecord.position(fieldStartIndex);
        //     var currentField = new byte[fieldEndIndex - fieldStartIndex];
            
        //     inputRecord.get(currentField, 0, fieldEndIndex - fieldStartIndex);
            
        //     fieldsInRecord.add(fieldPosition, new String(currentField));

        //     records.add(recordNumber++, fieldsInRecord);
        }
        logger.info("Formatter took {} ms.", System.currentTimeMillis() - startTime);

        return records;
    }

    
    /** 
     * @param recordDescriptor
     * @param fieldValues
     * @return GenericRecord
     */
    private GenericRecord buildDomainObject(RecordDescriptor recordDescriptor, List<String> fieldValues) {
        long startTime = System.currentTimeMillis();
        List<FieldDescriptor> fieldDescriptors = recordDescriptor.getFieldDescriptors();
        var schema = new Schema.Parser().parse(OutputRecordSchemaGenerator.createAvroSchema("Client_1", recordDescriptor, "HrData").toString()); 
        
        GenericRecord inputRecord = new GenericData.Record(schema);
        fieldDescriptors.stream().forEach(fd -> {
            inputRecord.put(fd.getPosition(), fieldValues.get(fd.getPosition()));
        });
        // for(FieldDescriptor fd : fieldDescriptors) {
        //     inputRecord.put(fd.getFieldNameForAvro(), fieldValues.get(fd.getPosition()));
        // }
        logger.info("Build Domain object took: {} ms", System.currentTimeMillis() - startTime);
        return inputRecord;
    }
}
