package com.minimalism.files.service.output.kafka;

import java.util.List;
import java.util.Properties;

import com.minimalism.files.domain.entities.InputEntity;
import com.minimalism.files.domain.entities.InputField;
import com.minimalism.files.domain.input.IngestorContext;
import com.minimalism.files.service.output.EntityTransformer;
import com.minimalism.files.service.output.IPublish;
import com.minimalism.shared.domain.Entity;
import com.minimalism.shared.service.BrokerConfiguration;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher implements IPublish {
    private static Logger logger = LoggerFactory.getLogger(Publisher.class);
    
    private Schema avroSchema;
    private BrokerConfiguration configuration;
    
    public Publisher(BrokerConfiguration configuration, IngestorContext serviceContext) {
        this.configuration = configuration;
        this.avroSchema = serviceContext.getAvroSchema();
    }
    
    @Override
    public void publish(List<InputEntity> records, boolean asGeneric) throws InterruptedException {
        if(asGeneric) {
            //publishGenericRecord(records);
        } else {
            publishEntity(records);
        }
    }

    // public void publishGenericRecord(List<InputEntity> records) throws InterruptedException {
    //     var props = setProperties();
        
    //     if(this.avroSchema == null) {
    //         InputEntity forSchema = records.get(0);
    //         var entitySchema = forSchema.forAvroSchema().toString();
    //         var parser = new Schema.Parser();
    //         this.avroSchema = parser.parse(entitySchema);
    //     }
    //     long start = System.currentTimeMillis();
                    
    //     try(Producer<String, GenericRecord> producer = new KafkaProducer<>(props)) {
    //         for(InputEntity inputRecord : records) {
    //             ProducerRecord<String, GenericRecord> producerRecord = 
    //             new ProducerRecord<>(this.configuration.getTopic(), entityToAvroGenericRecord(inputRecord));
    //             producer.send(producerRecord, new PublisherCallback());
    //         }
    //     } catch (Exception e) {
    //         logger.error("Error while publishing record: {}", e.getMessage());
    //         throw new InterruptedException(String.format("Error while publishing record due to: %s", e.getMessage()));
    //     }
    //     logger.info("Producer with id: {} took: {} ms for {} records", this.hashCode(), System.currentTimeMillis() - start, records.size());
    // }

    private void publishEntity(List<InputEntity> records) throws InterruptedException {
        var props = setProperties();

        long start = System.currentTimeMillis();
                    
        try(Producer<String, Entity> producer = new KafkaProducer<>(props)) {
            for(InputEntity inputRecord : records) {
                ProducerRecord<String, Entity> producerRecord = 
                new ProducerRecord<>(this.configuration.getTopic(), EntityTransformer.transform(inputRecord));
                producer.send(producerRecord, new PublisherCallback());
            }
        }
        logger.info("Producer with id: {} took: {} ms for {} records", this.hashCode(), System.currentTimeMillis() - start, records.size());
    }

    private GenericRecord entityToAvroGenericRecord(InputEntity inputEntity) {
        GenericRecord avroGenericRecord = new GenericData.Record(this.avroSchema);
        List<InputField> fields = inputEntity.getFields();
        for(var i = 0; i < fields.size(); i++) {
            avroGenericRecord.put(fields.get(i).getName(), fields.get(i).getValue());
        }
        return avroGenericRecord;
    }

    private class PublisherCallback implements Callback 
    {         
        @Override    
        public void onCompletion(RecordMetadata recordMetadata, Exception e) {     
            if (e != null) {         
                logger.error("Publisher failed to publish record.", e);         
            }    
        }
    }

    private Properties setProperties() {
        var props = new Properties();
        props.put("bootstrap.servers", this.configuration.getBootstrapServers());
        props.put("buffer.memory", this.configuration.getPublisherBufferMemory());
        props.put("retries", this.configuration.getPublisherRetries());
        props.put("linger.ms", this.configuration.getPublisherLingerMilliseconds());
        props.put("batch.size", this.configuration.getPublisherBatchSize());
        props.put("auto.register.schemas", this.configuration.getAutoRegisterSchemasFlag());
        props.put("schema.registry.url", this.configuration.getSchemaRegistryUrl());
        props.put("key.serializer", this.configuration.getKeySerializer());
        props.put("value.serializer", this.configuration.getValueSerializer());

        return props;
    }
}