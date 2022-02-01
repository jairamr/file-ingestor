package com.minimalism.files.service.input;

import java.io.IOException;

import com.minimalism.files.domain.SystemRecources;
import com.minimalism.files.domain.input.IngestorContext;
import com.minimalism.files.domain.input.SlicerConfigurationInformation;
public class Slicer {
    private static int BUFFER_SIZE_MULTIPLIER = 1024;
    public SlicerConfigurationInformation 
    sliceFile(IngestorContext context) {
        
        SlicerConfigurationInformation returnValue = null;

        if(context.getServiceConfiguration().getOperatingMode().equalsIgnoreCase("balanced")) {
            returnValue = balancedOperationalMode(context);
        } else if(context.getServiceConfiguration().getOperatingMode().equalsIgnoreCase("single")) { 
            returnValue = singleThreadedOperationMode(context);
        }
        return returnValue;
    }
    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation 
    singleThreadedOperationMode(IngestorContext context) {
        
        int bufferSize = context.getServiceConfiguration().getFileReadBufferSize() * BUFFER_SIZE_MULTIPLIER;
        SystemRecources.getInstance().loadSystemState();

        var targetThreadCount = 0;
        var iterations = 0;
        int availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        
        targetThreadCount = 1;
        iterations = (int) context.getInputFileInformation().getFileSize() / bufferSize;
        if(context.getInputFileInformation().getFileSize() % bufferSize > 0) iterations++;
    
        return new SlicerConfigurationInformation(targetThreadCount, bufferSize, 
            context.getInputFileInformation().getFileSize(), iterations, availableCores);
    }

    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation 
    balancedOperationalMode(IngestorContext context) {
        var bufferSize = context.getServiceConfiguration().getFileReadBufferSize() * BUFFER_SIZE_MULTIPLIER;
        var availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        var targetThreadCount = context.getServiceConfiguration().getThreadsLoadingFactor();
        var bytesProcessedPerIteration = bufferSize * targetThreadCount;
        var iterations = (int)context.getInputFileInformation().getFileSize() / bytesProcessedPerIteration;
        if(context.getInputFileInformation().getFileSize() % bytesProcessedPerIteration > 0) {
            iterations++;
        }
        return new SlicerConfigurationInformation(targetThreadCount, bufferSize, 
        context.getInputFileInformation().getFileSize(), iterations, availableCores);
    }
}
