package com.minimalism.files.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.json.JsonObject;

import com.minimalism.files.domain.RecordDescriptor;
import com.minimalism.files.service.input.RecordDescriptorReader;
import com.minimalism.files.service.output.avro.OutputRecordSchemaGenerator;

import org.junit.jupiter.api.Test;

public class OutputRecordSchemaGeneratorTests {
    @Test
    void testCreateAvroSchema() {
        RecordDescriptor hrRecordDescriptor = RecordDescriptorReader.readDefinition("Client_1", "_HrData_Kaggle_Hr5m.csv");
        JsonObject result = OutputRecordSchemaGenerator.createAvroSchema("Client_1", hrRecordDescriptor, "HrData");
        assertNotNull(result); 
    }
}
