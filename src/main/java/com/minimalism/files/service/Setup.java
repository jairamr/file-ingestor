package com.minimalism.files.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.minimalism.files.domain.InputOutputFileSystem;
import com.minimalism.shared.AppConfigHelper;
import com.minimalism.shared.FileSystemConfigHelper;
import com.minimalism.shared.exceptions.NoSuchPathException;

public class Setup {
    
    /** 
     * @param clientName
     * @throws IOException
     * @throws NoSuchPathException
     */
    public void register(String clientName) throws IOException, NoSuchPathException {
        //get rid of <sp>s in client name
        if(clientName.contains(" ")) {
            clientName = clientName.replace(" ", "_");
        }
        
        Path clientDirectory = FileSystemConfigHelper.getInstance().getServiceClientRoottDirectory(clientName);
        if(!Files.exists(clientDirectory)) {
            InputOutputFileSystem.createFileSystem(clientName);
        }
    }

}
