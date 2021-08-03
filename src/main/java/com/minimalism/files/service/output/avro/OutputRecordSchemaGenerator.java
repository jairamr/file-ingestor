package com.minimalism.files.service.output.avro;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import com.minimalism.files.domain.FieldDescriptor;
import com.minimalism.files.domain.RecordDescriptor;

public class OutputRecordSchemaGenerator {

    private OutputRecordSchemaGenerator(){}
    /** 
     * @param inputRecordDescriptor
     * @param recordName
     * @return JsonObject
     */
    public static JsonObject createAvroSchema(String clientName, RecordDescriptor inputRecordDescriptor, String recordName) {
        
        if(recordName.contains(" ")) {
            recordName = recordName.replace(" ", "_");
        }
        if(recordName.contains("-")) {
            recordName = recordName.replace("-","_");
        }
        String namespace = clientName.concat(".").concat(recordName).concat(".avro");
        
        JsonArrayBuilder fieldsBuilder = Json.createArrayBuilder();
        for(FieldDescriptor fd : inputRecordDescriptor.getFieldDescriptors()) {
            fieldsBuilder.add(fd.asJson());
        }
        return Json.createObjectBuilder()
        .add("namespace", namespace)
        .add("type", "record")
        .add("name", recordName)
        .add("fields", fieldsBuilder)
        .build();
    }
}
