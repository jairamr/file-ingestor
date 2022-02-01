package com.minimalism.files.domain.output;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.FileSystemConfigHelper;

public class OutputConfigurationReader {
    private Properties properties;

    public OutputConfigurationReader(String clientName) throws NoSuchPathException, IOException, URISyntaxException {
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
