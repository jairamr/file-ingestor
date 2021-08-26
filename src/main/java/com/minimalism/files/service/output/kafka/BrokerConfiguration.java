package com.minimalism.files.service.output.kafka;

public class BrokerConfiguration {
    private String bootstrapServers;
    private String topic;
    private int partitions;
    private long publisherBufferMemory;
    private int publisherRetries;
    private int publisherLingerMilliseconds;
    private int publisherBatchSize;
    private boolean autoRegisterSchemas;
    private String schemaRegistryUrl;
    private String keySerializer;
    private String valueSerializer;
    
    public String getBootstrapServers() {
        return this.bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getPartitions() {
        return partitions;
    }
    
    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public long getPublisherBufferMemory() {
        return publisherBufferMemory;
    }

    public void setPublisherBufferMemory(long publisherBufferMemory) {
        this.publisherBufferMemory = publisherBufferMemory;
    }

    public int getPublisherRetries() {
        return publisherRetries;
    }

    public void setPublisherRetries(int publisherRetries) {
        this.publisherRetries = publisherRetries;
    }

    public int getPublisherLingerMilliseconds() {
        return publisherLingerMilliseconds;
    }

    public void setPublisherLingerMilliseconds(int publisherLingerMilliseconds) {
        this.publisherLingerMilliseconds = publisherLingerMilliseconds;
    }

    public int getPublisherBatchSize() {
        return publisherBatchSize;
    }

    public void setPublisherBatchSize(int publisherBatchSize) {
        this.publisherBatchSize = publisherBatchSize;
    }

    public boolean autoRegisterSchemas() {
        return autoRegisterSchemas;
    }

    public void setAutoRegisterSchemas(boolean autoRegisterSchemas) {
        this.autoRegisterSchemas = autoRegisterSchemas;
    }

    public String getSchemaRegistryUrl() {
        return schemaRegistryUrl;
    }

    public void setSchemaRegistryUrl(String schemaRegistryUrl) {
        this.schemaRegistryUrl = schemaRegistryUrl;
    }

    public String getPublisherKeySerializer() {
        return keySerializer;
    }

    public void setPublisherKeySerializer(String publisherKeySerializer) {
        this.keySerializer = publisherKeySerializer;
    }

    public String getPublisherValueSerializer() {
        return valueSerializer;
    }

    public void setPublisherValueSerializer(String publisherValueSerializer) {
        this.valueSerializer = publisherValueSerializer;
    }
    

}
