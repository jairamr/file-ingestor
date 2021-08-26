package com.minimalism.files.service.output.kafka.seralizers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimalism.shared.domain.Entity;

import org.apache.kafka.common.serialization.Serializer;

public class EntitySerializer implements Serializer<Entity> {

    @Override
    public byte[] serialize(String topic, Entity data) {
        byte[] retVal = null;
        //ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = data.asJsonObject().toString().getBytes();
            //retVal = objectMapper.writeValueAsString(data.asJsonObject()).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
