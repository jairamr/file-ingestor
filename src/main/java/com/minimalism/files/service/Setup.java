package com.minimalism.files.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.minimalism.AppConfigHelper;
import com.minimalism.files.FileSystemConfigHelper;
import com.minimalism.files.domain.InputOutputFileSystem;
import com.minimalism.files.exceptions.NoSuchPathException;

public class Setup {
    
    /** 
     * @param clientName
     * @throws IOException
     * @throws NoSuchPathException
     */
    public void register(String clientName) throws IOException, NoSuchPathException {
        //get rid of <sp>s in client name
        if(clientName.contains(" ")) {
            clientName = clientName.replaceAll(" ", "_");
        }
        
        Path root = AppConfigHelper.getInstance().getServiceRootDirectory();
        Path clientDirectory = FileSystemConfigHelper.getInstance().getServiceClientRoottDirectory(clientName);
        if(!Files.exists(clientDirectory)) {
            InputOutputFileSystem.createFileSystem(clientName);
        }
    }

}
