package com.minimalism.files.service.output.kafka.seralizers;

import com.minimalism.shared.domain.Entity;
import org.apache.kafka.common.serialization.Serializer;

public class EntitySerializer implements Serializer<Entity> {

    @Override
    public byte[] serialize(String topic, Entity data) {
        byte[] retVal = null;
        try {
            retVal = data.toString().getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
