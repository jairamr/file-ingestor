package com.minimalism.files;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.minimalism.common.AppConfigHelper;
import com.minimalism.files.exceptions.NoSuchPathException;

public class FileSystemConfigHelper {
    private static String SERVICE_INPUT_DIRECTORY = "service.input.directory";
    private static String SERVICE_OUTPUT_DIRECTORY = "service.output.directory";
    private static String SERVICE_INPUT_DATA_CSV_DIRECTORY = "service.input.data.csv.directory";
    private static String SERVICE_INPUT_DATA_BIN_DIRECTORY = "service.input.data.bin.directory";
    private static String SERVICE_OUTPUT_DATA_CSV_DIRECTORY = "service.output.data.csv.directory";
    private static String SERVICE_OUTPUT_DATA_BIN_DIRECTORY = "service.output.data.bin.directory";
    private static String SERVICE_INPUT_DATA_DEFINITION_DIRECTORY = "service.input.data.definition.directory";
    private static String SERVICE_OUTPUT_DATA_DEFINITION_DIRECTORY = "service.output.data.definition.directory";
    private static String SERVICE_INSTRUMENTATION_DIRECTORY = "service.instrumentation.directory";
    private static String SERVICE_ARCHIVE_DIRECTORY = "service.archive.directory";
    private static String SERVICE_ARCHIVE_INPUT_DATA_DIRECTORY = "service.archive.input.data.directory";
    private static String SERVICE_ARCHIVE_OUTPUT_DATA_DIRECTORY = "service.archive.output.data.directory";
    private static String SERVICE_ARCHIVE_INPUT_DATA_CSV_DIRECTORY = "service.archive.input.data.csv.directory";
    private static String SERVICE_ARCHIVE_INPUT_DATA_BIN_DIRECTORY = "service.archive.input.data.bin.directory";
    private static String SERVICE_ARCHIVE_OUTPUT_DATA_CSV_DIRECTORY = "service.archive.output.data.csv.directory";
    private static String SERVICE_ARCHIVE_OUTPUT_DATA_BIN_DIRECTORY = "service.archive.output.data.bin.directory";
    private static String SERVICE_LOST_AND_FOUND_DIRECTORY = "service.lost.and.found.directory";

    private static FileSystemConfigHelper instance;
    private static Properties fileSystemConfigProperties;
    
