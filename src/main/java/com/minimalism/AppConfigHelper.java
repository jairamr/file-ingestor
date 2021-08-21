package com.minimalism;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfigHelper {
    private static String SERVICE_ROOT_DIRECTORY = "service.root.directory";
    private static String THREADS_LOADING_FACTOR = "threads.loading.factor";
    private static String FILE_READ_BUFFER_SISE = "file.read.buffer.size";
    private static String SERVICE_OPERATING_MODE = "service.operating.mode";
    private static String SERVICE_OUTPUT_ENDPOINT = "service.output.endpoint";
    private static String SERVICE_OUTPUT_FAILOVER_ENDPOINT = "service.output.failover.endpoint";
    
    private static int KB_SCALER = 1024;
    
    private static AppConfigHelper instance;
    private static Properties serviceProperties = new Properties();
    
    private AppConfigHelper() throws IOException {
        var basePath = Paths.get(".").toAbsolutePath();
        var toPropertiesFile = basePath.resolve("src/main/resources/app.properties");
        try(var reader = new FileReader(toPropertiesFile.toString())) {
            serviceProperties.load(reader);
        }
    }
    /** 
     * @return AppConfigHelper
     * @throws IOException
     */
    public static synchronized AppConfigHelper getInstance() throws IOException {
        if(instance == null) {
            instance = new AppConfigHelper();
        }
        return instance;
    }
    /** 
     * @return int
     */
    public int getThreadsLoadingFactor() {
        return Integer.parseInt(serviceProperties.getProperty(THREADS_LOADING_FACTOR));
    }
    /** 
     * @return int
     */
    public int getBufferSize() {
        return Integer.parseInt(serviceProperties.getProperty(FILE_READ_BUFFER_SISE)) * KB_SCALER;
    }
    /** 
     * @return Path
     */
    public Path getServiceRootDirectory() {
        return Paths.get(serviceProperties.getProperty(SERVICE_ROOT_DIRECTORY));
    }
    /** 
     * @return String
     */
    public String getServiceOperatingMode() {
        return serviceProperties.getProperty(SERVICE_OPERATING_MODE);
    }
    public String getOutputEndpoint() {
        return serviceProperties.getProperty(SERVICE_OUTPUT_ENDPOINT);
    }
    public String getOutputFailoverEndpoint() {
        return serviceProperties.getProperty(SERVICE_OUTPUT_FAILOVER_ENDPOINT);
    }
}
