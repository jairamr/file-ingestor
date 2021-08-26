package com.minimalism.files.service.output.kafka;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.exceptions.NoSuchPathException;

public class BrokerConfigurationReader {
    private String clientName;
    private String recordTypeName;
    private Properties kafkaProperties;
    
    public BrokerConfigurationReader(String clientName, String recordTypeName) throws NoSuchPathException, IOException {
        this.kafkaProperties = new Properties();
        this.clientName = clientName;
        this.recordTypeName = recordTypeName;
        loadKafkaProperties();
    }

    public String getBootsatrapServers() {
        return kafkaProperties.getProperty("bootstrap_servers");
    }

    public String getTopic() {
        return kafkaProperties.getProperty(this.recordTypeName.toLowerCase().concat("_topic"));
    }

    public int getPartitionsCount() {
        return Integer.parseInt(this.kafkaProperties.getProperty(this.recordTypeName.toLowerCase().concat("_topic_partitions").toLowerCase()));
    }

    public long getPublisherBufferMemorySize() {
        return Long.parseLong(this.kafkaProperties.getProperty("publisher.buffer.memory"));
    }

    public int getPublisherRetriesCount() {
        return Integer.parseInt(this.kafkaProperties.getProperty("publisher.retries"));
    }

    public int getPublisherLingerMilliseconds() {
        return Integer.parseInt(this.kafkaProperties.getProperty("publisher.linger.milliseconds"));
    }

    public int getPublisherBatchSize() {
        return Integer.parseInt(this.kafkaProperties.getProperty("publisher.batch.size"));
    }

    public boolean autoRegisterSchemas() {
        return Boolean.parseBoolean(this.kafkaProperties.getProperty("auto.register.schemas"));
    }

    public String getSchemaRegistryUrl() {
        return this.kafkaProperties.getProperty("schema.registry.url");
    }

    public String getKeySerializerName() {
        return this.kafkaProperties.getProperty("key.serializer");
    }

    public String getValueSerializerName() {
        return this.kafkaProperties.getProperty("value.serializer");
    }

    public BrokerConfiguration getBrokerConfiguration() {
        var returnValue = new BrokerConfiguration();
        returnValue.setBootstrapServers(this.getBootsatrapServers());
        returnValue.setTopic(this.getTopic());
        returnValue.setPartitions(this.getPartitionsCount());
        returnValue.setAutoRegisterSchemas(this.autoRegisterSchemas());
        returnValue.setPublisherBatchSize(this.getPublisherBatchSize());
        returnValue.setPublisherBufferMemory(this.getPublisherBufferMemorySize());
        returnValue.setPublisherKeySerializer(this.getKeySerializerName());
        returnValue.setPublisherLingerMilliseconds(this.getPublisherLingerMilliseconds());
        returnValue.setPublisherRetries(this.getPublisherRetriesCount());
        returnValue.setPublisherValueSerializer(this.getValueSerializerName());
        returnValue.setSchemaRegistryUrl(this.getSchemaRegistryUrl());

        return returnValue;
    }

    private void loadKafkaProperties() throws NoSuchPathException, IOException {
        var kafkaConfigFilePath = FileSystemConfigHelper.getInstance().getServiceOutputDataDefinitionDirectory(this.clientName).resolve("kafka.properties");
        try(var kafkaPropertiesFile = new FileReader(kafkaConfigFilePath.toString())) {
            kafkaProperties.load(kafkaPropertiesFile);
        }
    }
}
