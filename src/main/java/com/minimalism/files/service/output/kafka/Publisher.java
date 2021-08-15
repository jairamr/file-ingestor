package com.minimalism.files.service.output.kafka;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import com.minimalism.files.domain.entities.Entity;
import com.minimalism.files.domain.entities.Field;
import com.minimalism.files.service.output.IPublish;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher implements IPublish{
    private static Logger logger = LoggerFactory.getLogger(Publisher.class);
    
    private Schema avroSchema;
    private BrokerConfiguration configuration;
    
    public Publisher(BrokerConfiguration configuration) {
        this.configuration = configuration;
    }
    public void publish(List<Entity> records) throws InterruptedException {
        var props = new Properties();
        props.put("bootstrap.servers", this.configuration.getBootstrapServers());
        props.put("buffer.memory", 1258300);
        props.put("retries", 3);
        props.put("batch.size", 1048576);
        props.put("auto.register.schemas", true);
        props.put("schema.registry.url", "something.or.other");
        props.put("key.serializer",   "com.minimalism.files.service.output.kafka.seralizers.CustomAvroSerializer");
        props.put("value.serializer",   "com.minimalism.files.service.output.kafka.seralizers.CustomAvroSerializer");
        if(this.avroSchema == null) {
            Entity forSchema = records.get(0);
            var entitySchema = forSchema.forAvroSchema().toString();
            var parser = new Schema.Parser();
            this.avroSchema = parser.parse(entitySchema);
        }
        try(Producer<String, GenericRecord> producer = new KafkaProducer<>(props)) {
            for(Entity inputRecord : records) {
                ProducerRecord<String, GenericRecord> producerRecord = 
                new ProducerRecord<>(this.configuration.getTopic(), 
                inputRecord.getTargetDomainClassName(), entityToAvroGenericRecord(inputRecord));
                try {
                    producer.send(producerRecord).get();
                } catch (InterruptedException e) {
                    if(Thread.interrupted()) {
                        logger.error(String.format("Publisher thread was interrupted due to %s ", e.getCause().getMessage()));
                        throw e;
                    }
                } catch (ExecutionException e) {
                    logger.error(String.format("Error while publishing message: %s", e.getMessage()));
                }
            }
        }
    }

    private GenericRecord entityToAvroGenericRecord(Entity entity) {
        GenericRecord avroGenericRecord = new GenericData.Record(this.avroSchema);
        List<Field> fields = entity.getFields();
        for(var i = 0; i < fields.size(); i++) {
            avroGenericRecord.put(fields.get(i).getName(), fields.get(i).getValue());
        }
        return avroGenericRecord;
    }
}