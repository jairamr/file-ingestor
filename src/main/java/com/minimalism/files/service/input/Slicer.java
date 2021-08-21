package com.minimalism.files.service.input;

import java.io.IOException;

import com.minimalism.AppConfigHelper;
import com.minimalism.files.domain.SystemRecources;
import com.minimalism.files.domain.input.InputFileInformation;
import com.minimalism.files.domain.input.SlicerConfigurationInformation;

public class Slicer {
    private InputFileInformation inputFileInformation;

    public Slicer(InputFileInformation inputFileInformation) {
        this.inputFileInformation = inputFileInformation;
    }

    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    public SlicerConfigurationInformation sliceFile() throws IOException {
        SlicerConfigurationInformation slicerConfig = null;
        slicerConfig = prepareForSlicing();
        return slicerConfig;
    }

    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation prepareForSlicing() throws IOException {
        SlicerConfigurationInformation returnValue = null;

        var opMode = AppConfigHelper.getInstance().getServiceOperatingMode();
        if(opMode.equalsIgnoreCase("balanced")) {
            returnValue = balancedOperationalMode();
        } else if(opMode.equalsIgnoreCase("single")) { 
            returnValue = singleThreadedOperationMode();
        }
        return returnValue;
    }

    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation singleThreadedOperationMode() throws IOException {
        
        int bufferSize = AppConfigHelper.getInstance().getBufferSize();
        
        SystemRecources.getInstance().loadSystemState();

        var targetThreadCount = 0;
        var iterations = 0;
        int availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        
        targetThreadCount = 1;
        iterations = (int) inputFileInformation.getFileSize() / bufferSize;
        if(inputFileInformation.getFileSize() % bufferSize > 0) iterations++;
    
        return new SlicerConfigurationInformation(targetThreadCount, bufferSize, 
            this.inputFileInformation.getFileSize(), iterations, availableCores);
    }
    
    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation moreThanRequiredMemory(boolean singleThreaded) throws IOException {
        int bufferSize = AppConfigHelper.getInstance().getBufferSize();

        SystemRecources.getInstance().loadSystemState();

        int availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        int targetThreadCount = availableCores * AppConfigHelper.getInstance().getThreadsLoadingFactor();
        
        bufferSize = (int)inputFileInformation.getFileSize() / targetThreadCount;
        
        //In case there are residual bytes (due to division) we increase the buffer size by additional 100 bytes
        long residualBytes = inputFileInformation.getFileSize() % targetThreadCount;
        if(residualBytes > 0)
            bufferSize += residualBytes;

        return new SlicerConfigurationInformation(targetThreadCount, bufferSize, 
            this.inputFileInformation.getFileSize(), 1, availableCores);
    }

    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation balancedOperationalMode() throws IOException {
        var bufferSize = AppConfigHelper.getInstance().getBufferSize();
        var availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        var targetThreadCount = availableCores * AppConfigHelper.getInstance().getThreadsLoadingFactor();
        var bytesProcessedPerIteration = bufferSize * targetThreadCount;
        var iterations = (int)inputFileInformation.getFileSize() / bytesProcessedPerIteration;
        if(inputFileInformation.getFileSize() % bytesProcessedPerIteration > 0) {
            iterations++;
        }
        return new SlicerConfigurationInformation(targetThreadCount, bufferSize, 
        inputFileInformation.getFileSize(), iterations, availableCores);
    }
}
