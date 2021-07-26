package com.minimalism.files.service;

import java.io.IOException;

import com.minimalism.common.AppConfigHelper;
import com.minimalism.files.domain.InputFileInformation;
import com.minimalism.files.domain.SlicerConfigurationInformation;
import com.minimalism.files.domain.SystemRecources;

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

        long availableMemory = SystemRecources.getInstance().getSysAvailablePhysicalMemory();
        
        if(this.inputFileInformation.getFileSize() >= availableMemory) {
            returnValue = lessThanRequiredMemory();
        } else {
            returnValue = moreThanRequiredMemory();
        }
        return returnValue;
    }

    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation lessThanRequiredMemory() throws IOException {
        // typically 1MiB, but can be changed through a config setting
        int bufferSize = AppConfigHelper.getInstance().getBufferSize();
        // Get a view of available recources (CPU cores, memory and number of processes)
        SystemRecources.getInstance().loadSystemState();

        // We use the available physical memory to decide on the number of buffers to use
        int numberOfBuffers = (int) SystemRecources.getInstance().getSysAvailablePhysicalMemory() / bufferSize;
        if(numberOfBuffers == 0)
            numberOfBuffers = 1;
        int availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        // since the process is IO initiated and then compute bound (parsing and validation)
        // a large amount of threads will cause thrashing, which will slow things down.
        // We start with a configured loading factor(typically 3) 
        int targetThreadCount = availableCores * AppConfigHelper.getInstance().getThreadsLoadingFactor();
        // Ideally, we want one thread working on one buffer (no contention!)
        if(numberOfBuffers > targetThreadCount)
            numberOfBuffers = targetThreadCount;
        else 
            targetThreadCount = numberOfBuffers;

        // if the file size islarger than the number of buffers, we must iterate
        int iterationCount = (int) inputFileInformation.getFileSize() / (bufferSize * numberOfBuffers);
        // nothing divides perfectly; we make the necessary adjustment to the iteration count
        if(inputFileInformation.getFileSize()  % (bufferSize * numberOfBuffers) > 0)
            iterationCount++;
        
        return new SlicerConfigurationInformation(targetThreadCount, bufferSize, 
            this.inputFileInformation.getFileSize(), iterationCount, availableCores);
    }
    
    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation moreThanRequiredMemory() throws IOException {
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
        
        // return new SlicerConfigurationInformation(1, (int)this.inputFileInformation.getFileSize(), 
        //     this.inputFileInformation.getFileSize(), 1, 1);
    }
}
