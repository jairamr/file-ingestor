package com.minimalism.files.service.output.avro;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardOpenOption;

import javax.json.JsonObject;

import com.minimalism.shared.common.AllEnums.Directories;
import com.minimalism.shared.domain.Entity;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import com.minimalism.files.domain.InputOutputFileSystem;
import com.minimalism.files.domain.entities.InputEntity;
import com.minimalism.files.domain.input.RecordDescriptor;
import com.minimalism.files.service.output.generic.EntityBuilder;

public class OutputRecordSchemaGenerator {

    private OutputRecordSchemaGenerator(){}
    /** 
     * @param inputRecordDescriptor
     * @param recordName
     * @return JsonObject
     * @throws NoSuchPathException
     * @throws IOException
     * @throws URISyntaxException
     */
    // public static JsonObject createAvroSchema(String clientName, RecordDescriptor inputRecordDescriptor, String recordName) throws IOException, NoSuchPathException {
        
    //     // create an Entity object based on the record description
    //     InputEntity sampleEntity = EntityBuilder.build(inputRecordDescriptor);
    //     JsonObject avroSchema = sampleEntity.forAvroSchema();

    //     var dataDefinitionPath = InputOutputFileSystem.getPathFor(clientName, Directories.OUTPUT_DATA_DEFINITION);
    //     if(!Files.exists(dataDefinitionPath, LinkOption.NOFOLLOW_LINKS)) {
    //         Files.createDirectory(dataDefinitionPath);
    //     }
    //     Files.write(dataDefinitionPath.resolve(recordName.concat(".json")), 
    //         avroSchema.toString().getBytes(), StandardOpenOption.CREATE);
        
    //     return avroSchema; 
    // }

    public static org.apache.avro.Schema generateAvroSchemaForEntity() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper(new AvroFactory());
        AvroSchemaGenerator gen = new AvroSchemaGenerator();
        mapper.acceptJsonFormatVisitor(Entity.class, gen);
        AvroSchema schemaWrapper = gen.getGeneratedSchema();

        return schemaWrapper.getAvroSchema();
    }

    public static String getAvroSchemaForEntity(boolean pretty) throws JsonMappingException {
        return generateAvroSchemaForEntity().toString(pretty);
    }
}
