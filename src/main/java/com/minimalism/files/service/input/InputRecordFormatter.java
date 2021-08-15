package com.minimalism.files.service.input;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.domain.input.RecordDescriptor;
import com.minimalism.files.service.output.generic.EntityBuilder;

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
    public List<Entity> format(Map<Integer, ByteBuffer> inputFileRecords) {
        long startTime = System.currentTimeMillis();
        List<Entity> records = new ArrayList<>();
        
        var recordNumber = 0;

        for (var i = 0; i < inputFileRecords.size(); i++) {
            ByteBuffer inputRecord = inputFileRecords.get(i);
            try{
                records.add(recordNumber++, 
                EntityBuilder.build(new String(inputRecord.array()), this.recordDescriptor));

            } catch (Exception e) {
                logger.error("Exception while adding record Entity: message: {}, stack:{}", e.getMessage(), e.getStackTrace());
            }
        }
        logger.info("Formatter took {} ms.", System.currentTimeMillis() - startTime);

        return records;
    }

    public List<Entity> format(List<ByteArrayOutputStream> inputFileRecords) {
        List<Entity> records = new ArrayList<>(inputFileRecords.size());
        var recordNumber = 0;

        for(var i = 0; i < inputFileRecords.size(); i++) {
            records.add(recordNumber++, 
            EntityBuilder.build(inputFileRecords.get(i).toString(), this.recordDescriptor));
        }
        return records;
    }

}
