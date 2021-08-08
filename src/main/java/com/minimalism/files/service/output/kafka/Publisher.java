package com.minimalism.files.service.output.kafka;

import java.util.List;
import java.util.Properties;

import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.service.output.IPublish;

import org.apache.avro.Schema;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Publisher implements IPublish{
    private Schema avroSchema;
    private String topic;
    
    public Publisher(String topic) {
        this.topic = topic;
    }
    public void publish(List<Entity> records) {
        var props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("auto.register.schemas", false);
        props.put("key.serializer",   "com.minimalism.files.service.output.kafka.seralizers.CustomAvroSerializer");
        props.put("value.serializer",   "com.minimalism.files.service.output.kafka.seralizers.CustomAvroSerializer");
        if(this.avroSchema == null) {
            Entity forSchema = records.get(0);
            var entitySchema = forSchema.forAvroSchema().toString();
            var parser = new Schema.Parser();
            this.avroSchema = parser.parse(entitySchema);
        }
        Producer<String, Entity> producer = new KafkaProducer<>(props);
        for(Entity inputRecord : records) {
            ProducerRecord<String, Entity> producerRecord = new ProducerRecord<>(this.topic, 
            inputRecord.getTargetDomainClassName(), inputRecord);
            producer.send(producerRecord);
        }
        producer.close();
    }
}
