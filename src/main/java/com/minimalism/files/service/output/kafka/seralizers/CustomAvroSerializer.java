package com.minimalism.files.service.output.kafka.seralizers;

import java.util.Map;

import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

public class CustomAvroSerializer extends KafkaAvroSerializer {
    public CustomAvroSerializer() {
        super();
        super.schemaRegistry = new MockSchemaRegistryClient();
    }
    
    public CustomAvroSerializer(SchemaRegistryClient client) {
        super(new MockSchemaRegistryClient());
    }

    public CustomAvroSerializer(SchemaRegistryClient client, Map<String, ?> props) {
        super(new MockSchemaRegistryClient(), props);
    }
}
