package com.minimalism.files.domain.output;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.minimalism.shared.FileSystemConfigHelper;
import com.minimalism.shared.exceptions.NoSuchPathException;

public class OutputConfigurationReader {
    private Properties properties;

    public OutputConfigurationReader(String clientName) throws NoSuchPathException, IOException {
        var outputDefinitionPath = FileSystemConfigHelper.getInstance().getServiceOutputDataDefinitionDirectory(clientName);
        try(FileReader reader = new FileReader(outputDefinitionPath.toString())) {
            properties.load(reader);
        }
    }
    public String getDestination() {
        return properties.getProperty("service.output.destination");
    }
    public String getFailoverDestination() {
        return properties.getProperty("service.output.failover.destination");
    }
}
