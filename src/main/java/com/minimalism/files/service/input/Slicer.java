package com.minimalism.files.service.input;

import java.io.IOException;

import com.minimalism.files.domain.SystemRecources;
import com.minimalism.files.domain.input.IngestorContext;
import com.minimalism.files.domain.input.SlicerConfigurationInformation;
import com.minimalism.files.exceptions.ResourceAllocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import avro.shaded.com.google.common.annotations.VisibleForTesting;
public class Slicer {
    private static Logger logger = LoggerFactory.getLogger(Slicer.class);
    private static int BUFFER_SIZE_MULTIPLIER = 1024;
    private int optimalIterationCount;
    private int optimalThreadCount;
    private long fileSize;
    private int bufferSize;
    private double loadingFactor;
    private int availableCores;
    private long availableMemory;
    private IngestorContext context;

    @VisibleForTesting
    public Slicer() {}

    public Slicer(IngestorContext context) throws ResourceAllocationException {
        this.context = context;
        this.fileSize = this.context.getInputFileInformation().getFileSize();
        this.bufferSize = this.context.getServiceConfiguration().getFileReadBufferSize() * BUFFER_SIZE_MULTIPLIER;
        this.availableCores = SystemRecources.getInstance().getJVMMaxProcessors();
        this.availableMemory = SystemRecources.getInstance().getJVMMaxMemoryAllocated();
        if(availableMemory < this.bufferSize*this.availableCores) {
            throw new ResourceAllocationException(
                String.format("The JVM is allocated %d bytes of memory, while the service requires %d bytes. Please allocated the required memory.", 
                this.availableMemory, this.bufferSize*this.availableCores));
        }
    }

    @VisibleForTesting
    public void setContext(IngestorContext context) {
        this.context = context;
    }

    public SlicerConfigurationInformation sliceFile() {
        
        SlicerConfigurationInformation returnValue = null;

        if(this.context.getServiceConfiguration().getOperatingMode().equalsIgnoreCase("balanced")) {
            returnValue = balancedOperationalMode();
        } else if(context.getServiceConfiguration().getOperatingMode().equalsIgnoreCase("single")) { 
            returnValue = singleThreadedOperationMode();
        }
        return returnValue;
    }
    
    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation singleThreadedOperationMode() {
        
        this.bufferSize = this.context.getServiceConfiguration().getFileReadBufferSize() * BUFFER_SIZE_MULTIPLIER;
        SystemRecources.getInstance().loadSystemState();

        
        this.optimalThreadCount = 1;
        this.optimalIterationCount = (int) context.getInputFileInformation().getFileSize() / this.bufferSize;
        if(context.getInputFileInformation().getFileSize() % this.bufferSize > 0) this.optimalIterationCount++;
    
        return new SlicerConfigurationInformation(this.optimalThreadCount, this.bufferSize, 
            context.getInputFileInformation().getFileSize(), this.optimalIterationCount, this.availableCores);
    }

    /** 
     * @return SlicerConfigurationInformation
     * @throws IOException
     */
    private SlicerConfigurationInformation balancedOperationalMode() {
        setOptimalThresholds();
        //adjustIterations(this.bufferSize); 

        this.optimalIterationCount = (int)(this.fileSize / (this.availableCores * this.bufferSize));
        if(this.fileSize % (this.availableCores * this.bufferSize) > 0) {
            this.optimalIterationCount++;
        }
        return new SlicerConfigurationInformation(this.optimalThreadCount, bufferSize, 
        this.fileSize, this.optimalIterationCount, this.availableCores);
    }

    private void adjustIterations(int computedBufferSize) {
        int bytesPerIteration = (int)(computedBufferSize * this.availableCores * this.loadingFactor);
        int necessaryIterations = (int)(this.fileSize / bytesPerIteration);
        if(this.fileSize % bytesPerIteration > 0) {
            necessaryIterations++;
        }
        // if more iterations that the optimal number is necessary, we increase the buffer size 
        // to appproach the optimal iteration count.
        if(necessaryIterations > this.optimalIterationCount) {
            adjustBufferSize();
            if(necessaryIterations > this.optimalIterationCount) {
                logger.warn("The optimal iteration count: {} cannot be accomodated with available memory; working with {} iterations.", this.optimalIterationCount, necessaryIterations);
            }
        } else {
            // finising with fewer iterations; can reduce loading factor...
            adjustThreadCount(necessaryIterations);
        }
        //this.optimalIterationCount = necessaryIterations;
    }

    private void adjustBufferSize() {
        long availableMemory = (long)(SystemRecources.getInstance().getJVMMaxMemoryAllocated() * 0.5);
        // use  up to 50% of available memory for buffer space
        int adjustedBufferSize = 0;
        int totalBufferSizeForIteration = this.bufferSize * this.optimalThreadCount;
        // target buffer size = file size / (threads * iterations)
        if(availableMemory > totalBufferSizeForIteration) {
            //int adjustedBytesPerIteration = (int)(availableMemory - totalBufferSizeForIteration);
            int adjustedBytesPerIteration = (int)(this.fileSize / this.optimalIterationCount);
            adjustedBufferSize = adjustedBytesPerIteration / this.optimalThreadCount;
            if(adjustedBufferSize % 1024 > 0) {
                adjustedBufferSize += adjustedBufferSize % 1024;
            }
            this.bufferSize = adjustedBufferSize;
            this.optimalIterationCount = (int)(this.fileSize / (this.optimalThreadCount * this.bufferSize));
        } else {
            logger.warn("Available memory {} bytes, is lesser than required for increacing buffer size per iteration {} bytes.", availableMemory, totalBufferSizeForIteration);
        }
    }

    private void adjustThreadCount(int necessaryIterations) {
        if((this.optimalIterationCount - necessaryIterations) > this.optimalIterationCount / 2 &&
            this.loadingFactor > 1) {
                this.setLoadingFactor((int)(this.loadingFactor * 0.5));
                adjustBufferSize();
        }
    }

    private void setOptimalThresholds() {
        // FYI... typically, 1.3 GB file has some 5 million records
        // 1 MiB buffer size for files < 5 GiB otherwise 1 MiB
        if(fileSize > 5*1024*1024*1024) { // greater than 5 GiB
            this.bufferSize = 2*1024*1024;
        } else {
            this.bufferSize = 1*1024*1024;
        }

        if(this.availableCores <= 2) {
            setLoadingFactor(2);
        } else if(this.availableCores > 2 && this.availableCores <= 4) {
            setLoadingFactor(1.5);
        } else {
            setLoadingFactor(1);
        }
    }

    private void setLoadingFactor(double loadingFactor) {
        this.loadingFactor = loadingFactor;
        this.optimalThreadCount = (int)Math.round(this.availableCores * loadingFactor);
    }
}
