package com.minimalism.files.service.output.avro;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardOpenOption;

import javax.json.JsonObject;

import com.minimalism.shared.common.AllEnums.Directories;
import com.minimalism.shared.exceptions.NoSuchPathException;
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
    public static JsonObject createAvroSchema(String clientName, RecordDescriptor inputRecordDescriptor, String recordName) throws IOException, NoSuchPathException, URISyntaxException {
        
        // create an Entity object basedon the record description
        InputEntity sampleEntity = EntityBuilder.build(inputRecordDescriptor);
        JsonObject avroSchema = sampleEntity.forAvroSchema();

        var dataDefinitionPath = InputOutputFileSystem.getPathFor(clientName, Directories.OUTPUT_DATA_DEFINITION);
        if(!Files.exists(dataDefinitionPath, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectory(dataDefinitionPath);
        }
        Files.write(dataDefinitionPath.resolve(recordName.concat(".json")), 
            avroSchema.toString().getBytes(), StandardOpenOption.CREATE);
        
        return avroSchema; 

        // save the generate schema for sharing with consumers (if necessary)
        // migrate to Schema Registry later...
        
        // if(recordName.contains(" ")) {
        //     recordName = recordName.replace(" ", "_");
        // }
        // if(recordName.contains("-")) {
        //     recordName = recordName.replace("-","_");
        // }
        // String namespace = clientName.concat(".").concat(recordName).concat(".avro");
        
        // JsonArrayBuilder fieldsBuilder = Json.createArrayBuilder();
        // for(FieldDescriptor fd : inputRecordDescriptor.getFieldDescriptors()) {
        //     fieldsBuilder.add(fd.asJson());
        // }
        // return Json.createObjectBuilder()
        // .add("namespace", namespace)
        // .add("type", "record")
        // .add("name", recordName)
        // .add("fields", fieldsBuilder)
        // .build();
    }
}
