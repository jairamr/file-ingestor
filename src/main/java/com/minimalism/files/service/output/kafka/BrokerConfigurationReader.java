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

    public BrokerConfiguration getBrokerConfiguration() {
        var returnValue = new BrokerConfiguration();
        returnValue.setBootstrapServers(this.getBootsatrapServers());
        returnValue.setTopic(this.getTopic());
        returnValue.setPartitions(this.getPartitionsCount());

        return returnValue;
    }

    private void loadKafkaProperties() throws NoSuchPathException, IOException {
        var kafkaConfigFilePath = FileSystemConfigHelper.getInstance().getServiceOutputDataDefinitionDirectory(this.clientName).resolve("kafka.properties");
        try(var kafkaPropertiesFile = new FileReader(kafkaConfigFilePath.toString())) {
            kafkaProperties.load(kafkaPropertiesFile);
        }
    }
}
