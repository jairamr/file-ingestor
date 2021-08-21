package com.minimalism.files.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import javax.json.JsonObject;

import com.minimalism.files.domain.input.RecordDescriptor;
import com.minimalism.files.exceptions.NoSuchPathException;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.service.input.RecordDescriptorReader;
import com.minimalism.files.service.output.avro.OutputRecordSchemaGenerator;

import org.junit.jupiter.api.Test;

class OutputRecordSchemaGeneratorTests {
    @Test
    void testCreateAvroSchema() throws RecordDescriptorException {
        RecordDescriptor hrRecordDescriptor = RecordDescriptorReader.readDefinition("Client_1", "_HrData_Kaggle_Hr5m.csv");
        JsonObject result;
        try {
            result = OutputRecordSchemaGenerator.createAvroSchema("Client_1", hrRecordDescriptor, "HrData");
            assertNotNull(result);
            System.out.println(result.toString());
        } catch (IOException | NoSuchPathException e) {
            e.printStackTrace();
        }
    }
}
