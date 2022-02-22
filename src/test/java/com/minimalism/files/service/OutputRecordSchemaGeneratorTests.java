package com.minimalism.files.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.minimalism.files.exceptions.RecordDescriptorException;
import com.minimalism.files.service.output.avro.OutputRecordSchemaGenerator;
import com.minimalism.shared.common.AllEnums.DataTypes;
import com.minimalism.shared.domain.Entity;
import com.minimalism.shared.domain.Field;

import org.junit.jupiter.api.Test;

class OutputRecordSchemaGeneratorTests {
    @Test
    void testCreateAvroSchema() throws RecordDescriptorException {
        //RecordDescriptor hrRecordDescriptor = RecordDescriptorReader.readDefinition("Client_1", "_HrData_Kaggle_Hr5m.csv");
        org.apache.avro.Schema result;
        try {
            result = OutputRecordSchemaGenerator.generateAvroSchemaForEntity();// createAvroSchema("Client_1", hrRecordDescriptor, "HrData");
            assertNotNull(result);
            System.out.println(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEntityAvroSchema() {
        Entity entity = new Entity();
        Field f1 = new Field("Firstname", DataTypes.STRING, "Jairam");
        entity.addField(f1);

        Field f2 = new Field("LastName", DataTypes.STRING, "Iyer");
        entity.addField(f2);

        entity.setName("HrInfo");
        entity.setTargetDomainName("com.minimalism.filesliceanddice.entity");

        String avroSchema;
        try {
            avroSchema = OutputRecordSchemaGenerator.getAvroSchemaForEntity(false);
            assertEquals("{\"type\":\"record\",\"name\":\"Entity\",\"namespace\":\"com.minimalism.shared.domain\"," + 
                "\"fields\":[{\"name\":\"fields\",\"type\":[\"null\",{\"type\":\"map\",\"values\":{" + 
                "\"type\":\"record\",\"name\":\"Field\",\"fields\":[{\"name\":\"dataType\",\"type\":[\"null\",\"string\"]" + 
                "},{\"name\":\"name\",\"type\":[\"null\",\"string\"]},{\"name\":\"value\",\"type\":[\"null\",{" + 
                "\"type\":\"record\",\"name\":\"Object\",\"namespace\":\"java.lang\",\"fields\":[]}]}]}}]},{" +
                "\"name\":\"name\",\"type\":[\"null\",\"string\"]},{\"name\":\"targetDomainName\",\"type\":[\"null\",\"string\"]}]}", avroSchema);
            System.out.println(avroSchema);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    
}
 