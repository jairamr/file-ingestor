package com.minimalism.files.service;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import com.minimalism.files.domain.FieldDescriptor;
import com.minimalism.files.domain.RecordDescriptor;

public class OutputRecordSchemaGenerator {
    private String clientName;

    public OutputRecordSchemaGenerator(String clientName) {
        this.clientName = clientName;
    }

    
    /** 
     * @param inputRecordDescriptor
     * @param recordName
     * @return JsonObject
     */
    public JsonObject createAvroSchema(RecordDescriptor inputRecordDescriptor, String recordName) {
        
        if(recordName.contains(" ")) {
            recordName = recordName.replace(" ", "_");
        }
        if(recordName.contains("-")) {
            recordName = recordName.replace("-","_");
        }
        String namespace = this.clientName.concat(".").concat(recordName).concat(".avro");
        
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
