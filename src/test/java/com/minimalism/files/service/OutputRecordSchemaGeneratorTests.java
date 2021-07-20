package com.minimalism.files.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.json.JsonObject;

import com.minimalism.files.domain.RecordDescriptor;

import org.junit.jupiter.api.Test;

public class OutputRecordSchemaGeneratorTests {
    @Test
    void testCreateAvroSchema() {
        RecordDescriptorReader reader = new RecordDescriptorReader();
        RecordDescriptor hrRecordDescriptor = reader.readDefinition("Client_1", "_HrData_Kaggle_Hr5m.csv");
        OutputRecordSchemaGenerator iut = new OutputRecordSchemaGenerator("Client_1");
        JsonObject result = iut.createAvroSchema(hrRecordDescriptor, "HrData");
        assertNotNull(result); 
    }
}