    private FileSystemConfigHelper() throws IOException {
        fileSystemConfigProperties = new Properties();
        Path basePath = Paths.get(".").toAbsolutePath();
        Path toPropertiesFile = basePath.resolve("src/main/resources/filesystem.properties");
        FileReader reader = new FileReader(toPropertiesFile.toString());
        fileSystemConfigProperties.load(reader);
    }

    
    /** 
     * @return FileSystemConfigHelper
     * @throws IOException
     */
    public static synchronized FileSystemConfigHelper getInstance() throws IOException {
        if(instance == null) {
            instance = new FileSystemConfigHelper();
        }
        return instance;
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws IOException
     * @throws NoSuchPathException
     */
    public Path getServiceClientRoottDirectory(String clientName) throws IOException, NoSuchPathException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        return clientRoot;
    }

    
    /** 
     * @return String
     */
    public String getServiceInputDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_INPUT_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceInputDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path inputDirectory = clientRoot.resolve(getServiceInputDirectory());
        if(!Files.exists(inputDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", inputDirectory));
        }
        return inputDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceOutputDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_OUTPUT_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceOutputDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path outputDirectory = clientRoot.resolve(getServiceOutputDirectory());
        if(!Files.exists(outputDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", outputDirectory));
        }
        return outputDirectory;
    }    

    
    /** 
     * @return String
     */
    public String getServiceInputDataCSVDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_INPUT_DATA_CSV_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceInputDataCSVDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path inputDataDirectory = clientRoot.resolve(getServiceInputDirectory());
        Path inputDataCSVDirectory = inputDataDirectory.resolve(getServiceInputDataCSVDirectory());
        if(!Files.exists(inputDataCSVDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", inputDataCSVDirectory));
        }
        return inputDataCSVDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceInputDataBinDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_INPUT_DATA_BIN_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceInputDataBinDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path inputDataBinDirectory = clientRoot.resolve(getServiceInputDataBinDirectory());
        if(!Files.exists(inputDataBinDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", inputDataBinDirectory));
        }
        return inputDataBinDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceOutputDataCSVDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_OUTPUT_DATA_CSV_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceOutputDataCSVDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path outputDataCSVDirectory = clientRoot.resolve(getServiceOutputDataCSVDirectory());
        if(!Files.exists(outputDataCSVDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", outputDataCSVDirectory));
        }
        return outputDataCSVDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceOutputDataBinDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_OUTPUT_DATA_BIN_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceOutputDataBinDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path outputDataBinDirectory = clientRoot.resolve(getServiceOutputDataBinDirectory());
        if(!Files.exists(outputDataBinDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", outputDataBinDirectory));
        }
        return outputDataBinDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceInputDataDefinitionDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_INPUT_DATA_DEFINITION_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceInputDataDefinitionDirectory(String clientName) throws NoSuchPathException, IOException {
        Path inputDirectory = getServiceInputDirectory(clientName);
        Path inputDataDefinitionDirectory = inputDirectory.resolve(getServiceInputDataDefinitionDirectory());
        if(!Files.exists(inputDataDefinitionDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", inputDataDefinitionDirectory));
        }
        return inputDataDefinitionDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceOutputDataDefinitionDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_OUTPUT_DATA_DEFINITION_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceOutputDataDefinitionDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path outputDataDefinitionDirectory = clientRoot.resolve(getServiceOutputDataDefinitionDirectory());
        if(!Files.exists(outputDataDefinitionDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", outputDataDefinitionDirectory));
        }
        return outputDataDefinitionDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceInstrumentationDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_INSTRUMENTATION_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceInstrumentationDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path instrumentationDirectory = clientRoot.resolve(getServiceInstrumentationDirectory());
        if(!Files.exists(instrumentationDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", instrumentationDirectory));
        }
        return instrumentationDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceArchiveDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        if(!Files.exists(archiveDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveDirectory));
        }
        return archiveDirectory;
    }
    
    
    /** 
     * @return String
     */
    public String getServiceArchiveInputDataDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_INPUT_DATA_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveInputDataDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        Path archiveInputDataDirectory = archiveDirectory.resolve(getServiceArchiveInputDataDirectory());
        if(!Files.exists(archiveInputDataDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveInputDataDirectory));
        }
        return archiveInputDataDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceArchiveOutputDataDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_OUTPUT_DATA_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveOutputDataDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        Path archiveOutputDataDirectory = archiveDirectory.resolve(getServiceArchiveOutputDataDirectory());
        if(!Files.exists(archiveOutputDataDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveOutputDataDirectory));
        }
        return archiveOutputDataDirectory;
    }
    
    
    /** 
     * @return String
     */
    public String getServiceArchiveInputDataCSVDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_INPUT_DATA_CSV_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveInputDataCSVDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        Path archiveInputDataCSVDirectory = archiveDirectory
                                            .resolve(getServiceArchiveInputDataDirectory())
                                            .resolve(getServiceArchiveInputDataCSVDirectory());
        if(!Files.exists(archiveInputDataCSVDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveInputDataCSVDirectory));
        }
        return archiveInputDataCSVDirectory;
    }
    
    
    /** 
     * @return String
     */
    public String getServiceArchiveInputDataBinDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_INPUT_DATA_BIN_DIRECTORY);
    }
    
    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveInputDataBinDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        Path archiveInputDataBinDirectory = archiveDirectory
                                        .resolve(getServiceArchiveInputDataDirectory())
                                        .resolve(getServiceArchiveInputDataBinDirectory());
        if(!Files.exists(archiveInputDataBinDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveInputDataBinDirectory));
        }
        return archiveInputDataBinDirectory;
    }

    
    /** 
     * @return String
     */
    public String getServiceArchiveOutputDataCSVDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_OUTPUT_DATA_CSV_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveOutputDataCSVDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        Path archiveOutputDataCSVDirectory = archiveDirectory
                                            .resolve(getServiceArchiveOutputDataDirectory())
                                            .resolve(getServiceArchiveOutputDataCSVDirectory());
        if(!Files.exists(archiveOutputDataCSVDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveOutputDataCSVDirectory));
        }
        return archiveOutputDataCSVDirectory;
    }
    
    
    /** 
     * @return String
     */
    public String getServiceArchiveOutputDataBinDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_ARCHIVE_OUTPUT_DATA_BIN_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceArchiveOutputDataBinDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path archiveDirectory = clientRoot.resolve(getServiceArchiveDirectory());
        Path archiveOutputDataBinDirectory = archiveDirectory
                                            .resolve(getServiceArchiveOutputDataDirectory())
                                            .resolve(getServiceArchiveOutputDataBinDirectory());
        if(!Files.exists(archiveOutputDataBinDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", archiveOutputDataBinDirectory));
        }
        return archiveOutputDataBinDirectory;
    }
    
    
    /** 
     * @return String
     */
    public String getServiceLostAndFoundDirectory() {
        return fileSystemConfigProperties.getProperty(SERVICE_LOST_AND_FOUND_DIRECTORY);
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws NoSuchPathException
     * @throws IOException
     */
    public Path getServiceLostAndFoundDirectory(String clientName) throws NoSuchPathException, IOException {
        Path serviceRootPath = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientRoot = serviceRootPath.resolve(clientName);
        if(!Files.exists(clientRoot, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system with root at %s, does not have a directory for %s.", serviceRootPath.toString(), clientName));
        }
        Path lostAndFoundDirectory = clientRoot.resolve(getServiceLostAndFoundDirectory());
        if(!Files.exists(lostAndFoundDirectory, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(String.format("The file system does not have a directory for %s.", lostAndFoundDirectory));
        }
        return lostAndFoundDirectory;
    }
}
