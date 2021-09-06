package com.minimalism.files.domain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.minimalism.shared.common.AllEnums.Directories;
import com.minimalism.shared.exceptions.NoSuchPathException;
import com.minimalism.shared.service.AppConfigHelper;
import com.minimalism.shared.service.FileSystemConfigHelper;

public class InputOutputFileSystem {
    private InputOutputFileSystem(){}
    /** 
     * @param clientName
     * @throws IOException
     */
    public static void createFileSystem(String clientName) throws IOException {
        var root = Paths.get(AppConfigHelper.getInstance().getServiceRootDirectory().toString());
        if(!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
            Files.createDirectory(root);
        }
        // check if client path is created
        var clientPath = root.resolve(clientName.replaceAll(" ", "_"));
        if(!Files.exists(clientPath)) {
            Files.createDirectory(clientPath);
        }
        // check if input directories exist
        var inputDirectoryPath = clientPath.resolve(FileSystemConfigHelper.getInstance().getServiceInputDirectory());
        if(!Files.exists(inputDirectoryPath)) {
            Files.createDirectory(inputDirectoryPath);
        }
        // check input sub-directories
        var inputCSVDirectoryPath = inputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceInputDataCSVDirectory());
        if(!Files.exists(inputCSVDirectoryPath)) {
            Files.createDirectory(inputCSVDirectoryPath);
        }
        var inputBinDirectoryPath = inputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceInputDataBinDirectory());
        if(!Files.exists(inputBinDirectoryPath)) {
            Files.createDirectory(inputBinDirectoryPath);
        }
        var inputDataDefinitionDirectoryPath = inputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceInputDataDefinitionDirectory());
        if(!Files.exists(inputDataDefinitionDirectoryPath)) {
            Files.createDirectory(inputDataDefinitionDirectoryPath);
        }
        //Check output directory
        var outputDirectoryPath = clientPath.resolve(FileSystemConfigHelper.getInstance().getServiceOutputDirectory());
        if(!Files.exists(outputDirectoryPath)) {
            Files.createDirectory(outputDirectoryPath);
        }
        // check output sub-directories
        var outputCSVDirectoryPath = outputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceOutputDataCSVDirectory());
        if(!Files.exists(outputCSVDirectoryPath)) {
            Files.createDirectory(outputCSVDirectoryPath);
        }
        var outputBinDirectoryPath = outputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceOutputDataBinDirectory());
        if(!Files.exists(outputBinDirectoryPath)) {
            Files.createDirectory(outputBinDirectoryPath);
        }
        var outputDataDefinitionDirectoryPath = outputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceOutputDataDefinitionDirectory());
        if(!Files.exists(outputDataDefinitionDirectoryPath)) {
            Files.createDirectory(outputDataDefinitionDirectoryPath);
        }
        // check instrumentation directory
        var instrumentationDirectoryPath = inputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceInstrumentationDirectory());
        if(!Files.exists(instrumentationDirectoryPath)) {
            Files.createDirectory(instrumentationDirectoryPath);
        }
        // check archive directories
        var archiveDirectoryPath = clientPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory());
        if(!Files.exists(archiveDirectoryPath)) {
            Files.createDirectory(archiveDirectoryPath);
        }
        var archiveInputDirectoryPath = archiveDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataDirectory());
        if(!Files.exists(archiveInputDirectoryPath)) {
            Files.createDirectory(archiveInputDirectoryPath);
        }
        var archiveOutputDirectoryPath = archiveDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataDirectory());
        if(!Files.exists(archiveOutputDirectoryPath)) {
            Files.createDirectory(archiveOutputDirectoryPath);
        }
        // check archive input sub-directories
        var archiveInputCSVDirectoryPath = archiveInputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataCSVDirectory());
        if(!Files.exists(archiveInputCSVDirectoryPath)) {
            Files.createDirectory(archiveInputCSVDirectoryPath);
        }
        var archiveInputBinDirectoryPath = archiveInputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataBinDirectory());
        if(!Files.exists(archiveInputBinDirectoryPath)) {
            Files.createDirectory(archiveInputBinDirectoryPath);
        }
        // check archive output sub-directories
        var archiveOutputCSVDirectoryPath = archiveOutputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataCSVDirectory());
        if(!Files.exists(archiveOutputCSVDirectoryPath)) {
            Files.createDirectory(archiveOutputCSVDirectoryPath);
        }
        var archiveOutputBinDirectoryPath = archiveOutputDirectoryPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataBinDirectory());
        if(!Files.exists(archiveOutputBinDirectoryPath)) {
            Files.createDirectory(archiveOutputBinDirectoryPath);
        }
    }

    
    /** 
     * @param clientName
     * @return Path
     * @throws IOException
     * @throws NoSuchPathException
     */
    public static Path getPathFor(String clientName) throws IOException, NoSuchPathException {

        Path root = Paths.get(AppConfigHelper.getInstance().getServiceRootDirectory().toString());
        if(!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(
                String.format("The service root directory: %s, does not exist in the file system. Please use InputOutputFileSystem#createFileSystem() to create the directories.", root.toString()));
        }
        // check if client path is created
        Path clientPath = root.resolve(clientName.replace(" ", "_"));
        if(!Files.exists(clientPath)) {
            throw new NoSuchPathException(
                String.format("The directory for client: %s does not exist in the file system. Please use InputOutputFileSystem#createFileSystem() to create the directories.", clientPath.toString()));
        }
        return clientPath;
    }

    
    /** 
     * @param clientName
     * @param directory
     * @return Path
     * @throws IOException
     * @throws NoSuchPathException
     */
    public static Path getPathFor(String clientName, Directories directory) throws IOException, NoSuchPathException {
        Path returnValue = null;

        Path root = Paths.get(AppConfigHelper.getInstance().getServiceRootDirectory().toString());
        if(!Files.exists(root, LinkOption.NOFOLLOW_LINKS)) {
            throw new NoSuchPathException(
                String.format("The service root directory: %s, does not exist in the file system. Please use InputOutputFileSystem#createFileSystem() to create the directories.", root.toString()));
        }
        // check if client path is created
        Path clientPath = root.resolve(clientName.replace(" ", "_"));
        if(!Files.exists(clientPath)) {
            throw new NoSuchPathException(
                String.format("The directory for client: %s does not exist in the file system. Please use InputOutputFileSystem#createFileSystem() to create the directories.", clientPath.toString()));
        }
        switch(directory) {
            case INPUT_DATA:
                returnValue = clientPath.resolve(FileSystemConfigHelper.getInstance().getServiceInputDirectory());
            break;
            case INPUT_DATA_CSV:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDataCSVDirectory());
            break;
            case INPUT_DATA_BIN:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDataBinDirectory());
            break;
            case INPUT_DATA_DEFINITION:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDataDefinitionDirectory());
            break;
            case OUTPUT_DATA:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDirectory());
            break;
            case OUTPUT_DATA_CSV:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDataCSVDirectory());
            break;
            case OUTPUT_DATA_BIN:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDataBinDirectory());
            break;
            case OUTPUT_DATA_DEFINITION:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceOutputDataDefinitionDirectory());
            break;
            case ARCHIVE:
                returnValue = clientPath.resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory());
            break;
            case ARCHIVE_INPUT:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataDirectory());
            break;
            case ARCHIVE_INPUT_CSV:
            returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataCSVDirectory());
            break;
            case ARCHIVE_INPUT_BIN:
            returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveInputDataBinDirectory());
            break;
            case ARCHIVE_OUTPUT:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataDirectory());
            break;
            case ARCHIVE_OUTPUT_CSV:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataCSVDirectory());
            break;
            case ARCHIVE_OUTPUT_BIN:
            returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceArchiveOutputDataBinDirectory());
            break;
            case INSTRUMENTATION:
                returnValue = clientPath
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInputDirectory())
                                .resolve(FileSystemConfigHelper.getInstance().getServiceInstrumentationDirectory());
            break;
            default:
                returnValue = null;
            break;
        }
        return returnValue;
    }
}
