package com.minimalism.files.service.output.kafka;

public class BrokerConfiguration {
    private String bootstrapServers;
    private String topic;
    private int partitions;
    
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
    
}
